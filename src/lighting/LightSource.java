package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * External light source that propagates light through the scene to a given point.
 */
public interface LightSource {
    /**
     * Calculates the light intensity arriving at the given point.
     * @param p the illuminated point
     * @return the light intensity at the point
     */
    Color getIntensity(Point p);

    /**
     * Calculates the normalized direction from the light source to the given point.
     * @param p the illuminated point
     * @return the normalized light direction vector
     */
    Vector getL(Point p);

    /**
     * Calculates the distance between the light source and the given point.
     * @param point the illuminated point
     * @return the distance to the light source
     */
    double getDistance(Point point);
}