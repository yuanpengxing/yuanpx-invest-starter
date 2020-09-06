import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class InitChrome {

    public static ChromeDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", BaseConf.driverExecutor);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        options.addArguments("disable-extensions");
        options.addArguments("start-maximized");
        options.addArguments("no-sandbox");
        // 禁止默认浏览器检查
        options.addArguments("no-default-browser-check");
        // 显示历史记录
        options.addArguments("about:histograms");
        // 显示缓存页面
        options.addArguments("about:cache");
        // chrome正受到自动测试软件的控制
        options.addArguments("disable-infobars");

        Map<String, Object> prefs = new HashMap<String, Object>();
        // 配置禁止加载图片，options.addArguments("--disable-images")无效
        prefs.put("profile.managed_default_content_settings.images", 2);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        return new ChromeDriver(options);
    }
}
