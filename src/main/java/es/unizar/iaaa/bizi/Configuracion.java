package es.unizar.iaaa.bizi;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Configuracion {

	private JSONParser parser;
	private static Object ficheroJson;
	private static JSONObject jsonObject;
	
	public Configuracion() {
		parser = new JSONParser();
		try {
			ficheroJson = parser.parse(new FileReader(System.getProperty("user.dir")+"/config.json"));
			jsonObject = (JSONObject) ficheroJson;
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Obtiene parametros de login del fichero JSON
	 * @return ["user","password"]
	 */
	public ArrayList<String> getCredenciales() {
		ArrayList<String> result = new ArrayList<String>();
		JSONObject login = (JSONObject) jsonObject.get("login");
		String user = login.get("user").toString();
		String password = login.get("password").toString();
		result.add(user);
		result.add(password);
		return result;
	}
	
	public String getBaseURL() {
		JSONObject legacySystem = (JSONObject) jsonObject.get("legacySystem");
		String result = legacySystem.get("baseURL").toString();
		return result;
	}
	
	public String getChromeDriverLocation() {
		JSONObject fileLocation = (JSONObject) jsonObject.get("fileLocation");
		String result = fileLocation.get("chromeDriverLocation").toString();
		return result;
	}
	
	public String getDownloadPath() {
		JSONObject fileLocation = (JSONObject) jsonObject.get("fileLocation");
		String result = fileLocation.get("downloadLocation").toString();
		return result;
	}
	
}
