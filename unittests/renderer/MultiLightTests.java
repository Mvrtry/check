package renderer;

import static java.awt.Color.BLUE;

import org.junit.jupiter.api.Test;

import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.*;
import scene.Scene;

/**
 * Rendering tests for scenes lit by several external light sources of all types together.
 */
class MultiLightTests {

    // ============ Shared Fields ==============
    /** Image resolution of both tests. */
    private static final int RESOLUTION = 500;
    /** Camera location shared by both tests (copied from LightsTests). */
    private static final Point CAMERA_LOCATION = new Point(0, 0, 1000);
    /** Camera to view plane distance (copied from LightsTests). */
    private static final double VP_DISTANCE = 1000;
    /** Shininess of all geometries (copied from LightsTests). */
    private static final int SHININESS = 301;

    // ============ Fields for testMultiLightTriangles ==============
    /** View plane size for the triangles scene (copied from LightsTests). */
    private static final double TRIANGLES_VP_SIZE = 200;
    /** Ambient light color of the triangles scene (copied from LightsTests). */
    private static final Color TRIANGLES_AMBIENT = new Color(38, 38, 38);
    /** Diffuse factor of the triangles (copied from LightsTests). */
    private static final Double3 TRIANGLES_KD = new Double3(0.2, 0.6, 0.4);
    /** Specular factor of the triangles (copied from LightsTests). */
    private static final Double3 TRIANGLES_KS = new Double3(0.2, 0.4, 0.3);
    /** Shared left-bottom vertex of both triangles (copied from LightsTests). */
    private static final Point VERTEX_LEFT_BOTTOM = new Point(-110, -110, -150);
    /** Shared right-top vertex of both triangles (copied from LightsTests). */
    private static final Point VERTEX_RIGHT_TOP = new Point(95, 100, -150);
    /** Right-bottom vertex of the first triangle (copied from LightsTests). */
    private static final Point VERTEX_RIGHT_BOTTOM = new Point(110, -110, -150);
    /** Left-top vertex of the second triangle (copied from LightsTests). */
    private static final Point VERTEX_LEFT_TOP = new Point(-75, 78, 100);

    /** Directional light color for the triangles: warm amber. */
    private static final Color TRIANGLES_DIRECTIONAL_COLOR = new Color(160, 70, 20);
    /** Directional light direction for the triangles: from the upper right. */
    private static final Vector TRIANGLES_DIRECTIONAL_DIRECTION = new Vector(-1, -1, -3);
    /** Point light color for the triangles: cool blue. */
    private static final Color TRIANGLES_POINT_COLOR = new Color(80, 200, 600);
    /** Point light position for the triangles: near the lower left. */
    private static final Point TRIANGLES_POINT_POSITION = new Point(-60, -60, -100);
    /** Spot light color for the triangles: magenta. */
    private static final Color TRIANGLES_SPOT_COLOR = new Color(1000, 100, 350);
    /** Spot light position for the triangles: near the upper right. */
    private static final Point TRIANGLES_SPOT_POSITION = new Point(50, 40, -100);
    /** Spot light direction for the triangles: down and slightly to the right. */
    private static final Vector TRIANGLES_SPOT_DIRECTION = new Vector(0.5, -1, -2);
    /** Linear attenuation of the triangles point and spot lights. */
    private static final double TRIANGLES_KL = 0.001;
    /** Quadratic attenuation of the triangles point and spot lights. */
    private static final double TRIANGLES_KQ = 0.0002;

    // ============ Fields for testMultiLightSphere ==============
    /** View plane size for the sphere scene (copied from LightsTests). */
    private static final double SPHERE_VP_SIZE = 150;
    /** Center of the sphere (copied from LightsTests). */
    private static final Point SPHERE_CENTER = new Point(0, 0, -50);
    /** Radius of the sphere (copied from LightsTests). */
    private static final double SPHERE_RADIUS = 50d;
    /** Emission color of the sphere (copied from LightsTests). */
    private static final Color SPHERE_COLOR = new Color(BLUE).reduce(2);
    /** Diffuse and specular factor of the sphere (copied from LightsTests). */
    private static final double SPHERE_K = 0.5;

