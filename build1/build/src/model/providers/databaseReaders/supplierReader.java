package model.providers.databaseReaders;

import java.io.File;
import java.io.FilenameFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.Map;

import javax.annotation.processing.SupportedOptions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.database.dataReader;
import model.database.excelReader;
import model.providers.dataObject;

public class supplierReader extends dataReader {
	private static supplierReader self;
	private static HashMap<String,excelReader> supplierlist = new HashMap<String,excelReader>();
	
	
	private supplierReader() {}
	public static supplierReader getInstance() {
		if (self!=null) {return self;}
		else {self = new supplierReader();
		return self;}
		
	} 
	
	private static ArrayList<String> getAllFiles(File curDir) {
		ArrayList<String> res = new ArrayList<>();
        File[] filesList = curDir.listFiles();
        for(File f : filesList){
            if(f.isDirectory())
            	res.addAll(getAllFiles(f));
            if(f.isFile()){
            	String extension = "";

            	int j = f.getName().lastIndexOf('.');
            	if (j >= 0) {
            	    extension = f.getName().substring(j+1);
            	}
            	if (extension.equals("xls")) {
            		res.add(f.getAbsolutePath());
            		excelReader xlr = new excelReader();
            		supplierlist.put(f.getParentFile().getName(),new excelReader());
            		supplierlist.get(f.getParentFile().getName()).openfile(f.getAbsolutePath());
            		//res.add(f.getParent());
            		
            	}
            	
            }
        }
        return res;
	}
	@Override
	public boolean openfile(String fname) {
		File curDir = new File(fname);
        ArrayList resfiles = getAllFiles(curDir);
		if  (resfiles!=null) {
			return true;
		}
		return false;
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
		
		for (Map.Entry<String,excelReader> p : supplierlist.entrySet()) {
			ret.addAll(p.getValue().getSimilarArtNames(expr));
		}
		return ret;
	}
	@Override
	public ObservableList<dataObject> getRecordsById(String id) {
		ObservableList<dataObject> ret =  FXCollections.observableArrayList();
		for(Map.Entry<String,excelReader> xl: supplierlist.entrySet()) {
		ret.addAll(xl.getValue().getRecordsById(id));}
		return ret;
	}


}
