package es.unizar.iaaa.bizi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class UsoEstacionesTest {
	// Windows
//	private String loginLocation ="C:\\Users\\686013\\git\\UNIZAR-TFG-BIZI\\login";
//	private String chromeDriverLocation ="C:\\Users\\686013\\AppData\\Local\\Google\\Application\\chromedriver.exe";
	
	// Linux
	private String loginLocation ="/home/dani/git/UNIZAR-TFG-BIZI/login";
	private String chromeDriverLocation ="/home/dani/Escritorio/TFG/herramientas/chromedriver";
	
	private WebDriver driver;
	private String baseURL;
	private StringBuffer verificationErrors = new StringBuffer();
	
	
	/**
	 * Obtiene de un fichero el usuario y contrasena a introducir
	 * Formato fichero:
	 * usuario
	 * contrase�a
	 * @return userPas[0]=usuario, userPas[1]=contrase�a
	 */
	private String[] obtenerCredencial(){
		String[] userPas = new String[2];
		FileReader fichero;
		BufferedReader buffer;
		String conjunto="";
		try {
			fichero = new FileReader(loginLocation);
			buffer = new BufferedReader(fichero);
			int i=0;
			while((conjunto=buffer.readLine())!=null){
				userPas[i] = conjunto;
				i++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userPas;
	}

	/**
	 * Realiza el login en la web de bizi zaragoza
	 */
	private void login(){
		String[] credencial = obtenerCredencial();
		String user = credencial[0];
		String pass = credencial[1];
		driver.get(baseURL+"/login.aspx");
		driver.findElement(By.id("txtLogin")).clear();
		driver.findElement(By.id("txtLogin")).sendKeys(user);
		driver.findElement(By.id("txtPassword")).clear();	
		driver.findElement(By.id("txtPassword")).sendKeys(pass);
		driver.findElement(By.id("btnSubmit")).click();
	}
	
	public void accesoUsoEstaciones(){
		driver.findElement(By.linkText("Uso de las estaciones")).click();
		driver.findElement(By.linkText("3.1-Usos de las estaciones")).click();
		
		/*
		 * https://stackoverflow.com/questions/21422548/how-to-select-the-date-picker-in-selenium-webdriver
		 */
		// Modificar el parametro readonly del campo fecha
		((JavascriptExecutor)driver).executeScript ("document.getElementById('ucReportParameters1_StartDate').removeAttribute('readonly',0);"); // Enables the from date box

		WebElement fromDateBox= driver.findElement(By.id("ucReportParameters1_StartDate"));
		fromDateBox.clear();
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
		
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
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
		
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	}
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
		driver = new ChromeDriver();
		baseURL = "http://reportingportal-zar.clearchannel.com/";
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Test
	public void testUsoBicicletas() throws Exception {
		login();
		accesoUsoEstaciones();
		//Necesario calcular un tiempo para que sea posible la descarga
		Thread.sleep(50000);
		driver.findElement(By.id("lbLogout")).click();
	}

	@After
	public void tearDown() throws Exception {
		
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
}
