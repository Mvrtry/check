package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Camera class representing a viewpoint and a view plane to generate rays.
 * Uses the Builder pattern for initialization.
 */
public class Camera implements Cloneable {

    private Point _p0;
    private Vector _vTo;
    private Vector _vUp;
    private Vector _vRight;

    private double _width;
    private double _height;
    private double _distance;

    private int _nX = 1;
    private int _nY = 1;

    // Helper fields for calculations
    private Point _vpCenter;
    private double _pixelWidth;
    private double _pixelHeight;

    /**
     * Private constructor to enforce the use of the Builder.
     */
    private Camera() {
        // Empty by design
    }

    /**
     * Retrieves a new Builder to construct a Camera.
     *
     * @return a new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through a specific pixel on the view plane.
     *
     * @param nX the column index of the pixel
     * @param nY the row index of the pixel
     * @return the constructed Ray (currently null)
     */
    public Ray constructRay(int nX, int nY) {
        return null;
    }

    /**
     * Builder class for configuring and creating a Camera instance.
     */
    public static class Builder {
        private final Camera _camera = new Camera();

        // Temporary helper fields for direction settings
        private Vector _to;
        private Vector _up;
        private Point _target;

        /**
         * Sets the location of the camera.
         *
         * @param location the location point
         * @return the Builder instance
         */
        public Builder setLocation(Point location) {
            _camera._p0 = location;
            return this;
        }

        /**
         * Sets the direction of the camera using 'to' and 'up' vectors.
         *
         * @param to the forward direction vector
         * @param up the general up direction vector
         * @return the Builder instance
         */
        public Builder setDirection(Vector to, Vector up) {
            _to = to;
            _up = up;
            return this;
        }

        /**
         * Sets the direction of the camera using a target point and an 'up' vector.
         *
         * @param target the point the camera is looking at
         * @param up     the general up direction vector
         * @return the Builder instance
         */
        public Builder setDirection(Point target, Vector up) {
            _target = target;
            _up = up;
            return this;
        }

        /**
         * Sets the direction of the camera using only a target point.
         * The up vector will be implicitly determined.
         *
         * @param target the point the camera is looking at
         * @return the Builder instance
         */
        public Builder setDirection(Point target) {
            _target = target;
            return this;
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return the Builder instance
         */
        public Builder setVpSize(double width, double height) {
            _camera._width = width;
            _camera._height = height;
            return this;
        }

        /**
         * Sets the distance of the view plane from the camera.
         *
         * @param distance the distance
         * @return the Builder instance
         */
        public Builder setVpDistance(double distance) {
            _camera._distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the view plane (number of pixels).
         *
         * @param nX number of pixels in the X axis (columns)
         * @param nY number of pixels in the Y axis (rows)
         * @return the Builder instance
         */
        public Builder setResolution(int nX, int nY) {
            _camera._nX = nX;
            _camera._nY = nY;
            return this;
        }

        /**
         * Validates the resolution fields.
         */
        private void checkResolution() {
            // To be implemented
        }

        /**
         * Validates and calculates the location and orientation vectors of the camera.
         */
        private void checkLocationAndDirection() {
            // To be implemented
        }

        /**
         * Validates and calculates the view plane geometry.
         */
        private void checkViewPlane() {
            // To be implemented
        }

        /**
         * Builds and returns the Camera instance.
         * Ensures all necessary components are valid and fully calculated.
         *
         * @return a new, fully initialized Camera instance
         */
        public Camera build() {
            checkResolution();
            checkLocationAndDirection();
            checkViewPlane();

            try {
                return (Camera) _camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
}