package renderer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import lighting.DirectionalLight;
import primitives.*;

/**
 * Unit tests for class {@link DirectionalLight}.
 */
class DirectionalLightTests {

    // ============ Shared Fields ==============
    /** Error message for wrong light direction. */
    private static final String ERROR_DIRECTION = "ERROR: wrong light direction";
    /** Error message for wrong light intensity. */
    private static final String ERROR_INTENSITY = "ERROR: wrong light intensity";

    /** Original light intensity. Used in: all tests. */
    private static final Color INTENSITY = new Color(300, 600, 900);
    /** Non-normalized light direction (1,2,2), length 3. Used in: all tests. */
    private static final Vector DIRECTION = new Vector(1, 2, 2);
    /** The tested directional light. Used in: all tests. */
    private static final DirectionalLight LIGHT = new DirectionalLight(INTENSITY, DIRECTION);
    /** Arbitrary illuminated point. Used in: EP01 of both tests. */
    private static final Point POINT = new Point(5, -3, 7);

    /** Expected normalized light direction. Used in: testGetL EP01. */
    private static final Vector EXPECTED_L = new Vector(1d / 3, 2d / 3, 2d / 3);

    /**
     * Test method for {@link DirectionalLight#getL(Point)}.
     */
    @Test
    void testGetL() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Any point gets the constant normalized light direction
        assertEquals(EXPECTED_L, LIGHT.getL(POINT), ERROR_DIRECTION);
    }

    /**
     * Test method for {@link DirectionalLight#getIntensity(Point)}.
     */
    @Test
    void testGetIntensity() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Any point gets the original intensity without attenuation
        assertEquals(INTENSITY, LIGHT.getIntensity(POINT), ERROR_INTENSITY);
    }
}