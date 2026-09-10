package geometries.api;

import java.util.List;
import java.util.Objects;
import primitives.Material;
import primitives.Point;
import primitives.Ray;

/**
 * Common abstract class for all geometries that can be intersected by a ray.
 */
public abstract class Intersectable {

    /**
     * Default empty constructor.
     */
    public Intersectable() {
    }

    /**
     * PDS holding an intersection point together with the geometry it belongs to.
     */
    public static final class Intersection {
        /** The geometry that was intersected */
        public final Geometry geometry;
        /** The intersection point */
        public final Point point;
        /** The material of the intersected geometry (a default material if geometry is null) */
        public final Material material;

        /**
         * Constructs an Intersection with the given geometry and point.
         * @param geometry the intersected geometry
         * @param point the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry == null ? new Material() : geometry.getMaterial();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return obj instanceof Intersection other
                    && this.geometry == other.geometry
                    && this.point.equals(other.point);
        }

        @Override
        public int hashCode() {
            return Objects.hash(geometry, point);
        }

        @Override
        public String toString() {
            return "Intersection{" + "geometry=" + geometry + ", point=" + point + '}';
        }
    }

    /**
     * Helper method to be implemented by each concrete geometry, computing its intersections.
     * @param ray the ray intersecting the geometry
     * @return a list of intersections, or null if there are no intersections
     */
    protected abstract List<Intersection> calcIntersectionsHelper(Ray ray);

    /**
     * Finds all intersections of a ray with the geometry.
     * @param ray the ray intersecting the geometry
     * @return a list of intersections, or null if there are no intersections
     */
    public final List<Intersection> calcIntersections(Ray ray) {
        return calcIntersectionsHelper(ray);
    }

    /**
     * Finds all intersection points of a ray with the geometry.
     * @param ray the ray intersecting the geometry
     * @return a list of intersection points, or null if there are no intersections
     */
    public final List<Point> findIntersections(Ray ray) {
        var intersections = calcIntersections(ray);
        return intersections == null ? null
                : intersections.stream()
                  .map(intersection -> intersection.point)
                  .toList();
    }
}