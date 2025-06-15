import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class PlaceOrder extends BaseTest {
    @Test
    public static void testPlaceOrderFlow() {
        // Initialize Chrome WebDriver
        Actions actions = new Actions(driver);
        driver.manage().window().maximize();
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Navigate to the food delivery website
        driver.get("https://pwiddy.interview.tisostudio.com/");
        // Wait for the search input to be clickable
        explicitWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='text']")));

        // Search for the restaurant by name
        driver.findElement(By.xpath("//input[@type='text']")).sendKeys("West, Mayer and Wintheiser");
        actions.sendKeys(Keys.ENTER).perform();

        // Wait for the restaurant card to be clickable and click it
        explicitWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h2[@class='text-lg font-semibold' and contains(text(), 'West, Mayer and Wintheiser')]")));
        driver.findElement(
                        By.xpath("//h2[@class='text-lg font-semibold' and contains(text(), 'West, Mayer and Wintheiser')]"))
                .click();

        // Wait for the restaurant page to load and verify the title
        WebElement restaurantTitle = explicitWait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//h1[contains(text(),'West, Mayer and Wintheiser')]")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String restTitleText = (String) js.executeScript("return arguments[0].textContent;", restaurantTitle);
        Assert.assertEquals("West, Mayer and Wintheiser", restTitleText);

        // Prepare to add items to cart
        List<String> desiredItmNames = new ArrayList<>();
        List<WebElement> itemList = driver
                .findElements(By.xpath("//div[@class='border rounded-lg p-4 bg-white shadow-sm']//h3"));
        List<WebElement> itemListBtns = driver
                .findElements(By.xpath("//div[@class='border rounded-lg p-4 bg-white shadow-sm']//button"));
        int addedItemCount = 0;

        // Add every other item to the cart and store their names
        for (int i = 0; i < itemListBtns.size(); i++) {
            if (i % 2 == 0) {
                itemListBtns.get(i).click();
                desiredItmNames.add(itemList.get(i).getText());
                addedItemCount++;
            }
        }

        // Verify the cart badge count matches the number of items added
        int actItemAdded = Integer.parseInt(driver.findElement(By.xpath(
                        "//div[@class='absolute -top-2 -right-2 bg-white text-red-600 rounded-full w-6 h-6 flex items-center justify-center text-sm font-bold']"))
                .getText());
        Assert.assertEquals(addedItemCount, actItemAdded);

        // Open the cart
        driver.findElement(By.xpath("//div[@class='fixed bottom-8 right-8 z-50']")).click();

        // Verify the cart page title
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Your Cart')]")));
        String cartTitleText = driver.findElement(By.xpath("//h1[contains(text(), 'Your Cart')]")).getText();
        Assert.assertEquals("Your Cart", cartTitleText);

        // Collect the names of items in the cart and verify they match what was added
        List<String> addedItmNames = new ArrayList<>();
        List<WebElement> addedItms = driver.findElements(By.xpath("//h3[@class='text-lg font-medium text-gray-900']"));
        for (WebElement i : addedItms) {
            addedItmNames.add(i.getText());
        }
        Assert.assertEquals(desiredItmNames, addedItmNames);

        // Proceed to checkout
        driver.findElement(By.xpath("//button[contains(text(), 'Proceed to Checkout')]")).click();

        // Fill out checkout form & address
        explicitWait.until(ExpectedConditions.elementToBeClickable(By.id("cash")));
        driver.findElement(By.id("cash")).click();
        driver.findElement(By.id("address")).sendKeys("Park Street");
        driver.findElement(By.id("city")).sendKeys("Los Angels");
        driver.findElement(By.id("zipCode")).sendKeys("412345");
        driver.findElement(By.id("phone")).sendKeys("17418529634");
        driver.findElement(By.xpath("//button[contains(text(), 'Place Order')]")).click();


        // Wait for and verify the order confirmation message
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Thank you for your order!')]")));
        String ordConf = driver.findElement(By.xpath("//h1")).getText();
        System.out.println(ordConf);
        Assert.assertEquals("Thank you for your order!", ordConf);

    }
}