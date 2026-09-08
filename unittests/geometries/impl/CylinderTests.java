package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.*;

/**
 * Unit tests for class {@link Cylinder}.
 */
class CylinderTests {

    // ============ Shared Fields ==============
    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;
    /** Error message for wrong result */
    private static final String ERROR_RESULT = "ERROR: wrong result";

    // ============ Fields for testGetNormal ==============
    /** Origin of cylinder axis; also center of lower base. Used in: BV02. */
    private static final Point AXIS_ORIGIN = new Point(0, 0, 0);
    /** Direction of cylinder axis, +Z. */
    private static final Vector AXIS_DIR = new Vector(0, 0, 1);
    /** Radius of the cylinder under test. */
    private static final double RADIUS = 1d;
    /** Height of the cylinder under test. */
    private static final double HEIGHT = 2d;
    /** Cylinder under test. Used in: testGetNormal. */
    private static final Cylinder CYLINDER = new Cylinder(RADIUS, new Ray(AXIS_ORIGIN, AXIS_DIR), HEIGHT);

    /** Point on the lateral surface. Used in: EP01. */
    private static final Point P_SIDE = new Point(0, 1, 1);

    /** Point on upper base, off-center. Used in: EP02. */
    private static final Point P_UPPER = new Point(0, 0.5, 2);
    /** Point on lower base, off-center. Used in: EP03. */
    private static final Point P_LOWER = new Point(0, 0.5, 0);
    /** Center of the upper base. Used in: BV01. */
    private static final Point P_UPPER_CENTER = new Point(0, 0, 2);
    /** Edge of the upper base. Used in: BV03. */
    private static final Point P_UPPER_EDGE = new Point(0, 1, 2);
    /** Expected normal for the upper base. Used in: EP02, BV01, BV03. */
    private static final Vector NORMAL_UPPER = new Vector(0, 0, 1);

    /** Expected normal for the lower base. Used in: EP03, BV02, BV04. */
    private static final Vector NORMAL_LOWER = new Vector(0, 0, -1);
    /** Edge of the lower base. Used in: BV04. */
    private static final Point P_LOWER_EDGE = new Point(0, 1, 0);

    /**
     * Test method for {@link Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Point on the side of the cylinder
        Vector normalSide = CYLINDER.getNormal(P_SIDE);
        assertEquals(1d, normalSide.length(), DELTA, ERROR_RESULT);
        assertEquals(0d, normalSide.dotProduct(AXIS_DIR), DELTA, ERROR_RESULT);

        // EP02: Point on the upper base
        assertEquals(NORMAL_UPPER, CYLINDER.getNormal(P_UPPER), ERROR_RESULT);

        // EP03: Point on the lower base
        assertEquals(NORMAL_LOWER, CYLINDER.getNormal(P_LOWER), ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Center of the upper base
        assertEquals(NORMAL_UPPER, CYLINDER.getNormal(P_UPPER_CENTER), ERROR_RESULT);

        // BV02: Center of the lower base
        assertEquals(NORMAL_LOWER, CYLINDER.getNormal(AXIS_ORIGIN), ERROR_RESULT);

        // BV03: Edge of the upper base
        assertEquals(NORMAL_UPPER, CYLINDER.getNormal(P_UPPER_EDGE), ERROR_RESULT);

        // BV04: Edge of the lower base
        assertEquals(NORMAL_LOWER, CYLINDER.getNormal(P_LOWER_EDGE), ERROR_RESULT);
    }
}