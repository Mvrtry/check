package renderer;

import geometries.api.Intersectable.Intersection;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Basic ray tracer that colors each pixel using the closest intersection and its geometry's emission and ambient light.
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
        return calcColor(closestIntersection);
    }

    /**
     * Computes the color at a given intersection: ambient light attenuated by the material's kA, plus emission.
     * @param intersection the intersection whose color is computed
     * @return the resulting color at the intersection
     */
    private Color calcColor(Intersection intersection) {
        return scene.ambientLight.getIntensity().scale(intersection.material.kA).add(intersection.geometry.getEmission());
    }
}