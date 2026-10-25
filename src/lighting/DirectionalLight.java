package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Infinitely distant light source with a constant direction and no attenuation.
 */
public class DirectionalLight extends Light implements LightSource {
    /** The normalized direction of the light. */
    private final Vector _direction;

    /**
     * Creates a directional light.
     * @param intensity the original light intensity
     * @param direction the light direction (normalized on storage)
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        _direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return _intensity;
    }

    @Override
    public Vector getL(Point p) {
        return _direction;
    }
}