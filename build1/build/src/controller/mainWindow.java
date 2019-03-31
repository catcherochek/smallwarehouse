package controller;

import java.awt.List;
import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;

import com.ibm.icu.text.RuleBasedNumberFormat;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import model.database.excelReader;
import model.providers.ITableRecord;
import model.providers.Mw_tbl_warehouse_implementation;
import model.providers.dataObject;
import model.providers.journalObject;
import model.providers.toolProv;
import model.providers.databaseReaders.aliasesReader;
import model.providers.databaseReaders.journalReader;
import model.providers.databaseReaders.paragonCreater;
import model.providers.databaseReaders.supplierReader;
import model.providers.databaseReaders.warehouseReader;
import model.tools.AlertDlg;
import model.tools.transferobj.MyEventHandler;
import model.tools.transferobj.SynchronizedListener;
import javafx.fxml.FXML;
public class mainWindow implements Initializable{
	
	@FXML private ComboBox mf_btn_search;
	@FXML private ListView<String> mw_list_refs;
	@FXML private Button mf_btn_search_by_art;
	@FXML private Button mw_btn_showdata;
	
	
	@FXML private TableView<dataObject> mw_tbl_warehouse;
	@FXML private TableColumn<dataObject,String> mw_tbl_warehouse_ID;
	@FXML private TableColumn<dataObject,String> mw_tbl_warehouse_name;
	@FXML private TableColumn<dataObject,String> mw_tbl_warehouse_supplier;
	@FXML private TableColumn<dataObject,String> mw_tbl_warehouse_quantaty;
	@FXML private TableColumn<dataObject,String> mw_tbl_warehouse_price;
	
	
	@FXML private TableView<dataObject> mw_tbl_warehouse_history;
	@FXML private TableColumn<dataObject,String> mw_tbl_warehouse_history_ID;
	@FXML private TableColumn<dataObject,String> mw_tbl_warehouse_history_name;
	@FXML private TableColumn<dataObject,String> mw_tbl_warehouse_history_supplier;
	@FXML private TableColumn<dataObject,String> mw_tbl_warehouse_history_quantaty;
	@FXML private TableColumn<dataObject,String> mw_tbl_warehouse_history_price;
	
	
	@FXML private TableView<dataObject> mw_tbl_cart;
	@FXML private TableColumn<dataObject,String> mw_tbl_cart_ID;
	@FXML private TableColumn<dataObject,String> mw_tbl_cart_name;
	@FXML private TableColumn<dataObject,String> mw_tbl_cart_quantaty;
	@FXML private TableColumn<dataObject,String> mw_tbl_cart_price;
	@FXML private TableColumn<dataObject,String> mw_tbl_cart_total;
	
	@FXML private Label mw_label_total_price;
	//тут сумма всего для корзина
	@FXML private Label mw_lbl_cart_summ;
	private ArrayList<Double> totalList;
	@FXML Label mw_cart_paragonnum_label;
	@FXML Label mw_cart_label_date;
	
	@FXML Button mw_btn_delay;
	
	@FXML void mf_btn_search_by_art_click(){
		String s = mf_btn_search.getEditor().getText();		
		ArrayList<String> st  = aliasesReader.getInstance().findAliases(s);
		
		st = toolProv.deleteDulicates(st);
		mw_list_refs.getItems().clear();
		mw_list_refs.getItems().addAll(st);
		int y = 0;
	}
	@FXML void mw_list_refs_click() {

	}
	
	
	@FXML private void mw_btn_showdata_click() {
		String art = mf_btn_search.getEditor().getText();
		mw_tbl_warehouse.getItems().clear();
		ObservableList<dataObject> parts =  warehouseReader.getInstance().getRecordsById(art);
		
		
		mw_tbl_warehouse.setItems(parts);
		ObservableList<dataObject> partshist = supplierReader.getInstance().getRecordsById(art);
		mw_tbl_warehouse_history.setItems(partshist);
		
	}
			
