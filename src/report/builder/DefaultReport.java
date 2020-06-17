package report.builder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import report.DetectionReport;
import report.Paragraph;

 class DefaultReport implements DetectionReport {

	private List<Paragraph> paragraphs;

	public DefaultReport() {
		this.paragraphs = new LinkedList<>();
	}

	public void addParagraph(Paragraph paragraph) {
		this.paragraphs.add(paragraph);
	}

	public Collection<Paragraph> getParagraphs() {
		return new LinkedList<>(paragraphs);
	}

	@Override
	public String getHeadline() {
		return "Report";
	}

}
