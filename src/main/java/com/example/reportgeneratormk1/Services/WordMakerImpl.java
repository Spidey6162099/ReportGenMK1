package com.example.reportgeneratormk1.Services;

import com.example.reportgeneratormk1.Interfaces.WordMaker;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;

@Service
public class WordMakerImpl implements WordMaker {
    @Override
    public ByteArrayInputStream saveFile(Map<String,Object> jsonObj) {
        XWPFDocument document = new XWPFDocument();

        //parse the object given and do the "needful"
        //start directly the introduction is separate in most reports and generic
        //create heading and center it
        List<Map<String,Object>> pages=(List<Map<String,Object>>)jsonObj.get("pages");
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
                subTitleRun.addBreak();
                subTitleRun.setUnderline(UnderlinePatterns.SINGLE);
                subTitleRun.setBold(true);
                //add the points
                List<String> points=(List<String>)subsection.get("points");
                for(var point:points){
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
                        pointRun.addBreak();
                    }


//                    XWPFRun pointRun=pointPara.createRun();
//                    pointRun.setText(point);
//                    pointRun.setFontSize(12);
//                    pointRun.setFontFamily("Times New Roman");
//                    pointRun.addBreak();
                }
            }
            heading.setPageBreak(true);
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
