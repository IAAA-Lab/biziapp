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

public class UsoEstaciones {
	private WebDriver driver;
	private String baseURL;
	private StringBuffer verificationErrors = new StringBuffer();
	
	/**
	 * Obtiene de un fichero el usuario y contrasena a introducir
	 * Formato fichero:
	 * usuario
	 * contraseña
	 * @return userPas[0]=usuario, userPas[1]=contraseña
	 */
	private String[] obtenerCredencial(){
		String[] userPas = new String[2];
		FileReader fichero;
		BufferedReader buffer;
		String conjunto="";
		try {
			fichero = new FileReader("C:\\Users\\Daniel\\git\\UNIZAR-TFG-BIZI\\login");
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
		//TODO
	}
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Daniel\\AppData\\Local\\Google\\Application\\chromedriver.exe");
		driver = new ChromeDriver();
		baseURL = "http://reportingportal-zar.clearchannel.com/";
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Test
	public void testUsoBicicletas() throws Exception {
		login();
		accesoUsoEstaciones();
		//Necesario calcular un tiempo para que sea posible la descarga
		Thread.sleep(5000);
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
