package ui;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import parser.UMLParser;

public class Controller {

	@FXML
	private GridPane grid;
	@FXML
	private TextField textField;
	private File fileChosen;

	public void initialize() {
		this.addPatternsToList();

	}

	private void addPatternsToList() {
		// ToDo: use relative path
		File directory = new File("/home/ludwig/Repositories/Study/Seminar/code/eclipse-workspace/dpd/sql");

		int row = 0;
		for (File file : directory.listFiles()) {
			String name = file.getName();
			if (name.endsWith(".sql")) {
				CheckBox box = new CheckBox(name.substring(0, name.length() - 4));
				box.setSelected(true);
				grid.addRow(row, box);
			}
		}
	}

	@FXML
	private void selectUMLFile(ActionEvent event) {
		event.consume();

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("UML files", "*.uml", "*.xmi");
		fileChooser.getExtensionFilters().add(extFilter);

		this.fileChosen = fileChooser.showOpenDialog(grid.getScene().getWindow());
		if (fileChosen == null) {
			this.textField.setText("/path/to/uml");
		} else {
			this.textField.setText(fileChosen.getAbsolutePath());
		}
	}

	@FXML
	private void detectPatterns(ActionEvent event) {
		event.consume();

		if (fileChosen == null) {
			this.showErrorDialog("Please select a valid UML file");
		} else {
			try {
				UMLParser parser = new UMLParser(fileChosen.getAbsolutePath());
				parser.parse();
			} catch (Exception e) {
				this.showErrorDialog("Parsing data failed " + e);
			}
		}
	}

	private void showErrorDialog(String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText(content);
		alert.showAndWait();
	}
}