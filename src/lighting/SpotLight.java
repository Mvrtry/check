package lighting;

import static primitives.Util.alignZero;

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

    @Override
    public SpotLight setKc(double kC) {
        return (SpotLight) super.setKc(kC);
    }

    @Override
    public SpotLight setKl(double kL) {
        return (SpotLight) super.setKl(kL);
    }

    @Override
    public SpotLight setKq(double kQ) {
        return (SpotLight) super.setKq(kQ);
    }

    @Override
    public Color getIntensity(Point p) {
        Color iL = super.getIntensity(p);
        Vector l;
        try {
            l = getL(p);
        } catch (IllegalArgumentException ignored) {
            // the point coincides with the light position - no direction, full original intensity
            return iL;
        }
        double cos = alignZero(_direction.dotProduct(l));
        return cos <= 0 ? Color.BLACK : iL.scale(cos);
    }
}