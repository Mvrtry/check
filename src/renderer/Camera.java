package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import java.util.MissingResourceException;
import static primitives.Util.isZero;

/**
 * Camera class representing the observer's viewpoint and view plane setup.
 * Implements Cloneable to allow safe cloning during the build process.
 */
public class Camera implements Cloneable {
    // Camera position and orientation vectors
    private Point _p0;
    private Vector _vTo;
    private Vector _vUp;
    private Vector _vRight;

    // View plane physical geometry
    private double _width;
    private double _height;
    private double _distance;

    // View plane resolution (number of pixels), default initialized to 1
    private int _nX = 1;
    private int _nY = 1;

    // Calculated helper fields to optimize ray generation performance
    private Point _vpCenter;
    private double _pixelWidth;
    private double _pixelHeight;

    /**
     * Default private constructor to prevent direct instantiation.
     * Forces the use of the Builder pattern.
     */
    private Camera() {
    }

    /**
     * Static factory method to obtain a new Camera Builder.
     *
     * @return a new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray passing through the center of a specified pixel (j, i)
     * on the view plane.
     *
     * @param j the column index of the pixel (X-axis index)
     * @param i the row index of the pixel (Y-axis index)
     * @return a new Ray passing from the camera position through the pixel center
     */
    /**
     * Constructs a ray passing through the center of a specified pixel (j, i)
     * on the view plane.
     *
     * @param j the column index of the pixel (X-axis index)
     * @param i the row index of the pixel (Y-axis index)
     * @return a new Ray passing from the camera position through the pixel center
     */
    public Ray constructRay(int j, int i) {
        // Start from the center of the view plane
        Point pIJ = _vpCenter;

        // Calculate the ratio and scale for the Y axis (rows)
        // Notice the minus sign: pixel rows go down, but Vup goes up
        double yI = -(i - (_nY - 1) / 2d) * _pixelHeight;

        // Calculate the ratio and scale for the X axis (columns)
        double xJ = (j - (_nX - 1) / 2d) * _pixelWidth;

        // Move the point along the right vector if xJ is not zero
        if (!isZero(xJ)) {
            pIJ = pIJ.add(_vRight.scale(xJ));
        }

        // Move the point along the up vector if yI is not zero
        if (!isZero(yI)) {
            pIJ = pIJ.add(_vUp.scale(yI));
        }

        // The direction of the ray is from the camera position to the pixel center
        Vector direction = pIJ.subtract(_p0);
        return new Ray(_p0, direction);
    }

    /**
     * Builder class for Camera following the Builder pattern.
     */
    public static class Builder {
        private final Camera _camera = new Camera();

        // Temporary orientation helper fields
        private Vector _to;
        private Vector _up = Vector.AXIS_Y; // Default general up direction
        private Point _target;

        /**
         * Sets the camera's location point in 3D space.
         *
         * @param location the camera location point
         * @return this Builder instance
         */
        public Builder setLocation(Point location) {
            _camera._p0 = location;
            return this;
        }

        /**
         * Sets the camera's direction using explicit look-at (to) and up vectors.
         *
         * @param to the forward direction vector
         * @param up the general upward direction vector
         * @return this Builder instance
         */
        public Builder setDirection(Vector to, Vector up) {
            _to = to;
            _up = up;
            return this;
        }

        /**
         * Sets the camera's direction targeting a specific point with an explicit up vector.
         *
         * @param target the point the camera is looking at
         * @param up     the general upward direction vector
         * @return this Builder instance
         */
        public Builder setDirection(Point target, Vector up) {
            _target = target;
            _up = up;
            return this;
        }

        /**
         * Sets the camera's direction targeting a specific point.
         * The up vector defaults to the Y-axis.
         *
         * @param target the point the camera is looking at
         * @return this Builder instance
         */
        public Builder setDirection(Point target) {
            _target = target;
            return this;
        }

        /**
         * Sets the physical dimensions (width and height) of the view plane.
         *
         * @param width  the physical width
         * @param height the physical height
         * @return this Builder instance
         */
        public Builder setVpSize(double width, double height) {
            _camera._width = width;
            _camera._height = height;
            return this;
        }

        /**
         * Sets the distance between the camera location and the view plane.
         *
         * @param distance the physical distance
         * @return this Builder instance
         */
        public Builder setVpDistance(double distance) {
            _camera._distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the view plane in terms of pixel count.
         *
         * @param nX number of columns
         * @param nY number of rows
         * @return this Builder instance
         */
        public Builder setResolution(int nX, int nY) {
            _camera._nX = nX;
            _camera._nY = nY;
            return this;
        }

        /**
         * Validates that the resolution values are strictly positive.
         *
         * @throws IllegalArgumentException if nX or nY are less than or equal to 0
         */
        private void checkResolution() {
            if (_camera._nX <= 0 || _camera._nY <= 0) {
                throw new IllegalArgumentException("Resolution dimensions must be greater than zero.");
            }
        }

        /**
         * Validates presence of location and direction components, then computes
         * the complete orthonormal orientation coordinate system for the camera.
         *
         * @throws MissingResourceException if location or direction inputs are missing
         * @throws IllegalArgumentException if the look-at and up vectors are parallel
         */
        private void checkLocationAndDirection() {
            if (_camera._p0 == null) {
                throw new MissingResourceException("Camera location is not set.", "Camera", "location");
            }
            if (_to == null && _target == null) {
                throw new MissingResourceException("Camera direction is not set.", "Camera", "direction");
            }

            // Compute look-at vector (_vTo)
            if (_to == null) {
                _camera._vTo = _target.subtract(_camera._p0).normalize();
            } else {
                _camera._vTo = _to.normalize();
            }

            // Compute right vector (_vRight = _vTo x _up)
            try {
                _camera._vRight = _camera._vTo.crossProduct(_up).normalize();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("The look-at vector and the up vector cannot be parallel.");
            }

            // Compute true orthogonal up vector (_vUp = _vRight x _vTo)
            _camera._vUp = _camera._vRight.crossProduct(_camera._vTo).normalize();
        }

        /**
         * Validates view plane physical attributes and calculates pre-computed helper fields.
         *
         * @throws IllegalArgumentException if width, height, or distance are not strictly positive
         */
        private void checkViewPlane() {
            if (_camera._width <= 0 || _camera._height <= 0) {
                throw new IllegalArgumentException("View plane dimensions must be greater than zero.");
            }
            if (_camera._distance <= 0) {
                throw new IllegalArgumentException("View plane distance must be greater than zero.");
            }

            // Pre-compute the center point of the view plane: Pc = P0 + d * vTo
            _camera._vpCenter = _camera._p0.add(_camera._vTo.scale(_camera._distance));

            // Pre-compute pixel sizes
            _camera._pixelWidth = _camera._width / _camera._nX;
            _camera._pixelHeight = _camera._height / _camera._nY;
        }

        /**
         * Performs full validation, pre-computes operational parameters,
         * and returns a safely cloned instance of the fully configured Camera.
         *
         * @return the fully operational initialized Camera instance, or null if cloning fails
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