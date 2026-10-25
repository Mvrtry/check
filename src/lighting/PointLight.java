package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Light source at a specific position, radiating in all directions.
 */
public class PointLight extends Light implements LightSource {
    /** The position of the light source. */
    private final Point _position;

    /**
     * Creates a point light.
     * @param intensity the original light intensity
     * @param position  the position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        _position = position;
    }

    @Override
    public Color getIntensity(Point p) {
        return null;
    }

    @Override
    public Vector getL(Point p) {
        return null;
    }
}