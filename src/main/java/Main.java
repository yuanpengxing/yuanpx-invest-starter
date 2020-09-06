import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        String stockCodePath = "D:\\StockData\\SelfSelectStock.txt";
        String dataLoc = "D:\\StockData\\data\\";
        String yearOfData = "2020";
        String monthOfData = "09";
        String dayOfData = "05";
        String fullDataLoc = dataLoc + "/" + yearOfData + "/" + monthOfData + "/" + dayOfData;
        File dataParentFile = new File(fullDataLoc);
        if (!dataParentFile.exists()) {
            dataParentFile.mkdirs();
        }
        BufferedWriter logger = new BufferedWriter(new FileWriter(fullDataLoc + "/log.txt", true));
        BufferedReader br = new BufferedReader(new FileReader(stockCodePath));
        String line;
        while ((line = br.readLine()) != null) {
            String stockCode = line.trim();
            boolean isStockExist = false;
            String[] extensions = {"txt"};
            for (File file : FileUtils.listFiles(new File(fullDataLoc), extensions, false)) {
                if (file.getName().equals(stockCode + ".txt")) {
                    isStockExist = true;
                }
            }
            if (!isStockExist) {
                try {
                    logger.write("Stock " + stockCode + " task start " + new Date());
                    logger.newLine();
                    logger.flush();
                    Main.startCollectData(stockCode, fullDataLoc + "/" + stockCode + ".txt", logger);
                } catch (Exception e) {
                    logger.write("Exception: 无数据");
                    logger.newLine();
                    logger.flush();
                }
            }
            logger.newLine();
        }
        br.close();
        logger.close();

    }

    public static void startCollectData(String stockCode, String stockWriteTo, BufferedWriter logger) throws IOException, InterruptedException {
        if (stockCode.startsWith("6")) {
            DriverDataSource.getDriver().get("http://quote.eastmoney.com/f1.html?code=" + stockCode + "&market=1");
        } else {
            DriverDataSource.getDriver().get("http://quote.eastmoney.com/f1.html?code=" + stockCode + "&market=2");
        }

        String lastRecordTable1 = WaitElement.xpath(ElementManager.lastRecordTable1).getText().trim();
        BufferedWriter bw = new BufferedWriter(new FileWriter(stockWriteTo));
        bw.write(WaitElement.xpath(ElementManager.table1).getText());
        bw.newLine();
        bw.write(WaitElement.xpath(ElementManager.table2).getText());
        bw.newLine();
        bw.write(WaitElement.xpath(ElementManager.table3).getText());
        bw.newLine();
        bw.write(WaitElement.xpath(ElementManager.table4).getText());
        bw.newLine();

        String totalPages = WaitElement.xpath(ElementManager.totalPages).getText();
        logger.write("totalPages: " + totalPages);
        logger.flush();
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
                    bw.write(element.getText());
                    bw.newLine();
                }
            } else {
                bw.write(WaitElement.xpath(ElementManager.table1).getText());
                bw.newLine();
                bw.write(WaitElement.xpath(ElementManager.table2).getText());
                bw.newLine();
                bw.write(WaitElement.xpath(ElementManager.table3).getText());
                bw.newLine();
                bw.write(WaitElement.xpath(ElementManager.table4).getText());
                bw.newLine();
            }
        }
        bw.close();

        int totalDealRecords = (Integer.parseInt(totalPages) - 1) * 144 + lastPageElementsList.size();
        if ((Files.lines(Paths.get(stockWriteTo)).count() != totalDealRecords) || totalDealRecords == 0) {
            logger.write("下载数据不全,文件已删除？？？？");
            logger.newLine();
            logger.flush();
            File file = new File(stockWriteTo);
            if (file.exists()) {
                boolean isDelete = file.delete();
                if (isDelete) {
                    logger.write("下载数据不全,文件已删除haha");
                    logger.newLine();
                    logger.flush();
                } else {
                    boolean isisDelete = file.delete();
                    if (isisDelete) {
                        logger.write("下载数据不全,文件已删除haha");
                        logger.newLine();
                        logger.flush();
                    } else {
                        logger.write("文件删除失败，手动删除吧吧");
                        logger.newLine();
                        logger.flush();
                    }
                }
            }
        } else {
            logger.write("下载完成，FileLines: " + Files.lines(Paths.get(stockWriteTo)).count() + ", totalDealRecords: " + totalDealRecords);
            logger.newLine();
            logger.flush();
        }
    }

}
