package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Point light source whose emission is concentrated toward a given direction.
 */
public class SpotLight extends PointLight {
    /** The normalized direction of the spotlight beam. */
    private final Vector _direction;

    /**
     * Creates a spotlight.
     * @param intensity the original light intensity
     * @param position  the position of the light source
     * @param direction the beam direction (normalized on storage)
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        _direction = direction.normalize();
    }
}