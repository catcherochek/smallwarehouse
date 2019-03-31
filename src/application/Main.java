package application;
	
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import view.*;


public class Main extends Application {
	@Override
	
	public void start(Stage primaryStage) {
		try {
			 Parent root = FXMLLoader.load(getClass()
	                    .getResource("/view/mainWindow.fxml"));
			 	
	            primaryStage.setTitle("My Application");
	            primaryStage.setScene(new Scene(root));
	            primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
