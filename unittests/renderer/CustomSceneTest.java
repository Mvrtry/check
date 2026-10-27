package renderer;

import org.junit.jupiter.api.Test;

import geometries.impl.Plane;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.*;
import scene.Scene;

/**
 * Custom Stage 8 scene combining shadows, partial shadows, transparency and reflection.
 */
class CustomSceneTest {
    /** Default constructor to satisfy JavaDoc generator */
    CustomSceneTest() { /* to satisfy JavaDoc generator */ }

    // ============ Camera ==============
    /** Camera location: in front of and above the scene. */
    private static final Point CAMERA_LOCATION = new Point(0, 260, 1400);
    /** Camera target point: slightly behind the scene center. */
    private static final Point CAMERA_TARGET = new Point(0, -20, -100);
    /** Camera to view plane distance. */
    private static final double VP_DISTANCE = 1000;
    /** View plane width and height. */
    private static final double VP_SIZE = 700;
    /** Image resolution (pixels per side). */
    private static final int RESOLUTION = 800;

    // ============ Floor (Plane) - receives all shadows, slightly glossy ==============
    /** A point on the floor plane. */
    private static final Point FLOOR_POINT = new Point(0, -100, 0);
    /** Floor color: dark warm stone. */
    private static final Color FLOOR_COLOR = new Color(15, 12, 10);
    /** Floor material: matte with a faint reflection. */
    private static final Material FLOOR_MATERIAL = new Material().setKD(0.4).setKS(0.15).setShininess(40).setKR(0.15);

    // ============ Mirror (two Triangles) - back wall, reflection ==============
    /** Mirror bottom-left corner. */
    private static final Point MIRROR_BOTTOM_LEFT = new Point(-520, -100, -450);
    /** Mirror bottom-right corner. */
    private static final Point MIRROR_BOTTOM_RIGHT = new Point(420, -100, -600);
    /** Mirror top-right corner (top edge tilted toward the camera). */
    private static final Point MIRROR_TOP_RIGHT = new Point(420, 380, -450);
    /** Mirror top-left corner (top edge tilted toward the camera). */
    private static final Point MIRROR_TOP_LEFT = new Point(-520, 380, -300);
    /** Mirror color: almost black glass tint. */
    private static final Color MIRROR_COLOR = new Color(10, 10, 20);
    /** Mirror material: strong reflection, no transparency. */
    private static final Material MIRROR_MATERIAL = new Material().setKD(0.04).setKS(0.3).setShininess(200).setKR(0.85);

    // ============ Glass sphere - transparency and partial shadow ==============
    /** Glass sphere center: front-left. */
    private static final Point GLASS_CENTER = new Point(-120, 0, 150);
    /** Glass sphere radius. */
    private static final double GLASS_RADIUS = 100;
    /** Glass sphere color: deep cyan tint. */
    private static final Color GLASS_COLOR = new Color(0, 40, 60);
    /** Glass sphere material: highly transparent, no reflection. */
    private static final Material GLASS_MATERIAL = new Material().setKD(0.15).setKS(0.6).setShininess(300).setKT(0.75);

    // ============ Ruby sphere - seen through the glass sphere ==============
    /** Ruby sphere center: directly behind the glass sphere from the camera view. */
    private static final Point RUBY_CENTER = new Point(-170, -55, -110);
    /** Ruby sphere radius. */
    private static final double RUBY_RADIUS = 45;
    /** Ruby sphere color: deep red. */
    private static final Color RUBY_COLOR = new Color(170, 0, 20);
    /** Ruby sphere material: opaque and glossy. */
    private static final Material RUBY_MATERIAL = new Material().setKD(0.3).setKS(0.5).setShininess(120);

    // ============ Gold sphere - opaque, hard shadow ==============
    /** Gold sphere center: front-right, resting on the floor. */
    private static final Point GOLD_CENTER = new Point(170, -20, 60);
    /** Gold sphere radius. */
    private static final double GOLD_RADIUS = 80;
    /** Gold sphere color: warm gold. */
    private static final Color GOLD_COLOR = new Color(110, 70, 0);
    /** Gold sphere material: opaque and shiny. */
    private static final Material GOLD_MATERIAL = new Material().setKD(0.5).setKS(0.6).setShininess(150);

