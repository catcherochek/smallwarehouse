package model.tools;

import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellType;

public class aliasesHSSF extends aliases {

	public aliasesHSSF(HSSFRow row) {
		super(aliasesHSSF.createarraylist(row));
		
	}

	private static ArrayList<String> createarraylist(HSSFRow row) {
		//int i = 0;
		ArrayList<String> res = new ArrayList<String>();
		
		for (int i = 0 ; i<row.getLastCellNum();i++) {
			res.add(aliasesHSSF.getValinRow(row, i));
		}
		return res;
	}
	private static String getValinRow(HSSFRow row, int cellnum) {
		
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

}
