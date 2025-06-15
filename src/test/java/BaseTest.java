import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class BaseTest {
    protected static WebDriver driver;

    @Parameters("browser")
    @BeforeMethod
    public void setup(String browser) {
        ChromeOptions options = new ChromeOptions();
        options.setCapability("autofill.profile_enabled", false);
        switch (browser.toLowerCase()) {
            case "chrome":
//                System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");
                driver = new ChromeDriver();
                break;
            case "firefox":
//                System.setProperty("webdriver.gecko.driver", "C:\\Drivers\\geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            case "edge":
//                System.setProperty("webdriver.edge.driver", "C:\\Drivers\\msedgedriver.exe");
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
