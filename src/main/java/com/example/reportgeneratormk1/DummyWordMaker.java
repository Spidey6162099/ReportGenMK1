package com.example.reportgeneratormk1;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DummyWordMaker {

    public static void main(String[] args){
        XWPFDocument document=new XWPFDocument();
        XWPFTable table=document.createTable();
        table.setTableAlignment(TableRowAlign.CENTER);
        table.setCellMargins(0,1000,0,1000);


        XWPFTableRow row1=table.getRow(0);
        XWPFRun run1=row1.getCell(0).addParagraph().createRun();
        run1.setText("Topic");
        run1.setFontFamily("Times New Roman");
        run1.setFontSize(14);

        XWPFRun run2=row1.addNewTableCell().addParagraph().createRun();
        run2.setText("Pages");
        run2.setFontFamily("Times New Roman");
        run2.setFontSize(14);

        XWPFTableRow row2=table.createRow();
        XWPFRun run3=row2.getCell(0).addParagraph().createRun();
        run3.setText("Blah");
        run3.setFontFamily("Times New Roman");
        run3.setFontSize(12);
//        row2.getCell(0).setText("well hello");
        XWPFRun run4=row2.getCell(1).addParagraph().createRun();
        run4.setText("Bleh");
        run4.setFontFamily("Times New Roman");
        run4.setFontSize(12);



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
