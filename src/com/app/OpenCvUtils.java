package com.app;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;

public class OpenCvUtils {
    public static Image matToImage(Mat mat) {
        if(mat == null || mat.empty()) {
            return null;
        }

        MatOfByte mem = new MatOfByte();
        Imgcodecs.imencode(".bmp", mat, mem);

        try {
            return ImageIO.read(new ByteArrayInputStream(mem.toArray()));

        } catch (Exception e) {
            System.out.println("Error al convertir Mat a Image: " + e.getMessage());
            return null;
        }
    }

    public static Mat applySepiaFilter(Mat source) {
        Mat kernel = new Mat(3, 3, CvType.CV_32F);
        kernel.put(0, 0,
                0.131, 0.534, 0.272, //blue
                0.168, 0.686, 0.349, //green
                0.189, 0.769, 0.393 //red
        );

        Mat destination = new Mat();
        Core.transform(source, destination, kernel);

        return destination;
    }
}
