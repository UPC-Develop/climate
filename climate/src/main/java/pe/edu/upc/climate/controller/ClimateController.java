package pe.edu.upc.climate.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.climate.queue.ConfigQueue;
import pe.edu.upc.climate.queue.entity.MessageQueueEntity;
import pe.edu.upc.climate.response.ResponseHandler;
import pe.edu.upc.climate.service.ClimateService;
import pe.edu.upc.climate.util.CommonsUtil;
import pe.edu.upc.climate.util.NotifyUtil;

@RestController
@RequestMapping("/climate/v1")
public class ClimateController {
	
	@Autowired
	private RabbitTemplate template;
	
	@Autowired
	private ClimateService climateService;
	
	
	@GetMapping("/get-climate-by-cityname")
	public ResponseEntity<?> getClimateByCityName(@RequestParam String cityName){
		 
		double kelvinTemp=0;
		double celsiusTem=0;
		CommonsUtil commonsUtil = new CommonsUtil();
		MessageQueueEntity messageQueueEntity = new MessageQueueEntity();
		
		kelvinTemp = climateService.getClimateByCityName(cityName);
		
		celsiusTem = commonsUtil.convertKelvinToCentigrades(kelvinTemp);
		
		messageQueueEntity.cityName = cityName;
		messageQueueEntity.temp = celsiusTem;
				
		if(celsiusTem > 21) {
			
			messageQueueEntity.message = "El clima en " + messageQueueEntity.cityName + " es de " + messageQueueEntity.temp + " grados celsius";
			
			NotifyUtil notifyUtil = new NotifyUtil();
			notifyUtil.sendSMS(messageQueueEntity.message);
			
		}else {
			
			messageQueueEntity.cityName = cityName;
			messageQueueEntity.temp = celsiusTem;
			messageQueueEntity.message = "El clima esta en su punto. No hay alertas.";
			
			template.convertAndSend(ConfigQueue.EXCHANGE, ConfigQueue.ROUTING_KEY, messageQueueEntity);
		}

		return ResponseHandler.generateResponse("La solicitud ha sido procesada con éxito. " + messageQueueEntity.message, HttpStatus.ACCEPTED, messageQueueEntity);	
	}

}