	@FXML private void mf_btnsearch_filldata() {
		String suppl="история";
		for (int i = 1; i<30; i++) {
			//mf_btn_search.getItems().clear();
			//System.out.println("fsfsaf");
			//String suppl="история";
			mf_btn_search.getItems().add("запись номер"+i);
			mw_list_refs.getItems().add("запись номер"+i);
			
			//mw_tbl_warehouse.getItems().add(new Mw_tbl_warehouse_implementation("cross"+i, "Name"+i, "Supplier", String.valueOf(i), String.valueOf(240+i)));
			mw_tbl_warehouse_history.getItems().add(new dataObject("cross"+i, "Name"+i, suppl, String.valueOf(i), String.valueOf(240+i)));
			//mw_tbl_warehouse_history.row
			if (i>5)
				{suppl = "поставщик";}
			mw_tbl_cart.getItems().add(new dataObject("cross"+i, "Name"+i, suppl, String.valueOf(i), String.valueOf(240+i)));
		}
		
		excelReader xlr = new excelReader();
		
		
		if (xlr.openfile("d:\\1.xls")) {
			System.out.println("ok");
		};
		mw_tbl_warehouse.setItems(xlr.readAll());
		
		
		
		
		Double total =new Double(0);
		for (ITableRecord item : mw_tbl_cart.getItems()) {
			String s = item.getTotal();
			Double total2 =new  Double(s);
		    total = total + total2;
		}
		
		mw_label_total_price.setText("�����: "+total);
		
		mw_tbl_warehouse.getItems().add(new dataObject("sdfsaf", "SASAD", "asdf", "sadf", "asfd"));
		//mw_tbl_warehouse_history.getItems().add(new Mw_tbl_warehouse_implementation("sdfsaf", "SASAD", "asdf", "sadf", "asfd"));
	}
	/*    Контекстное меню */
	// нажатие на кнопку добавить в накладную контекстного меню склада
	//TODO контроллер - таблица корзины - контекстное меню
	
	@FXML private TextField mw_context_menu_quantaty;
	@FXML private TextField mw_context_menu_price;
	@FXML private Label mw_context_menu_price_buy;
	@FXML private ContextMenu mw_warehouse_contmen;
	
	
	//TODO:  контекстное меню нажатие кнопки добавить
	@FXML public void mw_tbl_warehouse_context_addto_click() {
		try {
			dataObject cr = mw_tbl_warehouse.getSelectionModel().getSelectedItem();
			Double plus,minus,ravno;
			plus  = Double.parseDouble(cr.getQuantaty());
			minus = Double.parseDouble(mw_context_menu_quantaty.getText());
			ravno =plus - minus; 
			dataObject cartrecord = new dataObject(cr.getCrossID(), cr.getName(), cr.getSupplier(), cr.getQuantaty(),
					cr.getPrice(), cr.getPriceBuy());
			//mw_tbl_warehouse.getSelectionModel().getSelectedItem();
			cartrecord.setQuantaty(mw_context_menu_quantaty.getText());
			cartrecord.setPrice(mw_context_menu_price.getText());
			//cr.setQuantaty(String.valueOf(ravno));
			mw_tbl_cart.getItems().add(cartrecord);
			
			int i =1;
			mw_tbl_warehouse.getItems().remove(cr);
			cr.setQuantaty(String.valueOf(ravno));
			mw_tbl_warehouse.getItems().add(cr);
			
		} catch (Exception e) {
			
		}
		
		
	}
	
	//показывает меню контектное в таблице склада
	@FXML public void mw_context_menu_show() {
		
		try {
			dataObject cartrecord = mw_tbl_warehouse.getSelectionModel().getSelectedItem();
			mw_context_menu_quantaty.setText("1");
			mw_context_menu_price.setText(cartrecord.getPrice());
			mw_context_menu_price_buy.setText(cartrecord.getPriceBuy());
		} catch (Exception e) {
			mw_warehouse_contmen.hide();
		}
		
		
	}

	/*   !!!!!Контекстное меню!!!!   */
	
