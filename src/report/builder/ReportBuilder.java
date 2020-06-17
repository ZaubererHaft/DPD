package report.builder;

import java.util.Collection;

import detection.DetectionResult;
import report.DetectionReport;

public interface ReportBuilder {
	DetectionReport build(Collection<DetectionResult> detectionResult);
}
