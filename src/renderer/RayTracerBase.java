package renderer;

import static primitives.Util.alignZero;

import geometries.api.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

/**
 * Base class for ray tracing engines, holding the scene to be rendered.
 */
abstract class RayTracerBase {

    /** The scene this ray tracer renders. */
    protected final Scene scene;

    /**
     * Creates a ray tracer for the given scene.
     * @param scene the scene to render
     */
    RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a ray through the scene and computes the resulting color.
     * @param ray the ray to trace
     * @return the color obtained by tracing the ray
     */
    abstract Color traceRay(Ray ray);

    /**
     * Caches the light-independent data of an intersection: ray direction, normal and their dot product.
     * @param intersection the intersection to preprocess
     * @param v            the direction of the intersecting ray
     * @return false if the ray is tangent to the surface (zero dot product), true otherwise
     */
    protected boolean preprocessIntersection(Intersection intersection, Vector v) {
        intersection.v = v;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = alignZero(intersection.v.dotProduct(intersection.normal));
        return intersection.vNormal != 0;
    }

    /**
     * Caches the light-dependent data of an intersection: light source, light direction and its dot product with the normal.
     * @param intersection the preprocessed intersection
     * @param light        the light source to process
     * @return true if the light and the viewer are on the same side of the surface, false otherwise
     */
    protected boolean preprocessLightSource(Intersection intersection, LightSource light) {
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.lNormal = alignZero(intersection.l.dotProduct(intersection.normal));
        return intersection.lNormal * intersection.vNormal > 0;
    }
}