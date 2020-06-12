package ui;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import detection.DetectionReport;
import detection.PatternDetector;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import log.Logger;
import parser.UMLParser;
import pattern.PatternDefinition;
import pattern.PatternDefinitionExtractor;

public class Controller {
	@FXML
	private GridPane metricsGrid;
	@FXML
	private TreeView<String> metricsReportTree;
	@FXML
	private GridPane grid;
	@FXML
	private TreeView<String> reportTree;
	@FXML
	private TextField textField;

	private File fileChosen;
	private final List<PatternDefinition> patternsToDetect;
	private DetectionReport lastReport;
	private UMLParser parser;

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

		if (fileChosen == null) {
			this.textField.setText("path/to/uml");
		} else {
			this.textField.setText(this.fileChosen.getAbsolutePath());

		}

	}

	@FXML
	private void detectPatterns(ActionEvent event) {
		event.consume();

		if (this.parser == null) {
			this.showErrorDialog("You have to parse a file before detecting patterns");
		} else if (patternsToDetect.size() <= 0) {
			this.showErrorDialog("Please select at least one pattern");
		} else {
			new Thread(() -> {
				this.executeDetection();
				Platform.runLater(() -> {
					this.readResult();
				});
			}).start();
		}
	}

	private void readResult() {
		TreeItem<String> rootItem = new TreeItem<String>("Report");
		rootItem.setExpanded(true);

		lastReport.getParagraphs().forEach(p -> {
			TreeItem<String> subItem = new TreeItem<String>(p.asText());

			p.getLines().forEach(l -> {
				TreeItem<String> subSubItem = new TreeItem<>(l);
				subItem.getChildren().add(subSubItem);
			});

			rootItem.getChildren().add(subItem);
		});

		reportTree.setRoot(rootItem);

	}

	private void executeDetection() {
		try {
			PatternDetector detector = new PatternDetector(patternsToDetect);
			lastReport = detector.detect();
		} catch (Exception e) {
			Logger.Error(e);
			this.showErrorDialog("detect patterns failed " + e);
		}

	}

	@FXML
	private void parseFile(ActionEvent event) {
		event.consume();

		if (this.fileChosen == null) {
			this.showErrorDialog("Please select a valid UML file");
		} else {

			if (this.parser != null) {
				if (!showYesNoDialog("Re-parse will delete all existing data")) {
					return;
				}
				this.parser.cleanUp();
			} else {
				this.parser = new UMLParser(fileChosen.getAbsolutePath());
			}

			new Thread(() -> {
				this.executeParsing();
			}).start();
		}

	}

	private void executeParsing() {
		try {
			this.parser.parse();

		} catch (Exception e) {
			Logger.Error(e);
			this.showErrorDialog("Parsing data failed " + e);
		}
	}

	private boolean showYesNoDialog(String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(content);
		alert.setContentText("Are you ok with this?");

		Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	private void showErrorDialog(String content) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText(content);
			alert.showAndWait();
		});
	}
}