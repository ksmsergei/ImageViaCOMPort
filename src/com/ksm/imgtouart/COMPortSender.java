package com.ksm.imgtouart;

import com.fazecast.jSerialComm.SerialPort;
import com.ksm.imgtouart.exceptions.com.COMPortOpenException;

import java.io.IOException;

public class COMPortSender {
    SerialPort comPort;

    public COMPortSender(String portName, int baudRate, int dataBits, int stopBits, int parity) {
        comPort = SerialPort.getCommPort(portName);

        comPort.setComPortParameters(baudRate, dataBits, stopBits, parity);

        comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
    }

    public void sendArray(byte[] bytes) throws IOException {
        if (!comPort.openPort()) {
            throw new COMPortOpenException();
        }

        try {
            comPort.getOutputStream().write(bytes);
        } finally {
            comPort.closePort();
        }
    }

    public String getPortName() {
        return comPort.getSystemPortName();
    }

    public int getBaudRate() {
        return comPort.getBaudRate();
    }

    public int getDataBits() {
        return comPort.getNumDataBits();
    }

    public int getStopBits() {
        return comPort.getNumStopBits();
    }

    public int getParity() {
        return comPort.getParity();
    }
}
