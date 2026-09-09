package scene;

import geometries.impl.Geometries;
import lighting.AmbientLight;
import primitives.Color;

/**
 * Passive data structure aggregating everything needed to render a scene:
 * name, background color, ambient light, and geometric model.
 */
public class Scene {
    /** The name of the scene. */
    public String name;

    /** The background color of the scene. Defaults to black. */
    public Color background = Color.BLACK;

    /** The ambient light of the scene. Defaults to {@link AmbientLight#NONE}. */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /** The geometric model of the scene. Defaults to an empty {@link Geometries}. */
    public Geometries geometries = new Geometries();

    /**
     * Creates a scene with the given name; other fields keep their defaults.
     * @param name the name of the scene
     */
    public Scene(String name) { this.name = name; }

    /**
     * Sets the background color of the scene.
     * @param  background the background color
     * @return            this scene, for method chaining
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     * @param  ambientLight the ambient light
     * @return              this scene, for method chaining
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometric model of the scene.
     * @param  geometries the geometries composing the scene
     * @return            this scene, for method chaining
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}