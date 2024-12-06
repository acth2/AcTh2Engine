package fr.acth2.lwjgl.engine.objects.camera;

import fr.acth2.lwjgl.Main;
import fr.acth2.lwjgl.engine.io.Input;
import fr.acth2.lwjgl.engine.maths.Vector3f;
import fr.acth2.lwjgl.engine.objects.GameObject;
import org.lwjgl.glfw.GLFW;

public class IntroCamera {
    private Vector3f position, rotation;

    public IntroCamera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void update(){
        if(!Main.introHold){
            position.set(-0.01673561f, 0.86030257f, -0.77405167f);
            rotation.set(-90f, 0.29f, 0.0f);
            Main.onMenu = true;
        }else {
            position.set(-0.15673561f, 0.86030257f, -0.77405167f);
            rotation.set(-90f, 0.30009097f, 0.0f);
            Main.onMenu = true;
        }
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