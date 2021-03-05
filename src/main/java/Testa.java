import libs.DriverDataSource;
import element.ElementManager;
import libs.WaitElement;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.io.*;

public class Testa {
    public static void main(String[] args) throws IOException {
        DriverDataSource.getDriver().get("http://quote.eastmoney.com/f1.html?code=601998&market=1");
        WaitElement.xpath(ElementManager.lastRecordTable1);
        String innerHTML = WaitElement.xpath(ElementManager.table1).getAttribute("innerHTML");
        System.out.println(innerHTML);
    }

    @Test
    public void generateStockCodeFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:\\StockData\\temp.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\StockData\\stocks.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            if (StringUtils.isNotEmpty(line)) {
                bw.write(line.substring(2, 8));
                bw.newLine();
            }
        }
        bw.close();
    }

    public void get() {
        DriverDataSource.getDriver().get("");
    }
}
