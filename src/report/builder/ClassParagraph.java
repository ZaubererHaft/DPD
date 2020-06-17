package report.builder;

import java.util.Collection;
import java.util.LinkedList;

import entity.Classifier;
import report.Line;
import report.Paragraph;

class ClassParagraph implements Paragraph {
	private Classifier classifier;
	private Collection<Line> lines;
	
	public ClassParagraph(Classifier classifier) {
		this.classifier = classifier;
		this.lines = new LinkedList<Line>();
	}

	@Override
	public String getHeader() {
		return classifier.getName();
	}

	@Override
	public Collection<Line> getLines() {
		return this.lines;
	}

	public void addLine(Line line) {
		this.lines.add(line);
		
	}
}
