package utilities;


import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class BrowserUtils {

    private static final Logger logger = LogManager.getLogger();

    public static void verifyEquals(String expectedResult, String actualResult) {
        if (expectedResult.equals(actualResult))
            System.out.println("PASSED");
        else
            System.out.println("FAILED");
        System.out.println("Expected Result: " + expectedResult);
        System.out.println("Actual Result " + actualResult);
    }

    public static void waitPlease(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
            logger.error(e);
            System.out.println(e.getMessage());
        }
    }

    public static void openPage(String page, WebDriver driver) {
        List<WebElement> listOfExamples = driver.findElements(By.tagName("a"));
        for (WebElement example : listOfExamples) {
            if (example.getText().contains(page))
                example.click();
            break;
        }
    }

    public static void verifyIsDisplayed(WebElement element) {
        if (element.isDisplayed()) {
            System.out.println("PASSED");
            System.out.println(element.getText() + ": is visible");
        } else {
            System.out.println("FAILED");
            System.out.println(element.getText() + ": is not visible");
        }
    }

    public static void clickWithWait(WebDriver driver, By by, int attempts) {
        int counter = 0;
        while (counter < attempts) {
            try {
                driver.findElement(by).click();
                break;
            } catch (WebDriverException e) {
                logger.error((e));
                logger.error("Attempt :: " + ++counter);
                waitPlease(1);
            }
        }

    }
    //Clicks on an element using JavaScript, @param element

    public static void clickWithJS(WebElement element) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();", element);
    }
    //Scrolls down to an element using JavaScript, @param element

    public static void scrollToElement(WebElement element) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void clickWithWait(By by, int attempts) {
        int counter = 0;
        while (counter < attempts) {
            try {
                clickWithJS(Driver.getDriver().findElement(by));
                break;
            } catch (WebDriverException e) {
                logger.error(e);
                logger.error("Attempt :: " + ++counter);
                waitPlease(1);
            }
        }
    }

    public static void switchToWindow(String targetTitle) {
        String origin = Driver.getDriver().getWindowHandle();
        for (String handle : Driver.getDriver().getWindowHandles()) {
            Driver.getDriver().switchTo().window(handle);
            if (Driver.getDriver().getTitle().equals(targetTitle)) {
                return;
            }
        }
        Driver.getDriver().switchTo().window(origin);

    }

    //Moves the mouse to given element
    //@param element on which to hover

    public static void hover(WebElement element) {
        Actions actions = new Actions(Driver.getDriver());
        actions.moveToElement(element).perform();
    }

    //return a list of string from a list of elements
    //text
    //@param list of webelements
    //@return
    public static List<String> getElementText(List<WebElement> list) {
        List<String> elementTexts = new ArrayList<>();
        for (WebElement el : list) {
            if (!el.getText().isEmpty()) {
                elementTexts.add(el.getText());
            }
        }
        return elementTexts;
    }

    //Extracts text from list of elements matching the provided locator into new List<String>
    //@param locator
    //@return list of strings

    public static List<String> getElementsText(By locator) {
        List<WebElement> elems = Driver.getDriver().findElements(locator);
        List<String> elementTexts = new ArrayList<>();
        for (WebElement el : elems) {
            if (!el.getText().isEmpty()) {
                elementTexts.add(el.getText());
            }
        }
        return elementTexts;
    }

    //Performs a pause, @param seconds

    public static void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Waits for the provided element to be visible on the page
    //@param element and timeToWaitInSec

    public static WebElement waitForVisibility(WebElement element, int timeToWaitInSec) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), timeToWaitInSec);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    //Waits for element matching the locator to be visible on the page
    //@param locator and timeout

    public static WebElement waitForVisibility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), timeout);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    //Waits for provided element to be clickable
    //@param element and timeout

    public static WebElement waitForClickability(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForClickability(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    //waits for backgrounds processes on the browser to complete
    //@param timeOutInSeconds

    public static void waitForPageToLoad(long timeOutInSeconds) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), timeOutInSeconds);
            wait.until(expectation);
        }catch (Throwable error){
            error.printStackTrace();
        }
    }
    //Verifies whether the element matching the provided locator is displayed on page, @param by
    //@throws AssertionError if the element matching the provided locator is not found or not displayed

    public static void verifyElementDisplayed(By by){
        try {
            assertTrue("Element not visible: " + by, Driver.getDriver().findElement(by).isDisplayed());
        }catch (java.util.NoSuchElementException e){
            logger.error(e);
            e.printStackTrace();
            Assert.fail("Element not found: " + by);
        }
    }
    //Verifies whether the element matching the provided locator is NOT displayed on page, @param by
    //@throws AssertionError the element matching the provided locator is displayed

    public static void verifyElementNotDisplayed(By by){
        try {
            Assert.assertFalse("Element should not be visible: " + by, Driver.getDriver().findElement(by).isDisplayed());
        }catch (java.util.NoSuchElementException e){
            logger.error(e);
            e.printStackTrace();
        }
    }
    //Verifies whether the element is displayed on page, @param element
    //@throws AssertionError if the element is not found or not displayed

    public static void verifyElementDisplayed(WebElement element){
        try {
            assertTrue("Element not visible: " + element, element.isDisplayed());
        }catch (NoSuchElementException e){
            e.printStackTrace();
            logger.error(":::Element not found:::");
            Assert.fail("Element not found: " + element);

        }
    }
    //Waits for element to be not stale, @param element

    public static void waitForStaleElement(WebElement element) {
        int y = 0;
        while (y <= 15) {
            try {
                element.isDisplayed();
                break;
            } catch (StaleElementReferenceException st) {
                y++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
    }
    //Performs double click action on an element, @param element

    public static void doubleClick(WebElement element){
        new Actions(Driver.getDriver()).doubleClick(element).build().perform();
    }


}