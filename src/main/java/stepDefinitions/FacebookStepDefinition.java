package stepDefinitions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class FacebookStepDefinition {

	WebDriver driver;

	@FindBy(id = "email")
	WebElement emailTextBox;

	@FindBy(id = "pass")
	WebElement passwordTextBox;

	@FindBy(id = "loginbutton")
	WebElement loginbutton;

	@FindBy(xpath = "//span[contains(text(),\"Create post\")]")
	WebElement createStatus;

	@FindBy(css = "div[data-testid='post_message']")
	List<WebElement> posts;

	@FindBy(css = "[data-testid='status-attachment-mentions-input']")
	WebElement TextArea;

	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
		Map<String, Object> prefs = new HashMap<String, Object>();

		// Set the notification setting it will override the default setting
		prefs.put("profile.default_content_setting_values.notifications", 2);

		// Create object of ChromeOption class
		ChromeOptions options = new ChromeOptions();

		// Set the experimental option
		options.setExperimentalOption("prefs", prefs);

		driver = new ChromeDriver(options);

		PageFactory.initElements(driver, this);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
	}

	@Given("^user is already on Facebook Web Page$")
	public void user_already_on_amazon_web_page() {

		driver.get("https://www.facebook.com");
	}

	@When("^user logs in with \"([^\"]*)\" and \"([^\"]*)\"$")
	public void user_logs_in_with_and(String username, String password) {
		emailTextBox.sendKeys(username);
		passwordTextBox.sendKeys(password);
		loginbutton.click();

	}

	@And("^user post status \"([^\"]*)\"$")
	public void user_post_status(String message) {
		createStatus.click();
		
		try 
		{
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.elementToBeClickable(TextArea));

			TextArea.click();
			TextArea.sendKeys(message);

			WebElement PostBtn = driver.findElement(By.cssSelector("button[data-testid='react-composer-post-button']"));
			PostBtn.click();
		}
		catch(NoSuchElementException noSuchElmExp)
		{
			System.out.println("Exception occured: "+noSuchElmExp);
		}
		
	}

	@Then("^user sees the \"([^\\\"]*)\" on Facebook page$")
	public void user_sees_the_on_Facebook_page(String message) {
		String actualText = posts.get(0).getText();
		System.out.println(posts.get(0).getText());
		Assert.assertEquals(actualText, message);
	}

	@After
	public void cleanUp() {
		driver.quit();
	}
}
