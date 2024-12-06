package fr.acth2.lwjgl.engine.graphics.light;

import fr.acth2.lwjgl.engine.maths.Vector3f;

public class DirectionalLight {
    private Vector3f direction;
    private Vector3f color;

    public DirectionalLight(Vector3f direction, Vector3f color) {
        this.direction = direction.normalize(true);
        this.color = color;
    }


    public Vector3f getDirection() {
        return direction;
    }

    public Vector3f getColor() {
        return color;
    }
}