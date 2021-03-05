package libs;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.io.*;

public class CommonsUtils {

    @Test
    public void getAllStocksNum() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Think\\Desktop\\Table.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Think\\Desktop\\StocksNumber.txt"));

        String line;
        while ((line = br.readLine()) != null) {
            if (StringUtils.isNotEmpty(line)){
                bw.write(line.substring(2));
                bw.newLine();
            }
        }
        bw.close();
        br.close();
    }
}
