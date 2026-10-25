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
    /** Constant attenuation factor. */
    private double _kC = 1;
    /** Linear attenuation factor. */
    private double _kL = 0;
    /** Quadratic attenuation factor. */
    private double _kQ = 0;

    /**
     * Creates a point light.
     * @param intensity the original light intensity
     * @param position  the position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        _position = position;
    }

    /**
     * Sets the constant attenuation factor.
     * @param kC the constant attenuation factor
     * @return this light, for chaining
     */
    public PointLight setKc(double kC) {
        _kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     * @param kL the linear attenuation factor
     * @return this light, for chaining
     */
    public PointLight setKl(double kL) {
        _kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     * @param kQ the quadratic attenuation factor
     * @return this light, for chaining
     */
    public PointLight setKq(double kQ) {
        _kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double dSquared = p.distanceSquared(_position);
        return _intensity.scale(1 / (_kC + _kL * Math.sqrt(dSquared) + _kQ * dSquared));
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(_position).normalize();
    }
}