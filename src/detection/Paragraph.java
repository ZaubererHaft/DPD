package detection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import entity.Classifier;
import pattern.PatternDefinition;

public class Paragraph {

	private final PatternDefinition definition;
	private final Collection<String> result;

	public Paragraph(PatternDefinition definition, Collection<String> queryResults) {
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
		return result;
	}

}
