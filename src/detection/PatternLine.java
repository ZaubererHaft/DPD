package detection;

import pattern.Pattern;

public class PatternLine implements Line{

	private Pattern pattern;

	public PatternLine(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public String asText() {
		return pattern.asPatternText();
	}

}
