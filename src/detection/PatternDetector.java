package detection;

import java.util.Collection;

import pattern.PatternDefinition;

public class PatternDetector {
	
	private final Collection<PatternDefinition> definitions;

	public PatternDetector(Collection<PatternDefinition> definitions) {
		this.definitions = definitions;
	}
	
	public DetectionReport detect() {
		//1. establish database connection
		//2. run queries for all definitions
		//3. create and return report
		return new DetectionReport();
	}
}