	/*        Кнопки корзины         */
	//TODO: Кнопка создание накладной
	@FXML public void mw_btn_order_add_click() {
		//добавляем данные в журнал
		if (journalReader.getInstance().addData(mw_tbl_cart.getItems(),"Продажа",mw_cart_paragonnum_label.getText())) {
			paragonCreater.getInstance().createParagon(mw_tbl_cart.getItems(),mw_cart_paragonnum_label.getText(),mw_cart_label_date.getText());
			warehouseReader.getInstance().updateData(mw_tbl_cart.getItems());
			String t = mw_cart_paragonnum_label.getText();
			int i = Integer.parseInt(t);
			i++;
			mw_cart_paragonnum_label.setText(String.valueOf(i));
			mw_tbl_cart.getItems().clear();
			AlertDlg.getInstance().OpenDialog(AlertType.INFORMATION, "данные добавлены в журнал", "Данные были успешно добавлены в журнал");
			
		}else {AlertDlg.getInstance().OpenDialog(AlertType.ERROR, "Не получилось добавить данные", "Не удалось добавить данные \n "
				+ "Проверьте не открыт ли  файлы журнала в других программах");}
	}
	/*   !!!!!Кнопки Корзины         */
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//init first table column
		mw_tbl_warehouse_ID.setCellValueFactory(new PropertyValueFactory<dataObject,String>("CrossID"));
		mw_tbl_warehouse_name.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Name"));
		mw_tbl_warehouse_supplier.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Supplier"));
		mw_tbl_warehouse_quantaty.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Quantaty"));
		mw_tbl_warehouse_price.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Price"));
		
		mw_tbl_warehouse_history_ID.setCellValueFactory(new PropertyValueFactory<dataObject,String>("CrossID"));
		mw_tbl_warehouse_history_name.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Name"));
		mw_tbl_warehouse_history_supplier.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Supplier"));
		mw_tbl_warehouse_history_quantaty.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Quantaty"));
		mw_tbl_warehouse_history_price.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Price"));
		
		mw_tbl_cart_ID.setCellValueFactory(new PropertyValueFactory<dataObject,String>("CrossID"));
		mw_tbl_cart_name.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Name"));
		mw_tbl_cart_quantaty.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Quantaty"));
		mw_tbl_cart_price.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Price"));
		mw_tbl_cart_total.setCellValueFactory(new PropertyValueFactory<dataObject,String>("Total"));
		
		//инициализация БД
		//подключение базы кроссов
		
		
		boolean ress =  aliasesReader.getInstance().openfile("d:\\bd\\кроссы\\бд.xls");
		
		boolean res22 = supplierReader.getInstance().openfile("d:\\bd\\поставщики\\");
		
		boolean res33 = warehouseReader.getInstance().openfile("d:\\bd\\склад\\склад.xls");
		
		boolean res44 = journalReader.getInstance().openfile("d:\\bd\\журнал\\журнал.xls");
		
		boolean res55 = paragonCreater.getInstance().openfile("d:\\bd\\накладные\\");
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		Date date = new Date();
		String data = date.toString();
		String   str = journalReader.getInstance().getLastParagonNum(dateFormat.format(date).toString());
		mw_cart_paragonnum_label.setText(str);
		mw_cart_label_date.setText(dateFormat.format(date).toString());
		//AlertDlg.getInstance().OpenDialog(AlertType.WARNING,  "xzcv");
		
