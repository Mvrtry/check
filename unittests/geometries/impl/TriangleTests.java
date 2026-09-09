package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;

/**
 * Unit tests for class {@link Triangle}.
 */
class TriangleTests {

    // ============ Shared Fields ==============
    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;
    /** Error message for wrong result */
    private static final String ERROR_RESULT = "ERROR: wrong result";
    /** Error message for missing intersections */
    private static final String ERROR_NO_INTERSECTION = "ERROR: Ray does not intersect (expected null)";
    /** Error message for wrong number of intersections */
    private static final String ERROR_WRONG_NUM_OF_INTERSECTIONS = "ERROR: Wrong number of intersection points";

    // ============ Fields for testGetNormal ==============
    /** First vertex of the getNormal test triangle. Used in: testGetNormal. */
    private static final Point P1 = new Point(0, 0, 1);
    /** Second vertex of the getNormal test triangle. Used in: testGetNormal. */
    private static final Point P2 = new Point(1, 0, 0);
    /** Third vertex of the getNormal test triangle; also reused as a TRIANGLE_INT vertex and BV20 origin. Used in: testGetNormal, testFindIntersections. */
    private static final Point P3 = new Point(0, 1, 0);
    /** Centroid-like point strictly inside the getNormal triangle. Used in: testGetNormal EP01. */
    private static final Point P_INSIDE = new Point(1d / 3, 1d / 3, 1d / 3);

    // ============ Fields for testFindIntersections ==============
    /** Third vertex of TRIANGLE_INT. Used in: TRIANGLE_INT construction. */
    private static final Point P4 = new Point(-1, 0, 0);
    /** Triangle under test, vertices P3, P2, P4. Used in: testFindIntersections. */
    private static final Triangle TRIANGLE_INT = new Triangle(P3, P2, P4);

    /** Ray origin below the triangle's interior. Used in: EP01. */
    private static final Point P5 = new Point(0, 0.5, -1);
    /** Ray origin below and outside, facing an edge. Used in: EP02. */
    private static final Point P6 = new Point(2, 1, -1);
    /** Ray origin below and outside, facing a vertex. Used in: EP03. */
    private static final Point P7 = new Point(1, 1, -1);
    /** Ray origin below, aimed exactly at an edge. Used in: BV01. */
    private static final Point P8 = new Point(0.5, 0.5, -1);
    /** Ray origin below, aimed exactly at a vertex. Used in: BV02. */
    private static final Point P9 = new Point(0, 1, -1);
    /** Ray origin below, aimed at an edge's continuation. Used in: BV03. */
    private static final Point P10 = new Point(-2, 1, -1);
    /** Point inside the triangle, on its plane. Expected hit point for EP01/BV16; ray origin for BV14, BV17, BV19. */
    private static final Point P11 = new Point(0, 0.5, 0);
    /** Off-plane point above the triangle's plane. Used in: EP04, BV15, BV18. */
    private static final Point P12 = new Point(0, 3, 1);
    /** Ray origin below the plane, projects onto P11 along V1. Used in: BV16. */
    private static final Point P13 = new Point(0, 0.5, -2);

    /** Direction orthogonal to the triangle's plane. Used in: EP01, BV16, BV17, BV18. */
    private static final Vector V1 = new Vector(0, 0, 1);
    /** Oblique direction used for the plane-hit-position cases. Used in: EP02, EP03, BV01, BV02, BV03, EP04, BV19, BV20. */
    private static final Vector V2 = new Vector(0, -1, 1);
    /** Direction parallel to the triangle's plane. Used in: BV14, BV15. */
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