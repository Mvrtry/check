package renderer;

import java.util.MissingResourceException;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a camera in 3D space, responsible for constructing rays through
 * a view plane for rendering purposes.
 * <p>
 * A {@link Camera} is built exclusively through its nested {@link Builder}
 * class, which validates and computes all derived geometric data only when
 * {@link Builder#build()} is invoked.
 * </p>
 */
public class Camera implements Cloneable {

    /** The camera's location point in 3D space. */
    private Point p0;

    /** Normalized vector pointing in the camera's viewing direction. */
    private Vector vTo;

    /** Normalized "up" vector of the camera, orthogonal to {@link #vTo}. */
    private Vector vUp;

    /** Normalized "right" vector of the camera, orthogonal to {@link #vTo} and {@link #vUp}. */
    private Vector vRight;

    /** Physical width of the view plane. */
    private double width;

    /** Physical height of the view plane. */
    private double height;

    /** Distance between the camera location and the view plane. */
    private double distance;

    /** Number of pixels along the X axis (columns) of the view plane. Defaults to 1. */
    private int nX = 1;

    /** Number of pixels along the Y axis (rows) of the view plane. Defaults to 1. */
    private int nY = 1;

    /** Center point of the view plane; computed during {@link Builder#build()}. */
    private Point vpCenter;

    /** Physical width of a single pixel; computed during {@link Builder#build()}. */
    private double pixelWidth;

    /** Physical height of a single pixel; computed during {@link Builder#build()}. */
    private double pixelHeight;

    /**
     * Private default constructor. A {@link Camera} instance must only be
     * created and populated via {@link Camera#getBuilder()} and its
     * {@link Builder}.
     */
    private Camera() { /* Instances are created and populated exclusively via Builder */ }

    /**
     * Returns a new {@link Builder} instance for constructing a {@link Camera}.
     * @return a new camera builder
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray from the camera through the center of the given pixel
     * on the view plane.
     * <p>
     * Not yet implemented in this sprint.
     * </p>
     * @param xIndex column index of the pixel (0-based, left to right)
     * @param yIndex row index of the pixel (0-based, top to bottom)
     * @return {@code null} (placeholder implementation)
     */
    public Ray constructRay(int xIndex, int yIndex) {
        return null;
    }

    /**
     * Nested static {@link Builder} class implementing the Builder design
     * pattern for {@link Camera}. Setter methods perform no validation and
     * simply store provided values; all validation and derived-field
     * computation happens in {@link #build()}.
     */
    public static class Builder {

        /** The camera instance being progressively populated by this builder. */
        private final Camera camera = new Camera();

        /**
         * Explicit forward ("to") vector, as provided via
         * {@link #setDirection(Vector, Vector)}. {@code null} if the camera
         * direction was instead specified via a target point.
         */
        private Vector vTo;

        /**
         * General "up" vector as provided by the caller. This is not
         * necessarily orthogonal to {@link #vTo}; orthogonality is enforced
         * during {@link #build()}. Defaults to {@link Vector#Y_AXIS} if never
         * set.
         */
        private Vector vUpGeneral = Vector.AXIS_Y;

        /**
         * Target point the camera should look at, as provided via
         * {@link #setDirection(Point)} or {@link #setDirection(Point, Vector)}.
         * {@code null} if the camera direction was instead specified via
         * explicit vectors.
         */
        private Point target;

        /**
         * Default constructor. Initializes the internal {@link #camera}
         * instance to be populated via the builder's setter methods.
         */
        public Builder() { /* camera field is initialized at declaration */ }

        /**
         * Sets the camera's location in 3D space.
         * @param location the camera's location point
         * @return this builder, for chaining
         */
        public Builder setLocation(Point location) {
            camera.p0 = location;
            return this;
        }

        /**
         * Sets the camera's direction using explicit forward and up vectors.
         * @param to the forward ("look at") direction vector
         * @param up a general up direction vector (not required to be
         *           orthogonal to {@code to})
         * @return this builder, for chaining
         */
        public Builder setDirection(Vector to, Vector up) {
            this.vTo = to;
            this.vUpGeneral = up;
            this.target = null;
            return this;
        }

        /**
         * Sets the camera's direction using a target point to look at, with the
         * up direction defaulting to {@link Vector#Y_AXIS}.
         * @param target the point the camera should look at
         * @return this builder, for chaining
         */
        public Builder setDirection(Point target) {
            this.target = target;
            this.vTo = null;
            this.vUpGeneral = Vector.AXIS_Y;
            return this;
        }

        /**
         * Sets the camera's direction using a target point to look at and an
         * explicit general up direction vector.
         * @param target the point the camera should look at
         * @param up     a general up direction vector (not required to be
         *               orthogonal to the resulting forward vector)
         * @return this builder, for chaining
         */
        public Builder setDirection(Point target, Vector up) {
            this.target = target;
            this.vTo = null;
            this.vUpGeneral = up;
            return this;
        }

        /**
         * Sets the physical size of the view plane.
         * @param width  the view plane's physical width
         * @param height the view plane's physical height
         * @return this builder, for chaining
         */
        public Builder setVpSize(double width, double height) {
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance between the camera and the view plane.
         * @param distance the distance from the camera to the view plane
         * @return this builder, for chaining
         */
        public Builder setVpDistance(double distance) {
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the view plane's resolution (pixel counts).
         * @param nX number of pixels along the X axis (columns)
         * @param nY number of pixels along the Y axis (rows)
         * @return this builder, for chaining
         */
        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Validates that the view plane resolution is strictly positive.
         * @throws IllegalArgumentException if {@code nX} or {@code nY} is not
         *                                  strictly positive
         */
        private void checkResolution() {
            if (camera.nX <= 0 || camera.nY <= 0) {
                throw new IllegalArgumentException(
                        "View plane resolution (nX, nY) must be strictly positive");
            }
        }

        /**
         * Validates that the camera location and direction data are present,
         * then computes and normalizes the camera's orthonormal direction
         * vectors ({@code vTo}, {@code vUp}, {@code vRight}).
         * @throws MissingResourceException if the location, or both direction
         *                                  source (explicit {@code vTo} /
         *                                  target point) and general up vector,
         *                                  are missing
         * @throws IllegalArgumentException if the forward and general up
         *                                  vectors are parallel
         */
        private void checkLocationAndDirection() {
            if (camera.p0 == null) {
                throw new MissingResourceException(
                        "Missing rendering data", Camera.class.getName(), "location (p0)");
            }
            if (vTo == null && target == null) {
                throw new MissingResourceException(
                        "Missing rendering data", Camera.class.getName(), "direction (vTo or target)");
            }
            if (vUpGeneral == null) {
                throw new MissingResourceException(
                        "Missing rendering data", Camera.class.getName(), "up vector (vUp)");
            }

            Vector to = (vTo != null) ? vTo : target.subtract(camera.p0);
            camera.vTo = to.normalize();

            Vector right;
            try {
                right = camera.vTo.crossProduct(vUpGeneral).normalize();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Camera direction (vTo) and up vector (vUp) must not be parallel", e);
            }
            camera.vRight = right;
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
        }

        /**
         * Validates that the view plane's physical dimensions and distance are
         * strictly positive, then computes and stores the view plane's center
         * point and per-pixel dimensions.
         * @throws IllegalArgumentException if {@code width}, {@code height}, or
         *                                  {@code distance} is not strictly
         *                                  positive
         */
        private void checkViewPlane() {
            if (camera.width <= 0 || camera.height <= 0) {
                throw new IllegalArgumentException(
                        "View plane size (width, height) must be strictly positive");
            }
            if (camera.distance <= 0) {
                throw new IllegalArgumentException(
                        "View plane distance from the camera must be strictly positive");
            }

            camera.vpCenter = camera.p0.add(camera.vTo.scale(camera.distance));
            camera.pixelWidth = camera.width / camera.nX;
            camera.pixelHeight = camera.height / camera.nY;
        }

        /**
         * Validates all camera data and builds a fully-populated, ready-to-use
         * {@link Camera} instance.
         * <p>
         * Validation and derived-field computation is performed in a fixed
         * order: resolution, then location/direction, then view plane data.
         * This order must not be changed.
         * </p>
         * @return a new, validated {@link Camera} instance (a clone of the
         *         internally built camera)
         * @throws MissingResourceException if required camera data is missing
         * @throws IllegalArgumentException if any camera data is invalid
         */
        public Camera build() {
            checkResolution();
            checkLocationAndDirection();
            checkViewPlane();
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
}