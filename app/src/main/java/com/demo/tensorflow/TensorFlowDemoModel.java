package com.demo.tensorflow;

import android.content.res.AssetManager;
import android.os.Trace;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

/**
 * 用于测试TensorFlow在Android设备是否正常
 * 参考 https://blog.csdn.net/cxq234843654/article/details/71171293
 */
public class TensorFlowDemoModel {

    private static final String MODEL_FILE = "file:///android_asset/cxq.pb"; //模型存放路径

    //数据的维度
    private static final int HEIGHT = 1;
    private static final int WIDTH = 2;

    //模型中输出变量的名称
    private static final String inputName = "input";
    //用于存储的模型输入数据
    private float[] inputs = new float[HEIGHT * WIDTH];

    //模型中输出变量的名称
    private static final String outputName = "output";
    //用于存储模型的输出数据
    private float[] outputs = new float[HEIGHT * WIDTH];

    private TensorFlowInferenceInterface inferenceInterface;

    static {
        //加载库文件
        System.loadLibrary("tensorflow_inference");
    }

    public TensorFlowDemoModel(AssetManager assetManager) {
        //接口定义
        inferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
    }

    public float[] getAddResult() {
        //为输入数据赋值
        inputs[0] = 1;
        inputs[1] = 3;

        //将数据feed给tensorflow
        Trace.beginSection("feed");
        inferenceInterface.feed(inputName, inputs, WIDTH, HEIGHT);
        Trace.endSection();

        //运行乘2的操作
        Trace.beginSection("run");
        String[] outputNames = new String[]{outputName};
        inferenceInterface.run(outputNames);
        Trace.endSection();

        //将输出存放到outputs中
        Trace.beginSection("fetch");
        inferenceInterface.fetch(outputName, outputs);
        Trace.endSection();

        return outputs;
    }
}
