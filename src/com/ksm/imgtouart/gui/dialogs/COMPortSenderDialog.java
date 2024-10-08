package com.ksm.imgtouart.gui.dialogs;

import com.fazecast.jSerialComm.SerialPort;
import com.ksm.imgtouart.COMPortSender;
import com.ksm.imgtouart.Main;
import com.ksm.imgtouart.gui.other.IntegerDialogDocumentFilter;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;

import static com.ksm.imgtouart.CLIManager.*;

public class COMPortSenderDialog {
    public static class COMPortSenderDialogResult {
        public final DialogResult result;
        public final COMPortSender sender;

        public COMPortSenderDialogResult(DialogResult result, COMPortSender sender) {
            this.result = result;
            this.sender = sender;
        }
    }

    public static COMPortSenderDialogResult show(Component parentComponent, COMPortSender sender) {
        String portName = Main.manager.getPortName();
        int baudRate = Main.manager.getBaudRate();
        int dataBits = Main.manager.getDataBits();
        int stopBits = Main.manager.getStopBits();
        int parity = Main.manager.getParity();

        if (sender != null) {
            portName = sender.getPortName();
            baudRate = sender.getBaudRate();
            dataBits = sender.getDataBits();
            stopBits = sender.getStopBits();
            parity = sender.getParity();
        }

        JComboBox<String> portNameComboBox = new JComboBox<>();
        for (SerialPort port : SerialPort.getCommPorts()) {
            String curr = port.getSystemPortName();
            portNameComboBox.addItem(curr);

            if (curr.equals(portName)) {
                portNameComboBox.setSelectedItem(portName);
            }
        }

        JTextField baudRateField = new JTextField();
        //Filter to accept only Integer
        ((PlainDocument) baudRateField.getDocument()).setDocumentFilter(new IntegerDialogDocumentFilter());
        baudRateField.setText(String.valueOf(baudRate));

        JComboBox<String> dataBitsComboBox = new JComboBox<>(new String[] {"5", "6", "7", "8"});
        dataBitsComboBox.setSelectedItem(String.valueOf(dataBits));

        JComboBox<String> stopBitsComboBox = new JComboBox<>(new String[] {"1", "1.5", "2"});
        switch (stopBits) {
            case SerialPort.TWO_STOP_BITS -> stopBitsComboBox.setSelectedItem("2");
            case SerialPort.ONE_POINT_FIVE_STOP_BITS -> stopBitsComboBox.setSelectedItem("1.5");
            default -> stopBitsComboBox.setSelectedItem(DEFAULT_STOP_BITS);
        }

        JComboBox<String> parityComboBox = new JComboBox<>(new String[] {"None", "Even", "Odd"});
        switch (parity) {
            case SerialPort.ODD_PARITY -> parityComboBox.setSelectedItem("Odd");
            case SerialPort.EVEN_PARITY -> parityComboBox.setSelectedItem("Even");
            default -> parityComboBox.setSelectedItem(DEFAULT_PARITY);
        }

        //Компоненты внутри диалогового окна
        final JComponent[] inputs = new JComponent[] {
                new JLabel("Select COM Port:"),
                portNameComboBox,
                new JLabel("Enter Baud Rate:"),
                baudRateField,
                new JLabel("Select Data Bits:"),
                dataBitsComboBox,
                new JLabel("Select Stop Bits:"),
                stopBitsComboBox,
                new JLabel("Select Parity:"),
                parityComboBox
        };

        //Show dialog box
        int selectedOption = JOptionPane.showConfirmDialog(parentComponent, inputs, "COM Port Options", JOptionPane.OK_CANCEL_OPTION);

        //If you pressed OK
        if (selectedOption == JOptionPane.OK_OPTION) {
            //If at least one field is empty - return an error

            if (portNameComboBox.getSelectedIndex() == -1 || baudRateField.getText().isEmpty()) {
                return new COMPortSenderDialogResult(DialogResult.INCORRECT_INPUT, null);
            }

            portName = portNameComboBox.getItemAt(portNameComboBox.getSelectedIndex());
            baudRate = Integer.parseInt(baudRateField.getText());
            dataBits = Integer.parseInt(dataBitsComboBox.getItemAt(dataBitsComboBox.getSelectedIndex()));

            stopBits = switch (stopBitsComboBox.getItemAt(stopBitsComboBox.getSelectedIndex())) {
                case "1":  yield SerialPort.ONE_STOP_BIT;
                case "1.5": yield SerialPort.ONE_POINT_FIVE_STOP_BITS;
                case "2": yield SerialPort.TWO_STOP_BITS;
                default:
                    throw new IllegalStateException("Unexpected value: " + stopBitsComboBox.getItemAt(stopBitsComboBox.getSelectedIndex()));
            };

            parity = switch (parityComboBox.getItemAt(parityComboBox.getSelectedIndex())) {
                case "None": yield SerialPort.NO_PARITY;
                case "Even": yield SerialPort.EVEN_PARITY;
                case "Odd": yield SerialPort.ODD_PARITY;
                default:
                    throw new IllegalStateException("Unexpected value: " + parityComboBox.getItemAt(parityComboBox.getSelectedIndex()));
            };

            return new COMPortSenderDialogResult(DialogResult.OK, new COMPortSender(
                    portName, baudRate, dataBits, stopBits, parity
            ));
        }

        return new COMPortSenderDialogResult(DialogResult.CANCEL, null);
    }
}
