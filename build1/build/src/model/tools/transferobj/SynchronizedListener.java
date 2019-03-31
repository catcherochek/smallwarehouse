package model.tools.transferobj;

import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import model.providers.dataObject;
import model.providers.journalObject;




/**
 * @author Клим
 * реализуеи объект, к-й организовывает передачу данных путем регистрации события
 */
public class SynchronizedListener {
	private static SynchronizedListener self;
	private ObservableList<journalObject> datatotransfer;
	List<MyEventHandler> myevh;
	
	private SynchronizedListener() {
		myevh = new ArrayList<MyEventHandler>();
	}
	public static SynchronizedListener getInstance() {
		if (self == null) {
			self = new SynchronizedListener();
		}
		return self;
	}
	
	//
	/** регистрирует событие,  используется в классе слушателе как  
	 * SynchronizedListener.getInstance().registerEvent(new MyEventHandler() {.....})
	 * @param nh 
	 */
	public void registerEvent(MyEventHandler nh) {
		myevh.add(nh);
	};
	
	/**генерирует события, все слушатели получат оповещение
	 * @param list 
	 * @return
	 */
	public void fire() {
		for (MyEventHandler temp : myevh) {
		temp.fireEvent(datatotransfer);}
	}
	
	/**добавляет переменную в класс,  эта переменная будет выдана, при событии
	 * @param list 
	 * @return
	 */
	public SynchronizedListener addObject(ObservableList<journalObject> list) {
		datatotransfer = list;
		return null;
	}


}
