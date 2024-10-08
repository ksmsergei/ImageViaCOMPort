/*
 * Created by JFormDesigner on Wed Sep 25 16:24:17 YEKT 2024
 */

package com.ksm.imgtouart.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.event.*;

import com.ksm.imgtouart.COMPortSender;
import com.ksm.imgtouart.LCDImageConverter;
import com.ksm.imgtouart.Main;
import com.ksm.imgtouart.exceptions.image.ImageException;
import com.ksm.imgtouart.exceptions.image.NoImageReaderException;
import com.ksm.imgtouart.exceptions.image.WrongImageSizeException;
import com.ksm.imgtouart.gui.dialogs.COMPortSenderDialog;
import com.ksm.imgtouart.gui.dialogs.GenerateCodeDialog;
import com.ksm.imgtouart.gui.other.JImagePanel;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author ksm
 */
public class MainFrame extends JFrame {
    private final JFileChooser fileChooser = new JFileChooser();
    private GenerateCodeDialog generateCodeDialog;

    private BufferedImage image = null;
    private COMPortSender sender = null;

    private void updatePreview() {
        if (image == null) {
            return;
        }

        pnlImage.setImage(LCDImageConverter.binarizeImage(image, slrThreshold.getValue(), cbInverse.isSelected()));
    }

    private void onImageLoad() {
        slrThreshold.setEnabled(true);
        snrThreshold.setEnabled(true);
        cbInverse.setEnabled(true);
        miSend.setEnabled(true);
        miGenerateCode.setEnabled(true);
    }

    public MainFrame() {
        initComponents();

        slrThreshold.setValue(Main.manager.getThreshold());
        cbInverse.setSelected(Main.manager.getInverse());

        if (Main.manager.getImagePath() != null) {
            try {
                image = LCDImageConverter.loadImage(Main.manager.getImagePath());
                updatePreview();
                onImageLoad();
            } catch (NoImageReaderException ex) {
                Main.showError(this, "Unsupported file type.");
            } catch (WrongImageSizeException ex) {
                Main.showError(this, "Wrong image size. Must be: " + LCDImageConverter.LCD_WIDTH + "x" + LCDImageConverter.LCD_HEIGHT);
            } catch (IOException | ImageException ex) {
                Main.showError(this, ex.getMessage());
            }
        }

        if (Main.manager.getPortName() != null) {
            sender = new COMPortSender(
                    Main.manager.getPortName(),
                    Main.manager.getBaudRate(),
                    Main.manager.getDataBits(),
                    Main.manager.getStopBits(),
                    Main.manager.getParity()
            );
        }
    }

