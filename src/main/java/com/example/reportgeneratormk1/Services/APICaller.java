package com.example.reportgeneratormk1.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class APICaller {
    private RestClient restClient=RestClient.create();
    private String query1="report on topic ";
    private String query2=" consider pagewise word document,in detail and do not start with created by etc.,return in an easily parseable json format,with pagewise distinction but detailed content,also break a page into subsections for better clarity, at highest level name of the project i.e title field etc. then a json array of pages , within each page should be a title field which should contain topic number not page number and a array of subsections each with a subtitle field with number like 1.1 etc. and array of points field,also ensure that most points start with surrounded like \"**cost**:\" to indicate bold text,do not _ between words use proper space";

    private String query3="also include pages/topics like: ";
    private String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key={API_KEY}";

    @Value("${API_KEY}")
    private String API_KEY;
    private String makeQuery(String topic,String requirements){
        //since requirements can be null
        return requirements==null?query1+topic+query2:query1+topic+query2+query3+requirements+" etc.";
    }


    public Map<String,Object> callGemini(String topic,String requirements){
        String query=makeQuery(topic,requirements);
        Map<String,Object> map1=Map.of("text",query);
        Map<String, Object> map2=Map.of("parts",List.of(map1));
        Map<String,Object> map3=Map.of("contents",List.of(map2));

        ObjectMapper objectMapper=new ObjectMapper();
//		System.out.println(API_KEY);
        URI uri= UriComponentsBuilder.fromUriString(url)
                .build("AIzaSyAn5YBARChTsoXkSZ1ay1xpdSEdm-JcB6Y");
        try {
            String body = objectMapper.writeValueAsString(map3);
//            System.out.println(uri);
            ResponseEntity<String> result=restClient.post()
                    .uri(uri)
                    .body(body)
                    .retrieve()
                    .toEntity(String.class);

            if(!result.getStatusCode().is2xxSuccessful()){
//                System.out.println("gemini call failed");
                throw new RuntimeException("gemini issues");

            }
            else {
                Map<String, Object> mapLvl1 = objectMapper.readValue(result.getBody(), HashMap.class);
                List<Map<String ,Object>> mapLvl2=(List<Map<String ,Object>>)mapLvl1.get("candidates");
                Map<String,Object> mapLvl3=(Map<String,Object>)mapLvl2.get(0).get("content");
                List<Map<String,Object>>mapLvl4=(List<Map<String,Object>>)mapLvl3.get("parts");
                String text=(String)mapLvl4.get(0).get("text");
                text=text.replace("json","");
                text=text.replace("`","");
				System.out.println(text);
                Map<String,Object> jsonObj=objectMapper.readValue(text,HashMap.class);
                System.out.println(jsonObj);
                System.out.println("api caller done!");
                return jsonObj;
            }

        }
        catch(JsonProcessingException e){
//            System.out.println("failed to convert query to json");
            throw new RuntimeException(e.getMessage());
        }
        catch(Exception e){
//            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
//        return null;

    }
}
