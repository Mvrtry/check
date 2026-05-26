package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;

/**
 * Unit tests for class {@link Plane}.
 */
class PlaneTests {
    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;
    /** Error message for expected exception. */
    private static final String ERROR_EXCEPTION = "ERROR: Exception was not thrown";
    /** Error message for wrong result. */
    private static final String ERROR_RESULT = "ERROR: Wrong result value";

    /** First point for plane tests */
    private static final Point P1 = new Point(0, 0, 1);
    /** Second point for plane tests */
    private static final Point P2 = new Point(1, 0, 0);
    /** Third point for plane tests */
    private static final Point P3 = new Point(0, 1, 0);

    /** Point for BV collinear tests */
    private static final Point P_LINE1 = new Point(1, 1, 1);
    /** Point for BV collinear tests */
    private static final Point P_LINE2 = new Point(2, 2, 2);
    /** Point for BV collinear tests */
    private static final Point P_LINE3 = new Point(3, 3, 3);

    /** Point for EP normal test (not the reference point) */
    private static final Point P_NORMAL_EP = new Point(0.5, 0.5, 0);

    // ============ Fields for findIntersections test ==============
    /** Error message for missing intersections */
    private static final String ERROR_NO_INTERSECTION = "ERROR: Ray does not intersect (expected null)";
    /** Error message for wrong number of intersections */
    private static final String ERROR_WRONG_NUM_OF_INTERSECTIONS = "ERROR: Wrong number of intersection points";

    private static final Point P4 = new Point(0, 0, 0);
    private static final Point P5 = new Point(0, 0, -1);
    private static final Point P6 = new Point(0, 0, 2);

    private static final Vector V1 = new Vector(0, 0, 1);
    private static final Vector V2 = new Vector(0, 1, 0);
    private static final Vector V3 = new Vector(1, 0, 0);
    private static final Vector V4 = new Vector(1, 1, 1);

    private static final Plane PLANE_INT = new Plane(P1, V1); // P1 is (0,0,1) from original file

    /**
     * Test method for {@link Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testConstructorThreePoints() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Test that a valid plane is created with three non-collinear points.
        assertDoesNotThrow(() -> new Plane(P1, P2, P3), "Failed constructing a correct plane");

        // =============== Boundary Values Tests ==================
        // BV01: Test construction with two points coinciding (1st and 2nd).
        assertThrows(IllegalArgumentException.class, () -> new Plane(P1, P1, P3), ERROR_EXCEPTION);

        // BV02: Test construction with two points coinciding (1st and 3rd).
        assertThrows(IllegalArgumentException.class, () -> new Plane(P1, P2, P1), ERROR_EXCEPTION);

        // BV03: Test construction with two points coinciding (2nd and 3rd).
        assertThrows(IllegalArgumentException.class, () -> new Plane(P1, P2, P2), ERROR_EXCEPTION);

        // BV04: Test construction with three points coinciding.
        assertThrows(IllegalArgumentException.class, () -> new Plane(P1, P1, P1), ERROR_EXCEPTION);

        // BV05: Test construction with three points on the same line.
        assertThrows(IllegalArgumentException.class, () -> new Plane(P_LINE1, P_LINE2, P_LINE3), ERROR_EXCEPTION);
    }

    /**
     * Test method for {@link Plane#Plane(Point, Vector)}.
     */
    @Test
    void testConstructorPointVector() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Test that the normal is properly normalized when using the Point & Vector constructor
        Vector unnormalizedVector = new Vector(0, 0, 5); // Length is 5
        Plane pl = new Plane(P1, unnormalizedVector);
        assertEquals(1d, pl.getNormal(null).length(), DELTA, "Normal vector was not normalized in constructor");
    }

    /**
     * Test method for {@link Plane#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Plane pl = new Plane(P1, P2, P3);

        // ============ Equivalence Partitions Tests ==============
        // EP01: Test normal at a point on the plane which is not the reference point.
        Vector normal = pl.getNormal(P_NORMAL_EP);
        assertEquals(1d, normal.length(), DELTA, ERROR_RESULT);
        // Ensure the normal is truly orthogonal to the plane
        assertEquals(0d, normal.dotProduct(P1.subtract(P2)), DELTA, ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Test normal calculation at the reference point of the plane.
        Vector refNormal = pl.getNormal(P1);
        assertEquals(1d, refNormal.length(), DELTA, ERROR_RESULT);
    }

    /**
     * Test method for {@link geometries.impl.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============

        // EP01: Ray intersects the plane (1 point)
        List<Point> resultEP01 = PLANE_INT.findIntersections(new Ray(P4, V1));
        assertNotNull(resultEP01, ERROR_RESULT);
        assertEquals(1, resultEP01.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);
        assertEquals(List.of(P1), resultEP01, ERROR_RESULT);

        // EP02: Ray does not intersect the plane
        assertNull(PLANE_INT.findIntersections(new Ray(P4, V2)), ERROR_NO_INTERSECTION);

        // =============== Boundary Values Tests ==================

        // **** Group 1: Ray is parallel to the plane

        // BV01: Ray is included in the plane
        assertNull(PLANE_INT.findIntersections(new Ray(P1, V3)), ERROR_NO_INTERSECTION);

        // BV02: Ray is not included in the plane
        assertNull(PLANE_INT.findIntersections(new Ray(P4, V3)), ERROR_NO_INTERSECTION);

        // **** Group 2: Ray is orthogonal to the plane

        // BV03: Ray starts before the plane
        List<Point> resultBV03 = PLANE_INT.findIntersections(new Ray(P5, V1));
        assertNotNull(resultBV03, ERROR_RESULT);
        assertEquals(1, resultBV03.size(), ERROR_WRONG_NUM_OF_INTERSECTIONS);
        assertEquals(List.of(P1), resultBV03, ERROR_RESULT);

        // BV04: Ray starts in the plane
        assertNull(PLANE_INT.findIntersections(new Ray(P1, V1)), ERROR_NO_INTERSECTION);

        // BV05: Ray starts after the plane
        assertNull(PLANE_INT.findIntersections(new Ray(P6, V1)), ERROR_NO_INTERSECTION);

        // **** Group 3: Ray is neither orthogonal nor parallel to the plane

        // BV06: Ray begins in the plane (P0 is in the plane, but not the ray)
        // Using P_LINE1 (1,1,1) which sits on the plane z=1, but is not the reference point
        assertNull(PLANE_INT.findIntersections(new Ray(P_LINE1, V4)), ERROR_NO_INTERSECTION);

        // BV07: Ray begins in the plane exactly at the reference point of the plane (Q)
        // Using P1 (0,0,1) which is exactly the reference point of PLANE_INT
        assertNull(PLANE_INT.findIntersections(new Ray(P1, V4)), ERROR_NO_INTERSECTION);
    }
}