    // ============ Emerald triangle - opaque standing shard ==============
    /** Emerald triangle first base vertex. */
    private static final Point EMERALD_BASE_LEFT = new Point(40, -100, -250);
    /** Emerald triangle second base vertex. */
    private static final Point EMERALD_BASE_RIGHT = new Point(260, -100, -320);
    /** Emerald triangle apex. */
    private static final Point EMERALD_APEX = new Point(130, 230, -300);
    /** Emerald triangle color: green. */
    private static final Color EMERALD_COLOR = new Color(0, 90, 40);
    /** Emerald triangle material: opaque, semi-glossy. */
    private static final Material EMERALD_MATERIAL = new Material().setKD(0.4).setKS(0.4).setShininess(80);

    // ============ Lights ==============
    /** Scene background: deep night blue, outlines the mirror edges. */
    private static final Color BACKGROUND = new Color(8, 10, 28);
    /** Ambient light: dim neutral fill. */
    private static final AmbientLight AMBIENT = new AmbientLight(new Color(25, 25, 30));
    /** Spot light color: warm white key light. */
    private static final Color SPOT_COLOR = new Color(420, 330, 240);
    /** Spot light position: upper front-left. */
    private static final Point SPOT_POSITION = new Point(-450, 500, 500);
    /** Spot light direction: toward the scene center floor. */
    private static final Vector SPOT_DIRECTION = new Vector(450, -560, -550);
    /** Point light color: cool blue rim light. */
    private static final Color POINT_COLOR = new Color(90, 120, 260);
    /** Point light position: upper right, behind the gold sphere. */
    private static final Point POINT_POSITION = new Point(450, 350, 100);
    /** Linear attenuation of both lights. */
    private static final double KL = 4E-4;
    /** Quadratic attenuation of both lights. */
    private static final double KQ = 1E-6;

    /** Renders the custom scene demonstrating all Stage 8 effects together. */
    @Test
    void testCustomScene() {
        Scene scene = new Scene("Custom scene")
                .setAmbientLight(AMBIENT)
                .setBackground(BACKGROUND);
        scene.geometries.add(
                new Plane(FLOOR_POINT, Vector.AXIS_Y)
                        .setEmission(FLOOR_COLOR).setMaterial(FLOOR_MATERIAL),
                new Triangle(MIRROR_BOTTOM_LEFT, MIRROR_BOTTOM_RIGHT, MIRROR_TOP_RIGHT)
                        .setEmission(MIRROR_COLOR).setMaterial(MIRROR_MATERIAL),
                new Triangle(MIRROR_BOTTOM_LEFT, MIRROR_TOP_RIGHT, MIRROR_TOP_LEFT)
                        .setEmission(MIRROR_COLOR).setMaterial(MIRROR_MATERIAL),
                new Sphere(GLASS_CENTER, GLASS_RADIUS)
                        .setEmission(GLASS_COLOR).setMaterial(GLASS_MATERIAL),
                new Sphere(RUBY_CENTER, RUBY_RADIUS)
                        .setEmission(RUBY_COLOR).setMaterial(RUBY_MATERIAL),
                new Sphere(GOLD_CENTER, GOLD_RADIUS)
                        .setEmission(GOLD_COLOR).setMaterial(GOLD_MATERIAL),
                new Triangle(EMERALD_BASE_LEFT, EMERALD_BASE_RIGHT, EMERALD_APEX)
                        .setEmission(EMERALD_COLOR).setMaterial(EMERALD_MATERIAL));
        scene.lights.add(new SpotLight(SPOT_COLOR, SPOT_POSITION, SPOT_DIRECTION).setKl(KL).setKq(KQ));
        scene.lights.add(new PointLight(POINT_COLOR, POINT_POSITION).setKl(KL).setKq(KQ));

        Camera.getBuilder()
                .setLocation(CAMERA_LOCATION)
                .setDirection(CAMERA_TARGET, Vector.AXIS_Y)
                .setVpDistance(VP_DISTANCE).setVpSize(VP_SIZE, VP_SIZE)
                .setResolution(RESOLUTION, RESOLUTION)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage("customScene");
    }
}