    private void miOpenFile(ActionEvent e) {
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            image = LCDImageConverter.loadImage(fileChooser.getSelectedFile().getAbsolutePath());
            updatePreview();
            onImageLoad();
        } catch (NoImageReaderException ex) {
            Main.showError(this, "Unsupported file type.");
        } catch (WrongImageSizeException ex) {
            Main.showError(this, "Wrong image size. Must be: " + LCDImageConverter.LCD_WIDTH + "x" + LCDImageConverter.LCD_HEIGHT);
        } catch (IOException | ImageException ex) {
            Main.showError(this, ex.getMessage());
        }
    }

    private void slrThresholdStateChanged(ChangeEvent e) {
        snrThreshold.setValue(slrThreshold.getValue());
        updatePreview();
    }

    private void snrThresholdStateChanged(ChangeEvent e) {
        slrThreshold.setValue((Integer) snrThreshold.getValue());
    }

    private void cbInverse(ActionEvent e) {
        updatePreview();
    }

    private void miSend(ActionEvent e) {
        if (sender == null) {
            miOptions(null);

            if (sender == null) {
                return;
            }
        }

        int threshold = slrThreshold.getValue();
        boolean inverse = cbInverse.isSelected();

        BufferedImage binarizedImg = LCDImageConverter.binarizeImage(image, threshold, inverse);

        try {
            sender.sendArray(LCDImageConverter.getByteArray(binarizedImg));
        } catch (IOException ex) {
            Main.showError(this, ex.getMessage());
        }
    }

    private void miOptions(ActionEvent e) {
        COMPortSenderDialog.COMPortSenderDialogResult dialogResult = COMPortSenderDialog.show(this, sender);

        switch (dialogResult.result) {
            case CANCEL:
                return;

            case INCORRECT_INPUT:
                Main.showError(this, "The COM port settings are incorrect");
                return;
        }

        sender = dialogResult.sender;
    }

    private void miLoadURL(ActionEvent e) {
        String url = JOptionPane.showInputDialog(this, "Enter Image URL:", "Load URL", JOptionPane.PLAIN_MESSAGE);

        if (url == null) {
            return;
        }

        try {
            image = LCDImageConverter.loadImage(url);
            updatePreview();
            onImageLoad();
        } catch (NoImageReaderException ex) {
            Main.showError(this, "Unsupported file type.");
        } catch (WrongImageSizeException ex) {
            Main.showError(this, "Wrong image size. Must be: " + LCDImageConverter.LCD_WIDTH + "x" + LCDImageConverter.LCD_HEIGHT);
        } catch (IOException | ImageException ex) {
            Main.showError(this, ex.getMessage());
        }
    }

    private void miGenerateCode(ActionEvent e) {
        if (generateCodeDialog == null) {
            generateCodeDialog = new GenerateCodeDialog(this);
        }

        generateCodeDialog.imageArray = LCDImageConverter.getByteArray(LCDImageConverter.binarizeImage(image, slrThreshold.getValue(), cbInverse.isSelected()));
        generateCodeDialog.generateCode();
        generateCodeDialog.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        menuBar1 = new JMenuBar();
        mFile = new JMenu();
        miOpenFile = new JMenuItem();
        miLoadURL = new JMenuItem();
        mCOM = new JMenu();
        miOptions = new JMenuItem();
        miSend = new JMenuItem();
        mTools = new JMenu();
        miGenerateCode = new JMenuItem();
        pnlImage = new JImagePanel();
        pnlOptions = new JPanel();
        slrThreshold = new JSlider();
        lblThreshold = new JLabel();
        snrThreshold = new JSpinner();
        cbInverse = new JCheckBox();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("ImageViaCOMPort");
        var contentPane = getContentPane();

        //======== menuBar1 ========
        {

            //======== mFile ========
            {
                mFile.setText("File");
                mFile.setMnemonic('F');

                //---- miOpenFile ----
                miOpenFile.setText("Open File");
                miOpenFile.setIcon(new ImageIcon(getClass().getResource("/res/folder.png")));
                miOpenFile.setMnemonic('F');
                miOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
                miOpenFile.addActionListener(e -> miOpenFile(e));
                mFile.add(miOpenFile);

                //---- miLoadURL ----
                miLoadURL.setText("Load URL");
                miLoadURL.setIcon(new ImageIcon(getClass().getResource("/res/earth.png")));
                miLoadURL.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
                miLoadURL.addActionListener(e -> miLoadURL(e));
                mFile.add(miLoadURL);
            }
            menuBar1.add(mFile);

            //======== mCOM ========
            {
                mCOM.setText("COM");
                mCOM.setMnemonic('C');

                //---- miOptions ----
                miOptions.setText("Options");
                miOptions.setIcon(new ImageIcon(getClass().getResource("/res/applications.png")));
                miOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.SHIFT_DOWN_MASK));
                miOptions.addActionListener(e -> miOptions(e));
                mCOM.add(miOptions);

                //---- miSend ----
                miSend.setText("Send Image");
                miSend.setIcon(new ImageIcon(getClass().getResource("/res/arrow.png")));
                miSend.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
                miSend.setEnabled(false);
                miSend.addActionListener(e -> miSend(e));
                mCOM.add(miSend);
            }
            menuBar1.add(mCOM);

            //======== mTools ========
            {
                mTools.setText("Tools");
                mTools.setMnemonic('T');

                //---- miGenerateCode ----
                miGenerateCode.setText("Generate C Code");
                miGenerateCode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK));
                miGenerateCode.setIcon(new ImageIcon(getClass().getResource("/res/modify.png")));
                miGenerateCode.setEnabled(false);
                miGenerateCode.addActionListener(e -> miGenerateCode(e));
                mTools.add(miGenerateCode);
            }
            menuBar1.add(mTools);
        }
        setJMenuBar(menuBar1);

        //======== pnlOptions ========
        {
            pnlOptions.setBorder(new MatteBorder(1, 0, 0, 0, Color.black));

            //---- slrThreshold ----
            slrThreshold.setMaximum(255);
            slrThreshold.setMajorTickSpacing(1);
            slrThreshold.setValue(127);
            slrThreshold.setEnabled(false);
            slrThreshold.addChangeListener(e -> slrThresholdStateChanged(e));

            //---- lblThreshold ----
            lblThreshold.setText("Binarization threshold:");

            //---- snrThreshold ----
            snrThreshold.setModel(new SpinnerNumberModel(127, 0, 255, 1));
            snrThreshold.setEnabled(false);
            snrThreshold.addChangeListener(e -> snrThresholdStateChanged(e));

            //---- cbInverse ----
            cbInverse.setText("Inversed");
            cbInverse.setEnabled(false);
            cbInverse.addActionListener(e -> cbInverse(e));

            GroupLayout pnlOptionsLayout = new GroupLayout(pnlOptions);
            pnlOptions.setLayout(pnlOptionsLayout);
            pnlOptionsLayout.setHorizontalGroup(
                pnlOptionsLayout.createParallelGroup()
                    .addGroup(pnlOptionsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblThreshold)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(slrThreshold, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(snrThreshold, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbInverse)
                        .addContainerGap(9, Short.MAX_VALUE))
            );
            pnlOptionsLayout.setVerticalGroup(
                pnlOptionsLayout.createParallelGroup()
                    .addGroup(GroupLayout.Alignment.TRAILING, pnlOptionsLayout.createSequentialGroup()
                        .addGap(0, 7, Short.MAX_VALUE)
                        .addGroup(pnlOptionsLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addGroup(pnlOptionsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(snrThreshold)
                                .addComponent(cbInverse))
                            .addComponent(slrThreshold, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblThreshold, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(8, 8, 8))
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(pnlOptions, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(pnlImage, GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(pnlImage, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(pnlOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JMenuBar menuBar1;
    private JMenu mFile;
    private JMenuItem miOpenFile;
    private JMenuItem miLoadURL;
    private JMenu mCOM;
    private JMenuItem miOptions;
    private JMenuItem miSend;
    private JMenu mTools;
    private JMenuItem miGenerateCode;
    private JImagePanel pnlImage;
    private JPanel pnlOptions;
    private JSlider slrThreshold;
    private JLabel lblThreshold;
    private JSpinner snrThreshold;
    private JCheckBox cbInverse;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
