package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.*;

/**
 * Unit tests for class {@link Cylinder}.
 */
class CylinderTests {
    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;
    /** Error message for wrong result */
    private static final String ERROR_RESULT = "ERROR: wrong result";

    /** Origin point of the axis ray */
    private static final Point AXIS_ORIGIN = new Point(0, 0, 0);
    /** Direction vector of the axis ray */
    private static final Vector AXIS_DIR = new Vector(0, 0, 1);

    /** Point on the side of the cylinder */
    private static final Point P_SIDE = new Point(0, 1, 1);

    /** Point on the upper base */
    private static final Point P_UPPER = new Point(0, 0.5, 2);
    /** Point on the lower base */
    private static final Point P_LOWER = new Point(0, 0.5, 0);
    /** Point at the center of the upper base */
    private static final Point P_UPPER_CENTER = new Point(0, 0, 2);
    /** Point at the edge of the upper base */
    private static final Point P_UPPER_EDGE = new Point(0, 1, 2);
    /** Expected normal for upper base points */
    private static final Vector NORMAL_UPPER = new Vector(0, 0, 1);

    /** Expected normal for lower base points */
    private static final Vector NORMAL_LOWER = new Vector(0, 0, -1);
    /** Point at the edge of the lower base */
    private static final Point P_LOWER_EDGE = new Point(0, 1, 0);

    /**
     * Test method for {@link Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Cylinder cyl = new Cylinder(1d, new Ray(AXIS_ORIGIN, AXIS_DIR), 2d);

        // ============ Equivalence Partitions Tests ==============
        // EP01: Point on the side of the cylinder
        Vector normalSide = cyl.getNormal(P_SIDE);
        assertEquals(1d, normalSide.length(), DELTA, ERROR_RESULT);
        assertEquals(0d, normalSide.dotProduct(AXIS_DIR), DELTA, ERROR_RESULT);

        // EP02: Point on the upper base
        assertEquals(NORMAL_UPPER, cyl.getNormal(P_UPPER), ERROR_RESULT);

        // EP03: Point on the lower base
        assertEquals(NORMAL_LOWER, cyl.getNormal(P_LOWER), ERROR_RESULT);

        // =============== Boundary Values Tests ==================
        // BV01: Center of the upper base
        assertEquals(NORMAL_UPPER, cyl.getNormal(P_UPPER_CENTER), ERROR_RESULT);

        // BV02: Center of the lower base
        assertEquals(NORMAL_LOWER, cyl.getNormal(AXIS_ORIGIN), ERROR_RESULT);

        // BV03: Edge of the upper base
        assertEquals(NORMAL_UPPER, cyl.getNormal(P_UPPER_EDGE), ERROR_RESULT);

        // BV04: Edge of the lower base
        assertEquals(NORMAL_LOWER, cyl.getNormal(P_LOWER_EDGE), ERROR_RESULT);
    }
}