    /** Directional light color for the sphere: soft violet. */
    private static final Color SPHERE_DIRECTIONAL_COLOR = new Color(200, 80, 160);
    /** Directional light direction for the sphere: from the upper right. */
    private static final Vector SPHERE_DIRECTIONAL_DIRECTION = new Vector(-1, -1, -1);
    /** Point light color for the sphere: green. */
    private static final Color SPHERE_POINT_COLOR = new Color(150, 600, 200);
    /** Point light position for the sphere: lower right, in front. */
    private static final Point SPHERE_POINT_POSITION = new Point(60, -60, 40);
    /** Spot light color for the sphere: warm orange. */
    private static final Color SPHERE_SPOT_COLOR = new Color(900, 450, 0);
    /** Spot light position for the sphere: upper left, in front. */
    private static final Point SPHERE_SPOT_POSITION = new Point(-60, 60, 40);
    /** Spot light direction for the sphere: toward the sphere from the upper left. */
    private static final Vector SPHERE_SPOT_DIRECTION = new Vector(1, -1, -1);
    /** Linear attenuation of the sphere point and spot lights. */
    private static final double SPHERE_KL = 0.001;
    /** Quadratic attenuation of the sphere point and spot lights. */
    private static final double SPHERE_KQ = 0.0002;

    /**
     * Produce a picture of two triangles lit by a directional, a point and a spot light together.
     */
    @Test
    void testMultiLightTriangles() {
        Scene scene = new Scene("Multi light triangles").setAmbientLight(new AmbientLight(TRIANGLES_AMBIENT));
        Material material = new Material().setKD(TRIANGLES_KD).setKS(TRIANGLES_KS).setShininess(SHININESS);
        scene.geometries.add(
                new Triangle(VERTEX_LEFT_BOTTOM, VERTEX_RIGHT_TOP, VERTEX_RIGHT_BOTTOM).setMaterial(material),
                new Triangle(VERTEX_LEFT_BOTTOM, VERTEX_RIGHT_TOP, VERTEX_LEFT_TOP).setMaterial(material));
        scene.lights.add(new DirectionalLight(TRIANGLES_DIRECTIONAL_COLOR, TRIANGLES_DIRECTIONAL_DIRECTION));
        scene.lights.add(new PointLight(TRIANGLES_POINT_COLOR, TRIANGLES_POINT_POSITION)
                .setKl(TRIANGLES_KL).setKq(TRIANGLES_KQ));
        scene.lights.add(new SpotLight(TRIANGLES_SPOT_COLOR, TRIANGLES_SPOT_POSITION, TRIANGLES_SPOT_DIRECTION)
                .setKl(TRIANGLES_KL).setKq(TRIANGLES_KQ));

        Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(CAMERA_LOCATION)
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpSize(TRIANGLES_VP_SIZE, TRIANGLES_VP_SIZE).setVpDistance(VP_DISTANCE)
                .setResolution(RESOLUTION, RESOLUTION)
                .build()
                .renderImage()
                .writeToImage("multiLightTriangles");
    }

    /**
     * Produce a picture of a sphere lit by a directional, a point and a spot light together.
     */
    @Test
    void testMultiLightSphere() {
        Scene scene = new Scene("Multi light sphere");
        scene.geometries.add(new Sphere(SPHERE_CENTER, SPHERE_RADIUS).setEmission(SPHERE_COLOR)
                .setMaterial(new Material().setKD(SPHERE_K).setKS(SPHERE_K).setShininess(SHININESS)));
        scene.lights.add(new DirectionalLight(SPHERE_DIRECTIONAL_COLOR, SPHERE_DIRECTIONAL_DIRECTION));
        scene.lights.add(new PointLight(SPHERE_POINT_COLOR, SPHERE_POINT_POSITION)
                .setKl(SPHERE_KL).setKq(SPHERE_KQ));
        scene.lights.add(new SpotLight(SPHERE_SPOT_COLOR, SPHERE_SPOT_POSITION, SPHERE_SPOT_DIRECTION)
                .setKl(SPHERE_KL).setKq(SPHERE_KQ));

        Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(CAMERA_LOCATION)
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpSize(SPHERE_VP_SIZE, SPHERE_VP_SIZE).setVpDistance(VP_DISTANCE)
                .setResolution(RESOLUTION, RESOLUTION)
                .build()
                .renderImage()
                .writeToImage("multiLightSphere");
    }
}