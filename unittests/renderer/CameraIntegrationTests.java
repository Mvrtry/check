package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.api.Intersectable;
import geometries.impl.Plane;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Integration tests between {@link Camera} ray construction and geometry intersection calculations.
 */
class CameraIntegrationTests {

    /** Default constructor. */
    CameraIntegrationTests() { /* Default constructor to satisfy documentation tools */ }

    // ============ Shared Fields ==============

    /** Resolution (both axes) shared by every camera in this test class. */
    private static final int RESOLUTION = 3;

    /** Physical view plane size (both axes) shared by every camera in this test class. */
    private static final double VP_SIZE = 3d;

    /** View plane distance shared by every camera in this test class. */
    private static final double VP_DISTANCE = 1d;

    /** Error message prefix for a mismatched total intersection count. */
    private static final String ERROR_INTERSECTIONS_COUNT = "Wrong total intersections count: ";

    /** Shared camera: located at the origin, looking down the -Z axis, up along +Y. */
    private static final Camera CAMERA = Camera.getBuilder()
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpDistance(VP_DISTANCE)
            .setVpSize(VP_SIZE, VP_SIZE)
            .setResolution(RESOLUTION, RESOLUTION)
            .build();

    /**
     * Second shared camera, pulled back along +Z from the origin so that it sits strictly
     * outside the larger spheres in this suite (avoiding the degenerate case of the camera
     * lying exactly on a sphere's surface, which would halve the expected intersection count).
     * Same orientation as {@link #CAMERA}.
     */
    private static final Camera CAMERA_PULLED_BACK = Camera.getBuilder()
            .setLocation(new Point(0, 0, 0.5))
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpDistance(VP_DISTANCE)
            .setVpSize(VP_SIZE, VP_SIZE)
            .setResolution(RESOLUTION, RESOLUTION)
            .build();

    // ============ Sphere Test Data ==============

    /** Small sphere fully inside the view plane's central pixel. TC01: expects 2 intersections. */
    private static final Sphere SPHERE_SMALL = new Sphere(new Point(0, 0, -3), 1);

    /**
     * Large sphere covering the entire view plane. Paired with {@link #CAMERA_PULLED_BACK}
     * (distance 3 &gt; radius 2.5) so the camera sits strictly outside it. TC02: expects 18
     * intersections.
     */
    private static final Sphere SPHERE_LARGE = new Sphere(new Point(0, 0, -2.5), 2.5);

    /**
     * Medium sphere covering most of the view plane. Paired with {@link #CAMERA_PULLED_BACK}
     * (distance 2.5 &gt; radius 2) so the camera sits strictly outside it. TC03: expects 10
     * intersections.
     */
    private static final Sphere SPHERE_MEDIUM = new Sphere(new Point(0, 0, -2), 2);

    /** Sphere large enough that the camera sits inside it. TC04: expects 9 intersections. */
    private static final Sphere SPHERE_CAMERA_INSIDE = new Sphere(new Point(0, 0, -1), 4);

    /** Sphere located entirely behind the camera. TC05: expects 0 intersections. */
    private static final Sphere SPHERE_BEHIND_CAMERA = new Sphere(new Point(0, 0, 1), 0.5);

    // ============ Plane Test Data ==============

    /** Plane parallel to the view plane. TC01: expects 9 intersections. */
    private static final Plane PLANE_PARALLEL = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));

    /** Plane at a slight angle to the view plane. TC02: expects 9 intersections. */
    private static final Plane PLANE_SLIGHT_ANGLE = new Plane(new Point(0, 0, -5), new Vector(0, 1, -2));

    /** Plane at a steep angle to the view plane. TC03: expects 6 intersections. */
    private static final Plane PLANE_STEEP_ANGLE = new Plane(new Point(0, 0, -5), new Vector(0, 1, -1));

    // ============ Triangle Test Data ==============

    /** Small triangle covering only the central pixel. TC01: expects 1 intersection. */
    private static final Triangle TRIANGLE_SMALL =
            new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));

    /** Tall triangle covering the central and top-middle pixels. TC02: expects 2 intersections. */
    private static final Triangle TRIANGLE_TALL =
            new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2));

    /**
     * Constructs a ray through every pixel of the shared camera's view plane, sums the total
     * number of intersections with the given geometry, and asserts it matches the expected count.
     * @param camera   the camera to construct rays from
     * @param geometry the geometry to intersect each ray with
     * @param expected the expected total number of intersection points across all pixels
     * @param testName the scenario name, included in the failure message
     */
    private void assertIntersectionsCount(Camera camera, Intersectable geometry, int expected, String testName) {
        int count = 0;
        for (int i = 0; i < RESOLUTION; i++) {
            for (int j = 0; j < RESOLUTION; j++) {
                Ray ray = camera.constructRay(j, i);
                List<Point> intersections = geometry.findIntersections(ray);
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }
        assertEquals(expected, count, ERROR_INTERSECTIONS_COUNT + testName);
    }

    /**
     * Test method for camera ray construction integrated with {@link Sphere#findIntersections(Ray)}.
     */
    @Test
    void testCameraRaySphereIntegration() {
        // TC01: small sphere - only the central ray hits it, twice
        assertIntersectionsCount(CAMERA, SPHERE_SMALL, 2, "Small sphere in front of the view plane");

        // TC02: large sphere, camera pulled back strictly outside it - every ray hits it twice
        assertIntersectionsCount(CAMERA_PULLED_BACK, SPHERE_LARGE, 18, "Large sphere covering the entire view plane");

        // TC03: medium sphere, camera pulled back strictly outside it - all rays except the four corners hit it twice
        assertIntersectionsCount(CAMERA_PULLED_BACK, SPHERE_MEDIUM, 10, "Medium sphere covering most of the view plane");

        // TC04: camera located inside the sphere - every ray hits it once
        assertIntersectionsCount(CAMERA, SPHERE_CAMERA_INSIDE, 9, "Camera located inside the sphere");

        // TC05: sphere entirely behind the camera - no ray can reach it
        assertIntersectionsCount(CAMERA, SPHERE_BEHIND_CAMERA, 0, "Sphere located behind the camera");
    }

    /**
     * Test method for camera ray construction integrated with {@link Plane#findIntersections(Ray)}.
     */
    @Test
    void testCameraRayPlaneIntegration() {
        // TC01: plane parallel to the view plane - every ray hits it once
        assertIntersectionsCount(CAMERA, PLANE_PARALLEL, 9, "Plane parallel to the view plane");

        // TC02: plane at a slight angle - every ray still hits it once
        assertIntersectionsCount(CAMERA, PLANE_SLIGHT_ANGLE, 9, "Plane at a slight angle to the view plane");

        // TC03: plane at a steep angle - the two bottom rays diverge away from the plane and miss it
        assertIntersectionsCount(CAMERA, PLANE_STEEP_ANGLE, 6, "Plane at a steep angle to the view plane");
    }

    /**
     * Test method for camera ray construction integrated with {@link Triangle#findIntersections(Ray)}.
     */
    @Test
    void testCameraRayTriangleIntegration() {
        // TC01: small triangle - only the central ray hits it
        assertIntersectionsCount(CAMERA, TRIANGLE_SMALL, 1, "Small triangle covering the central pixel");

        // TC02: tall triangle - the central and top-middle rays hit it
        assertIntersectionsCount(CAMERA, TRIANGLE_TALL, 2, "Tall triangle covering two pixels");
    }
}