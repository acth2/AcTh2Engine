package fr.acth2.lwjgl.engine.objects.camera;

import fr.acth2.lwjgl.Main;
import fr.acth2.lwjgl.engine.io.Input;
import fr.acth2.lwjgl.engine.io.ModelLoader;
import fr.acth2.lwjgl.engine.maths.Vector3f;
import fr.acth2.lwjgl.engine.objects.GameObject;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.test.spaceinvaders.Game;

import java.io.IOException;

import static fr.acth2.lwjgl.Main.*;

public class PhysicsCamera {
    private Vector3f position, rotation;
    private float moveSpeed, mouseSensitivity = 0.15f;
    private double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;
    public static boolean hasPistol = false;
    public static boolean hasWeapon = false;
    // dAd  = didnt Already delete
    // sMc  = single model  create
    public static boolean dAd = true;
    public static boolean dAd2 = false;
    public static boolean sMc = true;
    public static boolean sMc2 = false;
    private boolean isJumping = false;
    private float jumpSpeed = 0.075f;
    private float gravity = 0.0015f;
    private float verticalVelocity = 0.0f;
    private float groundPos;
    public PhysicsCamera(Vector3f position, Vector3f rotation, float speed, GameObject ground, float groundColliderComplement) {
        this.position = position;
        this.rotation = rotation;
        this.moveSpeed = speed;
        this.groundPos = ground.getPosition().getY() + groundColliderComplement;
    }

    public void update() throws IOException {
        newMouseX = Input.getMouseX();
        newMouseY = Input.getMouseY();

        float x = (float) Math.sin(Math.toRadians(rotation.getY())) * moveSpeed;
        float z = (float) Math.cos(Math.toRadians(rotation.getY())) * moveSpeed;

        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) position = Vector3f.add(position, new Vector3f(-z, 0, x)); GameObject.cameraRightMovement -= 1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) position = Vector3f.add(position, new Vector3f(z, 0, -x)); GameObject.cameraRightMovement += 1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) position = Vector3f.add(position, new Vector3f(-x, 0, -z)); GameObject.cameraForwardMovement += 1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) position = Vector3f.add(position, new Vector3f(x, 0, z)); GameObject.cameraForwardMovement -= 1;

        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE) && !isJumping && position.getY() == groundPos) {
            isJumping = true;
            verticalVelocity = jumpSpeed;
        }

        if(Input.isKeyDown(GLFW.GLFW_KEY_1)){
            hasWeapon = false;
            hasPistol = true;
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_2)){
            hasWeapon = true;
            hasPistol = false;
        }

        if(!hasPistol){
            if(dAd){
                pistol.getMesh().destroy();
                dAd = false;
                dAd2 = true;
            }
            if(sMc){
                weapon.getMesh().create();
                sMc = false;
                sMc2 = true;
            }
            hasWeapon = true;
            hasPistol = false;
            if(!sMc){
                Main.weapon.update(true);
            }
            Main.renderer.renderObject(weapon, playerCamera);
        }

        if(!hasWeapon){
            if(dAd2){
                weapon.getMesh().destroy();
                dAd2 = false;
                dAd = true;
            }
            if(sMc2){
                pistol.getMesh().create();
                sMc2 = false;
                sMc = true;
            }
            hasWeapon = false;
            hasPistol = true;
            if(!sMc){
                Main.pistol.update(true);
            }
            Main.renderer.renderObject(pistol, playerCamera);
        }

        verticalVelocity -= gravity;
        position = Vector3f.add(position, new Vector3f(0, verticalVelocity, 0));

        if (position.getY() < groundPos) {
            position.setY(groundPos);
            isJumping = false;
            verticalVelocity = 0.0f;
        }

        System.out.println("RX: " + getRotation().getX() + ", RY: " + getRotation().getY() + ", RZ: " + getRotation().getZ());

        float dx = (float) (newMouseX - oldMouseX);
        float dy = (float) (newMouseY - oldMouseY);
        rotation = Vector3f.add(rotation, new Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0));

        if (rotation.getX() >= 90) {
            rotation.setX(90);
        } else if (rotation.getX() <= -90) {
            rotation.setX(-90);
        }

        if(position.getX() == 19.430094f){
            position.setX(19.430094f);
        }

        oldMouseX = newMouseX;
        oldMouseY = newMouseY;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    private Vector3f calculateGunPosition() {
        float gunOffsetX = 2.0f;
        float gunOffsetY = -1.0f;
        float gunOffsetZ = 5.0f;

        float x = position.getX() - gunOffsetX * (float) Math.sin(Math.toRadians(rotation.getY()));
        float y = position.getY() + gunOffsetY;
        float z = position.getZ() - gunOffsetZ * (float) Math.cos(Math.toRadians(rotation.getY()));

        return new Vector3f(x, y, z);
    }
}