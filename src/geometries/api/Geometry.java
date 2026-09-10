package geometries.api;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Base abstract class for all geometric shapes.
 */
public abstract class Geometry extends Intersectable {
    /** Emission color of the geometry, default is black (no emission) */
    private Color emission = Color.BLACK;

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

    /**
     * Returns the emission color of the geometry.
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     * @param emission the emission color to set
     * @return this geometry, for chaining
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
}