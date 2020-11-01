import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        Map<String, String> cmdLineParamsMap = CommandLineUtils.parserCommandLine(args);
        if (!cmdLineParamsMap.containsKey("selfSelectStock") || !cmdLineParamsMap.containsKey("dataRootLoc")
                || !cmdLineParamsMap.containsKey("dataYear") || !cmdLineParamsMap.containsKey("dataMonth")
                || !cmdLineParamsMap.containsKey("dataDay")) {
            throw new RuntimeException("usage example: java -jar yuanpx-invest-starter.jar " +
                    "-s D:\\stockcode.txt -l D:\\StockData\\data\\ -y 2020 -m 10 -d 01");
        }
        String dataParent = cmdLineParamsMap.get("dataRootLoc") + "/" + cmdLineParamsMap.get("dataYear") + "/"
                + cmdLineParamsMap.get("dataMonth") + "/" + cmdLineParamsMap.get("dataDay");
        File dp = new File(dataParent);
        if (!dp.exists()) {
            dp.mkdirs();
        }

        for (int i = 0; i < 10; i++) {
            BufferedWriter logFile = new BufferedWriter(new FileWriter(dataParent + "/log.txt", true));
            BufferedReader br = new BufferedReader(new FileReader(cmdLineParamsMap.get("selfSelectStock")));
            String line;
            while ((line = br.readLine()) != null) {
                String stockCode = line.trim();
                boolean isStockExist = false;
                String[] extensions = {"txt"};
                for (File file : FileUtils.listFiles(new File(dataParent), extensions, false)) {
                    if (file.getName().equals(stockCode + ".txt")) {
                        isStockExist = true;
                    }
                }
                String stockWriteTo = dataParent + "/" + stockCode + ".txt";
                if (!isStockExist) {
                    logFile.write("Stock " + stockCode + " task start " + new Date());
                    Main.startCollectData(stockCode, stockWriteTo, logFile);
                    logFile.newLine();
                    logFile.flush();
                }
            }
            br.close();
            logFile.close();
        }
    }

    public static void startCollectData(String stockCode, String stockWriteTo, BufferedWriter logFile) throws IOException {
        BufferedWriter stockFile = new BufferedWriter(new FileWriter(stockWriteTo));
        try {
            if (stockCode.startsWith("6")) {
                DriverDataSource.getDriver().get("http://quote.eastmoney.com/f1.html?code=" + stockCode + "&market=1");
            } else {
                DriverDataSource.getDriver().get("http://quote.eastmoney.com/f1.html?code=" + stockCode + "&market=2");
            }
            String lastRecordTable1 = WaitElement.xpath(ElementManager.lastRecordTable1).getText().trim();
            stockFile.write(WaitElement.xpath(ElementManager.table1).getAttribute("innerHTML"));
            stockFile.write(WaitElement.xpath(ElementManager.table2).getAttribute("innerHTML"));
            stockFile.write(WaitElement.xpath(ElementManager.table3).getAttribute("innerHTML"));
            stockFile.write(WaitElement.xpath(ElementManager.table4).getAttribute("innerHTML"));

            String totalPages = WaitElement.xpath(ElementManager.totalPages).getText();
            logFile.write(", TotalPages: " + totalPages);
            List<WebElement> lastPageElementsList = new ArrayList<>();
            for (int i = 2; i < Integer.parseInt(totalPages) + 1; i++) {
                WaitElement.xpath(ElementManager.pageInput).sendKeys(i + "");
                WaitElement.xpath(ElementManager.goBtn).click();
                Thread.sleep(200);
                String newLastRecordTable1 = WaitElement.xpath(ElementManager.lastRecordTable1).getText().trim();
                if (lastRecordTable1.equals(newLastRecordTable1)) {
                    WaitElement.xpath(ElementManager.pageInput).sendKeys(i + "");
                    WaitElement.xpath(ElementManager.goBtn).click();
                    Thread.sleep(1000);
                    String nnewLastRecordTable1 = WaitElement.xpath(ElementManager.lastRecordTable1).getText().trim();
                    if (lastRecordTable1.equals(nnewLastRecordTable1)) {
                        break;
                    } else {
                        lastRecordTable1 = nnewLastRecordTable1;
                    }
                } else {
                    lastRecordTable1 = newLastRecordTable1;
                }
                if (i == Integer.parseInt(totalPages)) {
                    lastPageElementsList = WaitElement.getElements(ElementManager.everyDealRecord);
                    for (WebElement element : lastPageElementsList) {
                        stockFile.newLine();
                        stockFile.write("    <tr>");
                        stockFile.write(element.getAttribute("innerHTML"));
                        stockFile.write("</tr>");
                        stockFile.newLine();
                    }
                } else {
                    stockFile.write(WaitElement.xpath(ElementManager.table1).getAttribute("innerHTML"));
                    stockFile.write(WaitElement.xpath(ElementManager.table2).getAttribute("innerHTML"));
                    stockFile.write(WaitElement.xpath(ElementManager.table3).getAttribute("innerHTML"));
                    stockFile.write(WaitElement.xpath(ElementManager.table4).getAttribute("innerHTML"));
                }
            }
            stockFile.close();
            int totalDealRecords = (Integer.parseInt(totalPages) - 1) * 144 + lastPageElementsList.size();
            if ((Files.lines(Paths.get(stockWriteTo)).count() < totalDealRecords * 6) || totalDealRecords == 0) {
                logFile.write(", 下载数据不全,文件已删除？？？？");
                File f = new File(stockWriteTo);
                boolean result = f.delete();//判断是否删除完毕
                if (!result) {
                    System.gc();//系统进行资源强制回收
                    f.delete();
                }
            } else {
                logFile.write(", 下载完成 FileLines: " + Files.lines(Paths.get(stockWriteTo)).count() + ", totalDealRecords: " + totalDealRecords);
            }
        } catch (Exception e) {
            logFile.write(" Exception: 抛异常了，啥异常暂时不想弄，把文件删除再执行一遍吧");
            stockFile.close();
            File f = new File(stockWriteTo);
            boolean result = f.delete();//判断是否删除完毕
            if (!result) {
                System.gc();//系统进行资源强制回收
                f.delete();
            }
        }
    }

}
