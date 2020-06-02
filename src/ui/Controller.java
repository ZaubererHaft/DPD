package ui;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import detection.DetectionReport;
import detection.PatternDetector;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import parser.UMLParser;
import pattern.PatternDefinition;
import pattern.PatternDefinitionExtractor;

public class Controller {

	@FXML
	private GridPane grid;
	@FXML
	private Label reportLabel;
	@FXML
	private TextField textField;

	private File fileChosen;
	private final List<PatternDefinition> patternsToDetect;
	private DetectionReport lastReport;
	private boolean parsed;

	public Controller() {
		patternsToDetect = new LinkedList<PatternDefinition>();
	}

	public void initialize() {
		this.addPatternsToList();
	}

	private void addPatternsToList() {
		// ToDo: use relative path
		File directory = new File("/home/ludwig/Repositories/Study/Seminar/code/eclipse-workspace/dpd/definitions");

		PatternDefinitionExtractor extractor = new PatternDefinitionExtractor(directory);
		Collection<PatternDefinition> definitions = extractor.extractDefinitions();

		int row = 0;
		for (PatternDefinition pattern : definitions) {
			CheckBox box = new CheckBox(pattern.getPatternName());
			box.setSelected(true);
			grid.addRow(row, box);

			patternsToDetect.add(pattern);
			box.setOnAction(event -> {
				event.consume();
				if (box.isSelected()) {
					patternsToDetect.add(pattern);
				} else {
					patternsToDetect.remove(pattern);
				}
			});

			row++;
		}
	}

	@FXML
	private void selectUMLFile(ActionEvent event) {
		event.consume();

		this.parsed = false;
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
		} else if (patternsToDetect.size() <= 0) {
			this.showErrorDialog("Please select at least one pattern");
		} else {
			this.reportLabel.setText("loading...");

			new Thread(() -> {
				this.executeDetection();
				Platform.runLater(() -> {
					this.reportLabel.setText(lastReport.asText());
				});
			}).start();
		}
	}

	private void executeDetection() {
		try {

			if (!parsed) {
				UMLParser parser = new UMLParser(fileChosen.getAbsolutePath());
				parser.parse();
				parsed = true;
			}
			PatternDetector detector = new PatternDetector(patternsToDetect);
			lastReport = detector.detect();

		} catch (Exception e) {
			this.showErrorDialog("Parsing data failed " + e);
		}
	}

	private void showErrorDialog(String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText(content);
		alert.showAndWait();
	}
}