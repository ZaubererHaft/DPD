package detection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import entity.Classifier;
import pattern.PatternDefinition;

public class Paragraph {

	private PatternDefinition definition;
	private final Collection<ClassifierJoinedClassifier> result;

	public Paragraph(PatternDefinition definition, Collection<ClassifierJoinedClassifier> result) {
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

	public Collection<String> getLines() {

		List<String> lines = new LinkedList<String>();

		for (ClassifierJoinedClassifier joinedClassifier : result) {
			String line = joinedClassifier.getParent().getName();
			String children = joinedClassifier.getChildren().stream().map(Classifier::getName).reduce("",
					(a, b) -> a + "," + b);
			lines.add(line + children);
		}

		return lines;

	}

}
