package primitives;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for class {@link Ray}.
 */
class RayTests {
    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;
    /** Error message for wrong result */
    private static final String ERROR_RESULT = "ERROR: wrong result";

    /** Base point for ray tests */
    private static final Point P1 = new Point(1, 2, 3);
    /** Unnormalized direction vector for ray tests */
    private static final Vector DIR = new Vector(0, 0, 5);

    // ============ Fields for getPoint test ==============
    private static final Ray RAY = new Ray(P1, DIR); // Ray origin: (1,2,3), direction: (0,0,1)
    private static final Point P2 = new Point(1, 2, 5); // Expected point for t = 2
    private static final Point P3 = new Point(1, 2, 1); // Expected point for t = -2

    /**
     * Test method for {@link Ray#Ray(Point, Vector)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Check if constructor properly normalizes the direction vector
        Ray ray = new Ray(P1, DIR);

        // Verify the length of the direction is exactly 1
        assertEquals(1d, ray.direction().length(), DELTA, ERROR_RESULT);

        // Verify the direction is correctly calculated
        assertEquals(new Vector(0, 0, 1), ray.direction(), ERROR_RESULT);
    }

    /**
     * Test method for {@link Ray#getPoint(double)}.
     */
    @Test
    void testGetPoint() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: t > 0 (Positive distance)
        assertEquals(P2, RAY.getPoint(2), ERROR_RESULT);

        // EP02: t < 0 (Negative distance)
        assertEquals(P3, RAY.getPoint(-2), ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: t = 0 (Zero distance, should return origin point)
        assertEquals(P1, RAY.getPoint(0), ERROR_RESULT);
    }
}