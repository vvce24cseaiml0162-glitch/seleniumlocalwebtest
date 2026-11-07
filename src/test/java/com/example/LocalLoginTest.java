package com.example;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LocalLoginTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        // Check if headless mode is requested or in CI/Docker environment (default: headless for CI reliability)
        String headlessProperty = System.getProperty("headless", "true");
        boolean isCi = System.getenv("CI") != null || System.getenv("GITHUB_ACTIONS") != null;
        if ("true".equalsIgnoreCase(headlessProperty) || isCi) {
            options.addArguments("--headless=new");
        }

        // Essential flags for Linux / Docker / CI environments
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        // Custom Chrome binary path if specified (e.g. In Docker container)
        String chromeBin = System.getenv("CHROME_BIN");
        if (chromeBin != null && !chromeBin.trim().isEmpty()) {
            options.setBinary(chromeBin);
        }

        driver = new ChromeDriver(options);

        // Resolve local HTML file path reliably across all environments (classpath, relative, or root)
        String pageUrl = null;
        URL resource = getClass().getClassLoader().getResource("webpage/login.html");
        if (resource != null) {
            pageUrl = resource.toString();
        } else {
            File rootFile = new File("webpage/login.html");
            if (rootFile.exists()) {
                pageUrl = rootFile.getAbsoluteFile().toURI().toString();
            } else {
                File resFile = new File("src/main/resources/webpage/login.html");
                pageUrl = resFile.getAbsoluteFile().toURI().toString();
            }
        }

        System.out.println("Navigating to: " + pageUrl);
        driver.get(pageUrl);
    }

    @Test(priority = 1)
    public void validLoginTest() {
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("12345");
        driver.findElement(By.id("loginButton")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));

        String message = messageElement.getText();
        System.out.println("Message: " + message);
        Assert.assertEquals(message, "Login Successful");
    }

    @Test(priority = 2)
    public void invalidLoginTest() {
        driver.findElement(By.id("username")).sendKeys("wrong");
        driver.findElement(By.id("password")).sendKeys("wrong");
        driver.findElement(By.id("loginButton")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));

        String message = messageElement.getText();
        System.out.println("Message: " + message);
        Assert.assertEquals(message, "Invalid Username or Password");
    }

    @Test(priority = 3)
    public void checkBackgroundColor() {
        String bgColor = driver.findElement(By.id("loginBox")).getCssValue("background-color");
        System.out.println("Background Color: " + bgColor);
        Assert.assertEquals(bgColor, "rgba(173, 216, 230, 1)");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
