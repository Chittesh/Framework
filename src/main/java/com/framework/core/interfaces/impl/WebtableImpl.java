package com.framework.core.interfaces.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.framework.core.interfaces.Element;
import com.framework.core.interfaces.Webtable;
import com.framework.utils.AllegisDriver;
import com.framework.utils.AuditLogger;
import com.framework.utils.utilities.StackTraceInfo;

public class WebtableImpl extends ElementImpl implements Webtable {

	private String elementIdentifier;
	
    public WebtableImpl(AllegisDriver driver, By by) {
        super(driver, by);
        
        this.elementIdentifier = getElementIdentifier();
    }
    
    @Override
    public int getRowCount() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getRows().size();
    }

    @Override
    public int getColumnCount(int rowNumber) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	WebElement row = getRows().get(rowNumber);
    	
        return getCells(row).size();
    }

    @Override
    public Element getCell(int row, int column) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	String locator = String.format("%s//tr[%d]/td[%d]", this.elementIdentifier, row, column); 
    	
        return new ElementImpl(getWrappedDriver(), By.xpath(locator));
    }
   
    public Element getHeaderCell(int column) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	String locator = String.format("%s//tr[1]/th[%d]", this.elementIdentifier, column);    	    	
    	
        return new ElementImpl(getWrappedDriver(), By.xpath(locator));
    }
    
    @Override
    public void clickCell(int row, int column) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        getCell(row, column).click();
    }
    
    public void clickHeaderCell(int column) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	getHeaderCell(column).click();
    }

    @Override
    public String getCellData(int row, int column) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getCell(row, column).getText();
    }

    @Override
    public int getRowWithCellText(String text) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getRowWithText(text);
    }

    @Override
    @Deprecated
    public int getRowWithCellText(String text, int columnPosition) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	return getRowWithText(text);
    }

    @Override
    @Deprecated
    public int getRowWithCellText(String text, int columnPosition, int startRow) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	return getRowWithText(text);
    }

    @Override
    @Deprecated
    public int getRowWithCellText(String text, int columnNumber, int rowNumber, boolean matchType) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getRowWithText(text);
    }
    
    private int getRowWithText(String text) {

        List<WebElement> rows = getRows();
        
        for (int i = 0; i < rows.size(); i++) {

        	WebElement row = rows.get(i);
        	List<WebElement> cells = getCells(row);
        	
        	for (WebElement cell : cells) 
                if (Boolean.TRUE.equals(partialMatch(cell.getText(), text))) 
                	return i + 1;
            
       }

        return -1;
    }
    
    @Override
    public int getColumnWithCellText(String text) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	for (int i = 0; i < getRowCount(); i++) {

    		WebElement row    = getRows().get(i);
        	List<WebElement> columns = getCells(row);
            
            for (int j = 0; j < columns.size(); j++) 
                if (columns.get(j).getText().trim().equals(text)) 
                    return j + 1;

    	}
    	
    	return -1;
    }

    @Override
    @Deprecated
    public int getColumnWithCellText(String text, int rowNumber) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getColumnWithCellText(text);
    }
    
    private Boolean partialMatch(String actual, String expected) {
    	return actual.toLowerCase().trim().contains(expected.toLowerCase().trim());
    }
    
    private List<WebElement> getRows() {
    	String locator = String.format("%s/tbody/tr", this.elementIdentifier);
        return this.element.findElements(By.xpath(locator));
    }

    private List<WebElement> getCells(WebElement row) {
        return row.findElements(By.xpath("./td"));
    }

    
}