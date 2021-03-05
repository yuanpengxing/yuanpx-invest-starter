package libs;

public class DriverMgr {

    public static void switchIframe(String xpath){
        DriverDataSource.getDriver().switchTo().frame(xpath);
    }
}
