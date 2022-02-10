package pe.edu.upc.climate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.edu.upc.climate.service.impl.ClimateServiceImpl;

@Service
public class ClimateService implements ClimateServiceImpl {

	
	@Override
	public double getClimateByCityName(String cityName) {
		
		double kelvinTemp=0;
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		RestTemplate restTemplate = new RestTemplate();
		
		String url = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=8b20203bd2a2ec227355fee0365debae";
		
		String result = restTemplate.getForObject(url, String.class);
		JsonNode jsonNode;
		
		try {
			jsonNode = objectMapper.readTree(result).get("main");
			kelvinTemp = Double.parseDouble(jsonNode.get("temp").asText());
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return  kelvinTemp;
	}
}
