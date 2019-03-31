package model.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.providers.dataObject;
import model.providers.databaseReaders.aliasesReader;

//сам класс, реализующий чтение, в ко
public class excelReader extends dataReader {
	private HSSFWorkbook myExcelBook=null;
	private ArrayList<String> headerrow;
	private HSSFSheet myExcelSheet;
	
	public String getValinRow(HSSFRow row, int cellnum) {
		try {
			if  (row.getCell(cellnum).getCellType()==CellType.STRING)
			{
				return row.getCell(cellnum).getStringCellValue();
			}
			else if (row.getCell(cellnum).getCellType()==CellType.NUMERIC)
			{return String.valueOf(row.getCell(cellnum).getNumericCellValue());}
			else {return "";}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "";
		}
	}
	@Override
	public
	boolean openfile(String fname) {
		try {
			myExcelBook = new HSSFWorkbook(new FileInputStream(fname));
			myExcelSheet = myExcelBook.getSheet("Лист1");
			HSSFRow row = myExcelSheet.getRow(0);
			headerrow = new ArrayList<String>();
			java.util.Iterator<Cell> cellIter =  row.cellIterator();
			while ( cellIter.hasNext()) {
				HSSFCell cell = (HSSFCell) cellIter.next();
				headerrow.add(cell.getStringCellValue());
				
			}
			System.out.println(headerrow.indexOf("цена зак"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		//myExcelSheet.getRow(1).getCell(1).setCellValue(value);
		return true;
	}

	@Override
	public
	dataObject readRow(int num) {
		if (myExcelBook==null) { 
		return null;}
		// TODO Auto-generated method stub
		myExcelSheet = myExcelBook.getSheet("Лист1");
		HSSFRow row = myExcelSheet.getRow(num);
		
		dataObject dO = new dataObject(this.getValinRow(row, headerrow.indexOf("артикул")), 
									this.getValinRow(row, headerrow.indexOf("наименование")), 
									this.getValinRow(row, headerrow.indexOf("поставщик")), 
									this.getValinRow(row, headerrow.indexOf("кол-во")), 
									this.getValinRow(row, headerrow.indexOf("цена продажн")));
		return dO;
		
	}

	@Override
	public
	boolean setRow(int num, dataObject dobj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public
	ObservableList<dataObject> readAll() {
		
		if (myExcelBook==null) { 
			return null;}
			// TODO Auto-generated method stub
		ObservableList<dataObject> res = FXCollections.observableArrayList();
		myExcelSheet = myExcelBook.getSheet("Лист1");
		java.util.Iterator<Row> it =   myExcelSheet.iterator();
		it.next();
		while (it.hasNext()) {
			HSSFRow row = (HSSFRow) it.next();
			res.add(new dataObject(this.getValinRow(row, headerrow.indexOf("артикул")), 
									this.getValinRow(row, headerrow.indexOf("наименование")), 
									this.getValinRow(row, headerrow.indexOf("поставщик")), 
									this.getValinRow(row, headerrow.indexOf("кол-во")), 
									this.getValinRow(row, headerrow.indexOf("цена продажн"))));
		}
		return res;
	}
	@Override
	public ArrayList<Integer> findRowById(String id) {
		myExcelSheet = myExcelBook.getSheet("Лист1");
		java.util.Iterator<Row> it =   myExcelSheet.iterator();
		it.next();
		ArrayList<Integer> res = new ArrayList<Integer>();
		int i = 1;
		while (it.hasNext()) {
			HSSFRow row = (HSSFRow) it.next();
			if (this.getValinRow(row, headerrow.indexOf("артикул"))==id) {
				res.add(new Integer(i));
				i++;
			}
		}
		return res;
	}
	@Override
	public ArrayList<String> getSimilarArtNames(String expr) {
		int colIndex = headerrow.indexOf("артикул");
		ArrayList<String> ret  = new ArrayList<String>();
		for (int rowIndex = 1; rowIndex <= myExcelSheet.getLastRowNum(); rowIndex++) {
			  HSSFRow row = myExcelSheet.getRow(rowIndex);
			  if (row != null) {
			    String tempstr = this.getValinRow(row, colIndex);
			    tempstr = tempstr.replaceAll("\\s", "");
			    tempstr = tempstr.toLowerCase();
			    if (tempstr.contains(expr)) {
			    	ret.add(tempstr);
			    }
			  }
			  }
			
		
		return ret;
	}
	@Override
	public ObservableList<dataObject> getRecordsById(String id) {
		if (id.equals("")){
			return FXCollections.observableArrayList();
		}
		if (myExcelBook==null) { 
			return null;}
		id = id.replaceAll("\\s", "");
	    id = id.toLowerCase();
		
		ObservableList<dataObject> res = FXCollections.observableArrayList();
		myExcelSheet = myExcelBook.getSheet("Лист1");
		java.util.Iterator<Row> it =   myExcelSheet.iterator();
		it.next();
		while (it.hasNext()) {
			HSSFRow row = (HSSFRow) it.next();
			String ref = this.getValinRow(row, headerrow.indexOf("артикул"));
			ref = ref.replaceAll("\\s", "");
		    ref = ref.toLowerCase();
			if ((ref.equals(id))) {
				
				res.add(new dataObject(this.getValinRow(row, headerrow.indexOf("артикул")), 
										this.getValinRow(row, headerrow.indexOf("наименование")), 
										this.getValinRow(row, headerrow.indexOf("поставщик")), 
										this.getValinRow(row, headerrow.indexOf("кол-во")), 
									this.getValinRow(row, headerrow.indexOf("цена продажн"))));	}
			}
		return res;
	}

}
