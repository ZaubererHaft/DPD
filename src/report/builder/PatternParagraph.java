package report.builder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import pattern.Pattern;
import pattern.PatternDefinition;
import report.Line;
import report.Paragraph;

class PatternParagraph implements Paragraph{

	private final PatternDefinition definition;
	private final Collection<Pattern> result;

	public PatternParagraph(PatternDefinition definition, Collection<Pattern> queryResults) {
		this.result = queryResults;
		this.definition = definition;
	}

	public int getCount() {
		return result.size();
	}

	public String getPattern() {
		return definition.getPatternName();
	}

	public Collection<Line> getLines() {
		List<Line> lines = new LinkedList<>();

		for (Pattern pattern : result) {
			lines.add(new PatternLine(pattern));
		} 

		return lines;
	}

	@Override
	public String getHeader() {
		return getPattern() + " (" + getCount() + ")";
	}

}
