package renderer;

import static primitives.Util.alignZero;

import geometries.api.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

/**
 * Basic ray tracer that colors each pixel using the Phong reflection model with shadows, reflection and transparency.
 */
class SimpleRayTracer extends RayTracerBase {
    /** Maximum recursion depth for global effects. */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /** Minimal accumulated attenuation factor for which global effects are still calculated. */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /** Initial accumulated attenuation factor at the start of the recursion. */
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * Creates a simple ray tracer for the given scene.
     * @param scene the scene to render
     */
    SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    Color traceRay(Ray ray) {
        Intersection closestIntersection = findClosestIntersection(ray);
        return closestIntersection == null ? scene.background : calcColor(closestIntersection, ray.direction());
    }

    /**
     * Finds the intersection closest to the ray origin.
     * @param ray the ray to intersect with the scene
     * @return the closest intersection, or null if there are no intersections
     */
    private Intersection findClosestIntersection(Ray ray) {
        return ray.findClosestIntersection(scene.geometries.calcIntersections(ray));
    }

    /**
     * Computes the color at a given intersection: ambient light once, plus the recursive local and global effects.
     * @param intersection the intersection whose color is computed
     * @param v            the direction of the intersecting ray
     * @return the resulting color at the intersection, black if the ray is tangent to the surface
     */
    private Color calcColor(Intersection intersection, Vector v) {
        return !preprocessIntersection(intersection, v) ? Color.BLACK
                : scene.ambientLight.getIntensity().scale(intersection.material.kA)
                .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }

    /**
     * Recursively computes the local and global effects at a preprocessed intersection (without ambient light).
     * @param intersection the preprocessed intersection
     * @param level        the remaining recursion depth
     * @param k            the accumulated attenuation factor
     * @return the color at the intersection
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcLocalEffects(intersection, k);
        return level == 1 ? color : color.add(calcGlobalEffects(intersection, level, k));
    }

    /**
     * Computes the sum of the transparency and reflection contributions at an intersection.
     * @param intersection the preprocessed intersection
     * @param level        the remaining recursion depth
     * @param k            the accumulated attenuation factor
     * @return the global effects color
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        Material material = intersection.material;
        return calcGlobalEffect(constructRefractedRay(intersection), level, k, material.kT)
                .add(calcGlobalEffect(constructReflectedRay(intersection), level, k, material.kR));
    }

    /**
     * Computes the color contribution of a single secondary ray, attenuated by its coefficient.
     * @param ray   the secondary ray (reflected or refracted)
     * @param level the remaining recursion depth
     * @param k     the accumulated attenuation factor
     * @param kx    the attenuation coefficient of the current effect (kT or kR)
     * @return the attenuated color of the secondary ray
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.isLowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;

        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.background.scale(kx);

        return preprocessIntersection(intersection, ray.direction())
                ? calcColor(intersection, level - 1, kkx).scale(kx)
                : Color.BLACK;
    }

    /**
     * Constructs the reflected ray at an intersection: r = v - 2(v*n)n.
     * @param intersection the preprocessed intersection
     * @return the reflected ray, with its origin shifted off the surface
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector r = intersection.v.add(intersection.normal.scale(-2 * intersection.vNormal));
        return new Ray(intersection.point, r, intersection.normal);
    }

    /**
     * Constructs the refracted (transparency) ray at an intersection, continuing in the original direction.
     * @param intersection the preprocessed intersection
     * @return the refracted ray, with its origin shifted past the surface
     */
    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v, intersection.normal);
    }

    /**
     * Computes the emission plus the diffuse and specular contributions of all light sources, attenuated by shadows.
     * @param intersection the preprocessed intersection
     * @param k            the accumulated attenuation factor
     * @return the local effects color at the intersection
     */
    private Color calcLocalEffects(Intersection intersection, Double3 k) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (!preprocessLightSource(intersection, lightSource)) continue;
            Double3 ktr = transparency(intersection);
            if (ktr.product(k).isGreaterThan(MIN_CALC_COLOR_K)) {
                color = color.add(lightSource.getIntensity(intersection.point).scale(ktr)
                        .scale(calcDiffuse(intersection).add(calcSpecular(intersection))));
            }
        }
        return color;
    }

    /**
     * Checks whether no opaque geometry blocks the current light source from the intersection point.
     * @param intersection the intersection preprocessed for the current light source
     * @return true if the point is not shaded from the light source, false otherwise
     */
    private boolean unshaded(Intersection intersection) {
        Ray shadowRay = new Ray(intersection.point, intersection.l.scale(-1), intersection.normal);

        var shadowIntersections = scene.geometries.calcIntersections(shadowRay);
        if (shadowIntersections == null) return true;

        double lightDistance = intersection.light.getDistance(intersection.point);
        for (Intersection shadowIntersection : shadowIntersections) {
            if (shadowIntersection.material.kT.isLowerThan(MIN_CALC_COLOR_K)
                    && alignZero(shadowIntersection.point.distance(intersection.point) - lightDistance) < 0)
                return false;
        }
        return true;
    }

    /**
     * Computes the accumulated transparency factor of all geometries between the intersection point and the light.
     * @param intersection the intersection preprocessed for the current light source
     * @return the accumulated transparency factor, ZERO if the light is practically blocked
     */
    private Double3 transparency(Intersection intersection) {
        Ray shadowRay = new Ray(intersection.point, intersection.l.scale(-1), intersection.normal);

        Double3 ktr = Double3.ONE;
        var shadowIntersections = scene.geometries.calcIntersections(shadowRay);
        if (shadowIntersections == null) return ktr;

        double lightDistance = intersection.light.getDistance(intersection.point);
        for (Intersection shadowIntersection : shadowIntersections) {
            if (alignZero(shadowIntersection.point.distance(intersection.point) - lightDistance) < 0) {
                ktr = ktr.product(shadowIntersection.material.kT);
                if (ktr.isLowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
            }
        }
        return ktr;
    }

    /**
     * Computes the diffuse reflection factor kD * |l * n| for the current light source.
     * @param intersection the intersection preprocessed for the current light source
     * @return the diffuse reflection factor
     */
    private Double3 calcDiffuse(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.lNormal));
    }

    /**
     * Computes the specular reflection factor kS * max(0, -v * r)^nSh for the current light source.
     * @param intersection the intersection preprocessed for the current light source
     * @return the specular reflection factor
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.l.add(intersection.normal.scale(-2 * intersection.lNormal));
        double minusVR = alignZero(-intersection.v.dotProduct(r));
        Material material = intersection.material;
        return minusVR <= 0 ? Double3.ZERO : material.kS.scale(Math.pow(minusVR, material.nShininess));
    }
}