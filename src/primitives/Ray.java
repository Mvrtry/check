package primitives;

import static primitives.Util.alignZero;

import java.util.List;
import java.util.Objects;

import geometries.api.Intersectable.Intersection;

/**
 * Represents a ray in 3D space, defined by an origin point and a direction vector.
 */
public class Ray {
    /** Origin offset along the normal for secondary rays, preventing self-intersection */
    private static final double DELTA = 0.1;
    /** The origin point of the ray */
    private final Point _origin;
    /** The normalized direction vector of the ray */
    private final Vector _direction;

    /**
     * Constructor for Ray. Normalizes the direction vector.
     * @param origin the origin point
     * @param direction the direction vector
     */
    public Ray(Point origin, Vector direction) {
        _origin = origin;
        _direction = direction.normalize();
    }

    /**
     * Constructor for a secondary ray whose origin is shifted by DELTA along the normal, toward the ray direction side.
     * @param origin    the original point on the surface
     * @param direction the ray direction
     * @param normal    the surface normal at the origin point
     */
    public Ray(Point origin, Vector direction, Vector normal) {
        this(shiftOrigin(origin, direction, normal), direction);
    }

    /**
     * Shifts a point by DELTA along the normal, to the side the direction points to (no shift if they are orthogonal).
     * @param origin    the point to shift
     * @param direction the ray direction
     * @param normal    the surface normal
     * @return the shifted point
     */
    private static Point shiftOrigin(Point origin, Vector direction, Vector normal) {
        double nd = alignZero(normal.dotProduct(direction));
        return nd == 0 ? origin : origin.add(normal.scale(nd > 0 ? DELTA : -DELTA));
    }


    /**
     * Returns the normalized direction vector of the ray.
     * @return the direction vector
     */
    public Vector direction() {
        return _direction;
    }

    /**
     * Returns the origin point of the ray.
     * @return the origin point
     */
    public Point origin() {
        return _origin;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ray other = (Ray) obj;
        return _origin.equals(other._origin) && _direction.equals(other._direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_origin, _direction);
    }

    @Override
    public String toString() {
        return "Ray: " + _origin + _direction;
    }

    /**
     * Calculates a point on the ray line at a given distance from the origin.
     * P = P0 + t * v
     *
     * @param t the distance from the origin
     * @return the calculated point
     */
    public Point getPoint(double t) {
        try {
            return _origin.add(_direction.scale(t));
        } catch (IllegalArgumentException e) {
            // If t is zero, scaling creates a zero vector which throws an exception.
            // In this case, the distance is 0, meaning the point is exactly the origin.
            return _origin;
        }
    }

    /**
     * Finds the point closest to the ray's origin from a list of points.
     * @param points list of points to search; assumed non-empty if not null
     * @return the closest point to the ray's origin, or null if the list is null
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null ? null
                : findClosestIntersection(
                points.stream()
                        .map(point -> new Intersection(null, point))
                        .toList()
        ).point;
    }

    /**
     * Finds the intersection closest to the ray's origin from a list of intersections.
     * @param intersections list of intersections to search; assumed non-empty if not null
     * @return the closest intersection to the ray's origin, or null if the list is null or empty
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null || intersections.isEmpty()) return null;

        Intersection closestIntersection = null;
        double closestDistanceSquared = Double.POSITIVE_INFINITY;

        for (Intersection intersection : intersections) {
            double distanceSquared = _origin.distanceSquared(intersection.point);
            if (distanceSquared < closestDistanceSquared) {
                closestDistanceSquared = distanceSquared;
                closestIntersection = intersection;
            }
        }

        return closestIntersection;
    }
}