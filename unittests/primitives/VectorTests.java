package primitives;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for class {@link Vector}.
 */
class VectorTests {
    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;
    /** Error message for expected exception */
    private static final String ERROR_EXCEPTION = "ERROR: Exception was not thrown";
    /** Error message for wrong result */
    private static final String ERROR_RESULT = "ERROR: wrong result";

    /** Vector (1, 2, 3) used in several tests */
    private static final Vector V1 = new Vector(1, 2, 3);
    /** Opposite vector to V1 (-1, -2, -3) */
    private static final Vector V1_OPPOSITE = new Vector(-1, -2, -3);
    /** Orthogonal vector to V1 (0, 3, -2) */
    private static final Vector V2 = new Vector(0, 3, -2);
    /** Vector (1, 1, 1) used for simple arithmetic */
    private static final Vector V3 = new Vector(1, 1, 1);

    /**
     * Test method for {@link Vector#Vector(double, double, double)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Simple construction of a non-zero vector
        assertDoesNotThrow(() -> new Vector(1, 2, 3), "Failed constructing a correct vector");

        // =============== Boundary Values Tests ==================
        // BV01: Construction of the zero vector (should throw)
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), ERROR_EXCEPTION);
    }

    /**
     * Test method for {@link Vector#add(Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Simple addition of two vectors
        assertEquals(new Vector(2, 3, 4), V1.add(V3), ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Adding opposite vector (should throw zero vector exception)
        assertThrows(IllegalArgumentException.class, () -> V1.add(V1_OPPOSITE), ERROR_EXCEPTION);
    }

    /**
     * Test method for {@link Vector#subtract(Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Simple subtraction of a vector from a vector
        assertEquals(new Vector(0, 1, 2), V1.subtract(V3), ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Subtracting a vector from itself (should throw zero vector exception)
        assertThrows(IllegalArgumentException.class, () -> V1.subtract(V1), ERROR_EXCEPTION);
    }

    /**
     * Test method for {@link Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Normal scaling by a positive number
        assertEquals(new Vector(2, 4, 6), V1.scale(2), ERROR_RESULT);

        // EP02: Normal scaling by a negative number
        assertEquals(new Vector(-2, -4, -6), V1.scale(-2), ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Scale by zero (should throw zero vector exception)
        assertThrows(IllegalArgumentException.class, () -> V1.scale(0), ERROR_EXCEPTION);
    }

    /**
     * Test method for {@link Vector#dotProduct(Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Normal dot product calculation between acute angle vectors
        assertEquals(6d, V1.dotProduct(V3), DELTA, ERROR_RESULT);

        // EP02: Dot product between opposite direction vectors
        assertEquals(-14d, V1.dotProduct(V1_OPPOSITE), DELTA, ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Dot product of orthogonal vectors (must be exactly zero)
        assertEquals(0d, V1.dotProduct(V2), DELTA, ERROR_RESULT);
    }

    /**
     * Test method for {@link Vector#crossProduct(Vector)}.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Normal cross product (tests orthogonality and correct length)
        Vector vr = V1.crossProduct(V2);

        // Test that the result is orthogonal to both original vectors
        assertEquals(0d, vr.dotProduct(V1), DELTA, ERROR_RESULT);
        assertEquals(0d, vr.dotProduct(V2), DELTA, ERROR_RESULT);

        // Test that the length of the cross product is correct (since V1 and V2 are orthogonal)
        assertEquals(V1.length() * V2.length(), vr.length(), DELTA, ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Cross product of a vector with itself
        assertThrows(IllegalArgumentException.class, () -> V1.crossProduct(V1), ERROR_EXCEPTION);

        // BV02: Cross product of parallel (opposite) vectors
        assertThrows(IllegalArgumentException.class, () -> V1.crossProduct(V1_OPPOSITE), ERROR_EXCEPTION);
    }

    /**
     * Test method for {@link Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Normal length squared calculation
        assertEquals(14d, V1.lengthSquared(), DELTA, ERROR_RESULT);
    }

    /**
     * Test method for {@link Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Normal length calculation
        assertEquals(Math.sqrt(14d), V1.length(), DELTA, ERROR_RESULT);
    }

    /**
     * Test method for {@link Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        Vector n = V1.normalize();

        // EP01: Test normalized length is exactly 1
        assertEquals(1d, n.length(), DELTA, ERROR_RESULT);

        // EP02: Test the normalized vector is parallel to the original (throws exception on cross product)
        assertThrows(IllegalArgumentException.class, () -> V1.crossProduct(n), ERROR_EXCEPTION);

        // EP03: Test the normalized vector is in the same direction as the original
        assertTrue(V1.dotProduct(n) > 0, ERROR_RESULT);
    }
}