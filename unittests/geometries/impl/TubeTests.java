package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.*;

/**
 * Unit tests for class {@link Tube}.
 */
class TubeTests {
    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;
    /** Error message for wrong result */
    private static final String ERROR_RESULT = "ERROR: wrong result";

    /** Origin point of the axis ray */
    private static final Point AXIS_ORIGIN = new Point(0, 0, 0);
    /** Direction vector of the axis ray */
    private static final Vector AXIS_DIR = new Vector(0, 1, 0);

    /** Point on the tube in front of the ray's origin */
    private static final Point P_FRONT = new Point(1, 5, 0);
    /** Point on the tube behind the ray's origin */
    private static final Point P_BEHIND = new Point(1, -5, 0);
    /** Point exactly opposite the ray's origin */
    private static final Point P_OPPOSITE = new Point(1, 0, 0);

    /**
     * Test method for {@link Tube#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Tube tube = new Tube(1d, new Ray(AXIS_ORIGIN, AXIS_DIR));

        // ============ Equivalence Partitions Tests ==============
        // EP01: Point on the tube in front of the ray's origin (t > 0)
        Vector normalFront = tube.getNormal(P_FRONT);
        assertEquals(1d, normalFront.length(), DELTA, ERROR_RESULT);
        assertEquals(0d, normalFront.dotProduct(AXIS_DIR), DELTA, ERROR_RESULT);

        // EP02: Point on the tube behind the ray's origin (t < 0)
        Vector normalBehind = tube.getNormal(P_BEHIND);
        assertEquals(1d, normalBehind.length(), DELTA, ERROR_RESULT);
        assertEquals(0d, normalBehind.dotProduct(AXIS_DIR), DELTA, ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Point exactly opposite the ray's origin (t = 0)
        Vector normalOpposite = tube.getNormal(P_OPPOSITE);
        assertEquals(1d, normalOpposite.length(), DELTA, ERROR_RESULT);
        assertEquals(0d, normalOpposite.dotProduct(AXIS_DIR), DELTA, ERROR_RESULT);
    }
}