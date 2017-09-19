/**
 * Clase para la descarga de un unico fichero del tipo "Usos de las estaciones"
 * @author Daniel Cabrera
 * @version 1.0
 * @since 2017-09-19
 */
package es.unizar.iaaa.bizi;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import es.unizar.iaaa.bizi.Configuracion;


public class UsoEstaciones {

	private static String chromeDriverLocation;
	private static WebDriver driver;
	private static StringBuffer verificationErrors = new StringBuffer();
	
public static void main(String[] args) throws Exception {
		
		Configuracion config = new Configuracion();
		chromeDriverLocation = config.getChromeDriverLocation();
		
		setUp();
		
		// Login
		ArrayList<String> credenciales = config.getCredenciales();
		String baseURL = config.getBaseURL();
		login(credenciales, baseURL);
		
		// Acceso hasta zona de descarga
		accesoA("Uso de las estaciones", "3.1-Usos de las estaciones");
		
		// Rellenar campos de busqueda
		rellenarCamposDeBusqueda();
		
		// Descarga de fichero xls
		descargarFichero();
		
		Thread.sleep(10000);
		
		// Desconexion
		tearDown();
		
		
	}
	
	private static void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	private static void tearDown() throws Exception {
		driver.findElement(By.id("lbLogout")).click();
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
	private static void login(ArrayList<String> credenciales, String baseURL) {
		driver.get(baseURL+"/login.aspx");
		driver.findElement(By.id("txtLogin")).clear();
		driver.findElement(By.id("txtLogin")).sendKeys(credenciales.get(0));
		driver.findElement(By.id("txtPassword")).clear();	
		driver.findElement(By.id("txtPassword")).sendKeys(credenciales.get(1));
		driver.findElement(By.id("btnSubmit")).click();
	}
	
	private static void accesoA(String categoria, String subcategoria) {
		driver.findElement(By.linkText(categoria)).click();
		driver.findElement(By.linkText(subcategoria)).click();
	}
	
	private static void rellenarCamposDeBusqueda(){
//		driver.findElement(By.linkText("Uso de las estaciones")).click();
//		driver.findElement(By.linkText("3.1-Usos de las estaciones")).click();
		
		/*
		 * https://stackoverflow.com/questions/21422548/how-to-select-the-date-picker-in-selenium-webdriver
		 */
		// Modificar el parametro readonly del campo fecha
		((JavascriptExecutor)driver).executeScript ("document.getElementById('ucReportParameters1_StartDate').removeAttribute('readonly',0);"); // Enables the from date box

		WebElement fromDateBox= driver.findElement(By.id("ucReportParameters1_StartDate"));
		fromDateBox.clear();
		//TODO:PONER LA FECHA DESEADA PARA LA DESCARGA
		fromDateBox.sendKeys("25/08/2017"); //Enter date in required format
		
		((JavascriptExecutor)driver).executeScript ("document.getElementById('ucReportParameters1_EndDate').removeAttribute('readonly',0);"); // Enables the from date box

		WebElement toDateBox= driver.findElement(By.id("ucReportParameters1_EndDate"));
		toDateBox.clear();
		toDateBox.sendKeys("25/08/2017"); //Enter date in required format
		
		// Hacer click sobre la casilla de eleccion de dia
		driver.findElement(By.xpath("//*[@id=\"ucReportParameters1_ReportParametersColumn2\"]/div[2]/span/span/span")).click();
		// Seleccionar dentro de Dia la opcion "select all"
		driver.findElement(By.id("ucReportParameters1_Dayofweek0")).click();
		// Necesario segundo click para cerrar el desplegable
		driver.findElement(By.xpath("//*[@id=\"ucReportParameters1_ReportParametersColumn2\"]/div[2]/span/span/span")).click();;
		
		
		// Hacer click sobre la casilla de eleccion de estacion 
		driver.findElement(By.xpath("//*[@id=\"ucReportParameters1_ReportParametersColumn2\"]/div[4]/span/span/span")).click();
		// Seleccionar dentro de Estacion la opcion "select all"
		driver.findElement(By.id("ucReportParameters1_Station0")).click();
		// Necesario segundo click para cerrar el desplegable
		driver.findElement(By.xpath("//*[@id=\"ucReportParameters1_ReportParametersColumn2\"]/div[4]/span/span/span")).click();

		// Hacer peticion
		driver.findElement(By.id("ucReportParameters1_btnRun")).click();
		
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
		
	}
	
	private static void descargarFichero() {
		/*
		 * https://stackoverflow.com/questions/9942928/how-to-handle-iframe-in-webdriver
		 */
		// Mover el foco al iframe que contiene la informacion
		driver.switchTo().defaultContent();
		driver.switchTo().frame("iframeReport");

		// Seleccionar dentro del iframe el formato del fichero a descargar
		driver.findElement(By.id("ReportViewer1_ctl01_ctl05_ctl00")).click();
		
		/*
		 * https://stackoverflow.com/questions/12940592/how-to-select-an-item-from-a-dropdown-list-using-selenium-webdriver-with-java
		 */
		// Seleccionar el formato a descargar
		new Select(driver.findElement(By.id("ReportViewer1_ctl01_ctl05_ctl00"))).selectByVisibleText("Excel");
		
		// Descargar fichero
		driver.findElement(By.id("ReportViewer1_ctl01_ctl05_ctl01")).click();
		
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
		
		//Devolver foco a origen
		driver.switchTo().defaultContent();
	}

}
