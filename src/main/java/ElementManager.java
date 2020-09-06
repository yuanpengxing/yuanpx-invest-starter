public interface ElementManager {

    String totalPages = "//ul[@class='PageNavBtm clearfix']//span[@class='count']";
    String curPage = "//ul[@class='PageNavBtm clearfix']//span[@class='curPage']";
    String pageInput = "//ul[@class='PageNavBtm clearfix']/li[last()]/div/input";
    String goBtn = "//ul[@class='PageNavBtm clearfix']/li[last()]/button";
    String table1 = "//table[@id='listTable1']/tbody";
    String table2 = "//table[@id='listTable2']/tbody";
    String table3 = "//table[@id='listTable3']/tbody";
    String table4 = "//table[@id='listTable4']/tbody";
    String everyDealRecord = "//div[@class='pge_box  contentDiv']//table[@class='listTable']/tbody/tr";

    String lastRecordTable1 = "//table[@id='listTable1']/tbody/tr[last()]";
    String lastLineTable4 = "//table[@id='listTable4']/tbody/tr[last()]";

}
