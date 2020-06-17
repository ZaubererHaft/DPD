package detection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import pattern.Pattern;
import pattern.PatternDefinition;

public class Paragraph {

	private final PatternDefinition definition;
	private final Collection<Pattern> result;

	public Paragraph(PatternDefinition definition, Collection<Pattern> queryResults) {
		this.result = queryResults;
		this.definition = definition;
	}

	public int getCount() {
		return result.size();
	}

	public String getPattern() {
		return definition.getPatternName();
	}

	public String asText() {
		return getPattern() + " (" + getCount() + ")";
	}

	public Collection<String> getLines() {
		List<String> lines = new LinkedList();
		
		for (Pattern pattern : result) {
			lines.add(pattern.asText());
		}
		
		return lines;
	}

}
