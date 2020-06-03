package ui;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

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
import log.Logger;
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

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("UML files", "*.uml", "*.xmi");
		fileChooser.getExtensionFilters().add(extFilter);

		if (this.fileChosen != null) {
			fileChooser.setInitialDirectory(this.fileChosen.getParentFile());
		}

		File oldFile = this.fileChosen;
		this.fileChosen = fileChooser.showOpenDialog(grid.getScene().getWindow());
		
		if (this.fileChosen == null) {
			this.fileChosen = oldFile;
		}

		this.textField.setText(this.fileChosen.getAbsolutePath());

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
			UMLParser parser = new UMLParser(fileChosen.getAbsolutePath());
			parser.parse();

			PatternDetector detector = new PatternDetector(patternsToDetect);
			lastReport = detector.detect();

		} catch (Exception e) {
			Logger.Error(e);
			Platform.runLater(() -> {
				this.showErrorDialog("Parsing data failed " + e);
				this.reportLabel.setText("");
			});
		}
	}

	private void showErrorDialog(String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText(content);
		alert.showAndWait();
	}
}