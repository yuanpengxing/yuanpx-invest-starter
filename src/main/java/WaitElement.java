import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class WaitElement {

    public static WebElement xpath(String xpath) {
        WebDriverWait wait = new WebDriverWait(DriverDataSource.getDriver(), 5);
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    public static WebElement id(String id) {
        WebDriverWait wait = new WebDriverWait(DriverDataSource.getDriver(), 5);
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    public static List<WebElement> getElements(String xpath) {
        WebDriverWait wait = new WebDriverWait(DriverDataSource.getDriver(), 5);
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath)));
    }
}
