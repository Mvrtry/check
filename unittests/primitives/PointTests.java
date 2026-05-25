package primitives;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for class {@link Point}.
 */
class PointTests {

    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;
    /** Error message for wrong result */
    private static final String ERROR_RESULT = "ERROR: wrong result";
    /** Error message for expected exception */
    private static final String ERROR_EXCEPTION = "ERROR: Exception was not thrown";

    /** Point (1, 2, 3) used in several tests */
    private static final Point P1 = new Point(1, 2, 3);
    /** Point (4, 5, 6) used in several tests */
    private static final Point P2 = new Point(4, 5, 6);
    /** Point (1, 4, 9) used for distance tests */
    private static final Point P3 = new Point(1, 4, 9);
    /** Point (1, 4, 1) used for distance tests */
    private static final Point P4 = new Point(1, 4, 1);
    /** Origin point (0, 0, 0) */
    private static final Point ZERO_POINT = new Point(0, 0, 0);

    /** Vector (3, 3, 3) used for addition/subtraction */
    private static final Vector V1 = new Vector(3, 3, 3);

    /**
     * Test method for {@link Point#add(Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Simple addition of Vector
        assertEquals(P2, P1.add(V1), ERROR_RESULT);

        // EP02: Addition of Vector with negative coordinates
        assertEquals(ZERO_POINT, P1.add(new Vector(-1, -2, -3)), ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Addition to the zero point
        assertEquals(new Point(3, 3, 3), ZERO_POINT.add(V1), ERROR_RESULT);
    }

    /**
     * Test method for {@link Point#subtract(Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Simple subtraction of Point
        assertEquals(V1, P2.subtract(P1), ERROR_RESULT);

        // EP02: Subtraction of Point with negative values
        assertEquals(new Vector(5, 7, 9), P1.subtract(new Point(-4, -5, -6)), ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Subtraction of zero Point
        assertEquals(new Vector(1, 2, 3), P1.subtract(ZERO_POINT), ERROR_RESULT);

        // BV02: Subtracting identical point (should throw exception due to zero vector creation)
        assertThrows(IllegalArgumentException.class, () -> P1.subtract(P1), ERROR_EXCEPTION);
    }

    /**
     * Test method for {@link Point#distanceSquared(Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Distance squared between two points
        assertEquals(64d, P3.distanceSquared(P4), DELTA, ERROR_RESULT);

        // EP02: Distance squared with negative coordinates
        assertEquals(56d, P1.distanceSquared(new Point(-1, -2, -3)), DELTA, ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Distance squared of Point from itself
        assertEquals(0d, P1.distanceSquared(P1), DELTA, ERROR_RESULT);

        // BV02: Distance squared from the origin Point
        assertEquals(14d, P1.distanceSquared(ZERO_POINT), DELTA, ERROR_RESULT);
    }

    /**
     * Test method for {@link Point#distance(Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Distance between two points
        assertEquals(8d, P3.distance(P4), DELTA, ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Distance of Point from itself
        assertEquals(0d, P1.distance(P1), DELTA, ERROR_RESULT);

        // BV02: Distance from the origin Point
        assertEquals(5d, new Point(4, 3, 0).distance(ZERO_POINT), DELTA, ERROR_RESULT);
    }
}