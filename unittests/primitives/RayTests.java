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
}