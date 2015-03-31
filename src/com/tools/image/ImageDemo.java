package com.tools.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图片灰度化 二值化处理
 *
 * @author Administrator
 */
public class ImageDemo {

    /**
     * 图片二值化
     *
     * @throws IOException
     */
    public void binaryImage() throws IOException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\QQ截图20130606172421.jpg");
        BufferedImage image = ImageIO.read(file);

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                grayImage.setRGB(i, j, rgb);
            }
        }

        File newFile = new File("C:\\Users\\Administrator\\Desktop\\ccc.jpg");
        ImageIO.write(grayImage, "jpg", newFile);
    }

    /**
     * 图片灰度化
     *
     * @throws IOException
     */
    public void grayImage() throws IOException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\QQ截图20130310184340.png");
        BufferedImage image = ImageIO.read(file);

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                grayImage.setRGB(i, j, rgb);
            }
        }

        File newFile = new File("C:\\Users\\Administrator\\Desktop\\bbb.png");
        ImageIO.write(grayImage, "jpg", newFile);
    }

    public static void main(String[] args) throws IOException {
        ImageDemo demo = new ImageDemo();
        demo.binaryImage();
       // demo.grayImage();
    }
}
