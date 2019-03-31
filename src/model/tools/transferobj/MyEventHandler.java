package model.tools.transferobj;

import javafx.collections.ObservableList;
import model.providers.journalObject;

public interface MyEventHandler {
	public void fireEvent(ObservableList<journalObject> list);
}


