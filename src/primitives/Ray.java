package primitives;

import java.util.Objects;

/**
 * Represents a ray in 3D space, defined by an origin point and a direction vector.
 */
public class Ray {
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
}