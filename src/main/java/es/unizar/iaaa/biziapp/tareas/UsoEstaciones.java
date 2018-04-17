package es.unizar.iaaa.biziapp.tareas;

import static org.junit.Assert.fail;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Clase para la descarga de un unico fichero del tipo "Usos de las estaciones"
 * @author Daniel Cabrera
 * @version 1.0
 * @since 2017-09-19
 */
@Component
public class UsoEstaciones {

    @Autowired
    private Configuracion configuracion;

    private WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();

	/**
	 *
	 * @param fecha
	 *            fecha del fichero que se desea descargar. Formato 'dd/MM/yyyy'
     *
	 */
	public int descargar(String fecha) {

		try {
            setUp();
			// Login
			ArrayList<String> credenciales = configuracion.getCredenciales();
			String baseURL = configuracion.getBaseURL();
			login(credenciales, baseURL);

			// Acceso hasta zona de descarga
			accesoA("Uso de las estaciones", "3.1-Usos de las estaciones");

			// Rellenar campos de busqueda
			rellenarCamposDeBusqueda(fecha);

			// Descarga de fichero xls
			descargarFichero();

			// Desconexion
			tearDown();

			return 1;

		} catch (Exception e) {
            e.printStackTrace();
		    return -1;
		}
	}

	/**
	 * Configuracion inicial para ChromeDriver Se especifica la carpeta de descargas
	 *
	 * @throws Exception
	 */
	private void setUp() throws Exception {
		ChromeOptions options = new ChromeOptions();
		Map<String, Object> prefs = new HashMap<>();
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
		prefs.put("download.default_directory", configuracion.getDownloadPath());
		options.setExperimentalOption("prefs", prefs);

		//Si la variable Docker está activada y estamos en un entorno docker, lanzamos el driver sin interfaz
		if(configuracion.isDockerMode()) {
            options.addArguments("--headless","--no-sandbox");
        } else { //If running on docker, will be on PATH so this step is unnecesary
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }

		driver = new ChromeDriver(options);
		// Hacer que la pantalla se posicione en una zona no visible de la pantalla
		// Point punto = new Point(10000, 10000);
		// driver.manage().window().setPosition(punto);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	/**
	 * Salir de la web legada y cerrar la conexion de ChromeDriver
	 *
	 * @throws Exception
	 */
	private void tearDown() throws Exception {
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
	private  void login(ArrayList<String> credenciales, String baseURL) {
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
	private  void accesoA(String categoria, String subcategoria) {
		driver.findElement(By.linkText(categoria)).click();
		driver.findElement(By.linkText(subcategoria)).click();
	}

	private  void rellenarCamposDeBusqueda(String fecha) {
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

	private  void descargarFichero() {
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

		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

		// Devolver foco a origen
		driver.switchTo().defaultContent();
	}

}
