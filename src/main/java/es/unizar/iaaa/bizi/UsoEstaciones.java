/**
 * Clase para la descarga de un unico fichero del tipo "Usos de las estaciones"
 * @author Daniel Cabrera
 * @version 1.0
 * @since 2017-09-19
 */
package es.unizar.iaaa.bizi;


import es.unizar.iaaa.bizi.Configuracion;
import static org.junit.Assert.fail;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

public class UsoEstaciones {

	private static String chromeDriverLocation;
	private static WebDriver driver;
	private static StringBuffer verificationErrors = new StringBuffer();
	private static Configuracion config;
	private static String downloadPath;
	
	// FINALMENTE ESTE MAIN SE CONVERTIRA EN UNA FUNCION LLAMADA DESDE UN MAIN EXTERNO
	public static void main(String[] args) throws Exception {

		config = new Configuracion();
		downloadPath = System.getProperty("user.dir") + config.getDownloadPath();
		chromeDriverLocation = config.getChromeDriverLocation();

		setUp();

		// Login
		ArrayList<String> credenciales = config.getCredenciales();
		String baseURL = config.getBaseURL();
		login(credenciales, baseURL);

		// Acceso hasta zona de descarga
		accesoA("Uso de las estaciones", "3.1-Usos de las estaciones");

		// Fecha del fichero que se quiere descargar(dd-MM-yyyy)
		String fecha = obtenerFecha();

		// Rellenar campos de busqueda (Esta hecho para el caso de uso de estaciones)
		rellenarCamposDeBusqueda(fecha);

		// Descarga de fichero xls
		descargarFichero();

		Thread.sleep(10000);

		// Desconexion
		tearDown();

		// Comprobar existencia del fichero y renombrar
		renameFile(downloadPath, "3.1-Usos de las estaciones.xls", fecha);
	}

