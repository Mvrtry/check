package geometries.api;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * Base abstract class for all geometric shapes.
 */
public abstract class Geometry extends Intersectable {
    /** Emission color of the geometry, default is black (no emission) */
    private Color emission = Color.BLACK;
    /** Material of the geometry, defaults to a plain material with full ambient reflection */
    private Material material = new Material();

    /**
     * Calculates the normal vector to the geometry at a specific point.
     * @param point the point on the geometry surface
     * @return the normal vector
     */
    public abstract Vector getNormal(Point point);

    /**
     * Default constructor for Geometry.
     */
    protected Geometry() {
    }

    /**
     * Returns the emission color of the geometry.
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     * @param emission the emission color to set
     * @return this geometry, for chaining
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Returns the material of the geometry.
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the geometry.
     * @param material the material to set
     * @return this geometry, for chaining
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}