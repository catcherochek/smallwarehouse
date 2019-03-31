package model.providers.databaseReaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.plaf.FileChooserUI;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.shape.Path;
import model.providers.dataObject;
import model.providers.journalObject;
import model.tools.AlertDlg;
import model.tools.aliases;
import model.tools.aliasesHSSF;

public class journalReader extends model.database.dataReader {
	private HSSFWorkbook myExcelBook=null;
	private ArrayList<String> headerrow;
	private HSSFSheet myExcelSheet;
	private String filename;
	private String tempfile = "temp.xls";
	private FileInputStream finstream;
	private static journalReader self = null; 
	private File tempFile;
	private File originalFile;
	private aliases rowheader; 
	
	private journalReader() {}
	
	public static journalReader getInstance() {
		if (self!=null) {return self;}
		else {self = new journalReader();
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
			originalFile  = new File(fname);
			//tempFile = new File( originalFile.getAbsolutePath()+tempfile);
			//FileInputStream StrIn = new FileInputStream(originalFile);
			//FileOutputStream StrOut = new FileOutputStream(tempFile);
			//long copy = Files.copy(originalFile.toPath(),StrOut );
			//StrOut.close();
			//StrIn.close();
			finstream = new FileInputStream(originalFile);
			myExcelBook = new HSSFWorkbook(finstream);
			
			myExcelSheet = myExcelBook.getSheet("Лист1");
			HSSFRow row = myExcelSheet.getRow(0);
			rowheader = new aliasesHSSF(row);
			
			headerrow = new ArrayList<String>();
			java.util.Iterator<Cell> cellIter =  row.cellIterator();
			while ( cellIter.hasNext()) {
				HSSFCell cell = (HSSFCell) cellIter.next();
				headerrow.add(cell.getStringCellValue());
				
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
	public ObservableList<dataObject> getRecordsById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean addData(ObservableList<dataObject> items,String operation,String Kontonum) {
		
		try {
			originalFile  = new File(filename);
			//tempFile = new File( originalFile.getAbsolutePath()+tempfile);
			//FileInputStream StrIn = new FileInputStream(originalFile);
			//FileOutputStream StrOut = new FileOutputStream(tempFile);
			//long copy = Files.copy(originalFile.toPath(),StrOut );
			//StrOut.close();
			//StrIn.close();
			
			//HSSFWorkbook myExcelBook1 = new HSSFWorkbook(finstream);
			
			myExcelSheet = myExcelBook.getSheet("Лист1");
			for (dataObject res : items) {
				int j = myExcelSheet.getLastRowNum();
				HSSFRow row = myExcelSheet.getRow(1+j);
				if (row == null) {
					row = myExcelSheet.createRow(1+j);
				}
				
				row.createCell(headerrow.indexOf("артикул")).setCellValue(res.getCrossID());
				row.createCell(headerrow.indexOf("наименование")).setCellValue(res.getName());
				row.createCell(headerrow.indexOf("операция")).setCellValue(operation);
				DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
				Date date = new Date();

				row.createCell(headerrow.indexOf("дата")).setCellValue(dateFormat.format(date).toString());

				row.createCell(headerrow.indexOf("поставщик")).setCellValue(res.getSupplier());
				row.createCell(headerrow.indexOf("номер чека")).setCellValue(Kontonum);
				row.createCell(headerrow.indexOf("кол-во")).setCellValue(res.getQuantaty());
				row.createCell(headerrow.indexOf("цена продажи")).setCellValue(res.getPrice());
				row.createCell(headerrow.indexOf("цена закупки")).setCellValue(res.getPriceBuy());
				//finstream.getChannel()
				//finstream.close();
				finstream.close();
				FileOutputStream out = new FileOutputStream(originalFile.getAbsolutePath());
				//in.open();
				myExcelBook.write(out);
				out.close();
				finstream = new FileInputStream(originalFile.getAbsolutePath());
				//myExcelBook.close();
				//finstream = new FileInputStream(t);

			} 
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}



	/**
	 * @param paragonDate - строка типа new SimpleDateFormat("yyyy.MM.dd");
	 * @return последний номер чека  за этот день
	 */
	public String getLastParagonNum(String paragonDate) {
		int ret  = 0; 
		int rownum = 1;
		int j = myExcelSheet.getLastRowNum();
		if (j==0) {
			return "1";
		}
		java.util.Iterator<Row> rowIter =  myExcelSheet.iterator();
		rowIter.next();
		HSSFRow row = null;
		while ( rowIter.hasNext()) {
			row = (HSSFRow) rowIter.next();
			if ( getValinRow(row, headerrow.indexOf("дата")).contains(paragonDate) ){
				int temp = Integer.parseInt(getValinRow(row, headerrow.indexOf("номер чека")));
				if (temp>ret) {
					ret = temp;
				}
			}
		}
		ret++;
		return String.valueOf(ret);
	}

	public ObservableList<journalObject> selectData(String from, String to) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		ObservableList<journalObject> res = FXCollections.observableArrayList();
		Date Dfrom,Dto;
		try {
			Dfrom = simpleDateFormat.parse(from);
			Dto = simpleDateFormat.parse(to);
			HashMap<String,journalObject> index = new HashMap<String, journalObject>();
			
			Iterator<Row> rowIter =  myExcelSheet.iterator();
			rowIter.next();
			HSSFRow row = null;
			
			while ( rowIter.hasNext()) {
				row = (HSSFRow) rowIter.next();
				Date currdate = simpleDateFormat.parse(getValinRow(row, headerrow.indexOf("дата")));
				if ( (currdate.compareTo(Dfrom)>=0) & (currdate.compareTo(Dto)<=0)  ){
					//String s  = ;
					Double total = ((new BigDecimal(this.getValinRow(row, headerrow.indexOf("кол-во"))).setScale(2, RoundingMode.HALF_UP).doubleValue())*
							(new BigDecimal(this.getValinRow(row, headerrow.indexOf("цена продажи"))).setScale(2, RoundingMode.HALF_UP).doubleValue()));
					
					String keytocheck =  this.getValinRow(row, headerrow.indexOf("номер чека")) + getValinRow(row, headerrow.indexOf("дата"));
					if (index.containsKey(keytocheck)) {
						
						index.get(keytocheck).setTotal(
								String.valueOf((new BigDecimal(index.get(keytocheck).getTotal()).setScale(2, RoundingMode.HALF_UP).doubleValue())+
								(new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue()))
								);
						
						
					}
					else{
						
						//index.put(keytocheck)
						journalObject jo = new journalObject(this.getValinRow(row, headerrow.indexOf("артикул")), 
								this.getValinRow(row, headerrow.indexOf("наименование")), 
								this.getValinRow(row, headerrow.indexOf("поставщик")), 
								this.getValinRow(row, headerrow.indexOf("кол-во")), 
							this.getValinRow(row, headerrow.indexOf("цена продажи")),
							this.getValinRow(row, headerrow.indexOf("цена закупки")), 
							this.getValinRow(row, headerrow.indexOf("операция")),
							this.getValinRow(row, headerrow.indexOf("номер чека")), 
							this.getValinRow(row, headerrow.indexOf("дата")),
							this.getValinRow(row, headerrow.indexOf("покупатель"))
						);
						
						jo.setTotal(String.valueOf(total));
						res.add(jo);
						index.put(keytocheck, jo);
						
					};

				}
					
			}
				
			
			
			
		} catch (ParseException e) {
			AlertDlg.getInstance().OpenDialog(AlertType.ERROR, "не удалось получить данные из журнала");
			e.printStackTrace();
		}
		
		
		
		
		
		return res;
	}

	public ObservableList<journalObject> getListFromrecord(journalObject item) {
		Iterator<Row> rowIter =  myExcelSheet.iterator();
		rowIter.next();
		HSSFRow row = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		ObservableList<journalObject> res = FXCollections.observableArrayList();
		String date = item.getDate();
		String checknum = item.getCheckNum();
		while ( rowIter.hasNext()) {
			row = (HSSFRow) rowIter.next();
			if ( (date.contains(this.getValinRow(row, headerrow.indexOf("дата"))))&(checknum.contains(this.getValinRow(row, headerrow.indexOf("номер чека"))))  ){
				journalObject jo = new journalObject(this.getValinRow(row, headerrow.indexOf("артикул")), 
						this.getValinRow(row, headerrow.indexOf("наименование")), 
						this.getValinRow(row, headerrow.indexOf("поставщик")), 
						this.getValinRow(row, headerrow.indexOf("кол-во")), 
					this.getValinRow(row, headerrow.indexOf("цена продажи")),
					this.getValinRow(row, headerrow.indexOf("цена закупки")), 
					this.getValinRow(row, headerrow.indexOf("операция")),
					this.getValinRow(row, headerrow.indexOf("номер чека")), 
					this.getValinRow(row, headerrow.indexOf("дата")),
					this.getValinRow(row, headerrow.indexOf("покупатель"))
				);
				res.add(jo);
			}
		}
		return res;
	}
	

	
	


}
