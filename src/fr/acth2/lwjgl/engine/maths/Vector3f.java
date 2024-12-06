package fr.acth2.lwjgl.engine.maths;

import java.util.Objects;

public class Vector3f {
    private float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3f add(Vector3f vector1, Vector3f vector2){
        return new Vector3f(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY(), vector1.getZ() + vector2.getZ());
    }

    public void add(Vector3f other) {
        this.x += other.getX();
        this.y += other.getY();
        this.z += other.getZ();
    }

    public static Vector3f subtract(Vector3f vector1, Vector3f vector2) {
        return new Vector3f(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(), vector1.getZ() - vector2.getZ());
    }

    public static Vector3f multiply(Vector3f vector1, Vector3f vector2){
        return new Vector3f(vector1.getX() * vector2.getX(), vector1.getY() * vector2.getY(), vector1.getZ() * vector2.getZ());
    }

    public static Vector3f multiply(Vector3f vector, float scalar) {
        return new Vector3f(vector.getX() * scalar, vector.getY() * scalar, vector.getZ() * scalar);
    }

    public Vector3f multiply(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    public void scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    public static Vector3f div(Vector3f vector1, Vector3f vector2){
        return new Vector3f(vector1.getX() / vector2.getX(), vector1.getY() / vector2.getY(), vector1.getZ() / vector2.getZ());
    }

    public static float length(Vector3f vector) {
        return (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public static Vector3f normalize(Vector3f vector){
        float len = Vector3f.length(vector);
        return Vector3f.div(vector, new Vector3f(len, len, len));
    }

    public void normalize() {
        float length = length();
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
        }
    }

    public Vector3f normalize(boolean diff1) {
        float length = (float) Math.sqrt(x * x + y * y + z * z);
        if (length != 0) {
            float invLength = 1.0f / length;
            return new Vector3f(x * invLength, y * invLength, z * invLength);
        } else {
            return new Vector3f(0, 0, 0);
        }
    }


    public static Vector3f rotateY(Vector3f vector, float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        float newX = cos * vector.getX() - sin * vector.getZ();
        float newY = vector.getY();
        float newZ = sin * vector.getX() + cos * vector.getZ();

        return new Vector3f(newX, newY, newZ);
    }

    public static float dot(Vector3f vector1, Vector3f vector2){
        return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3f vector3f = (Vector3f) o;
        return Float.compare(x, vector3f.x) == 0 && Float.compare(y, vector3f.y) == 0 && Float.compare(z, vector3f.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Vector3f getForward() {
        float x = (float) (-Math.sin(Math.toRadians(y)));
        float y = 0;
        float z = (float) (Math.cos(Math.toRadians(y)));
        return new Vector3f(x, y, z);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

}
