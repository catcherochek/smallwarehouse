package model.providers.databaseReaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;

import com.ibm.icu.text.RuleBasedNumberFormat;

import javafx.collections.ObservableList;
import model.database.dataReader;
import model.database.excelReader;
import model.providers.dataObject;
import model.tools.fwMoney;

public class paragonCreater extends dataReader {
	
	private String paragontpl;
	private String paragonname;
	private String currentdir;
	private static paragonCreater self;
	@Override
	public ArrayList<String> getSimilarArtNames(String expr) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

	@Override
	public boolean openfile(String fname) {
		paragontpl = fname+"tpl.xls";
		paragonname = fname;
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		Date date = new Date();
		String data = dateFormat.format(date).toString();
		File curDir = new File(fname+data);
		currentdir = fname+data+"\\";
		if (curDir.exists()) {
			return true;
		}
		return curDir.mkdir();
		
		
		
	}
	private boolean checkdir(String dir) {
		//paragonname = fname;
		//DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		//Date date = new Date();
		//String data = dateFormat.format(date).toString();
		File curDir = new File(paragonname+dir);
		currentdir = paragonname+dir+"\\";
		if (curDir.exists()) {
			return true;
		}
		return curDir.mkdir();
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

	public static paragonCreater getInstance() {
		if (self!=null) {return self;}
		else {self = new paragonCreater();
		return self;}
	}




	public void createParagon(ObservableList<dataObject> items, String paragonnum, String date) {
		this.checkdir(date);
		String filename = "накладная_N"+paragonnum+"_"+date+".xls";
		File from  = new File(paragontpl);
		File to  = new File(currentdir+"\\"+filename);
		
		if (to.exists()) {
			to.delete();
		}
		
		try {
			Files.copy(from.toPath(),to.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		try {
			
		//fwMoney mo = new fwMoney("7654321.98");
		//String money_as_string = mo.num2str();	
		FileInputStream finstream = new FileInputStream(to.toPath().toString());
		HSSFWorkbook myExcelBook = new HSSFWorkbook(finstream);
		
		HSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
		
		myExcelSheet.getRow(0).getCell(2).setCellValue(paragonnum);
		myExcelSheet.getRow(0).getCell(4).setCellValue(date);
		
		Iterator<dataObject> it = items.iterator();
		int tempfrom = 5;
		Double summ  = new Double(0);
		while (it.hasNext()) {
			dataObject dobj = it.next();
			if (it.hasNext()) {
				this.copyRow(myExcelBook, myExcelSheet, tempfrom, tempfrom+1);
				
			}
			HSSFRow row = myExcelSheet.getRow(tempfrom);
			row.getCell(0).setCellValue(String.valueOf(tempfrom-4));
			row.getCell(1).setCellValue(dobj.getName()+" "+dobj.getCrossID());
			row.getCell(2).setCellValue("шт");
			row.getCell(4).setCellValue(dobj.getQuantaty());
			row.getCell(5).setCellValue(dobj.getPrice());
			row.getCell(6).setCellValue(dobj.getTotal());
			summ = summ+Double.parseDouble(dobj.getTotal());
			tempfrom++;
		}
		myExcelSheet.getRow(tempfrom).getCell(6).setCellValue(String.valueOf(summ));
		DecimalFormat f = new DecimalFormat("##.00");
		String ffs = f.format(summ);
		ffs = ffs.replace(",", ".");
		fwMoney fwm = new fwMoney(ffs);
		String summstr = fwm.num2str();
		myExcelSheet.getRow(tempfrom+1).getCell(0).setCellValue("Всего на сумму :"+summstr);
		
		
		//String rub = nf.format(Math.abs(summ));
		//myExcelSheet.getRow(tempfrom+3).getCell(0).setCellValue("Всего на сумму :"+String.valueOf(summ));
		//HSSFRow existingRow = myExcelSheet.getRow(6);
		finstream.close();
		FileOutputStream out = new FileOutputStream(to.toPath().toString());
		//in.open();
		myExcelBook.write(out);
		out.close();
		//HSSF CellStyle currentStyle = ;
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		
	}
	private static void copyRow(HSSFWorkbook workbook, HSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        HSSFRow newRow = worksheet.getRow(destinationRowNum);
        HSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = sourceRow.getCell(i);
            HSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            HSSFCellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            ;
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());
            //CellType.BLANK
            // Set the cell data value
            switch (oldCell.getCellType()) {
                case BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }

        // If there are are any merged regions in the source row, copy to new row
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                                        )),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }



}
