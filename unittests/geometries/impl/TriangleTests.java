package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.*;

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
}