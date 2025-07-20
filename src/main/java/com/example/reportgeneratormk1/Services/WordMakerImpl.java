package com.example.reportgeneratormk1.Services;

import com.example.reportgeneratormk1.Interfaces.WordMaker;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;

@Service
public class WordMakerImpl implements WordMaker {
    @Override
    public ByteArrayInputStream saveFile(Map<String,Object> jsonObj) {
        XWPFDocument document = new XWPFDocument();

        XWPFHeader xwpfHeader=document.createHeader(HeaderFooterType.DEFAULT);

        var headerPara=xwpfHeader.createParagraph();
//        headerPara.getCTP().getPPr().addNewTabs()
//                .addNewTab()
//                .setVal(STTabJc.RIGHT);


        headerPara.setAlignment(ParagraphAlignment.BOTH);
        var headerRun1=headerPara.createRun();
        headerRun1.setFontSize(12);
        headerRun1.setFontFamily("Times New Roman");
        headerRun1.setText("Title");
        headerRun1.addTab();

        var headerRun2=headerPara.createRun();
        headerRun2.setFontSize(12);
        headerRun2.setFontFamily("Times New Roman");
        headerRun2.setText("Report Date");


        XWPFFooter xwpfFooter=document.createFooter(HeaderFooterType.DEFAULT);

        var footerPara=xwpfFooter.createParagraph();
        footerPara.setAlignment(ParagraphAlignment.RIGHT);
        footerPara.getCTP().addNewFldSimple().setInstr("PAGE");
        var footerRun=footerPara.createRun();
        footerRun.setFontFamily("Times New Roman");
//        footerRun.setText("Page Num");
        footerRun.setFontSize(12);


//        XWPFDocument document=new XWPFDocument();
        //create a table of content table

        XWPFParagraph index = document.createParagraph();
        index.setAlignment(ParagraphAlignment.CENTER);
        index.setSpacingBetween(1.5);

        XWPFRun indexRun = index.createRun();
        indexRun.setText("Table of Contents");
//            headingRun.setColor("009933");
        indexRun.setBold(true);
        indexRun.setFontFamily("Times New Roman");
        indexRun.setFontSize(16);
        indexRun.setUnderline(UnderlinePatterns.SINGLE);
        indexRun.addBreak();

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

        List<Map<String,Object>> pages=(List<Map<String,Object>>)jsonObj.get("pages");
        //parse the list of pages to create the index
        for(var page:pages){
            XWPFTableRow row2=table.createRow();
            XWPFRun run3=row2.getCell(0).addParagraph().createRun();
            run3.setText((String)page.get("title"));
            run3.setFontFamily("Times New Roman");
            run3.setFontSize(12);

            XWPFRun run4=row2.getCell(1).addParagraph().createRun();
            run4.setText("1-2");
            run4.setFontFamily("Times New Roman");
            run4.setFontSize(12);
        }

        index.setPageBreak(true);
        //parse the object given and do the "needful"
        //start directly the introduction is separate in most reports and generic
        //create heading and center it



        for(var page:pages){
            XWPFParagraph heading = document.createParagraph();
            heading.setAlignment(ParagraphAlignment.CENTER);
            heading.setSpacingBetween(1.5);

            XWPFRun headingRun = heading.createRun();
            headingRun.setText((String)page.get("title"));
//            headingRun.setColor("009933");
            headingRun.setBold(true);
            headingRun.setFontFamily("Times New Roman");
            headingRun.setFontSize(16);
            headingRun.setUnderline(UnderlinePatterns.SINGLE);
//            headingRun.addBreak();

            List<Map<String,Object>> subsections=(List<Map<String,Object>> )page.get("subsections");
            for(var subsection:subsections){
                XWPFParagraph subtitle=document.createParagraph();
                subtitle.setAlignment(ParagraphAlignment.LEFT);
                subtitle.setSpacingBetween(1.5);


                //make the subtitle
                XWPFRun subTitleRun=subtitle.createRun();
                subTitleRun.setText((String) subsection.get("subtitle"));
                subTitleRun.setFontSize(14);
                subTitleRun.setFontFamily("Times New Roman");
                subTitleRun.setUnderline(UnderlinePatterns.SINGLE);
                subTitleRun.setBold(true);

                //add the points
                List<String> points=(List<String>)subsection.get("points");
                int count=0;
                for(var point:points){
                    count++;
                    //extract the bold things from points if present
                    XWPFParagraph pointPara=document.createParagraph();
                    pointPara.setAlignment(ParagraphAlignment.LEFT);
                    pointPara.setSpacingBetween(1.5);

                    int iterator=0;
                    StringBuilder stringBuilder=new StringBuilder();
                    while(iterator<point.length()){
                        if(point.charAt(iterator)=='*'){
                            if(!stringBuilder.isEmpty()){
                                //first append to the existing i.e make a run
                                XWPFRun pointRun=pointPara.createRun();
                                pointRun.setText(stringBuilder.toString());
                                pointRun.setFontSize(12);
                                pointRun.setFontFamily("Times New Roman");
                                stringBuilder.setLength(0);
                            }
                            //extract the word and make a bold run
                            int actIdx=iterator+2;
                            while(actIdx<point.length()){
                                if(point.charAt(actIdx)=='*'){
                                    break;
                                }
                                stringBuilder.append(point.charAt(actIdx));
                                actIdx++;
                                //end found

                            }
                            //now make another run for bold text
                            XWPFRun pointRun=pointPara.createRun();
                            pointRun.setText(stringBuilder.toString());
                            pointRun.setFontSize(12);
                            pointRun.setFontFamily("Times New Roman");
                            pointRun.setBold(true);

                            stringBuilder.setLength(0);
                            iterator=actIdx+2;
                        }
                        else {
                            //normal case
                            stringBuilder.append(point.charAt(iterator));
                            iterator++;
                        }

                    }
                    if(!stringBuilder.isEmpty()){
                        XWPFRun pointRun=pointPara.createRun();
                        pointRun.setText(stringBuilder.toString());
                        pointRun.setFontSize(12);
                        pointRun.setFontFamily("Times New Roman");


                        stringBuilder.setLength(0);
//                        pointRun.addBreak();
                    }
                    if(count==points.size()){
                        XWPFRun pointRun=pointPara.createRun();
                        pointRun.addBreak();
                    }



//                    XWPFRun pointRun=pointPara.createRun();
//                    pointRun.setText(point);
//                    pointRun.setFontSize(12);
//                    pointRun.setFontFamily("Times New Roman");



                }
            }
            heading.setPageBreak(true);
            //add headers


        }


        try {
//            FileOutputStream fileOutputStream = new FileOutputStream("./docFiles/hello.docx");
//            byte[] data=new byte[1048576];
            //create an output buffer
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
//            System.out.println(fileOutputStream);
            //write into the buffer
            document.write(byteArrayOutputStream);
//            System.out.println("written into buffer "+byteArrayOutputStream);
            byteArrayOutputStream.close();
            document.close();

            //write into input stream for cloudinary
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            return byteArrayInputStream;

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            System.out.println("IO exception");
            throw new RuntimeException(e.getMessage());
        }
    }
}
