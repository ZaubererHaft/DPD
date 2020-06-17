package report.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import detection.DetectionResult;
import entity.Classifier;
import report.DetectionReport;

public class ClassesReportBuilder implements ReportBuilder {

	@Override
	public DetectionReport build(Collection<DetectionResult> detectionResult) {
		DefaultReport report = new DefaultReport();

		Map<Classifier, ClassParagraph> classesToParagraph = new HashMap<>();

		detectionResult.forEach(result -> {

			result.getDetectedPatterns().forEach(pattern -> {

				pattern.getRoles().forEach(role -> {
					Classifier classifier = role.getClassifier();

					ClassParagraph paragraph = classesToParagraph.get(classifier);
					if (paragraph == null) {
						paragraph = new ClassParagraph(classifier);
						classesToParagraph.put(classifier, paragraph);
						report.addParagraph(paragraph);
					}

					Classline line = new Classline(pattern, role);
					paragraph.addLine(line);

				});
			});
		});

		return report;
	}

}