		//journalReader.getInstance().test();
		//ObservableList<dataObject> list = new FXCollections.observableArrayList(new dataObject("d","d","d","d", "f"));
		//ObservableList<dataObject> list = FXCollections.observableArrayList();
		//list.add(new dataObject("d","d","d","5", "10"));
		//list.add(new dataObject("d","d","d","5.25", "10"));
		//journalReader.getInstance().addData(list,"Продажа","1");
		//journalReader.getInstance().addData(list,"Продажа","1");
		//paragonCreater.getInstance().createParagon(list, "2", "222");
		
		
		mf_btn_search.setOnKeyPressed(e -> {
			String s = mf_btn_search.getEditor().getText();			
			int e4 = 0;
			ArrayList<String> res = aliasesReader.getInstance().getSimilarArtNames(s);
			res.addAll(supplierReader.getInstance().getSimilarArtNames(s));
			res.addAll(warehouseReader.getInstance().getSimilarArtNames(s));
			res.addAll(journalReader.getInstance().getSimilarArtNames(s));
			res = toolProv.deleteDulicates(res);
			mf_btn_search.getItems().clear();
			mf_btn_search.getItems().addAll(res);
			mf_btn_search.getEditor().setText(s);
			mf_btn_search.show();
			System.out.println(res.toString());
			});
		mw_list_refs.setOnMouseClicked(e->{
			  if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2 ) {
				  	//TODO: обработчик двойного клика на списке кросов
				 AlertDlg.getInstance().OpenDialog(AlertType.WARNING, "sss");
				 String s =  mw_list_refs.getItems().get(mw_list_refs.getSelectionModel().getSelectedIndex());
				 String s1 = mf_btn_search.getEditor().getText();
				 mf_btn_search.getEditor().setText(s);
				 mw_list_refs.getItems().remove(s);
				 mw_list_refs.getItems().add(s1);
			         }  
		});

		//TODO союытие при добавлении данных в таблицу склада
		mw_tbl_warehouse.setRowFactory(tv -> new TableRow<dataObject>() {
		    @Override
		    public void updateItem(dataObject item, boolean empty) {
		        super.updateItem(item, empty) ;
		        if (item == null) {
		            setStyle("");
		        } else if (Double.parseDouble(item.getQuantaty())<=0) {
		            setStyle("-fx-background-color: red;");
		        } else {
		            setStyle("-fx-background-color: azure;");
		        }
		        
		    }
		});
		
		
		//TODO союытие при добавлении данных в таблицу поставщиков
		
		mw_tbl_cart.setRowFactory(tv -> new TableRow<dataObject>() {
		    @Override
		    public void updateItem(dataObject item, boolean empty) {
		        super.updateItem(item, empty) ;
		        Double d = new Double(0);
		        for (dataObject temp : mw_tbl_cart.getItems()) {
		        	d+= Double.parseDouble(temp.getTotal());
		        }
		        mw_lbl_cart_summ.setText(d.toString());
		    }
		});
		
		mw_tbl_warehouse_history.setRowFactory(tv -> new TableRow<dataObject>() {
		    @Override
		    public void updateItem(dataObject item, boolean empty) {
		        super.updateItem(item, empty) ;
		        if (item == null) {
		            setStyle("");
		        } else if (!(item.getSupplier().equals("история"))) {
		            setStyle("-fx-background-color: yellow;");
		        } else {
		            setStyle("-fx-background-color: green;");
		        }
		    }
		});
		
		SynchronizedListener.getInstance().registerEvent(new MyEventHandler() {
			// TODO: перелачапараметров с другого окна
			@Override
			public void fireEvent(ObservableList<journalObject> list) {
				mw_tbl_cart.getItems().clear();
				mw_tbl_cart.getItems().addAll(list);
				
			}
		});
		}
	  
	//TODO: кнопка корзина отложить
	@FXML void mw_btn_delay_clicked(){
		//добавляем данные в журнал
				if (journalReader.getInstance().addData(mw_tbl_cart.getItems(),"Отложено",mw_cart_paragonnum_label.getText())) {
					paragonCreater.getInstance().createParagon(mw_tbl_cart.getItems(),mw_cart_paragonnum_label.getText(),mw_cart_label_date.getText());
					warehouseReader.getInstance().updateData(mw_tbl_cart.getItems());
					String t = mw_cart_paragonnum_label.getText();
					int i = Integer.parseInt(t);
					i++;
					mw_cart_paragonnum_label.setText(String.valueOf(i));
					mw_tbl_cart.getItems().clear();
					AlertDlg.getInstance().OpenDialog(AlertType.INFORMATION, "данные добавлены в журнал", "Данные были успешно добавлены в журнал");
					
				}else {AlertDlg.getInstance().OpenDialog(AlertType.ERROR, "Не получилось добавить данные", "Не удалось добавить данные \n "
						+ "Проверьте не открыт ли  файлы журнала в других программах");}
				
			}
	//TODO ПУНКТЫ главного МЕНЮ 
	@FXML void  mw_journal_show(){
		try {
	        FXMLLoader fxmlLoader = new FXMLLoader();
	        fxmlLoader.setLocation(getClass().getResource("/view/journalWindow.fxml"));
	        /* 
	         * if "fx:controller" is not set in fxml
	         * fxmlLoader.setController(NewWindowController);
	         */
	        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
	        
	        Stage stage = new Stage();
	        stage.setTitle("журнал");
	        stage.setScene(scene);
	        stage.show();
	        //Dialog<R>
	    } catch (IOException e) {
	        System.out.println(e.toString());
	       
	    }
	
	}
	
	//           !!!!Пункты меню
	
}
