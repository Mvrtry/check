package geometries.impl;

import static primitives.Util.alignZero;

import java.util.List;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a sphere in 3D space.
 */
public class Sphere extends RadialGeometry {
    /** The center point of the sphere */
    private final Point _center;

    /**
     * Constructor for Sphere.
     * @param center the center point
     * @param radius the radius
     */
    public Sphere(Point center, double radius) {
        super(radius);
        _center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        // Normal to a sphere is the vector from the center to the point, normalized.
        return point.subtract(_center).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.origin();
        Vector v = ray.direction();

        // Special Case: Ray starts exactly at the center of the sphere
        if (_center.equals(p0)) {
            return List.of(p0.add(v.scale(_radius)));
        }

        Vector u = _center.subtract(p0);
        double tm = alignZero(v.dotProduct(u));
        double dSquared = alignZero(u.lengthSquared() - tm * tm);
        double thSquared = alignZero(_radius * _radius - dSquared);

        // If thSquared <= 0, the ray's line is outside the sphere or tangent to it
        if (thSquared <= 0) {
            return null;
        }

        double th = alignZero(Math.sqrt(thSquared));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        // Both intersections are behind the ray's origin
        if (t1 <= 0 && t2 <= 0) {
            return null;
        }

        // Two intersection points in front of the ray's origin
        if (t1 > 0 && t2 > 0) {
            // t1 is always smaller than t2 (since th is positive), so it's closer to p0.
            return List.of(p0.add(v.scale(t1)), p0.add(v.scale(t2)));
        }

        // Only one intersection point is in front of the ray's origin (Ray starts inside)
        if (t1 > 0) {
            return List.of(p0.add(v.scale(t1)));
        }

        if (t2 > 0) {
            return List.of(p0.add(v.scale(t2)));
        }

        return null;
    }
}