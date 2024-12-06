package fr.acth2.lwjgl.engine.objects;


import fr.acth2.lwjgl.Main;
import fr.acth2.lwjgl.engine.graphics.Mesh;
import fr.acth2.lwjgl.engine.maths.Vector3f;

public class GameObject {
    private Vector3f position, rotation, scale;
    public static float cameraRightMovement = 0, cameraForwardMovement = 0;
    private Mesh mesh;

    public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.mesh = mesh;
    }

    public void update(boolean b) {
        double playerX = Main.playerCamera.getPosition().getX(); // Remplacez par la vraie méthode pour obtenir la coordonnée X du joueur
        double playerY = Main.playerCamera.getPosition().getY(); // Remplacez par la vraie méthode pour obtenir la coordonnée Y du joueur
        double playerZ = Main.playerCamera.getPosition().getZ(); // Remplacez par la vraie méthode pour obtenir la coordonnée Z du joueur
        
        double objectX = 1.0;
        double objectY = 2.0;
        double objectZ = 3.0;

        double yaw = Math.toRadians(playerY);

        double objectXPrime = objectX * Math.cos(yaw) - objectZ * Math.sin(yaw);
        double objectZPrime = objectX * Math.sin(yaw) + objectZ * Math.cos(yaw);

        System.out.println("Coordonnées de l'objet après rotation : (" + objectXPrime + ", " + objectY + ", " + objectZPrime + ")");
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Mesh getMesh() {
        return mesh;
    }
}