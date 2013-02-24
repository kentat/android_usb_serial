package com.example.sample02;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * �W�����o�͂� Log �� �e�L�X�g�r���[�ɏo�͂�����悤�ɂ���N���X.
 * �w�肳�ꂽ�e�L�X�g�r���[�́A�X�N���[���r���[�Ɉ͂܂�Ă���K�v������B
 * @author marunomaruno
 * @version 1.0, 2011-12-14
 * @since 1.0
 */
public class TextViewPrintStream extends PrintStream { // (1)

    private TextView view; // (2)
    private Runnable scrollDown = new ScrollDown();    // (3)
    
    public TextViewPrintStream(File file, String csn, TextView view)
            throws FileNotFoundException, UnsupportedEncodingException { // (4)
        super(file, csn);
        this.view = view;
        ((View) view.getParent()).post(scrollDown);    // (5)
    }

    public TextViewPrintStream(File file, TextView view)
            throws FileNotFoundException {    // (4)
        super(file);
        this.view = view;
        ((View) view.getParent()).post(scrollDown);    // (5)
    }

    public TextViewPrintStream(OutputStream out, boolean autoFlush, String enc,
            TextView view) throws UnsupportedEncodingException {    // (4)
        super(out, autoFlush, enc);
        this.view = view;
        ((View) view.getParent()).post(scrollDown);    // (5)
    }

    public TextViewPrintStream(OutputStream out, boolean autoFlush,
            TextView view) {    // (4)
        super(out, autoFlush);
        this.view = view;
        ((View) view.getParent()).post(scrollDown);    // (5)
    }

    public TextViewPrintStream(OutputStream out, TextView view) {    // (4)
        super(out);
        this.view = view;
        ((View) view.getParent()).post(scrollDown);    // (5)
    }

    public TextViewPrintStream(String fileName, String csn, TextView view)
            throws FileNotFoundException, UnsupportedEncodingException { // (4)
        super(fileName, csn);
        this.view = view;
        ((View) view.getParent()).post(scrollDown);    // (5)
    }

    public TextViewPrintStream(String fileName, TextView view)
            throws FileNotFoundException {    // (4)
        super(fileName);
        this.view = view;
        ((View) view.getParent()).post(scrollDown);    // (5)
    }

    @Override
    public synchronized void print(String str) { // (6)
        super.print(str);
        view.append(str); // (7)
    }

    /**
     * �X�N���[���_�E��������X���b�h�̃N���X
     * @author marunomaruno
     */
    private class ScrollDown implements Runnable { // (8)
        public void run() {
            ((ScrollView) view.getParent()).fullScroll(View.FOCUS_DOWN); // (9)
        }
    }
}
