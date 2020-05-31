package detection;

import java.util.Collection;

import pattern.PatternDefinition;

public class Detector {
	
	private final Collection<PatternDefinition> definitions;

	public Detector(Collection<PatternDefinition> definitions) {
		this.definitions = definitions;
	}
	
	public DetectionReport detect() {
		return new DetectionReport();
	}
}
