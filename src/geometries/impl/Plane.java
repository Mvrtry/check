package geometries.impl;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.List;
import geometries.api.Geometry;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a plane in 3D space.
 */
public class Plane extends Geometry {
    /** A point on the plane */
    @SuppressWarnings("unused")
    private final Point _q;
    /** The normal vector to the plane */
    private final Vector _normal;

    /**
     * Constructor taking three points on the plane.
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Plane(Point p1, Point p2, Point p3) {
        _q = p1;
        Vector U = p2.subtract(p1);
        Vector V = p3.subtract(p1);
        _normal = U.crossProduct(V).normalize();
    }

    /**
     * Constructor taking a point and a normal vector.
     * @param point a point on the plane
     * @param normal the normal vector
     */
    public Plane(Point point, Vector normal) {
        _q = point;
        _normal = normal.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return _normal;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.origin();
        Vector v = ray.direction();

        double nv = _normal.dotProduct(v);

        // Ray is parallel to the plane (nv == 0)
        // Included in the plane or strictly parallel
        if (isZero(nv)) {
            return null;
        }

        // Ray starts exactly at the plane's reference point Q
        // We must check this to avoid a zero-vector exception in the subtract method
        if (_q.equals(p0)) {
            return null;
        }

        Vector p0ToQ = _q.subtract(p0);
        double numerator = _normal.dotProduct(p0ToQ);

        // Calculate the scalar t
        double t = alignZero(numerator / nv);

        // Intersection point is behind the ray's origin or exactly on it (t <= 0)
        if (t <= 0) {
            return null;
        }

        // Calculate the actual intersection point: P = P0 + t * v
        return List.of(ray.getPoint(t));
    }
}