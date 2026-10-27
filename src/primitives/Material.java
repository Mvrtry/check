package primitives;

/**
 * PDS representing the material properties of a geometry, controlling how it reacts to light.
 */
public class Material {
    /** Ambient light attenuation coefficient, default is full reflection (1,1,1) */
    public Double3 kA = Double3.ONE;
    /** Diffuse reflection attenuation coefficient, default is no reflection */
    public Double3 kD = Double3.ZERO;
    /** Specular reflection attenuation coefficient, default is no reflection */
    public Double3 kS = Double3.ZERO;
    /** Transparency attenuation coefficient, default is opaque */
    public Double3 kT = Double3.ZERO;
    /** Reflection attenuation coefficient, default is no reflection */
    public Double3 kR = Double3.ZERO;
    /** Shininess exponent of the specular reflection, default is 0 */
    public int nShininess = 0;

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

    /**
     * Sets the diffuse reflection attenuation coefficient.
     * @param kD the diffuse attenuation coefficient
     * @return this material, for chaining
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection attenuation coefficient from a single scalar.
     * @param kD the diffuse attenuation coefficient value for all channels
     * @return this material, for chaining
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular reflection attenuation coefficient.
     * @param kS the specular attenuation coefficient
     * @return this material, for chaining
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection attenuation coefficient from a single scalar.
     * @param kS the specular attenuation coefficient value for all channels
     * @return this material, for chaining
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the transparency attenuation coefficient.
     * @param kT the transparency attenuation coefficient
     * @return this material, for chaining
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transparency attenuation coefficient from a single scalar.
     * @param kT the transparency attenuation coefficient value for all channels
     * @return this material, for chaining
     */
    public Material setKT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Sets the reflection attenuation coefficient.
     * @param kR the reflection attenuation coefficient
     * @return this material, for chaining
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection attenuation coefficient from a single scalar.
     * @param kR the reflection attenuation coefficient value for all channels
     * @return this material, for chaining
     */
    public Material setKR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Sets the shininess exponent of the specular reflection.
     * @param nShininess the shininess exponent
     * @return this material, for chaining
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}