package model.database;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import model.providers.dataObject;

//Абстрактный класс реализующий методы подключения и получения данных и таблицы
public abstract class dataReader {
	
	/**
	 * @param expr - String строковой запрос для поиска
	 * @return возвращает артикулы из базы, которые подходят синтаксически
	 */
	abstract public ArrayList<String> getSimilarArtNames(String expr);
	
	abstract public boolean openfile(String fname);
	
	abstract public dataObject readRow(int num);
	
	abstract public boolean setRow(int num, dataObject dobj);
	
	abstract public ObservableList<dataObject> readAll();
	
	abstract public ArrayList<Integer> findRowById(String id);
	
	abstract public ObservableList<dataObject> getRecordsById(String id);
}
