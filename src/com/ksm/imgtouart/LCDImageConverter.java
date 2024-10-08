package com.ksm.imgtouart;

import com.ksm.imgtouart.exceptions.image.ImageException;
import com.ksm.imgtouart.exceptions.image.NoImageReaderException;
import com.ksm.imgtouart.exceptions.image.WrongImageSizeException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

public class LCDImageConverter {

    public static final int LCD_WIDTH = 128;
    public static final int LCD_HEIGHT = 64;

    public static BufferedImage loadImage(String pathname) throws ImageException, IOException {
        BufferedImage img;

        if (pathname.startsWith("http://") || pathname.startsWith("https://")) {
            img = ImageIO.read(URI.create(pathname).toURL());
        } else {
            img = ImageIO.read(new File(pathname));
        }

        //Checking for file type support
        if (img == null) {
            throw new NoImageReaderException();
        }

        //The picture should be the size of the screen
        if (img.getWidth() != LCD_WIDTH || img.getHeight() != LCD_HEIGHT) {
            throw new WrongImageSizeException();
        }

        return img;
    }

    public static BufferedImage binarizeImage(BufferedImage img, int threshold, boolean inverse) {
        BufferedImage resultImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = new Color(img.getRGB(x, y));

                int grayscale = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                //Set black or white color depending on the threshold and inverse
                if ((grayscale < threshold && !inverse) || (grayscale > threshold && inverse)) {
                    resultImg.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    resultImg.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }

        return resultImg;
    }

    public static byte[] getByteArray(BufferedImage img) {
        byte[] byteArray = new byte[img.getWidth() * img.getHeight() / 8];

        for (int y = 0; y < img.getHeight() / 8; y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                byte curr = 0;
                for (int i = 7; i >= 0; i--) {
                    Color color = new Color(img.getRGB(x, y*8 + i));

                    if (color.equals(Color.BLACK)) {
                        curr |= 1;
                    }

                    if (i != 0) {
                        curr <<= 1;
                    }
                }

                byteArray[y * img.getWidth() + x] = curr;
            }
        }

        return byteArray;
    }
    
}
