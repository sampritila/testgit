import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VerifyCarDetails {
	WebDriver driver ;
	
	@BeforeTest
    public void setBrowser() {
    	WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void verifyCarDetail() throws Throwable{

        List<String> actualOutput = new ArrayList<String>();
        List<String> expectedOutput = new ArrayList<String>();

       

        Scanner scanner = new Scanner(new File(System.getProperty("user.dir")+"/CarDetails/car_input.txt"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] inputNumber = line.split(" ");
            for (String inputCarNumber : inputNumber) {
                boolean number = inputCarNumber.matches("^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]+$");
                if (number) {
                    driver.get("https://cartaxcheck.co.uk/");
                    driver.manage().window().maximize();
                    WebDriverWait wait = new WebDriverWait(driver, 10);
                    wait.until(ExpectedConditions.elementToBeClickable(By.id("vrm-input"))).sendKeys(inputCarNumber);
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Free Car Check']"))).click();
                    actualOutput.add(wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//dt[text()='Registration']/following-sibling::dd"))).getText());
                    actualOutput.add(wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//dt[text()='Make']/following-sibling::dd"))).getText());
                    actualOutput.add(wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//dt[text()='Model']/following-sibling::dd"))).getText());
                    actualOutput.add(wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//dt[text()='Colour']/following-sibling::dd"))).getText());
                    actualOutput.add(wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//dt[text()='Year']/following-sibling::dd"))).getText());

                    Scanner outputScanner = new Scanner(new File(System.getProperty("user.dir")+"/CarDetails/car_output.txt"));
                    while (outputScanner.hasNextLine()) {
                        String line1 = outputScanner.nextLine();
                        //Here you can manipulate the string the way you want
                        System.out.println(line1);
                        boolean vaild = line1.contains(inputCarNumber);
                        if (vaild) {
                            String[] output = line1.split(",");
                            for (String expDetails : output) {
                                expectedOutput.add(expDetails);
                            }
                        }
                    }
                }

                for(int i=0; i<=actualOutput.size()-1; i++){
                	Assert.assertEquals(actualOutput.get(i),expectedOutput.get(i), "Actual is "+actualOutput.get(i)+" but expected is "+expectedOutput.get(i));
                }

                actualOutput.clear();
                expectedOutput.clear();

            }

        }
               
    }
    
    @AfterTest
    public void quitBrowser() {
    	driver.quit();
    }
}
