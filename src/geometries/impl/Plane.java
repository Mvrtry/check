package geometries.impl;

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
        return null; // Skeleton implementation
    }
}