package lighting;

import primitives.Color;

/**
 * Abstract base for all lights, holding the original (source) light intensity.
 */
abstract class Light {
    /** The original intensity (color) of the light source. */
    protected final Color _intensity;

    /**
     * Initializes the light with the given original intensity.
     * @param intensity the original light intensity
     */
    protected Light(Color intensity) {
        _intensity = intensity;
    }

    /**
     * Returns the original intensity of the light.
     * @return the light intensity
     */
    public Color getIntensity() { return _intensity; }
}