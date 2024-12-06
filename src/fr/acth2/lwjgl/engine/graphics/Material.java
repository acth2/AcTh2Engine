package fr.acth2.lwjgl.engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.IOException;

import static fr.acth2.lwjgl.utils.Logger.*;

public class Material {
    private String path;
    private Texture texture;
    private float width, height;
    private int textureID;

    public Material(String path) {
        this.path = String.valueOf(path);
    }

    public void create() throws IOException {
        try {
            texture = TextureLoader.getTexture(path.split("[.]")[1], Material.class.getResourceAsStream(path), GL11.GL_NEAREST);
            width = texture.getWidth();
            height = texture.getHeight();
            textureID = texture.getTextureID();
        }catch (NullPointerException e2){
            texture = TextureLoader.getTexture(path.split("[.]")[1], Material.class.getResourceAsStream("/textures/unknown.png"), GL11.GL_NEAREST);
            width = texture.getWidth();
            height = texture.getHeight();
            textureID = texture.getTextureID();
            err("Cannot find texture at " + path);
            err("Setting up the unknown.png texture");
        }
    }

    public void destroy() {
        GL13.glDeleteTextures(textureID);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getTextureID() {
        return textureID;
    }
}