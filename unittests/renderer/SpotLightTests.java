package renderer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import lighting.SpotLight;
import primitives.*;

/**
 * Unit tests for class {@link SpotLight}.
 */
class SpotLightTests {

    // ============ Shared Fields ==============
    /** Error message for wrong light direction. */
    private static final String ERROR_DIRECTION = "ERROR: wrong light direction";
    /** Error message for wrong light intensity. */
    private static final String ERROR_INTENSITY = "ERROR: wrong light intensity";
    /** Error message for a missing zero-vector exception. */
    private static final String ERROR_EXCEPTION = "ERROR: zero light direction must throw an exception";

    /** Original light intensity. Used in: all tests. */
    private static final Color INTENSITY = new Color(300, 600, 900);
    /** Light position. Used in: all tests. */
    private static final Point POSITION = new Point(1, 1, 1);
    /** Non-normalized spotlight direction, along +Z. Used in: all tests. */
    private static final Vector DIRECTION = new Vector(0, 0, 2);
    /** Linear attenuation factor. Used in: all tests. */
    private static final double KL = 0.2;
    /** Quadratic attenuation factor. Used in: all tests. */
    private static final double KQ = 0.04;
    /** The tested spotlight, attenuation 1 + 0.2d + 0.04d^2. Used in: all tests. */
    private static final SpotLight LIGHT = new SpotLight(INTENSITY, POSITION, DIRECTION).setKl(KL).setKq(KQ);
    /** No light (black). Used in: testGetIntensity EP02, BV02. */
    private static final Color NO_LIGHT = Color.BLACK;

    // ============ Fields for testGetL ==============
    /** Point in front of the spotlight at distance 5, offset (0,3,4). Used in: testGetL EP01. */
    private static final Point POINT_FRONT = new Point(1, 4, 5);
    /** Expected normalized direction from the light to POINT_FRONT. Used in: testGetL EP01. */
    private static final Vector EXPECTED_L = new Vector(0, 0.6, 0.8);

    // ============ Fields for testGetIntensity ==============
    /** Expected intensity at POINT_FRONT: cos 0.8 divided by attenuation 3. Used in: EP01. */
    private static final Color EXPECTED_FRONT = new Color(80, 160, 240);
    /** Point behind the spotlight at distance 5, offset (0,3,-4). Used in: EP02. */
    private static final Point POINT_BEHIND = new Point(1, 4, -3);
    /** Point at 90 degrees to the spotlight direction, offset (0,5,0). Used in: BV02. */
    private static final Point POINT_ORTHOGONAL = new Point(1, 6, 1);

    /**
     * Test method for {@link SpotLight#getL(Point)}.
     */
    @Test
    void testGetL() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Point in front of the spotlight gets the normalized direction from the light
        assertEquals(EXPECTED_L, LIGHT.getL(POINT_FRONT), ERROR_DIRECTION);

        // =============== Boundary Values Tests ==================
        // BV01: Point coincides with the light position - zero vector
        assertThrows(IllegalArgumentException.class, () -> LIGHT.getL(POSITION), ERROR_EXCEPTION);
    }

    /**
     * Test method for {@link SpotLight#getIntensity(Point)}.
     */
    @Test
    void testGetIntensity() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Point in front of the spotlight gets attenuated intensity scaled by the cosine
        assertEquals(EXPECTED_FRONT, LIGHT.getIntensity(POINT_FRONT), ERROR_INTENSITY);

        // EP02: Point behind the spotlight gets no light
        assertEquals(NO_LIGHT, LIGHT.getIntensity(POINT_BEHIND), ERROR_INTENSITY);

        // =============== Boundary Values Tests ==================
        // BV01: Point coincides with the light position - original intensity
        assertEquals(INTENSITY, LIGHT.getIntensity(POSITION), ERROR_INTENSITY);

        // BV02: Point at 90 degrees to the spotlight direction gets no light
        assertEquals(NO_LIGHT, LIGHT.getIntensity(POINT_ORTHOGONAL), ERROR_INTENSITY);
    }
}