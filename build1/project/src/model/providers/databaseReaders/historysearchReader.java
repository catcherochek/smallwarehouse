package model.providers.databaseReaders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import model.database.dataReader;
import model.providers.dataObject;

public class historysearchReader extends dataReader  {
	private ArrayList<Pair<String,String>> aliaseslist;
	HSSFWorkbook myExcelBook=null;
	ArrayList<String> headerrow;
	HSSFSheet myExcelSheet;
	private static historysearchReader self;
	
	public ArrayList<String> findAliases(String key) {
		ArrayList<String> searcharray = new ArrayList<String>();
		ArrayList<Integer> exclusionnum = new ArrayList<Integer>();
		searcharray.add("0");
		searcharray.add(key);
		//ArrayList<String> ret = new ArrayList<String>();
		key = key.replaceAll("\\s", "");
		key = key.toLowerCase();
		
		int countcycles = 1;
		for (int i = 1;i<=countcycles;i++) {
			for (int j = 0; j<aliaseslist.size(); j++) {
				Pair<String,String> p = aliaseslist.get(j);
				String res =  p.getKey()+";"+p.getValue();
				res = res.replaceAll("\\s", "");
				res = res.toLowerCase();
				String[] ress = res.split(";");
				ArrayList<String> resss = new ArrayList<String>();
				resss.addAll(Arrays.asList(ress));
				boolean findequalstring = false;
				for (String s:resss) {
					
					if (s.equals(searcharray.get(i))){
						if (!exclusionnum.contains(j)) {
						findequalstring = true;
						resss.remove(s);
						break;}

					}
				}
				if (findequalstring) {
					
					searcharray.addAll(resss);
					countcycles = countcycles+resss.size();
					exclusionnum.add(j);
					
				}
			}
		}
		searcharray.remove(key);
		searcharray.remove("0");
		return searcharray;
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
	
	private historysearchReader() {
		
	}
	public static historysearchReader getInstance() {
		if (self!=null) {return self;}
		else {self = new historysearchReader();
		return self;}
	}
	@Override
	public boolean openfile(String fname) {
		if (aliaseslist!=null) {
			return true;
		}
		try {
			aliaseslist = new ArrayList<Pair<String,String>>();
			myExcelBook = new HSSFWorkbook(new FileInputStream(fname));
			myExcelSheet = myExcelBook.getSheet("Лист1");
			HSSFRow row = myExcelSheet.getRow(0);
			headerrow = new ArrayList<String>();
			java.util.Iterator<Cell> cellIter =  row.cellIterator();
			while ( cellIter.hasNext()) {
				HSSFCell cell = (HSSFCell) cellIter.next();
				headerrow.add(cell.getStringCellValue());
				
			}
			java.util.Iterator<Row> rowIter =  myExcelSheet.iterator();
			rowIter.next();
			while ( rowIter.hasNext()) {
				row = (HSSFRow) rowIter.next();
				java.util.Iterator<Cell> cellIter1 =  row.cellIterator();
				String key = "";
				String val = "";
				int i = 0;
				while ( cellIter1.hasNext()) {
					if (i ==0) {
						key = this.getValinRow(row, i);
					}
					else {
						val = val+this.getValinRow(row, i)+";";
					}
					i++;
					cellIter1.next();
				}
				aliaseslist.add(new Pair(key,val));
			}
			
			
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
	public ArrayList<String> getSimilarArtNames(String expr) {
		expr = expr.replaceAll("\\s", "");
		expr = expr.toLowerCase();
		ArrayList<String> ret  = new  ArrayList<String>();
		for (Pair<String,String> p : aliaseslist) {
			String res =  p.getKey()+";"+p.getValue();
			if (res.contains(expr)) {
				res = res.replaceAll("\\s", "");
				res = res.toLowerCase();
				
				String[] ress = res.split(";");
				for(String st:ress) {
					st = st.replaceAll("\\s", "");
					st = st.toLowerCase();
					if (st.contains(expr)){
						ret.add(st);
					}
				}
			}
		}
		return ret;
	}


	@Override
	public ObservableList<dataObject> getRecordsById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
