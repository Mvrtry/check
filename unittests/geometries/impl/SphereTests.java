package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;

/**
 * Unit tests for class {@link Sphere}.
 */
class SphereTests {
    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;
    /** Error message for wrong result */
    private static final String ERROR_RESULT = "ERROR: wrong result";

    /** Center point of the sphere */
    private static final Point CENTER = new Point(1, 1, 1);
    /** Point on the sphere surface */
    private static final Point SURFACE_POINT = new Point(1, 2, 1);
    /** Expected normal vector */
    private static final Vector EXPECTED_NORMAL = new Vector(0, 1, 0);

    // ============ Fields for findIntersections test ==============
    /** Error message for missing intersections */
    private static final String ERROR_NO_INTERSECTION = "ERROR: Ray does not intersect (expected null)";
    /** Error message for wrong number of intersections */
    private static final String ERROR_WRONG_NUM_OF_INTERSECTIONS = "ERROR: Wrong number of intersection points";

    private static final Point CENTER_INT = new Point(1, 0, 0);
    private static final Sphere SPHERE_INT = new Sphere(CENTER_INT, 1d);

    private static final Point P1 = new Point(-1, 0, 0);
    private static final Point P2 = new Point(1, 0.5, 0);
    private static final Point P3 = new Point(3, 0, 0);
    private static final Point P4 = new Point(2, 2, 2);
    private static final Point P5 = new Point(0, 0, 0);
    private static final Point P6 = new Point(1, 0, 0);
    private static final Point P7 = new Point(0, 1, -1);
    private static final Point P8 = new Point(0, 1, 0);
    private static final Point P9 = new Point(0, 1, 1);
    private static final Point P10 = new Point(2, 0, 0);
    private static final Point P11 = new Point(1, 1, 0);

    private static final Vector V1 = new Vector(1, 1, 0);
    private static final Vector V2 = new Vector(3, 0, 0);
    private static final Vector V3 = new Vector(0, 1, 0);
    private static final Vector V4 = new Vector(1, 0, 0);
    private static final Vector V5 = new Vector(-1, 0, 0);
    private static final Vector V6 = new Vector(0, 0, 1);

    /**
     * Test method for {@link Sphere#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Sphere sp = new Sphere(CENTER, 1d);

        // ============ Equivalence Partitions Tests ==============
        // EP01: Normal on sphere surface
        Vector normal = sp.getNormal(SURFACE_POINT);
        assertEquals(EXPECTED_NORMAL, normal, ERROR_RESULT);
        assertEquals(1d, normal.length(), DELTA, ERROR_RESULT);
    }

    /**
     * Test method for {@link geometries.impl.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============

        // EP01: Ray's line is outside the sphere (0 points)
        assertNull(SPHERE_INT.findIntersections(new Ray(P1, V1)), ERROR_NO_INTERSECTION);

        // EP02: Ray starts before and crosses the sphere (2 points)
        List<Point> resultEP02 = SPHERE_INT.findIntersections(new Ray(P1, V2));
        assertNotNull(resultEP02, ERROR_RESULT);
        assertEquals(2, resultEP02.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);
        assertEquals(List.of(P5, P10), resultEP02, ERROR_RESULT);

        // EP03: Ray starts inside the sphere (1 point)
        List<Point> resultEP03 = SPHERE_INT.findIntersections(new Ray(P2, V3));
        assertNotNull(resultEP03, ERROR_RESULT);
        assertEquals(1, resultEP03.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);
        assertEquals(List.of(P11), resultEP03, ERROR_RESULT);

        // EP04: Ray starts after the sphere (0 points)
        assertNull(SPHERE_INT.findIntersections(new Ray(P3, V4)), ERROR_NO_INTERSECTION);

        // =============== Boundary Values Tests ==================

        // **** Group 1: Ray's line crosses the sphere (but not the center)

        // BV01: Ray starts at sphere and goes inside (1 point)
        List<Point> resultBV01 = SPHERE_INT.findIntersections(new Ray(P5, V4));
        assertNotNull(resultBV01, ERROR_RESULT);
        assertEquals(1, resultBV01.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);
        assertEquals(List.of(P10), resultBV01, ERROR_RESULT);

        // BV02: Ray starts at sphere and goes outside (0 points)
        assertNull(SPHERE_INT.findIntersections(new Ray(P5, V5)), ERROR_NO_INTERSECTION);

        // **** Group 2: Ray's line goes through the center

        // BV03: Ray starts before the sphere (2 points)
        List<Point> resultBV03 = SPHERE_INT.findIntersections(new Ray(P1, V4));
        assertNotNull(resultBV03, ERROR_RESULT);
        assertEquals(2, resultBV03.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);

        // BV04: Ray starts at sphere and goes inside (1 point)
        List<Point> resultBV04 = SPHERE_INT.findIntersections(new Ray(P5, V4));
        assertNotNull(resultBV04, ERROR_RESULT);
        assertEquals(1, resultBV04.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);

        // BV05: Ray starts inside (1 point)
        List<Point> resultBV05 = SPHERE_INT.findIntersections(new Ray(P2, V4));
        assertNotNull(resultBV05, ERROR_RESULT);
        assertEquals(1, resultBV05.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);

        // BV06: Ray starts at the center (1 point)
        List<Point> resultBV06 = SPHERE_INT.findIntersections(new Ray(P6, V4));
        assertNotNull(resultBV06, ERROR_RESULT);
        assertEquals(1, resultBV06.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);
        assertEquals(List.of(P10), resultBV06, ERROR_RESULT);

        // BV07: Ray starts at sphere and goes outside (0 points)
        assertNull(SPHERE_INT.findIntersections(new Ray(P10, V4)), ERROR_NO_INTERSECTION);

        // BV08: Ray starts after sphere (0 points)
        assertNull(SPHERE_INT.findIntersections(new Ray(P3, V4)), ERROR_NO_INTERSECTION);

        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)

        // BV09: Ray starts before the tangent point
        assertNull(SPHERE_INT.findIntersections(new Ray(P7, V6)), ERROR_NO_INTERSECTION);

        // BV10: Ray starts at the tangent point
        assertNull(SPHERE_INT.findIntersections(new Ray(P8, V6)), ERROR_NO_INTERSECTION);

        // BV11: Ray starts after the tangent point
        assertNull(SPHERE_INT.findIntersections(new Ray(P9, V6)), ERROR_NO_INTERSECTION);

        // **** Group 4: Special cases

        // BV12: Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line
        assertNull(SPHERE_INT.findIntersections(new Ray(P3, V3)), ERROR_NO_INTERSECTION);

        // BV13: Ray starts inside, ray is orthogonal to ray start to sphere's center line (1 point)
        List<Point> resultBV13 = SPHERE_INT.findIntersections(new Ray(P2, V6));
        assertNotNull(resultBV13, ERROR_RESULT);
        assertEquals(1, resultBV13.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);
    }
}