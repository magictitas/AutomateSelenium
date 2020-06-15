package testbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.bcel.classfile.Method;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;



/**
 * Hello world!
 *
 */
public class testbase 
{
	WebDriver driver;
	public Properties property; 
	public FileInputStream inputstreem;
	public File file;
	
	
	public static ExtentReports extent;
	public static ExtentTest test;
	public static ITestResult result;
	
	static {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_mm_yyyy_hh_mm_ss");
		extent = new ExtentReports(System.getProperty("user.dir") + "\\src\\main\\java\\Report\\test"+formater.format(calendar.getTime())+".html",false);
		
	}
	
	public void getResult(ITestResult result) throws IOException
	{
		if(result.getStatus() == ITestResult.SUCCESS)
		{
			test.log(LogStatus.PASS, result.getName()+"test is passed");
		}
		else if (result.getStatus() == ITestResult.SKIP)
		{
			test.log(LogStatus.SKIP, result.getName()+"test is skipped and skipped reason is :"+result.getThrowable());
		}
		else if (result.getStatus() == ITestResult.FAILURE)
		{
			test.log(LogStatus.FAIL, result.getName()+"test is failed, reason is :"+result.getThrowable());
			String screen = getscreenshot("");
			test.log(LogStatus.FAIL, test.addScreenCapture(screen));
		}
	}
	
	@BeforeMethod
	public void beforemethod(Method result)
	{
		test = extent.startTest(result.getName());
		test.log(LogStatus.INFO, result.getName()+"test started");
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException
	{
		getResult(result);
	}
	@AfterClass(alwaysRun = true)
	public void afterclass()
	{
		driver.quit();
		extent.endTest(test);
		extent.flush();
		
	}
	
	
	//Open browser method
	public void getbrowser(String browser)
	{
		if(System.getProperty("os.name").contains("Window"))
		{
			if (browser.equalsIgnoreCase("firefox"))
			{
				System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir")+"\\Driver\\geckodriver.exe");
				driver =new FirefoxDriver();
			}
			else if (browser.equalsIgnoreCase("chrome"))
			{
				System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\Driver\\chromedriver.exe");
				driver = (WebDriver) new ChromeDriver();
			}
		}
		if(System.getProperty("os.name").contains("Mac"))
		{
			if (browser.equalsIgnoreCase("chrome"))
			{
				System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\Driver\\chromedriver");
				driver = (WebDriver) new ChromeDriver();
			}
		}
	}
	
	
	//Take Screenshot method
	public String getscreenshot(String imagename) throws IOException
	{
		if(imagename.equals(""))
		{
			imagename = "Blank";
		}
		
		File image =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String ImageLocation = System.getProperty("user.dir")+"/src/main/java/testbase/Screenshot/";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_mm_yyyy_hh_mm_ss");
		String ActualimageName = ImageLocation+imagename+"_"+formater.format(calendar.getTime())+".png";
		File destfile =new File(ActualimageName);
		
		FileUtils.copyFile(image, destfile);
		return ActualimageName;
	}
	
	
	
	//Wait for element method explicit
	public WebElement waitForElementWithPolling(WebDriver driver,long time,WebElement element)
	{
		WebDriverWait wait = new WebDriverWait(driver ,time);
		wait.pollingEvery(5, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	//Wait for element method implicit
	public void impliciwait(long time)
	{
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
	}
	
	//Load properties
	public void loadpropertiesfile() throws IOException
	{
		property = new Properties();
		file = new File(System.getProperty("user.dir")+"\\src\\main\\java\\Config\\config.properties");
        inputstreem = new FileInputStream(file);
        property.load(inputstreem);
	}
	
	
	
    public static void main( String[] args ) throws IOException
    {
        testbase test=new testbase();
        test.getbrowser("chrome");
       /* test.loadpropertiesfile();
        System.out.println(test.property.getProperty("url"));
        System.out.println(System.getProperty("os.name"));
        */
    }
}
