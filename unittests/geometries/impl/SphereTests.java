package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.*;

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
}