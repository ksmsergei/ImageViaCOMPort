package com.ksm.imgtouart;

import com.ksm.imgtouart.exceptions.cli.IllegalArgumentValueException;
import com.ksm.imgtouart.exceptions.image.ImageException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import com.ksm.imgtouart.gui.MainFrame;
import org.apache.commons.cli.ParseException;

import javax.swing.*;

public class Main {
    public static CLIManager manager = null;

    public static void showError(Component comp, String msg) {
        JOptionPane.showMessageDialog(comp, msg, "ImageViaCOMPort", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) throws IOException, ImageException {
        try {
            manager = new CLIManager(args);
        } catch (IllegalArgumentValueException | ParseException e) {
            for (String str : args) {
                if (str.equals("-c") || str.equals("--console")) {
                    System.out.println(e.getMessage());
                    System.out.print(CLIManager.getHelp());
                    return;
                }
            }

            showError(null, e.getMessage() + "\n" + CLIManager.getHelp());
            return;
        }

        //Console version of program
        if (manager.isConsole()) {
            BufferedImage img = LCDImageConverter.loadImage(manager.getImagePath());
            img = LCDImageConverter.binarizeImage(img, manager.getThreshold(), manager.getInverse());
            byte[] bytes = LCDImageConverter.getByteArray(img);

            COMPortSender sender = new COMPortSender(
                    manager.getPortName(),
                    manager.getBaudRate(),
                    manager.getDataBits(),
                    manager.getStopBits(),
                    manager.getParity()
            );

            sender.sendArray(bytes);
        } else { //GUI version of program
            new MainFrame().setVisible(true);
        }
    }
}