	/**
	 * Configuracion inicial para ChromeDriver Se especifica la carpeta de descargas
	 * 
	 * @throws Exception
	 */
	private static void setUp() throws Exception {
		ChromeOptions options = new ChromeOptions();
		Map<String, Object> prefs = new HashMap<String, Object>();
		/*
		 * https://chromium.googlesource.com/chromium/src/+/master/chrome/common/
		 * pref_names.cc
		 */

		// Boolean which specifies whether we should ask the user if
		// we should download a file (true) or just download it automatically.
		prefs.put("download.prompt_for_download", false);
		// Boolean that records if the download directory was changed by
		// an upgrade a unsafe location to a safe location.
		prefs.put("download.directory_upgrade", true);
		// Descarga el fichero en el directorio asignado.
		// en caso de no existir, crea la carpeta
		prefs.put("download.default_directory", downloadPath);
		options.setExperimentalOption("prefs", prefs);
		System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	/**
	 * Salir de la web legada y cerrar la conexion de ChromeDriver
	 * 
	 * @throws Exception
	 */
	private static void tearDown() throws Exception {
		driver.findElement(By.id("lbLogout")).click();
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	/**
	 * Realizar el login en la web legada
	 * 
	 * @param credenciales
	 *            [usuario,password] para el acceso al servicio
	 * @param baseURL
	 *            url principal de la web legada
	 */
	private static void login(ArrayList<String> credenciales, String baseURL) {
		driver.get(baseURL + "/login.aspx");
		driver.findElement(By.id("txtLogin")).clear();
		driver.findElement(By.id("txtLogin")).sendKeys(credenciales.get(0));
		driver.findElement(By.id("txtPassword")).clear();
		driver.findElement(By.id("txtPassword")).sendKeys(credenciales.get(1));
		driver.findElement(By.id("btnSubmit")).click();
	}

	/**
	 * Acceder a la zona de descarga deseada, la categoria y subcategoria son
	 * coincidentes con los links de la web.
	 * 
	 * @param categoria
	 *            por ejemplo: "Uso de estaciones"
	 * @param subcategoria
	 *            por ejemplo "3.1-Usos de las estaciones"
	 */
	private static void accesoA(String categoria, String subcategoria) {
		driver.findElement(By.linkText(categoria)).click();
		driver.findElement(By.linkText(subcategoria)).click();
	}

	private static void rellenarCamposDeBusqueda(String fecha) {
		// driver.findElement(By.linkText("Uso de las estaciones")).click();
		// driver.findElement(By.linkText("3.1-Usos de las estaciones")).click();

		/*
		 * https://stackoverflow.com/questions/21422548/how-to-select-the-date-picker-in
		 * -selenium-webdriver
		 */
		// Modificar el parametro readonly del campo fecha
		((JavascriptExecutor) driver).executeScript(
				"document.getElementById('ucReportParameters1_StartDate').removeAttribute('readonly',0);");

		WebElement fromDateBox = driver.findElement(By.id("ucReportParameters1_StartDate"));
		fromDateBox.clear();
		fromDateBox.sendKeys(fecha); // Enter date in required format

		((JavascriptExecutor) driver)
				.executeScript("document.getElementById('ucReportParameters1_EndDate').removeAttribute('readonly',0);");

		WebElement toDateBox = driver.findElement(By.id("ucReportParameters1_EndDate"));
		toDateBox.clear();
		toDateBox.sendKeys(fecha); // Enter date in required format

		// Hacer click sobre la casilla de eleccion de dia
		driver.findElement(By.xpath("//*[@id=\"ucReportParameters1_ReportParametersColumn2\"]/div[2]/span/span/span"))
				.click();

		// Seleccionar dentro de Dia la opcion "select all"
		driver.findElement(By.id("ucReportParameters1_Dayofweek0")).click();

		// Necesario segundo click para cerrar el desplegable
		driver.findElement(By.xpath("//*[@id=\"ucReportParameters1_ReportParametersColumn2\"]/div[2]/span/span/span"))
				.click();

		// Hacer click sobre la casilla de eleccion de estacion
		driver.findElement(By.xpath("//*[@id=\"ucReportParameters1_ReportParametersColumn2\"]/div[4]/span/span/span"))
				.click();

		// Seleccionar dentro de Estacion la opcion "select all"
		driver.findElement(By.id("ucReportParameters1_Station0")).click();

		// Necesario segundo click para cerrar el desplegable
		driver.findElement(By.xpath("//*[@id=\"ucReportParameters1_ReportParametersColumn2\"]/div[4]/span/span/span"))
				.click();

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
		 * https://stackoverflow.com/questions/12940592/how-to-select-an-item-from-a-
		 * dropdown-list-using-selenium-webdriver-with-java
		 */
		// Seleccionar el formato a descargar
		new Select(driver.findElement(By.id("ReportViewer1_ctl01_ctl05_ctl00"))).selectByVisibleText("Excel");

		// Descargar fichero
		driver.findElement(By.id("ReportViewer1_ctl01_ctl05_ctl01")).click();

		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

		// Devolver foco a origen
		driver.switchTo().defaultContent();
	}

	/**
	 * Obtener fecha del dia anterior (según la fecha del sistema) Hace de manera
	 * correcta la resta, incluso en años bisiesto.
	 * 
	 * @return fecha en formato "dd/MM/yyyy"
	 */
	private static String obtenerFecha() {
		String fecha = null;
		// Obtener fecha del dia anterior
		LocalDate date = LocalDate.now().plusDays(-1);
		// Dar formato de salida
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		fecha = date.format(formatter);
		return fecha;
	}

	/**
	 * Renombrar el fichero anadiendole la fecha de la información que contiene
	 * 
	 * @param downloadPath
	 *            donde se encuentra el fichero a renombrar
	 * @param nombreFichero
	 *            nombre que tiene el fichero a renombrar
	 * @param fecha
	 *            fecha que se quiere anadir al nombre (formato "dd/MM/yyyy")
	 */
	private static void renameFile(String downloadPath, String nombreFichero, String fecha) {
		// Obtener path completo del fichero
		String path = downloadPath + System.getProperty("file.separator") + nombreFichero;
		File fichero = new File(path);

		// Comprobar si existe el fichero
		if (fichero.exists()) {
			// Obtener nombre sin extension
			String nombreSinExt = nombreFichero.substring(0, nombreFichero.lastIndexOf("."));
			// Anadir al nombre la fecha y de nuevo la extension
			String nuevoNombre = nombreSinExt + fecha.replaceAll("/", "") + ".xls";
			System.out.println(nuevoNombre);
			// Renombrar
			File dest = new File(downloadPath + System.getProperty("file.separator") + nuevoNombre);
			fichero.renameTo(dest);
			// TODO: GENERAR ENTRADA EN FICHERO DE LOGS PARA REALIZAR CONVERSION A CSV
			System.out.println("existe");
		} else {
			// TODO: GENERAR ENTRADA EN FICHERO DE LOGS DE ERRORES
			System.out.println("no existe");
		}
	}

}
