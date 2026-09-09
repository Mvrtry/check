package renderer;

import org.junit.jupiter.api.Test;

import primitives.Color;

/**
 * Unit tests for the {@link ImageWriter} class.
 * Produces a basic grid image to demonstrate correct pixel-writing usage.
 */
class ImageWriterTests {
    /** Default constructor to satisfy JavaDoc generator */
    ImageWriterTests() { /* to satisfy JavaDoc generator */ }

    /** Horizontal resolution of the test image, in pixels */
    private static final int    NX             = 800;

    /** Vertical resolution of the test image, in pixels */
    private static final int    NY             = 500;

    /** Size of a single grid square, in pixels */
    private static final int    STEP           = 50;

    /** Background color of the test image */
    private static final Color  BACKGROUND     = new Color(255, 255, 0);

    /** Grid line color of the test image, high-contrast against the background */
    private static final Color  GRID_COLOR     = new Color(255, 0, 0);

    /** Output file name for the generated test image */
    private static final String FILE_NAME      = "testImage";

    /**
     * Builds an image with a colored grid over a contrasting background and
     * writes it to a PNG file for visual verification.
     */
    @Test
    void testImageWriter() {
        ImageWriter imageWriter = new ImageWriter(NX, NY);

        for (int y = 0; y < NY; y++)
            for (int x = 0; x < NX; x++)
                imageWriter.writePixel(x, y, x % STEP == 0 || y % STEP == 0 ? GRID_COLOR : BACKGROUND);

        imageWriter.writeToImage(FILE_NAME);
    }
}