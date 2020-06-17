package detection;

import java.util.Collection;
import java.util.LinkedList;

import pattern.Pattern;
import pattern.PatternDefinition;

public class DetectionResult {
	private PatternDefinition definition;
	private Collection<Pattern> detectedPatterns;

	public DetectionResult(PatternDefinition definition) {
		this.definition = definition;
		this.detectedPatterns = new LinkedList<Pattern>();
	}

	public void addPatterns(Collection<Pattern> ps) {
		this.detectedPatterns.addAll(ps);
	}

	public PatternDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(PatternDefinition definition) {
		this.definition = definition;
	}

	public Collection<Pattern> getDetectedPatterns() {
		return detectedPatterns;
	}

	public void setDetectedPatterns(Collection<Pattern> detectedPatterns) {
		this.detectedPatterns = detectedPatterns;
	}
	
	
}
