package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import java.util.List;

/**
 * Unit tests for class {@link Geometries}.
 */
class GeometriesTests {

    /** Error message for missing intersections */
    private static final String ERROR_NULL = "ERROR: Expected null but got a list";
    /** Error message for wrong number of intersections */
    private static final String ERROR_COUNT = "ERROR: Wrong number of intersection points";

    private static final Plane PLANE = new Plane(new Point(0, 0, 5), new Vector(0, 0, 1));
    private static final Sphere SPHERE = new Sphere(new Point(0, 0, 2), 1d);
    private static final Triangle TRIANGLE = new Triangle(new Point(0, 1, 10), new Point(1, -1, 10), new Point(-1, -1, 10));

    private static final Geometries GEOMETRIES = new Geometries(PLANE, SPHERE, TRIANGLE);

    private static final Point P1 = new Point(0, 0, -1);
    private static final Point P2 = new Point(0, 0, 4);
    private static final Point P3 = new Point(0, 0, 6);
    private static final Point P4 = new Point(0, 0, 0);

    private static final Vector V1 = new Vector(0, 0, 1);
    private static final Vector V2 = new Vector(1, 0, 0);

    /**
     * Test method for {@link geometries.impl.Geometries#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============

        // EP01: Some geometries intersect (but not all)
        List<Point> resultEP01 = GEOMETRIES.findIntersections(new Ray(P2, V1));
        assertNotNull(resultEP01, "Expected intersections");
        assertEquals(2, resultEP01.size(), ERROR_COUNT);

        // =============== Boundary Values Tests ==================

        // BV01: Empty geometries collection (0 points)
        assertNull(new Geometries().findIntersections(new Ray(P4, V1)), ERROR_NULL);

        // BV02: No geometries intersect (0 points)
        assertNull(GEOMETRIES.findIntersections(new Ray(P1, V2)), ERROR_NULL);

        // BV03: Only one geometry intersects
        List<Point> resultBV03 = GEOMETRIES.findIntersections(new Ray(P3, V1));
        assertNotNull(resultBV03, "Expected intersections");
        assertEquals(1, resultBV03.size(), ERROR_COUNT);

        // BV04: All geometries intersect
        List<Point> resultBV04 = GEOMETRIES.findIntersections(new Ray(P1, V1));
        assertNotNull(resultBV04, "Expected intersections");
        assertEquals(4, resultBV04.size(), ERROR_COUNT);
    }
}