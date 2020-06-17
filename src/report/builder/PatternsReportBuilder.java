package report.builder;

import java.util.Collection;

import detection.DetectionResult;
import report.DetectionReport;

public class PatternsReportBuilder implements ReportBuilder{

	@Override
	public DetectionReport build(Collection<DetectionResult> detectionResult) {
		DefaultReport report = new DefaultReport();
		
		detectionResult.forEach(dr -> {
			PatternParagraph paragraph = new PatternParagraph(dr.getDefinition(), dr.getDetectedPatterns());
			report.addParagraph(paragraph);
		});
		
		return report;
	}

}
