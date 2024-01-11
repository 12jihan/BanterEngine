package utils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class Utils {
    public static float[] listFloatToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    public static int[] listIntToArray(List<Integer> list) {
        return list.stream().mapToInt((Integer v) -> v).toArray();
    }

    public static String readFile(String filePath) {
        String str;
        try {
            str = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException excp) {
            throw new RuntimeException("Error reading file [\n\t- " + filePath + "\n]", excp);
        }
        return str;
    }

    public static FloatBuffer storeDataInBuffer(float[] data) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer dataBuffer = stack.callocFloat(data.length);
            // puts the dat into the beginning of the float buffer:
            dataBuffer.put(0, data);
            // Flip buffer to prepare it to be read from (common practice with buffers):
            dataBuffer.flip();

            // return the buffer for use:
            return dataBuffer;
        }
    }
}
