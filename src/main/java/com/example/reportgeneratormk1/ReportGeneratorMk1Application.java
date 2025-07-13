package com.example.reportgeneratormk1;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
//import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootApplication
public class ReportGeneratorMk1Application {

	@Value("${API_KEY}")
	static String API_KEY;

	public static void main(String[] args) {
//		String query="make a report on topic healthcare solution app for rural areas,consider pagewise word document,in detail and do not start with created by etc.,return in an easily parseable json format,with pagewise distinction but detailed content,also break a page into subsections for better clarity, at highest level name of the project etc. then a json array of pages , within each page should be a title and a array of subsections each with a subtitle and points,clean up the beginning i.e ensure whole is a json object,sanitize the";
//		Map<String,Object> map1=Map.of("text",query);
//		Map<String, Object> map2=Map.of("parts", List.of(map1));
//		Map<String,Object> map3=Map.of("contents",List.of(map2));
//
//		ObjectMapper objectMapper=new ObjectMapper();
//		RestClient restClient=RestClient.create();
//		String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key={API_KEY}";
//
////		System.out.println(API_KEY);
//		URI uri= UriComponentsBuilder.fromUriString(url)
//				.build("AIzaSyAn5YBARChTsoXkSZ1ay1xpdSEdm-JcB6Y");
//		try {
//			String body = objectMapper.writeValueAsString(map3);
////			System.out.println(uri);
//			ResponseEntity<String> result=restClient.post()
//					.uri(uri)
//					.body(body)
//					.retrieve()
//					.toEntity(String.class);
//
////			System.out.println(result.getStatusCode());
//			if(!result.getStatusCode().is2xxSuccessful()){
//				System.out.println("gemini call failed");
//			}
//			else {
//				Map<String, Object> mapLvl1 = objectMapper.readValue(result.getBody(), HashMap.class);
//				List<Map<String ,Object>> mapLvl2=(List<Map<String ,Object>>)mapLvl1.get("candidates");
//				Map<String,Object> mapLvl3=(Map<String,Object>)mapLvl2.get(0).get("content");
//				List<Map<String,Object>>mapLvl4=(List<Map<String,Object>>)mapLvl3.get("parts");
//				String text=(String)mapLvl4.get(0).get("text");
//				text=text.replace("json","");
//				text=text.replace("`","");
////				System.out.println(text);
//				Map<String,Object> jsonObj=objectMapper.readValue(text,HashMap.class);
//				System.out.println(jsonObj);
//			}
////
////			System.out.println(result.getBody());
//		}
//		catch(JsonProcessingException e){
//			System.out.println("failed to convert query to json " +e.getMessage());
//		}
		SpringApplication.run(ReportGeneratorMk1Application.class, args);
	}

}
