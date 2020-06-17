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
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
	private TabPane tabPane;
	@FXML
	private GridPane metricsGrid;
	@FXML
	private GridPane patternGrid;
	@FXML
	private TreeView<String> reportTree;
	@FXML
	private TextField textField;
	@FXML
	private ComboBox<String> groupBox;

	private File fileChosen;
	private final List<PatternDefinition> patternsToDetect;
	private final List<PatternDefinition> metricsToDetect;
	private DetectionReport lastReport;
	private UMLParser parser;

	public Controller() {
		patternsToDetect = new LinkedList<PatternDefinition>();
		metricsToDetect = new LinkedList<PatternDefinition>();
	}

	public void initialize() {
		this.addPatternsToList();
		this.addMetricsToList();
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
			patternGrid.addRow(row, box);

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

	private void addMetricsToList() {
		File directory = new File(
				"/home/ludwig/Repositories/Study/Seminar/code/eclipse-workspace/dpd/definitions/metrics");

		PatternDefinitionExtractor extractor = new PatternDefinitionExtractor(directory);
		Collection<PatternDefinition> definitions = extractor.extractDefinitions();

		int row = 0;
		for (PatternDefinition pattern : definitions) {
			CheckBox box = new CheckBox(pattern.getPatternName());
			box.setSelected(true);
			metricsGrid.addRow(row, box);

			metricsToDetect.add(pattern);
			box.setOnAction(event -> {
				event.consume();
				if (box.isSelected()) {
					metricsToDetect.add(pattern);
				} else {
					metricsToDetect.remove(pattern);
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
		this.fileChosen = fileChooser.showOpenDialog(tabPane.getScene().getWindow());

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

		Collection<PatternDefinition> defintions;
		if (isPatternTab()) {
			defintions = this.patternsToDetect;
		} else {
			defintions = this.metricsToDetect;
		}

		if (defintions.size() <= 0) {
			this.showErrorDialog("Please select at least one detection definition");
		} else {
			new Thread(() -> {
				this.executeDetection(defintions);
				Platform.runLater(() -> {
					this.readResult();
				});
			}).start();
		}
	}

	private boolean isPatternTab() {
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		return selectionModel.getSelectedIndex() <= 0;
	}

	private void readResult() {

		TreeItem<String> rootItem = new TreeItem<String>(this.lastReport.getHeadline());
		rootItem.setExpanded(true);

		if (this.groupBox.getValue().equals("Patterns")) {
			lastReport.getParagraphs().forEach(p -> {
				TreeItem<String> subItem = new TreeItem<String>(p.getHeader());

				p.getLines().forEach(l -> {
					TreeItem<String> subSubItem = new TreeItem<>(l.asText());
					subItem.getChildren().add(subSubItem);
				});

				rootItem.getChildren().add(subItem);
			});
		}
		else
		{
			lastReport.getParagraphs().forEach(p -> {
				TreeItem<String> subItem = new TreeItem<String>(p.getHeader());

				p.getLines().forEach(l -> {
					TreeItem<String> subSubItem = new TreeItem<>(l.asText());
					subItem.getChildren().add(subSubItem);
				});

				rootItem.getChildren().add(subItem);
			});
		}

		reportTree.setRoot(rootItem);

	}

	private void executeDetection(Collection<PatternDefinition> defintions) {
		try {
			PatternDetector detector = new PatternDetector(defintions);
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
			}

			this.parser = new UMLParser(fileChosen.getAbsolutePath());

			new Thread(() -> {
				this.executeParsing();
			}).start();
		}

	}

	@FXML
	private void comboAction(ActionEvent event) {

		event.consume();

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