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

    // ============ Shared Fields ==============
    /** Error message for missing intersections */
    private static final String ERROR_NULL = "ERROR: Expected null but got a list";
    /** Error message for wrong number of intersections */
    private static final String ERROR_COUNT = "ERROR: Wrong number of intersection points";

    // ============ Fields for Geometries setup ==============
    /** Reference point on PLANE, Z = 5. */
    private static final Point PLANE_POINT = new Point(0, 0, 5);
    /** Normal of PLANE, +Z. */
    private static final Vector PLANE_NORMAL = new Vector(0, 0, 1);
    /** Plane at Z = 5. Used in: testFindIntersections. */
    private static final Plane PLANE = new Plane(PLANE_POINT, PLANE_NORMAL);

    /** Center of SPHERE, Z = 2. */
    private static final Point SPHERE_CENTER = new Point(0, 0, 2);
    /** Radius of SPHERE. */
    private static final double SPHERE_RADIUS = 1d;
    /** Unit sphere at Z = 2. Used in: testFindIntersections. */
    private static final Sphere SPHERE = new Sphere(SPHERE_CENTER, SPHERE_RADIUS);

    /** Vertex of TRIANGLE, Z = 10. */
    private static final Point TRIANGLE_V1 = new Point(0, 1, 10);
    /** Vertex of TRIANGLE, Z = 10. */
    private static final Point TRIANGLE_V2 = new Point(1, -1, 10);
    /** Vertex of TRIANGLE, Z = 10. */
    private static final Point TRIANGLE_V3 = new Point(-1, -1, 10);
    /** Triangle at Z = 10. Used in: testFindIntersections. */
    private static final Triangle TRIANGLE = new Triangle(TRIANGLE_V1, TRIANGLE_V2, TRIANGLE_V3);

    /** Composite of PLANE, SPHERE, TRIANGLE. Used in: testFindIntersections. */
    private static final Geometries GEOMETRIES = new Geometries(PLANE, SPHERE, TRIANGLE);

    // ============ Fields for testFindIntersections ==============
    /** Ray origin before all geometries. Used in: BV02, BV04. */
    private static final Point P1 = new Point(0, 0, -1);
    /** Ray origin between sphere and plane. Used in: EP01. */
    private static final Point P2 = new Point(0, 0, 4);
    /** Ray origin between plane and triangle. Used in: BV03. */
    private static final Point P3 = new Point(0, 0, 6);
    /** Arbitrary origin for the empty-collection case. Used in: BV01. */
    private static final Point P4 = new Point(0, 0, 0);

    /** Direction +Z, through all geometries. Used in: EP01, BV01, BV03, BV04. */
    private static final Vector V1 = new Vector(0, 0, 1);
    /** Direction +X, misses all geometries. Used in: BV02. */
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