package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.*;

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
}