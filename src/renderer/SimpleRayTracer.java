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
 * Basic ray tracer that colors each pixel using the closest intersection and the Phong reflection model.
 */
class SimpleRayTracer extends RayTracerBase {

    /**
     * Creates a simple ray tracer for the given scene.
     * @param scene the scene to render
     */
    SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    Color traceRay(Ray ray) {
        var intersections = scene.geometries.calcIntersections(ray);
        if (intersections == null) {
            return scene.background;
        }

        Intersection closestIntersection = ray.findClosestIntersection(intersections);
        return calcColor(closestIntersection, ray.direction());
    }

    /**
     * Computes the color at a given intersection: ambient light attenuated by the material's kA, plus local effects.
     * @param intersection the intersection whose color is computed
     * @param v            the direction of the intersecting ray
     * @return the resulting color at the intersection, black if the ray is tangent to the surface
     */
    private Color calcColor(Intersection intersection, Vector v) {
        return !preprocessIntersection(intersection, v) ? Color.BLACK
                : scene.ambientLight.getIntensity().scale(intersection.material.kA)
                .add(calcLocalEffects(intersection));
    }

    /**
     * Computes the emission plus the diffuse and specular contributions of all light sources in the scene.
     * @param intersection the preprocessed intersection
     * @return the local effects color at the intersection
     */
    private Color calcLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (preprocessLightSource(intersection, lightSource)) {
                color = color.add(lightSource.getIntensity(intersection.point)
                        .scale(calcDiffuse(intersection).add(calcSpecular(intersection))));
            }
        }
        return color;
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