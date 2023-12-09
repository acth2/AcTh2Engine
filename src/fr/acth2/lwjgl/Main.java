package fr.acth2.lwjgl;

import fr.acth2.lwjgl.engine.graphics.*;
import fr.acth2.lwjgl.engine.io.Input;
import fr.acth2.lwjgl.engine.io.ModelLoader;
import fr.acth2.lwjgl.engine.io.Window;
import fr.acth2.lwjgl.engine.maths.Vector3f;
import fr.acth2.lwjgl.engine.objects.Camera;
import fr.acth2.lwjgl.engine.objects.GameObject;
import fr.acth2.lwjgl.utils.Explorer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main implements Runnable
{
    public Thread game;
    public Window window;
    public Renderer renderer;
    public static AtomicBoolean fullscreenAtomic = new AtomicBoolean(false);
    public static int xPos;
    public static int yPos;
    public Shader shader;
    public static int[] WIDTH = new int [1], HEIGHT = new int [1];

    public static String os = System.getProperty("os.name").toLowerCase();

    public Mesh mesh = ModelLoader.loadModel("resources/models/dragon.obj", "/textures/unknown.png");

    public GameObject object = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), mesh);

    public Camera camera = new Camera(new Vector3f(0, 0, 1), new Vector3f(0, 0, 0));
    public void start(){
        game = new Thread(this, "game");
        game.run();
    }

    public void init() throws IOException {
        WIDTH[0] = 1280;
        HEIGHT[0] = 750;
        window = new Window(WIDTH[0], HEIGHT[0], "3D Template");
        shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
        renderer = new Renderer(window, shader);
        window.setBackgroundColor(0.35f, 0.23f, 0.10f);
        window.create();
        mesh.create();
        shader.create();
    }

    public void run() {
        try { init(); } catch (IOException e) { throw new RuntimeException(e); }

        boolean fullscreen = false;
        boolean f11Pressed = false;


        while (!window.getClose()) {
            update();
            render();

            if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)){ if(!window.isFullscreen()){ return; }else { if (Main.os.contains("win")) { try { Runtime.getRuntime().exec("explorer.exe"); } catch (IOException e) { throw new RuntimeException(e); } } return; } }
            if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SUPER) || Input.isKeyDown(GLFW.GLFW_KEY_RIGHT_SUPER)) { window.setFullscreen(false); if (Main.os.contains("win")) { if (!Explorer.isExplorerRunning()) { try { Runtime.getRuntime().exec("explorer.exe"); } catch (IOException e) { throw new RuntimeException(e); } } } }
            boolean currentF11State = Input.isKeyDown(GLFW.GLFW_KEY_F11); if (currentF11State && !f11Pressed) { fullscreen = !fullscreen; if (!fullscreenAtomic.getAndSet(true)) { IntBuffer xBuffer = BufferUtils.createIntBuffer(1); IntBuffer yBuffer = BufferUtils.createIntBuffer(1); GLFW.glfwGetWindowPos(window.getWindow(), xBuffer, yBuffer); GLFW.glfwGetWindowSize(window.getWindow(), WIDTH, HEIGHT);xPos = xBuffer.get(0); yPos = yBuffer.get(0);if (os.contains("win")) { try { Runtime.getRuntime().exec("taskkill /f /im explorer.exe"); } catch (IOException e) { throw new RuntimeException(e); } }} window.setFullscreen(fullscreen); } f11Pressed = currentF11State;
            if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) window.mouseState(true);
        }
        close();
    }



    private void update(){
        window.update();
        camera.update();
    }

    private void render(){
        renderer.renderObject(object, camera);
        window.swapBuffers();
    }

    private void close(){
        window.destroy();
        mesh.destroy();
        shader.destroy();
    }

    public static void main(String[] args){
        new Main().start();
    }
}
