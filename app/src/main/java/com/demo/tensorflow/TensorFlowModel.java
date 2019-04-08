package com.demo.tensorflow;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.os.TraceCompat;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class TensorFlowModel {

    private static final String MODEL_FILE = "file:///android_asset/trained.pb";

    public static final int HEIGHT = 208;
    public static final int WIDTH = 208;
    public static final int CHANNEL = 3;
    public static final int CLASSES = 2;
    public static final String INPUT_NAME = "input";
    public static final String OUTPUT_NAME = "output";

    private float[] inputs = new float[HEIGHT * WIDTH * CHANNEL];
    private float[] outputs = new float[CLASSES];

    private TensorFlowInferenceInterface inferenceInterface;

    static {
        System.loadLibrary("tensorflow_inference");
    }

    public TensorFlowModel(AssetManager assetManager) {
        inferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
    }

    @NonNull
    public float[] classifyImage(@NonNull Bitmap bitmap) {
        if (bitmap.getWidth() != WIDTH || bitmap.getHeight() != HEIGHT) {
            throw new RuntimeException("Bitmap尺寸错误");
        }
        TraceCompat.beginSection("process");
        int[] ints = new int[WIDTH * HEIGHT];
        bitmap.getPixels(ints, 0, WIDTH, 0, 0, WIDTH, HEIGHT);
        for (int i = 0; i < ints.length; ++i) {
            final int val = ints[i];
            inputs[i * 3] = normalize(Color.red(val));
            inputs[i * 3 + 1] = normalize(Color.green(val));
            inputs[i * 3 + 2] = normalize(Color.blue(val));
        }
        TraceCompat.endSection();

        TraceCompat.beginSection("feed");
        inferenceInterface.feed(INPUT_NAME, inputs,1, WIDTH, HEIGHT, CHANNEL);
        TraceCompat.endSection();

        TraceCompat.beginSection("run");
        String[] outputNames = new String[]{OUTPUT_NAME};
        inferenceInterface.run(outputNames);
        TraceCompat.endSection();

        TraceCompat.beginSection("fetch");
        inferenceInterface.fetch(OUTPUT_NAME, outputs);
        TraceCompat.endSection();

        return outputs;
    }

    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;

    private static float normalize(int d) {
        return ((float) (d - IMAGE_MEAN)) / IMAGE_STD;
    }
}
