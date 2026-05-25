package geometries.api;

import primitives.Point;
import primitives.Vector;

/**
 * Base abstract class for all geometric shapes.
 */
public abstract class Geometry {
    /**
     * Calculates the normal vector to the geometry at a specific point.
     * @param point the point on the geometry surface
     * @return the normal vector
     */
    public abstract Vector getNormal(Point point);

    /**
     * Default constructor for Geometry.
     */
    protected Geometry() {
    }
}