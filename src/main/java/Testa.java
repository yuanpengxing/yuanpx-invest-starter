import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.io.*;

public class Testa {
    public static void main(String[] args) throws IOException {

    }

    @Test
    public void generateStockCodeFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:\\StockData\\Table.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\StockData\\NewTable.txt"));
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
