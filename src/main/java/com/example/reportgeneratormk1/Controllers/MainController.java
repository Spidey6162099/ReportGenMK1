package com.example.reportgeneratormk1.Controllers;

import com.example.reportgeneratormk1.Services.APICaller;
import com.example.reportgeneratormk1.Services.CloudinaryService;
import com.example.reportgeneratormk1.Services.WordMakerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private APICaller apiCaller;

    @Autowired
    private WordMakerImpl wordMaker;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/v1/makePPT")
    public ResponseEntity<?> makePpt(@RequestParam String topic, @RequestParam String requirements){
        //this would call to the api caller and file maker in turn
        try {
            Map<String, Object> content = apiCaller.callGemini(topic, requirements);
            System.out.println(content);
            ByteArrayInputStream byteArrayInputStream=wordMaker.saveFile(content);
            String url=cloudinaryService.upload(byteArrayInputStream);
            return ResponseEntity.ok().body(url);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        //feed the jsonObj for processing

    }
}
