package com.example.reportgeneratormk1;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DummyWordMaker {

    public static void main(String[] args){
        XWPFDocument document=new XWPFDocument();

        //create heading and center it
        XWPFParagraph heading=document.createParagraph();
        heading.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun headingRun=heading.createRun();
        headingRun.setText("woah mama");
        headingRun.setColor("009933");
        headingRun.setBold(true);
        headingRun.setFontFamily("Courier");
        headingRun.setFontSize(25);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./docFiles/hello.docx");
            System.out.println(fileOutputStream);
            document.write(fileOutputStream);
            System.out.println("written");
            fileOutputStream.close();
            document.close();
        }
        catch (FileNotFoundException e){
            System.out.println("file not found");
        }
        catch(IOException e){
            System.out.println("IO exception");
        }
    }
}
