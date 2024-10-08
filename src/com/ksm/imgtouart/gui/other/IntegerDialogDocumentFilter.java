package com.ksm.imgtouart.gui.other;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class IntegerDialogDocumentFilter extends DocumentFilter {

    protected boolean isOkay(String text) {
        //Can leave an empty field
        if (text.isEmpty())
            return true;


        // It is possible to put a sign at the beginning by default - we forbid it.
        if (text.contains("-") || text.contains("+"))
            return false;

        try {
            //Try to convert the string to Integer
            //If we catch an exception, an invalid number has been entered
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string,
                             AttributeSet attr) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (isOkay(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        } else {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (isOkay(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }

    }

    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (isOkay(sb.toString())) {
            super.remove(fb, offset, length);
        } else {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }

    }
}