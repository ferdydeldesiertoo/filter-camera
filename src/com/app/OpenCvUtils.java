package com.app;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;

public class OpenCvUtils {
    private static final Mat SEPIA_KERNEL;
    static {
        SEPIA_KERNEL = new Mat(3, 3, CvType.CV_32F);
        SEPIA_KERNEL.put(0, 0,
                0.131, 0.534, 0.272, //blue
                0.168, 0.686, 0.349, //green
                0.189, 0.769, 0.393 //red
        );
    }

    private OpenCvUtils() {}

    public static Image matToImage(Mat mat) {
        if (mat == null || mat.empty()) {
            return null;
        }

        // Elige el tipo de BufferedImage correcto
        int type = mat.channels() == 1 ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR;

        // Si la imagen es B&N pero la queremos mostrar en el panel principal (que es a color),
        // podríamos necesitar convertirla a BGR para que el tipo de buffer coincida.
        // Pero probemos primero esta implementación directa.

        // Manejo de BGR (3 canales)
        if (mat.channels() == 3) {
            BufferedImage image = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_3BYTE_BGR);
            mat.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());
            return image;
        }
        // Manejo de Grayscale (1 canal)
        else if (mat.channels() == 1) {
            BufferedImage image = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_BYTE_GRAY);
            mat.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());
            return image;
        }

        // Si no es ninguno de esos, regresamos null
        return null;
    }

    public static void applySepiaFilter(Mat source, Mat destination) {
        Core.transform(source, destination, SEPIA_KERNEL);
    }

    public static Mat applyCustomFilter(Mat source, int brightness, double contrast, int blur, boolean invert) {
        Mat result = source.clone();

        if (blur > 1) {
            int kernelSize = blur;
            if (kernelSize % 2 == 0) {
                kernelSize++;
            }
            Imgproc.GaussianBlur(result, result, new Size(kernelSize, kernelSize), 0);
        }


        Mat temp = new Mat();
        result.convertTo(temp, -1, contrast, brightness);
        result = temp;

        if (invert) {
            Core.bitwise_not(result, result);
        }

        return result;
    }
}
