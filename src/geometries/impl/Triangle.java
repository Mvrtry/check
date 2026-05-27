package geometries.impl;

import static primitives.Util.alignZero;

import java.util.List;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a triangle in 3D space.
 */
public class Triangle extends Polygon {

    /**
     * Constructor taking the three vertices of the triangle.
     * @param p1 first vertex
     * @param p2 second vertex
     * @param p3 third vertex
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // Step 1: Intersect the ray with the plane containing the triangle
        List<Point> planeIntersections = _plane.findIntersections(ray);
        if (planeIntersections == null) {
            return null; // Ray doesn't even intersect the plane
        }

        // Step 2: Check if the intersection point is inside the triangle
        Point p0 = ray.origin();
        Vector v = ray.direction();

        Point p1 = _vertices.get(0);
        Point p2 = _vertices.get(1);
        Point p3 = _vertices.get(2);

        Vector v1 = p1.subtract(p0);
        Vector v2 = p2.subtract(p0);
        Vector v3 = p3.subtract(p0);

        try {
            // Normal 1
            Vector n1 = v1.crossProduct(v2);
            double s1 = alignZero(v.dotProduct(n1));
            if (s1 == 0) return null;

            // Normal 2
            Vector n2 = v2.crossProduct(v3);
            double s2 = alignZero(v.dotProduct(n2));
            if (s2 == 0) return null;

            // Normal 3
            Vector n3 = v3.crossProduct(v1);
            double s3 = alignZero(v.dotProduct(n3));
            if (s3 == 0) return null;

            // The point is inside the triangle IF AND ONLY IF all three signs are identical
            if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
                return planeIntersections; // We reuse the point computed by the Plane
            }
        } catch (IllegalArgumentException e) {
            // If crossProduct throws an exception, it means the vectors are parallel.
            // This happens when the ray's origin sits exactly on an edge's continuation line.
            // According to instructions, intersections ON edges or vertices return null.
            return null;
        }

        // The point is outside the triangle
        return null;
    }
}