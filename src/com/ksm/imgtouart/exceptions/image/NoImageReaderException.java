package com.ksm.imgtouart.exceptions.image;

public class NoImageReaderException extends ImageException {
  public NoImageReaderException() {
    super("No ImageReader registered for this filetype.");
  }
}
