package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;

/**
 * Unit tests for class {@link Triangle}.
 */
class TriangleTests {
    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;
    /** Error message for wrong result */
    private static final String ERROR_RESULT = "ERROR: wrong result";

    /** First point for triangle tests */
    private static final Point P1 = new Point(0, 0, 1);
    /** Second point for triangle tests */
    private static final Point P2 = new Point(1, 0, 0);
    /** Third point for triangle tests */
    private static final Point P3 = new Point(0, 1, 0);

    /** Point strictly inside the triangle for EP test */
    private static final Point P_INSIDE = new Point(1d / 3, 1d / 3, 1d / 3);

    // ============ Fields for findIntersections test ==============
    /** Error message for missing intersections */
    private static final String ERROR_NO_INTERSECTION = "ERROR: Ray does not intersect (expected null)";
    /** Error message for wrong number of intersections */
    private static final String ERROR_WRONG_NUM_OF_INTERSECTIONS = "ERROR: Wrong number of intersection points";

    private static final Point P4 = new Point(-1, 0, 0);
    private static final Triangle TRIANGLE_INT = new Triangle(P3, P2, P4); // Using original P3(0,1,0), P2(1,0,0) and new P4(-1,0,0)

    private static final Point P5 = new Point(0, 0.5, -1);
    private static final Point P6 = new Point(2, 1, -1);
    private static final Point P7 = new Point(1, 1, -1);
    private static final Point P8 = new Point(0.5, 0.5, -1);
    private static final Point P9 = new Point(0, 1, -1);
    private static final Point P10 = new Point(-2, 1, -1);
    private static final Point P11 = new Point(0, 0.5, 0);
    /** Off-plane point (z=1) used for parallel/orthogonal inherited-plane tests */
    private static final Point P12 = new Point(0, 3, 1);
    /** Below-plane point that projects to P11 when fired along V1 */
    private static final Point P13 = new Point(0, 0.5, -2);

    private static final Vector V1 = new Vector(0, 0, 1);
    private static final Vector V2 = new Vector(0, -1, 1);
    /** In-plane direction vector (parallel to TRIANGLE_INT's plane) */
    private static final Vector V3 = new Vector(1, 0, 0);

    /**
     * Test method for {@link Triangle#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Triangle tr = new Triangle(P1, P2, P3);

        // ============ Equivalence Partitions Tests ==============
        // EP01: Normal from a point inside the triangle
        Vector normal = tr.getNormal(P_INSIDE);
        assertEquals(1d, normal.length(), DELTA, ERROR_RESULT);
        assertEquals(0d, normal.dotProduct(P1.subtract(P2)), DELTA, ERROR_RESULT);
        assertEquals(0d, normal.dotProduct(P2.subtract(P3)), DELTA, ERROR_RESULT);
    }

    /**
     * Test method for {@link geometries.impl.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============

        // EP01: Ray intersects inside the triangle
        List<Point> resultEP01 = TRIANGLE_INT.findIntersections(new Ray(P5, V1));
        assertNotNull(resultEP01, ERROR_RESULT);
        assertEquals(1, resultEP01.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);
        assertEquals(List.of(P11), resultEP01, ERROR_RESULT);

        // EP02: Ray intersects outside the triangle against edge
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P6, V2)), ERROR_NO_INTERSECTION);

        // EP03: Ray intersects outside the triangle against vertex
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P7, V2)), ERROR_NO_INTERSECTION);

        // =============== Boundary Values Tests ==================

        // BV01: Ray intersects on edge of triangle
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P8, V2)), ERROR_NO_INTERSECTION);

        // BV02: Ray intersects on vertex of triangle
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P9, V2)), ERROR_NO_INTERSECTION);

        // BV03: Ray intersects on edge's continuation
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P10, V2)), ERROR_NO_INTERSECTION);

        // **** Group: Inherited Plane Cases ****

        // EP04: Ray is oblique to the plane and does not intersect it (t <= 0)
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P12, V2)), ERROR_NO_INTERSECTION);

        // BV14: Ray is parallel to the plane and included in it
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P11, V3)), ERROR_NO_INTERSECTION);

        // BV15: Ray is parallel to the plane and not included in it
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P12, V3)), ERROR_NO_INTERSECTION);

        // BV16: Ray is orthogonal to the plane, starts before it, and lands inside the triangle (1 point)
        List<Point> resultBV16 = TRIANGLE_INT.findIntersections(new Ray(P13, V1));
        assertNotNull(resultBV16, ERROR_RESULT);
        assertEquals(1, resultBV16.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);
        assertEquals(List.of(P11), resultBV16, ERROR_RESULT);

        // BV17: Ray is orthogonal to the plane and starts exactly in the plane
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P11, V1)), ERROR_NO_INTERSECTION);

        // BV18: Ray is orthogonal to the plane and starts after it
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P12, V1)), ERROR_NO_INTERSECTION);

        // BV19: Ray is oblique, begins in the plane, but not at the plane's reference point Q
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P11, V2)), ERROR_NO_INTERSECTION);

        // BV20: Ray is oblique and begins exactly at the plane's reference point Q
        assertNull(TRIANGLE_INT.findIntersections(new Ray(P3, V2)), ERROR_NO_INTERSECTION);
    }
}