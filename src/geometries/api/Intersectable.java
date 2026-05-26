package geometries.api;

import java.util.List;
import primitives.Point;
import primitives.Ray;

/**
 * Common abstract class for all geometries that can be intersected by a ray.
 */
public abstract class Intersectable {

    /**
     * Finds all intersection points of a ray with the geometry.
     * * @param ray the ray intersecting the geometry
     * @return a list of intersection points, or null if there are no intersections
     */
    public abstract List<Point> findIntersections(Ray ray);
}