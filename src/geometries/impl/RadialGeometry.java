package geometries.impl;

import geometries.api.Geometry;

/**
 * Base abstract class for geometries with a radius.
 */
public abstract class RadialGeometry extends Geometry {
    /** The radius of the geometry */
    protected final double _radius;
    /** The squared radius of the geometry */
    protected final double _radiusSquared;

    /**
     * Constructor for RadialGeometry.
     * @param radius the radius value
     */
    public RadialGeometry(double radius) {
        _radius = radius;
        _radiusSquared = radius * radius;
    }
}