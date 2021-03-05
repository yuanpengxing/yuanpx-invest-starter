package dateline;


import element.CandlestickChart;
import element.ElementManager;
import libs.DriverDataSource;
import libs.DriverMgr;
import libs.WaitElement;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Think\\Desktop\\StocksNumber.txt"));
        String stockCode;
//        while ((stockCode = br.readLine()) != null) {
//            if (stockCode.startsWith("6")) {
//                DriverDataSource.getDriver().get("http://quote.eastmoney.com/concept/sh" + stockCode + ".html#fschart-k");
//            } else {
//                DriverDataSource.getDriver().get("http://quote.eastmoney.com/concept/sz" + stockCode + ".html#fschart-k");
//            }
//        }

        DriverDataSource.getDriver().get("http://quote.eastmoney.com/concept/sz300885.html#fschart-k");
        Thread.sleep(4000);
        DriverMgr.switchIframe(CandlestickChart.iframe);
        WaitElement.xpath(CandlestickChart.dailyK).click();
        Thread.sleep(1000);
        WaitElement.xpath(CandlestickChart.monthK).click();
    }
}
