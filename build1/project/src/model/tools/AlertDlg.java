package model.tools;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
//аля system.out.println только диалоговое окно
public class AlertDlg {
	private static AlertDlg self;
	
	
	
    public static AlertDlg getInstance() {
		if (self == null)
		{
			self = new AlertDlg();
		}
		return self;
		
	}
    private AlertDlg(){}
    public int OpenDialog(AlertType type, String titlemsg, String headertext, String text) {
    	Alert al;
    	switch (type) {
    	case INFORMATION:
    		al = new Alert(AlertType.INFORMATION);
    		break;
    	case ERROR:
    		al = new Alert(AlertType.ERROR);
    		break;
    	case WARNING:
    		al = new Alert(AlertType.WARNING);
    		break;
    	default:
    		al = new Alert(AlertType.INFORMATION);
    		break;
    	}
    	
    	
    	al.setTitle(titlemsg);
    	al.setHeaderText(headertext);
    	al.setContentText(text);
    	

    	al.showAndWait();
    	return 1;
    }
    public int OpenDialog(AlertType type,String titlemsg, String text) {
    	return this.OpenDialog(type, titlemsg,null, text);
    }
    public int OpenDialog(AlertType type,String text) {
    	return this.OpenDialog(type, null ,null, text);
    }
    
    
}
