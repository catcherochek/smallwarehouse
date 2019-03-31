package controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.providers.dataObject;
import model.providers.journalObject;
import model.providers.databaseReaders.journalReader;
import model.tools.AlertDlg;
import model.tools.transferobj.SynchronizedListener;

public class journalWindow implements Initializable {
	@FXML ComboBox<String> jw_term_list;
	@FXML DatePicker jw_datepicker_to;
	@FXML DatePicker jw_datepicker_from;
	
	
	@FXML TableView<journalObject> jw_table;
	@FXML TableColumn<journalObject,String> jw_table_ID;
	@FXML TableColumn<journalObject,String> jw_table_name;
	@FXML TableColumn<journalObject,String> jw_table_seller;
	@FXML TableColumn<journalObject,String> jw_table_quantaty;
	@FXML TableColumn<journalObject,String> jw_table_price;
	@FXML TableColumn<journalObject,String> jw_table_status;
	@FXML TableColumn<journalObject,String> jw_table_price_buy;
	
	enum Checktype{
		TODAY,MONTH,YEAR,OTHER
	}
	
	Checktype checkType = Checktype.TODAY;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		jw_term_list.getItems().add("сегодня");
		jw_term_list.getItems().add("за месяц");
		jw_term_list.getItems().add("за год");
		jw_term_list.getItems().add("отдельный период");
		//jw_term_list.getSelectionModel().select(0);
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		Date date = new Date();
		String data = dateFormat.format(date).toString();
		 LocalDate localTime = LocalDate.parse(data, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
		jw_datepicker_from.setValue(localTime );
		jw_datepicker_to.setValue(localTime );
		
		//DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		//Date date = new Date();
		//String data = date.toString();
		//String   str = journalReader.getInstance().getLastParagonNum(dateFormat.format(date).toString());
		
		
		
		
		jw_table_ID.setCellValueFactory(new PropertyValueFactory<journalObject,String>("CheckNum"));
		jw_table_name.setCellValueFactory(new PropertyValueFactory<journalObject,String>("Date"));
		jw_table_seller.setCellValueFactory(new PropertyValueFactory<journalObject,String>("Operation"));
		jw_table_quantaty.setCellValueFactory(new PropertyValueFactory<journalObject,String>("Total"));
		jw_table_price.setCellValueFactory(new PropertyValueFactory<journalObject,String>("Buyer"));
		//jw_table_status.setCellValueFactory(new PropertyValueFactory<journalObject,String>("Operation"));
		//jw_table_price_buy.setCellValueFactory(new PropertyValueFactory<journalObject,String>("PriceBuy"));
		
		jw_table.setRowFactory(tv -> new TableRow<journalObject>() {
		    @Override
		    public void updateItem(journalObject item, boolean empty) {
		        super.updateItem(item, empty) ;
		        if (item == null) {
		            setStyle("");
		        } else if ((item.getOperation().equals("Продажа"))) {
		            setStyle("-fx-background-color: LightGreen;");
		        } else if ((item.getOperation().equals("Отложено"))) {
		            setStyle("-fx-background-color: DarkOrange;");
		        }
		        
		    }
		});
		
		jw_term_list.valueProperty().addListener(new ChangeListener<String>() {
			
			
			//TODO изменение списка 
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				jw_table.getItems().clear();
				int num = jw_term_list.getItems().indexOf(newValue);
				switch(num) {
				case 1:
					checkType = Checktype.MONTH;
					break;
				case 0:
					checkType = Checktype.TODAY;
					break;
				case 2:
					checkType = Checktype.MONTH;
					break;
				case 3:
					checkType = Checktype.OTHER;
					break;
					
					
				}
				String from,to;
				from = jw_datepicker_from.getValue().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
				to = jw_datepicker_to.getValue().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
				switch(checkType) {
				case TODAY:
					ObservableList<journalObject> obj = journalReader.getInstance().selectData(from,to);
					jw_table.getItems().addAll(obj);
					break;
				case MONTH:
					LocalDate date2 = (LocalDate.now()).with(TemporalAdjusters.firstDayOfMonth());
					String tmp = date2.format(formatter);
					 
					 
					 
					ObservableList<journalObject> obj2 = journalReader.getInstance().selectData(tmp,to);
					jw_table.getItems().addAll(obj2);
					break;
				case OTHER:
					ObservableList<journalObject> obj1 = journalReader.getInstance().selectData(from,to);
					jw_table.getItems().addAll(obj1);
					break;
				}
				
			}    
	      });
		jw_term_list.getSelectionModel().select(0);
		//jw_term_list.upd
		
		
	}

	
	
	//MENU
	@FXML public void jw_contextmenu_click(){
		AlertDlg.getInstance().OpenDialog(AlertType.INFORMATION, "contextmenu");
		journalObject item =  jw_table.getSelectionModel().getSelectedItem();
		
		
		ObservableList<journalObject> list = journalReader.getInstance().getListFromrecord(item);
		SynchronizedListener.getInstance().addObject(list);
		SynchronizedListener.getInstance().fire();
		Stage stage = (Stage) jw_table.getScene().getWindow();
		stage.close();
		
	}
	//!!!MENU
}
