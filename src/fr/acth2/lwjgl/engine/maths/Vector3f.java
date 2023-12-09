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

    public static Vector3f substract(Vector3f vector1, Vector3f vector2){
        return new Vector3f(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(), vector1.getZ() - vector2.getZ());
    }

    public static Vector3f multiply(Vector3f vector1, Vector3f vector2){
        return new Vector3f(vector1.getX() * vector2.getX(), vector1.getY() * vector2.getY(), vector1.getZ() * vector2.getZ());
    }

    public static Vector3f div(Vector3f vector1, Vector3f vector2){
        return new Vector3f(vector1.getX() / vector2.getX(), vector1.getY() / vector2.getY(), vector1.getZ() / vector2.getZ());
    }

    public static float length(Vector3f vector) {
        return (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
    }

    public static Vector3f normalize(Vector3f vector){
        float len = Vector3f.length(vector);
        return Vector3f.div(vector, new Vector3f(len, len, len));
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
