package lighting;

import primitives.Color;

/**
 * Uniform ambient light intensity applied identically to every point in a scene.
 * The class is immutable.
 */
public final class AmbientLight {
    /** The intensity (color) of the ambient light. */
    private final Color intensity;

    /** Represents the absence of ambient light (black); used as the default. */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Creates an ambient light with the given intensity.
     * @param intensity the intensity (color) of the ambient light
     */
    public AmbientLight(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the intensity of this ambient light.
     * @return the ambient light intensity
     */
    public Color getIntensity() { return intensity; }
}