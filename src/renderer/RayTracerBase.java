package renderer;

import primitives.Color;
import primitives.Ray;
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
}