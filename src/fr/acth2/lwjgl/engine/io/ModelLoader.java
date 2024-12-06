package fr.acth2.lwjgl.engine.io;

import fr.acth2.lwjgl.engine.graphics.Material;
import fr.acth2.lwjgl.engine.graphics.Mesh;
import fr.acth2.lwjgl.engine.graphics.Vertex;
import fr.acth2.lwjgl.engine.maths.Vector2f;
import fr.acth2.lwjgl.engine.maths.Vector3f;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;

public class ModelLoader {

    private static float x, y, z = 0;

    public static boolean stop = false;
    private static IntBuffer width = BufferUtils.createIntBuffer(1);
    static IntBuffer height = BufferUtils.createIntBuffer(1);


    public static Mesh loadModel(String filePath, String texturePath, float scale) {
        AIScene scene = Assimp.aiImportFile(filePath, Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate);

        if (scene == null) {
            System.err.println("Cannot load the model at: " + filePath);
            System.err.println("Loading model unknown..");
            texturePath = "/textures/unknownModel.png";
            stop = !stop;
            loadModel("resources/models/unknown.obj", "/textures/unknownModel.png", 1.0f);
            return null;
        }

        AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));
        int vertexCount = mesh.mNumVertices();

        AIVector3D.Buffer vertices = mesh.mVertices();
        AIVector3D.Buffer normals = mesh.mNormals();

        Vertex[] vertexList = new Vertex[vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            AIVector3D vertex = vertices.get(i);
            Vector3f meshVertex = new Vector3f(vertex.x() * scale, vertex.y() * scale, vertex.z() * scale);

            AIVector3D normal = normals.get(i);
            Vector3f meshNormal = new Vector3f(normal.x(), normal.y(), normal.z());

            Vector2f meshTextureCoord = new Vector2f(0, 0);
            if (mesh.mNumUVComponents().get(0) != 0) {
                AIVector3D texture = mesh.mTextureCoords(0).get(i);
                meshTextureCoord.setX(texture.x() * scale);
                meshTextureCoord.setY(texture.y() * scale);
            }

            vertexList[i] = new Vertex(meshVertex, meshNormal, meshTextureCoord);

            x = vertex.x();
            y = vertex.y();
            z = vertex.z();
        }

        int faceCount = mesh.mNumFaces();
        AIFace.Buffer indices = mesh.mFaces();
        int[] indicesList = new int[faceCount * 3];

        for (int i = 0; i < faceCount; i++) {
            AIFace face = indices.get(i);
            indicesList[i * 3 + 0] = face.mIndices().get(0);
            indicesList[i * 3 + 1] = face.mIndices().get(1);
            indicesList[i * 3 + 2] = face.mIndices().get(2);
        }

        return new Mesh(vertexList, indicesList, new Material(texturePath));
    }

    public static float getLatestModelX() {
        return x;
    }

    public static float getLatestModelY() {
        return y;
    }

    public static float getZ() {
        return z;
    }

    private static ByteBuffer loadImage(String filePath, IntBuffer width, IntBuffer height) {
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Chargement de l'image avec STBImage
        ByteBuffer image = STBImage.stbi_load(filePath, width, height, channels, 0);

        if (image == null) {
            throw new RuntimeException("Failed to load texture: " + STBImage.stbi_failure_reason());
        }

        return image;
    }

    public static int loadTexture(String filePath) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);

        // Chargement de l'image avec STBImage
        ByteBuffer image = loadImage(filePath, width, height);

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Configuration du wrapping de texture
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // Configuration des filtres de texture
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Envoi de l'image à la texture OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        // Génération des mipmaps
        glGenerateMipmap(GL_TEXTURE_2D);

        // Libération de la mémoire de l'image
        STBImage.stbi_image_free(image);

        glBindTexture(GL_TEXTURE_2D, 0);

        return textureID;
    }

    private static ByteBuffer loadImage(String filePath) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ModelLoader.width = width;
        ModelLoader.height = height;

        // Chargement de l'image avec STBImage
        ByteBuffer image = STBImage.stbi_load(filePath, width, height, channels, 0);

        if (image == null) {
            throw new RuntimeException("Failed to load texture: " + STBImage.stbi_failure_reason());
        }



        return image;
    }
}