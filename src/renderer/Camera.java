package renderer;

import java.util.MissingResourceException;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import static primitives.Util.isZero;

/**
 * Represents a camera in 3D space, responsible for constructing rays through a view plane.
 */
public class Camera implements Cloneable {

    /** The camera's location point in 3D space. */
    private Point p0;

    /** Normalized forward direction vector of the camera. */
    private Vector vTo;

    /** Normalized up direction vector of the camera. */
    private Vector vUp;

    /** Normalized right direction vector of the camera. */
    private Vector vRight;

    /** Physical width of the view plane. */
    private double width;

    /** Physical height of the view plane. */
    private double height;

    /** Distance between the camera location and the view plane. */
    private double distance;

    /** Number of pixels along the X axis (columns). Defaults to 1. */
    private int nX = 1;

    /** Number of pixels along the Y axis (rows). Defaults to 1. */
    private int nY = 1;

    /** Center point of the view plane. */
    private Point vpCenter;

    /** Physical width of a single pixel. */
    private double pixelWidth;

    /** Physical height of a single pixel. */
    private double pixelHeight;

    /** Writer used to color pixels and export the rendered image. */
    private ImageWriter imageWriter;

    /** Ray tracer used to compute the color of each pixel. */
    private RayTracerBase rayTracer;

    /** Private default constructor; instances are created only via {@link Builder}. */
    private Camera() { /* Populated exclusively via Builder */ }

    /**
     * Returns a new builder for constructing a {@link Camera}.
     * @return a new camera builder
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray from the camera through the center of the given pixel.
     * @param xIndex column index of the pixel
     * @param yIndex row index of the pixel
     * @return a ray from the camera through the pixel's center
     */
    public Ray constructRay(int xIndex, int yIndex) {
        double yI = -(yIndex - (nY - 1) / 2d) * pixelHeight;
        double xJ = (xIndex - (nX - 1) / 2d) * pixelWidth;

        Point pIJ = vpCenter;
        if (!isZero(xJ)) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (!isZero(yI)) {
            pIJ = pIJ.add(vUp.scale(yI));
        }

        return new Ray(p0, pIJ.subtract(p0));
    }

    /**
     * Constructs a ray through pixel (j, i), traces it, and colors the pixel accordingly.
     * @param j column index of the pixel
     * @param i row index of the pixel
     */
    private void castRay(int j, int i) {
        Ray ray = constructRay(j, i);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);
    }

    /**
     * Renders the image by casting a ray through every pixel of the view plane.
     * @return this camera, for method chaining
     */
    public Camera renderImage() {
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                castRay(j, i);
            }
        }
        return this;
    }

    /**
     * Paints a grid over the rendered image at the given pixel interval.
     * @param interval spacing, in pixels, between grid lines
     * @param color    the grid line color
     * @return this camera, for method chaining
     */
    public Camera printGrid(int interval, Color color) {
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                if (j % interval == 0 || i % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the rendered image to a PNG file with the given name.
     * @param fileName the output file name, without the {@code .png} extension
     */
    public void writeToImage(String fileName) {
        imageWriter.writeToImage(fileName);
    }

    /**
     * Builder for {@link Camera}, following the Builder design pattern.
     * Setters store raw values only; all validation and derived-field computation happens in {@link #build()}.
     */
    public static class Builder {

        /** The camera instance being progressively populated. */
        private final Camera camera = new Camera();

        /** Explicit forward vector, if set via {@link #setDirection(Vector, Vector)}. */
        private Vector vTo;

        /** General up vector; defaults to {@link Vector#AXIS_Y}. */
        private Vector vUpGeneral = Vector.AXIS_Y;

        /** Target point to look at, if set via a point-based {@code setDirection} overload. */
        private Point target;

        /** Default constructor. */
        public Builder() { /* camera field is initialized at declaration */ }

        /**
         * Sets the camera's location.
         * @param location the camera's location point
         * @return this builder
         */
        public Builder setLocation(Point location) {
            camera.p0 = location;
            return this;
        }

        /**
         * Sets the camera's direction using explicit forward and up vectors.
         * @param to the forward direction vector
         * @param up a general up direction vector
         * @return this builder
         */
        public Builder setDirection(Vector to, Vector up) {
            this.vTo = to;
            this.vUpGeneral = up;
            this.target = null;
            return this;
        }

        /**
         * Sets the camera's direction using a target point, with up defaulting to {@link Vector#AXIS_Y}.
         * @param target the point the camera should look at
         * @return this builder
         */
        public Builder setDirection(Point target) {
            this.target = target;
            this.vTo = null;
            this.vUpGeneral = Vector.AXIS_Y;
            return this;
        }

        /**
         * Sets the camera's direction using a target point and an explicit up vector.
         * @param target the point the camera should look at
         * @param up     a general up direction vector
         * @return this builder
         */
        public Builder setDirection(Point target, Vector up) {
            this.target = target;
            this.vTo = null;
            this.vUpGeneral = up;
            return this;
        }

        /**
         * Sets the physical size of the view plane.
         * @param width  the view plane's width
         * @param height the view plane's height
         * @return this builder
         */
        public Builder setVpSize(double width, double height) {
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance between the camera and the view plane.
         * @param distance the distance to the view plane
         * @return this builder
         */
        public Builder setVpDistance(double distance) {
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the view plane's resolution.
         * @param nX number of pixels along the X axis
         * @param nY number of pixels along the Y axis
         * @return this builder
         */
        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Sets the ray tracer to be used by the camera, based on the given scene and type.
         * @param  scene                    the scene to render
         * @param  type                     the ray tracer type to instantiate
         * @return                          this builder
         * @throws IllegalArgumentException if the given type is not supported
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            if (type == RayTracerType.SIMPLE) {
                camera.rayTracer = new SimpleRayTracer(scene);
            } else {
                throw new IllegalArgumentException("Unsupported ray tracer type: " + type);
            }
            return this;
        }

        /**
         * Sets the image writer to be used by the camera.
         * @param  imageWriter the image writer
         * @return             this builder
         */
        Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Validates that the resolution is strictly positive, then creates the camera's image writer.
         * @throws IllegalArgumentException if nX or nY is not strictly positive
         */
        private void checkResolution() {
            if (camera.nX <= 0 || camera.nY <= 0) {
                throw new IllegalArgumentException(
                        "View plane resolution (nX, nY) must be strictly positive");
            }
            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);
        }

        /**
         * Validates location and direction data, then computes and normalizes vTo, vRight, and vUp.
         * @throws MissingResourceException if location, direction, or up vector is missing
         * @throws IllegalArgumentException if the forward and up vectors are parallel
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
         * Validates view plane dimensions and distance, then computes vpCenter and pixel size.
         * @throws IllegalArgumentException if width, height, or distance is not strictly positive
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
         * Validates all camera data and builds a ready-to-use {@link Camera} instance.
         * Validation order is fixed: resolution, then location/direction, then view plane.
         * If no ray tracer was supplied, defaults to a {@link SimpleRayTracer} over an empty scene.
         * @return a new, validated {@link Camera} instance
         * @throws MissingResourceException if required camera data is missing
         * @throws IllegalArgumentException if any camera data is invalid
         */
        public Camera build() {
            checkResolution();
            checkLocationAndDirection();
            checkViewPlane();

            if (camera.imageWriter == null) {
                throw new MissingResourceException(
                        "Missing rendering data", Camera.class.getName(), "image writer");
            }
            if (camera.rayTracer == null) {
                setRayTracer(new Scene("test"), RayTracerType.SIMPLE);
            }

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
}