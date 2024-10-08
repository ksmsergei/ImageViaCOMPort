# ImageViaCOMPort
This tool allows to convert most types of images (PNG, JPG, WEBP, etc.) of suitable size (128x64) into their byte representation for monochrome LCD screens with vertical addressing, with the possibility to transfer the image via COM port. There is both GUI and CLI.

You can use my STM32 Blue Pill microcontroller [program](https://github.com/ksmsergei/img_via_uart_receiver) for the ST7920 LCD screen to test this tool.

# Usage
## CLI
This is what calling a program from the CLI looks like:
```batch
ImageViaCOMPort
        -c, --console           Enable console version
        -i, --image <arg>       Path to image
        -t, --threshold <arg>   Pixel binarization threshold (0-255)
        -I, --inverse           Inverse image
        -p, --port <arg>        COM port name
        -b, --baudrate <arg>    Data transfer rate
        -d, --databits <arg>    Number of data bits (5, 6, 7, 8)
        -s, --stopbits <arg>    Number of stop bits (1, 1.5, 2)
        -P, --parity <arg>      Parity (N, E, O)
```

The mandatory arguments include only **-i** and **-p**.

**Example**: ```ImageViaCOMPort -c -i image.png -t 127 -I -p COM6 -b 9600 -d 8 -s 1 -P N``` 

**NOTE**: If you remove the **--console** (or **-c**) argument while specifying other arguments, the GUI will be invoked and the fields you specified will be filled in automatically.

**NOTE**: Instead of the path to the image, you can specify the **URL** address of the image.

## GUI
The GUI features are the same as the CLI features:
- From the ‘**File**’ menu item, you can download images either from the file system or via URL.
- In the ‘**COM**’ section, you can select the COM port settings and send the current image via the previously configured port.
- In the ‘**Tools**’ section you can generate C code for direct insertion of the image into the microcontrollers. It is possible to copy the array to the clipboard or save it to a file.
- In the **main part** there is a preview of the selected picture.
- In the **bottom menu** you can set the threshold of image binarisation, as well as enable or disable colour inversion.<br>

![1](https://github.com/user-attachments/assets/7edc09be-b0d1-45f2-aa5c-bc400a067a8c)<br>
![2](https://github.com/user-attachments/assets/fbaff230-4e18-4c2a-921b-ab4cb03e19f4)<br>
![3](https://github.com/user-attachments/assets/646f802f-3681-43d4-99c8-06e7aba980e4)
