package com.ksm.imgtouart;

import com.fazecast.jSerialComm.SerialPort;
import com.ksm.imgtouart.exceptions.cli.IllegalArgumentValueException;
import org.apache.commons.cli.*;

import java.util.HashMap;

public class CLIManager {
    public static final String DEFAULT_THRESHOLD = "127";
    public static final String DEFAULT_BAUD_RATE = "9600";
    public static final String DEFAULT_DATA_BITS = "8";

    public static final String DEFAULT_STOP_BITS = "1";

    public static final String DEFAULT_PARITY = "None";

    public static int strStopBitsToInt(String stopBits) {
        return switch (stopBits) {
            case "1" -> SerialPort.ONE_STOP_BIT;
            case "1.5" -> SerialPort.ONE_POINT_FIVE_STOP_BITS;
            case "2" -> SerialPort.TWO_STOP_BITS;
            default -> -1;
        };
    }

    public static int strParityToInt(String parity) {
        return switch (parity) {
            case "None", "N" -> SerialPort.NO_PARITY;
            case "Odd", "O" -> SerialPort.ODD_PARITY;
            case "Even", "E" -> SerialPort.EVEN_PARITY;
            default -> -1;
        };
    }

    private static final String CMD_LINE_SYNTAX = "ImageViaCOMPort";

    private static final Options OPTIONS;

    private final boolean isConsole;

    private final String imagePath;
    private final int threshold;
    private final boolean inverse;

    private final String portName;
    private final int baudRate;
    private final int dataBits;
    private final int stopBits;
    private final int parity;

    static {

        OPTIONS = new Options();

        //Add options
        OPTIONS.addOption("c", "console", false, "Enable console version");

        OPTIONS.addOption("i", "image", true, "Path to image");
        OPTIONS.addOption("t", "threshold", true, "Pixel binarization threshold (0-255)");
        OPTIONS.addOption("I", "inverse", false, "Inverse image");

        OPTIONS.addOption("p", "port", true, "COM port name");
        OPTIONS.addOption("b", "baudrate", true, "Data transfer rate");
        OPTIONS.addOption("d", "databits", true, "Number of data bits (5, 6, 7, 8)");
        OPTIONS.addOption("s", "stopbits", true, "Number of stop bits (1, 1.5, 2)");
        OPTIONS.addOption("P", "parity", true, "Parity (N, E, O)");
    }

    public CLIManager(String[] args) throws ParseException {
        //Parser for processing arguments
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(OPTIONS, args);

        isConsole = cmd.hasOption("c");

        if (isConsole) {
            if (!cmd.hasOption("i")) {
                throw new MissingArgumentException(OPTIONS.getOption("image"));
            }

            if (!cmd.hasOption("port")) {
                throw new MissingArgumentException(OPTIONS.getOption("port"));
            }
        }

        imagePath = cmd.getOptionValue("image", (String) null);

        threshold = Integer.parseInt(cmd.getOptionValue("threshold", DEFAULT_THRESHOLD));
        if (threshold < 0 || threshold > 255) {
            throw new IllegalArgumentValueException(OPTIONS.getOption("threshold"));
        }

        inverse = cmd.hasOption("inverse");

        portName = cmd.getOptionValue("port", (String) null);
        baudRate = Integer.parseInt(cmd.getOptionValue("baudrate", DEFAULT_BAUD_RATE));

        dataBits = Integer.parseInt(cmd.getOptionValue("databits", DEFAULT_DATA_BITS));
        switch (dataBits) {
            case 5: case 6: case 7: case 8: break;

            default:
                throw new IllegalArgumentValueException(OPTIONS.getOption("databits"));
        }

        stopBits = strStopBitsToInt(cmd.getOptionValue("stopbits", DEFAULT_STOP_BITS));
        if (stopBits == -1) {
            throw new IllegalArgumentValueException(OPTIONS.getOption("stopbits"));
        }

        parity = strParityToInt(cmd.getOptionValue("parity", DEFAULT_PARITY));
        if (parity == -1) {
            throw new IllegalArgumentValueException(OPTIONS.getOption("parity"));
        }
    }

    public static String getHelp() {
        HashMap<Option, Integer> lengths = new HashMap<>();

        int maxLength = 0;
        for (Option currOption : OPTIONS.getOptions()) {
            int currLength = 0;

            if (currOption.hasLongOpt()) {
                currLength += ", --".length() + currOption.getLongOpt().length();
            }

            if (currOption.hasArg()) {
                currLength += "<arg>".length();
            }

            lengths.put(currOption, currLength);

            maxLength = Math.max(maxLength, currLength);
        }

        StringBuilder help = new StringBuilder("Usage: " + CMD_LINE_SYNTAX + "\n");

        for (Option o : OPTIONS.getOptions()) {
            int currLength = lengths.get(o);
            help.append(String.format("\t-%s, --%s %s%" + (maxLength - currLength + 3) + "s%s\n",
                    o.getOpt(),
                    o.getLongOpt(),
                    o.hasArg() ? "<arg>" : "",
                    "",
                    o.getDescription()
            ));
        }

        return help.toString();
    }

    public boolean isConsole() {
        return isConsole;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getThreshold() {
        return threshold;
    }

    public boolean getInverse() {
        return inverse;
    }

    public String getPortName() {
        return portName;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }
}
