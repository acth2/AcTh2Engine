package fr.acth2.lwjgl;

import fr.acth2.lwjgl.engine.graphics.*;
import fr.acth2.lwjgl.engine.graphics.light.DirectionalLight;
import fr.acth2.lwjgl.engine.io.Input;
import fr.acth2.lwjgl.engine.io.ModelLoader;
import fr.acth2.lwjgl.engine.io.Window;
import fr.acth2.lwjgl.engine.maths.Vector3f;
import fr.acth2.lwjgl.engine.objects.camera.IntroCamera;
import fr.acth2.lwjgl.engine.objects.GameObject;
import fr.acth2.lwjgl.engine.objects.camera.StaticCamera;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main implements Runnable
{
    public Thread game;
    public static Window window;
    public static Renderer renderer;
    public static boolean onPlayButton = true;
    public static boolean onQuitButton = false;
    public static boolean onMenu = false;
    public static boolean introHold = true;
    public static AtomicBoolean fullscreenAtomic = new AtomicBoolean(false);
    public static int xPos;
    public static int yPos;
    public static Shader shader;
    public static int[] WIDTH = new int [1], HEIGHT = new int [1];

    public static String os = System.getProperty("os.name").toLowerCase();
    public static AtomicBoolean playAtomic = new AtomicBoolean(false);

    static Mesh introMesh = ModelLoader.loadModel("resources/models/cube.obj", "/textures/a2ei.png", 1f);
    static Mesh playMenuMesh = ModelLoader.loadModel("resources/models/cube.obj", "/textures/menu/playButtonActived/menuPlayActived.png", 1f);
    static Mesh quitMenuMesh = ModelLoader.loadModel("resources/models/cube.obj", "/textures/menu/exitButtonActived/quitButtonActived.png", 1f);
    static Mesh worldMesh = ModelLoader.loadModel("resources/models/world.obj", "/textures/world.png", 1f);
    public static Mesh pistolMesh = ModelLoader.loadModel("resources/models/pistol.obj", "/textures/weapon/weapon.png", 1f);
    public static Mesh weaponMesh = ModelLoader.loadModel("resources/models/weapon.obj", "/textures/weapon/weapon.png", 0.25f);
    public static GameObject intro = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), introMesh);

    public static GameObject menuPlay = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), playMenuMesh);
    public static GameObject menuQuit = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), quitMenuMesh);
    public static GameObject world = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(6.5f, 6.5f, 6.5f), worldMesh);
    public static GameObject pistol = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0.25f, 0.25f, 0.25f), pistolMesh);
    public static GameObject weapon = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f), weaponMesh);

    public IntroCamera introCamera = new IntroCamera(new Vector3f(0, 0, 1), new Vector3f(0, 0, 0));
    public IntroCamera menuCamera = new IntroCamera(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));

    public static StaticCamera playerCamera = new StaticCamera(new Vector3f(-0.058033675f, 1.8549997f, 0.1646241f), new Vector3f(0.0f, 0.0f, 0.0f));


    static {
        if(ModelLoader.stop){
            introMesh = ModelLoader.loadModel("resources/models/unknown.obj", "/textures/unknown.png", 1f);
            playMenuMesh = ModelLoader.loadModel("resources/models/unknown.obj", "/textures/unknown.png", 1f);
            quitMenuMesh = ModelLoader.loadModel("resources/models/unknown.obj", "/textures/unknown.png", 1f);
        }
    }
    public void start(){
        game = new Thread(this, "game");
        game.run();
    }

    public void loadGame() throws IOException {
        onMenu = false;
        onQuitButton = false;
        onPlayButton = false;
        window.mouseState(true);
        playMenuMesh.destroy();
        quitMenuMesh.destroy();
        worldMesh.create();
    }

    public void init() throws IOException {
        WIDTH[0] = 1280;
        HEIGHT[0] = 750;
        window = new Window(WIDTH[0], HEIGHT[0], "3D Template", Integer.MAX_VALUE);
        shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
        renderer = new Renderer(window, shader);
        window.create();
        introMesh.create();
        playMenuMesh.create();
        quitMenuMesh.create();
        shader.create();
    }

    public void run() {
        try { init(); } catch (IOException e) { throw new RuntimeException(e); }

        boolean fullscreen = false;
        boolean f11Pressed = false;


        while (!window.getClose()) {
            try { update(); render(); } catch (IOException e) { throw new RuntimeException(e); }
            if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)){ if(!window.isFullscreen()){ return; }else { if (Main.os.contains("win")) { try { Runtime.getRuntime().exec("explorer.exe"); } catch (IOException e) { throw new RuntimeException(e); } } return; } }
            if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && !onMenu){ window.mouseState(true); }
            if(introHold){ try { Thread.sleep(2000); } catch (InterruptedException e) { throw new RuntimeException(e); } introHold = false; introMesh.destroy(); }
            if(onMenu){
                if (Input.isKeyDown(GLFW.GLFW_KEY_UP) || Input.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
                    try { Thread.sleep(120); } catch (InterruptedException e) { throw new RuntimeException(e); }
                    onQuitButton = !onQuitButton;
                    onPlayButton = !onPlayButton;
                    try {
                        if(onPlayButton){
                            playMenuMesh.create();
                            quitMenuMesh.destroy();
                        }else {
                            quitMenuMesh.create();
                            playMenuMesh.destroy();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (Input.isKeyDown(GLFW.GLFW_KEY_ENTER)){
                    if(onPlayButton && !playAtomic.getAndSet(true)){
                        try { loadGame(); } catch (IOException e) { throw new RuntimeException(e); }
                    }
                    if(onQuitButton){
                        System.exit(0);
                    }
                }
            }
        }
        close(true, true);
    }


    private void update() throws IOException {
        window.update();
        if(introHold){
            introCamera.update();
        }else if (onPlayButton || onQuitButton) {
            menuCamera.update();
        }else {
            playerCamera.update();
        }
    }

    private void render(){
        if(introHold){
            renderer.renderObject(intro, introCamera);
        }else {
            if(onPlayButton){
                renderer.renderObject(menuPlay, menuCamera);
            }else if (onQuitButton)
            {
                renderer.renderObject(menuQuit, menuCamera);
            }else {
                renderer.renderObject(world, playerCamera);
            }
        }
        window.swapBuffers();
    }

    public static void close(boolean destroyWindow, boolean destroyShader){
        if(destroyWindow) window.destroy();
        introMesh.destroy();
        if (destroyShader) shader.destroy();
    }

    public static void main(String[] args){
        new Main().start();
    }
}
