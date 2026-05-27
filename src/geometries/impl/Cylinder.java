package geometries.impl;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.isZero;
import static primitives.Util.alignZero;

/**
 * Represents a finite cylinder in 3D space.
 * The cylinder is defined by a radius, an axis ray, and a height.
 */
public class Cylinder extends Tube {
    /** The height of the cylinder */
    private final double _height;

    /**
     * Constructor for Cylinder.
     * @param radius the radius
     * @param axis   the central axis
     * @param height the height
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        _height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0 = _axis.origin();
        Vector v = _axis.direction();

        // Check if the point is at the center of the lower base
        if (point.equals(p0)) return v.scale(-1);

        Vector p0ToPoint = point.subtract(p0);
        double t = alignZero(v.dotProduct(p0ToPoint));

        // Check if the point is on the lower base (t is effectively 0)
        if (isZero(t)) return v.scale(-1);

        // Check if the point is on the upper base
        Point p1 = _axis.getPoint(_height);
        if (point.equals(p1)) return v;

        Vector p1ToPoint = point.subtract(p1);
        double t2 = alignZero(v.dotProduct(p1ToPoint));

        // Check if the point is on the upper base (t2 is effectively 0)
        if (isZero(t2)) return v;

        // Otherwise, it's on the side of the cylinder
        Point o = _axis.getPoint(t);
        return point.subtract(o).normalize();
    }
}