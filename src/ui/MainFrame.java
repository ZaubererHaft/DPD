package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import log.Logger;

public class MainFrame extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Logger.Info("starting main frame...");
		
		Parent root = FXMLLoader.load(getClass().getResource("mainframe.fxml"));
		primaryStage.setTitle("Design Pattern Detector");
		primaryStage.setScene(new Scene(root, 400, 300));
		primaryStage.show();
		
		Logger.Info("done");
	}

	public static void main(String[] args) {
		launch(args);
	}

}