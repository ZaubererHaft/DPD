package report.builder;

import pattern.Pattern;
import report.Line;

class PatternLine implements Line{

	private Pattern pattern;

	public PatternLine(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public String asText() {
		return pattern.asPatternText();
	}

}
