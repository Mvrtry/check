package renderer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import lighting.PointLight;
import primitives.*;

/**
 * Unit tests for class {@link PointLight}.
 */
class PointLightTests {

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
    /** Linear attenuation factor. Used in: all tests. */
    private static final double KL = 0.2;
    /** Quadratic attenuation factor. Used in: all tests. */
    private static final double KQ = 0.04;
    /** The tested point light, attenuation 1 + 0.2d + 0.04d^2. Used in: all tests. */
    private static final PointLight LIGHT = new PointLight(INTENSITY, POSITION).setKl(KL).setKq(KQ);

    /** Illuminated point at distance 5 from the light, offset (0,3,4). Used in: EP01 of both tests. */
    private static final Point POINT = new Point(1, 4, 5);
    /** Expected normalized direction from the light to POINT. Used in: testGetL EP01. */
    private static final Vector EXPECTED_L = new Vector(0, 0.6, 0.8);
    /** Expected intensity at distance 5: attenuation 1 + 1 + 1 = 3. Used in: testGetIntensity EP01. */
    private static final Color EXPECTED_INTENSITY = new Color(100, 200, 300);

    /**
     * Test method for {@link PointLight#getL(Point)}.
     */
    @Test
    void testGetL() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Point away from the light gets the normalized direction from the light
        assertEquals(EXPECTED_L, LIGHT.getL(POINT), ERROR_DIRECTION);

        // =============== Boundary Values Tests ==================
        // BV01: Point coincides with the light position - zero vector
        assertThrows(IllegalArgumentException.class, () -> LIGHT.getL(POSITION), ERROR_EXCEPTION);
    }

    /**
     * Test method for {@link PointLight#getIntensity(Point)}.
     */
    @Test
    void testGetIntensity() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Point away from the light gets the distance-attenuated intensity
        assertEquals(EXPECTED_INTENSITY, LIGHT.getIntensity(POINT), ERROR_INTENSITY);

        // =============== Boundary Values Tests ==================
        // BV01: Point coincides with the light position - original intensity
        assertEquals(INTENSITY, LIGHT.getIntensity(POSITION), ERROR_INTENSITY);
    }
}