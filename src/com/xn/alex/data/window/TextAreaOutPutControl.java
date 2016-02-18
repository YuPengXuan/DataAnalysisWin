package com.xn.alex.data.window;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

public class TextAreaOutPutControl {
	
	private static JTextArea mtextArea = null;
	
	private static TextAreaOutPutControl textOutController = null;
	
	private TextAreaOutPutControl(){
		
	}
	
	public static TextAreaOutPutControl Instance(){
		if(null == textOutController){
			textOutController = new TextAreaOutPutControl();
		}
		return textOutController;
	}
	
	public void setTextArea(JTextArea textArea){
		mtextArea = textArea;
	}
	
	public void Initialize(){
        OutputStream textAreaStream = new OutputStream() {
            public void write(int b) throws IOException {
            	mtextArea.append(String.valueOf((char)b));
            }
             
            public void write(byte b[]) throws IOException {
            	mtextArea.append(new String(b));
            }
             
            public void write(byte b[], int off, int len) throws IOException {
            	mtextArea.append(new String(b, off, len));
            }
    };  
    PrintStream myOut = new PrintStream(textAreaStream);
    System.setOut(myOut);
    System.setErr(myOut);
     
}
	
	
public void refresh(String message){
		
		System.out.println(message);
		
		mtextArea.paintImmediately(mtextArea.getBounds());
}

}
