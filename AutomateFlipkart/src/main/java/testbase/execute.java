package testbase;

import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;





public class execute {
	
	
	@Test
	public void testLogin() throws Exception {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		ChromeDriver webdriver = new ChromeDriver();
	    
       ReadData file = new ReadData();
        ReadObject object = new ReadObject();
        Properties allObjects =  object.getObjectRepository();
        UiOperatio operation = new UiOperatio(webdriver);
        //Read keyword sheet
        Sheet guru99Sheet = file.readExcel(System.getProperty("user.dir")+"\\","TestCase.xlsx" , "KeywordFramework");
      //Find number of rows in excel file
    	int rowCount = guru99Sheet.getLastRowNum()-guru99Sheet.getFirstRowNum();
    	//Create a loop over all the rows of excel file to read it
    	for (int i = 1; i < rowCount+1; i++) {
    		//Loop over all the rows
    		Row row = guru99Sheet.getRow(i);
    		//Check if the first cell contain a value, if yes, That means it is the new testcase name
    		if(row.getCell(0).toString().length()==0){
    		//Print testcase detail on console
    			System.out.println(row.getCell(1).toString()+"----"+ row.getCell(2).toString()+"----"+
    			row.getCell(3).toString()+"----"+ row.getCell(4).toString());
    		//Call perform function to perform operation on UI
    			operation.perform(allObjects, row.getCell(1).toString(), row.getCell(2).toString(),
    				row.getCell(3).toString(), row.getCell(4).toString());
    	    }
    		else{
    			//Print the new  testcase name when it started
    				System.out.println("New Testcase->"+row.getCell(0).toString() +" Started");
    			}
    		}
	}
}
