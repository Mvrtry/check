package geometries.impl;

import geometries.api.Intersectable;
import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite class representing a collection of intersectable geometries.
 */
public class Geometries extends Intersectable {

    /** List of intersectable geometries */
    private final List<Intersectable> _geometries = new ArrayList<>();

    /**
     * Default empty constructor.
     */
    public Geometries() {
    }

    /**
     * Constructor taking a collection of geometries.
     * @param geometries variable length arguments of intersectable geometries
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds a collection of geometries to the internal list.
     * @param geometries variable length arguments of intersectable geometries
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(_geometries, geometries);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;

        for (Intersectable geo : _geometries) {
            List<Point> geoIntersections = geo.findIntersections(ray);

            // Only if the current geometry has intersections we process them
            if (geoIntersections != null) {
                // Lazy instantiation: create the list only when the first intersection is found
                if (intersections == null) {
                    intersections = new ArrayList<>();
                }
                intersections.addAll(geoIntersections);
            }
        }

        // Will return null if no intersections were found in any geometry
        return intersections;
    }
}