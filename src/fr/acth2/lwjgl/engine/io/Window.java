package fr.acth2.lwjgl.engine.io;
import fr.acth2.lwjgl.Main;
import fr.acth2.lwjgl.engine.maths.Matrix4f;
import fr.acth2.lwjgl.engine.maths.Vector3f;
import fr.acth2.lwjgl.engine.objects.GameObject;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

import static fr.acth2.lwjgl.Main.*;
import static fr.acth2.lwjgl.utils.Logger.*;
import static org.lwjgl.opengl.GL11.glColor3f;

public class Window
{
    private int width, height;
    private String title;
    private long window;
    public int frames;
    public Input input;
    private Vector3f background = new Vector3f(0, 0, 0);
    private GLFWWindowSizeCallback sizeCallback;
    private boolean isResized;
    private boolean isFullscreen;
    public GLFWVidMode videoMode;
    public static long time;
    private Matrix4f projection;

    public Window(int width, int height, String title, float howFar){
        this.width = width;
        this.height = height;
        this.title = title;
        projection = Matrix4f.projection(80, (float) width / (float) height, 0.01f, howFar);
    }

    private int[] windowPosX = new int [1], windowPosY = new int [1];

    public void create(){
        if (!GLFW.glfwInit()){
            err("GFLW is not initialized");
            return;
        }


        input = new Input();
        window = GLFW.glfwCreateWindow(width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);

        if(window == 0) {
            err("Window is not created");
            return;
        }
        videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        windowPosX[0] = (videoMode.width() - width / 2);
        windowPosY[0] = (videoMode.height() - height / 2);
        GLFW.glfwSetWindowPos(window, 0, 35);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        createCallbacks();

        GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());

        GLFW.glfwShowWindow(window);

        GLFW.glfwSwapInterval(1);
        time = System.currentTimeMillis();
    }

    public void destroy(){
        input.destroy();
        sizeCallback.free();
        GLFW.glfwWindowShouldClose(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    public void setBackgroundColor(float r, float g, float b){
        background.set(r, g, b);
    }

    private void createCallbacks(){

        sizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                isResized = !isResized;
            }
        };
        GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
        GLFW.glfwSetScrollCallback(window, input.getMouseScrollCallback());
        GLFW.glfwSetWindowSizeCallback(window, sizeCallback);
    }

    public void update(){
        if (isResized) {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int)size.getWidth();
            int screenHeight = (int)size.getHeight();
            GL11.glViewport(0, 0, width, height);
            isResized = !isResized;
        }
        GL11.glViewport(0, 0, width, height);
        GL11.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GLFW.glfwPollEvents();
        frames++;
        if (System.currentTimeMillis() > time + 1000) {
            GLFW.glfwSetWindowTitle(window, "[" + title + "] " + "FPS: " + frames);
            time = System.currentTimeMillis();
            frames = 0;
        }
    }

    public void swapBuffers(){
        GLFW.glfwSwapBuffers(window);
    }

    public boolean getClose(){
        return GLFW.glfwWindowShouldClose(window);
    }

    public GLFWVidMode getVideoMode() {
        return videoMode;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public long getWindow() {
        return window;
    }

    public boolean isResized() {
        return isResized;
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void mouseState(boolean lock){
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, lock ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
    }

    public Matrix4f getProjectionMatrix() {
        return projection;
    }

}
