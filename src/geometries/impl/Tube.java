package geometries.impl;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents an infinite tube in 3D space.
 */
public class Tube extends RadialGeometry {
    /** The central axis ray of the tube */
    protected final Ray _axis;

    /**
     * Constructor for Tube.
     * @param radius the radius
     * @param axis the central axis
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        _axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        Vector p0ToPoint = point.subtract(_axis.origin());
        double t = _axis.direction().dotProduct(p0ToPoint);

        // If the point is exactly orthogonal to the origin, t will be zero
        if (primitives.Util.isZero(t)) {
            return p0ToPoint.normalize();
        }

        Point o = _axis.origin().add(_axis.direction().scale(t));
        return point.subtract(o).normalize();
    }
}