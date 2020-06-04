package detection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import entity.ClassifierJoinedClassifier;
import pattern.PatternDefinition;

public class Paragraph {

	private PatternDefinition definition;
	private final List<ClassifierJoinedClassifier> result;

	public Paragraph(PatternDefinition definition, List<ClassifierJoinedClassifier> result) {
		this.result = result;
		this.definition = definition;
	}

	public int getCount() {
		return result.size();
	}

	public String getPattern() {
		return definition.getPatternName();
	}

	public String asText() {
		return getPattern() + " pattern: " + getCount();
	}
	
	public Collection<String> getLines()
	{
		List<String> lines = new LinkedList<>();
		
		result.forEach(r -> 
		{
			lines.add(r.getParent().getName() + " and " + r.getChild().getName());
		});
		
		return lines;
	}

}
