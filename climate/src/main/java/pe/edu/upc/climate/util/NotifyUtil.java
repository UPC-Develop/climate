package pe.edu.upc.climate.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class NotifyUtil {

	public String sendSMS(String message) {

		String result = "Mensaje enviado con exito";

		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(60000).build();

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setDefaultRequestConfig(config);
		CloseableHttpClient httpClient = builder.build();

		HttpPost post = new HttpPost("http://www.altiria.net/api/http");

		List<NameValuePair> parametersList = new ArrayList<NameValuePair>();

		parametersList.add(new BasicNameValuePair("cmd", "sendsms"));
		parametersList.add(new BasicNameValuePair("login", "jcajasba@gmail.com"));
		parametersList.add(new BasicNameValuePair("passwd", "symasqqy"));
		parametersList.add(new BasicNameValuePair("dest", "51995458116"));
		parametersList.add(new BasicNameValuePair("msg", message));

		try {

			post.setEntity(new UrlEncodedFormEntity(parametersList, "UTF-8"));
		} catch (UnsupportedEncodingException uex) {
			result = "ERROR: codificación de caracteres no soportada";
		}

		CloseableHttpResponse response = null;

		try {
			// System.out.println("Enviando petición");
			response = httpClient.execute(post);
			String resp = EntityUtils.toString(response.getEntity());

			if (response.getStatusLine().getStatusCode() != 200) {
				// System.out.println("ERROR: Código de error HTTP: " +
				// response.getStatusLine().getStatusCode());
				// System.out.println("Compruebe que ha configurado correctamente la
				// direccion/url ");
				// System.out.println("suministrada por Altiria");
				result = "ERROR: Código de error HTTP:  " + response.getStatusLine().getStatusCode();
			} else {
				if (resp.startsWith("ERROR")) {
					// System.out.println(resp);
					result = "ERROR: Código de error de Altiria. Compruebe las especificaciones";
				} else
					System.out.println(resp);
			}
		} catch (Exception e) {
			System.out.println("Excepción");
			result = "ERROR: Excepción no controlado";
			e.printStackTrace();
		} finally {
			// En cualquier caso se cierra la conexión
			post.releaseConnection();
			if (response != null) {
				try {
					response.close();
				} catch (IOException ioe) {
					result = "ERROR cerrando recursos";
				}
			}
		}

		return result;
	}
}
