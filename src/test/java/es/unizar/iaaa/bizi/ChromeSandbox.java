package es.unizar.iaaa.bizi;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeSandbox {
	private WebDriver driver;
	//private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\686013\\AppData\\Local\\Google\\Application\\chromedriver.exe");
		driver = new ChromeDriver();
		//baseUrl = "http://www.zaragoza.es/";
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

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
	
	//@Test
	public void testPrueba2() throws Exception {
		driver.get("http://www.zaragoza.es/api/recurso/urbanismo-infraestructuras/solar");
		driver.findElement(By.linkText("CSV")).click();
		//Necesario calcular un tiempo para que sea posible la descarga
		Thread.sleep(5000);
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
