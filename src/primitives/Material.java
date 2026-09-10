package primitives;

/**
 * PDS representing the material properties of a geometry, controlling how it reacts to light.
 */
public class Material {
    /** Ambient light attenuation coefficient, default is full reflection (1,1,1) */
    public Double3 kA = Double3.ONE;

    /**
     * Sets the ambient light attenuation coefficient.
     * @param kA the attenuation coefficient
     * @return this material, for chaining
     */
    public Material setKa(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Sets the ambient light attenuation coefficient from a single scalar.
     * @param kA the attenuation coefficient value for all channels
     * @return this material, for chaining
     */
    public Material setKa(double kA) {
        this.kA = new Double3(kA);
        return this;
    }
}
