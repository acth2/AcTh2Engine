package fr.acth2.lwjgl.engine.objects.camera;

import fr.acth2.lwjgl.Main;
import fr.acth2.lwjgl.engine.io.Input;
import fr.acth2.lwjgl.engine.maths.Vector3f;
import fr.acth2.lwjgl.engine.objects.GameObject;
import org.lwjgl.glfw.GLFW;

public class StaticCamera {
    private Vector3f position, rotation;
    private float mouseSensitivity = 0.15f, distance = 2.0f, horizontalAngle = 0.0f, verticalAngle = 0.0f;
    private double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;

    public StaticCamera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void update() {
        newMouseX = Input.getMouseX();
        newMouseY = Input.getMouseY();

        float dx = (float) (newMouseX - oldMouseX);
        float dy = (float) (newMouseY - oldMouseY);

        rotation = Vector3f.add(rotation, new Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0));

        if (rotation.getX() >= 90) {
            rotation.setX(90);
        } else if (rotation.getX() <= -90) {
            rotation.setX(-90);
        }

        oldMouseX = newMouseX;
        oldMouseY = newMouseY;
    }

    public void update(GameObject object) {
        newMouseX = Input.getMouseX();
        newMouseY = Input.getMouseY();

        float dx = (float) (newMouseX - oldMouseX);
        float dy = (float) (newMouseY - oldMouseY);

        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            verticalAngle -= dy * mouseSensitivity;
            horizontalAngle += dx * mouseSensitivity;
        }
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            if (distance > 0) {
                distance += dy * mouseSensitivity / 4;
            } else {
                distance = 0.1f;
            }
        }

        float horizontalDistance = (float) (distance * Math.cos(Math.toRadians(verticalAngle)));
        float verticalDistance = (float) (distance * Math.sin(Math.toRadians(verticalAngle)));

        float xOffset = (float) (horizontalDistance * Math.sin(Math.toRadians(-horizontalAngle)));
        float zOffset = (float) (horizontalDistance * Math.cos(Math.toRadians(-horizontalAngle)));

        position.set(object.getPosition().getX() + xOffset, object.getPosition().getY() - verticalDistance, object.getPosition().getZ() + zOffset);

        rotation.set(verticalAngle, -horizontalAngle, 0);

        oldMouseX = newMouseX;
        oldMouseY = newMouseY;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    private boolean checkCollision(GameObject object) {
        float distanceX = Math.abs(position.getX() - object.getPosition().getX());
        float distanceY = Math.abs(position.getY() - object.getPosition().getY());
        float distanceZ = Math.abs(position.getZ() - object.getPosition().getZ());

        float threshold = 1.0f;

        return distanceX < threshold && distanceY < threshold && distanceZ < threshold;
    }
}