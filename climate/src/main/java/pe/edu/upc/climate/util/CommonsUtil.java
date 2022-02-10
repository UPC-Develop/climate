package pe.edu.upc.climate.util;

import java.text.DecimalFormat;

public class CommonsUtil {

	private static final DecimalFormat format = new DecimalFormat("0.00");
	
	public double convertKelvinToCentigrades(double value){
		  return  Double.parseDouble(format.format(value-273.5));
		}
}
