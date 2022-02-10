package pe.edu.upc.climate.queue.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageQueueEntity {
	
	public double temp;
	public String cityName;
	public String message;
}
