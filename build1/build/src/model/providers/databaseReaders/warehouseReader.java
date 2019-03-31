package model.providers.databaseReaders;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.database.dataReader;
import model.providers.dataObject;

public class warehouseReader extends dataReader {
	private HSSFWorkbook myExcelBook=null;
	private ArrayList<String> headerrow;
	private HSSFSheet myExcelSheet;
	private static warehouseReader self = null; 
	private String filename;
	private warehouseReader() {}
	
	public static warehouseReader getInstance() {
		if (self!=null) {return self;}
		else {self = new warehouseReader();
		return self;}
		
	}
	
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
			
		// TODO Auto-generated method stub
		return ret;
	}
	
	
	
	@Override
	public boolean openfile(String fname) {
		try {
			filename = fname;
			FileInputStream fin = new FileInputStream(fname);
			myExcelBook = new HSSFWorkbook(fin);
			myExcelSheet = myExcelBook.getSheet("Лист1");
			HSSFRow row = myExcelSheet.getRow(0);
			headerrow = new ArrayList<String>();
			java.util.Iterator<Cell> cellIter =  row.cellIterator();
			while ( cellIter.hasNext()) {
				HSSFCell cell = (HSSFCell) cellIter.next();
				headerrow.add(cell.getStringCellValue());
				
			}
			//System.out.println(headerrow.indexOf("цена зак"));
			fin.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
			
		}
		
		return true;
	}

	@Override
	public dataObject readRow(int num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setRow(int num, dataObject dobj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ObservableList<dataObject> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Integer> findRowById(String id) {
		// TODO Auto-generated method stub
		return null;
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
			// TODO Auto-generated method stub
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
									this.getValinRow(row, headerrow.indexOf("цена продажн")),
									this.getValinRow(row, headerrow.indexOf("цена зак"))));	}
			}
		
		return res;
	}
	//обновляет данные в таблице БД отнимая количество
	public boolean updateData(ObservableList<dataObject> items) {
		java.util.Iterator<Row> it =   myExcelSheet.iterator();
		it.next();
		while (it.hasNext()) {
			HSSFRow row = (HSSFRow) it.next();
			String name,cross;
			Iterator<dataObject> objit = items.iterator();
			while (objit.hasNext()) {
				dataObject nextt = objit.next();
				if (nextt.getName().equals(this.getValinRow(row, headerrow.indexOf("наименование"))) &&
					(nextt.getSupplier().equals(this.getValinRow(row, headerrow.indexOf("поставщик")))) &&
					(nextt.getCrossID().equals(this.getValinRow(row, headerrow.indexOf("артикул")))) ){
					Double num1 = Double.parseDouble(nextt.getQuantaty());
					Double num2 = Double.parseDouble(this.getValinRow(row, headerrow.indexOf("кол-во")));
					Double res = num2-num1;
					
						row.getCell(headerrow.indexOf("кол-во")).setCellValue(String.valueOf(res));
					
					objit.remove();
					
				}
			}
			
		}
		try {
			FileOutputStream fout = new FileOutputStream(filename);
			myExcelBook.write(fout);
			fout.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}

}
