package es.unizar.iaaa.bizi;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeSandbox {
	
	/*
	 * Test obtenido de https://sites.google.com/a/chromium.org/chromedriver/getting-started
	 */
	
	@Test
	public void testGoogleSearch() throws InterruptedException {
	  // Optional, if not specified, WebDriver will search your path for chromedriver.
		System.setProperty("webdriver.chrome.driver", "/home/dani/Escritorio/TFG/herramientas/chromedriver");
		
		WebDriver driver = new ChromeDriver();
		
		driver.get("http://apache.rediris.es/sqoop/1.4.6/");
		Thread.sleep(5000);  // Let the user actually see something!
		driver.findElement(By.linkText("sqoop-1.4.6.bin__hadoop-0.23.tar.gz")).click();
		Thread.sleep(50000);  // Let the user actually see something!
		driver.quit();
	}
	
	
//	private WebDriver driver;
//	private String baseUrl;
//	private StringBuffer verificationErrors = new StringBuffer();

//	@Before
//	public void setUp() throws Exception {
//		System.setProperty("webdriver.chrome.driver", "C:\\Users\\686013\\AppData\\Local\\Google\\Application\\chromedriver.exe");
//		driver = new ChromeDriver();
//		//baseUrl = "http://www.zaragoza.es/";
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//	}

//	@Test
//	public void testPrueba() throws Exception {
//		driver.get(baseUrl + "about:newtab");
//		driver.findElement(By.cssSelector("span.newtab-thumbnail.enhanced-content")).click();
//		driver.findElement(By.cssSelector("span.newtab-thumbnail.enhanced-content")).click();
//		driver.findElement(By.linkText("Conjunto de datos")).click();
//		driver.findElement(By.linkText("Conjunto de datos")).click();
//		driver.findElement(By.linkText("http://www.zaragoza.es/api/recurso/urbanismo-infraestructuras/solar")).click();
//		driver.findElement(By.linkText("http://www.zaragoza.es/api/recurso/urbanismo-infraestructuras/solar")).click();
//		driver.findElement(By.linkText("CSV")).click();
//		driver.findElement(By.linkText("CSV")).click();
//	}
	
//	//@Test
//	public void testPrueba2() throws Exception {
//		driver.get("http://www.zaragoza.es/api/recurso/urbanismo-infraestructuras/solar");
//		driver.findElement(By.linkText("CSV")).click();
//		//Necesario calcular un tiempo para que sea posible la descarga
//		Thread.sleep(5000);
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		driver.quit();
//		String verificationErrorString = verificationErrors.toString();
//		if (!"".equals(verificationErrorString)) {
//			fail(verificationErrorString);
//		}
//	}

}
