package primitives;

/**
 * Represents a vector in 3D space.
 */
public class Vector extends Point {

    /** The X axis vector (1,0,0) */
    @SuppressWarnings("unused")
    public static final Vector AXIS_X = new Vector(1, 0, 0);

    /** The Y axis vector (0,1,0) */
    @SuppressWarnings("unused")
    public static final Vector AXIS_Y = new Vector(0, 1, 0);

    /** The Z axis vector (0,0,1) */
    public static final Vector AXIS_Z = new Vector(0, 0, 1);

    /**
     * Constructor taking three coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @throws IllegalArgumentException if it's the zero vector
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (_xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be zero");
        }
    }

    /**
     * Constructor taking a Double3 object.
     * @param xyz the Double3 tuple
     * @throws IllegalArgumentException if it's the zero vector
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (_xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be zero");
        }
    }

    /**
     * Adds another vector to this vector.
     * @param other the vector to add
     * @return a new Vector
     */
    public Vector add(Vector other) {
        return new Vector(_xyz.add(other._xyz));
    }

    /**
     * Scales the vector by a scalar.
     * @param scalar the scalar to multiply by
     * @return a new scaled Vector
     */
    public Vector scale(double scalar) {
        return new Vector(_xyz.scale(scalar));
    }

    /**
     * Computes the dot product of this vector and another vector.
     * @param other the other vector
     * @return the dot product
     */
    public double dotProduct(Vector other) {
        return _xyz._d1() * other._xyz._d1() +
                _xyz._d2() * other._xyz._d2() +
                _xyz._d3() * other._xyz._d3();
    }

    /**
     * Computes the cross product of this vector and another vector.
     * @param other the other vector
     * @return a new orthogonal Vector
     */
    public Vector crossProduct(Vector other) {
        return new Vector(
                _xyz._d2() * other._xyz._d3() - _xyz._d3() * other._xyz._d2(),
                _xyz._d3() * other._xyz._d1() - _xyz._d1() * other._xyz._d3(),
                _xyz._d1() * other._xyz._d2() - _xyz._d2() * other._xyz._d1()
        );
    }

    /**
     * Calculates the squared length of the vector.
     * @return the squared length
     */
    public double lengthSquared() {
        return dotProduct(this);
    }

    /**
     * Calculates the length of the vector.
     * @return the length
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes the vector.
     * @return a new normalized Vector
     */
    public Vector normalize() {
        return scale(1.0 / length());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Vector other && super.equals(other);
    }

    @Override
    public String toString() {
        return "->" + super.toString();
    }
}