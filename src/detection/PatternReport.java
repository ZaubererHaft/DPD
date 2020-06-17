package detection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

class PatternReport implements DetectionReport {

	private List<PatternParagraph> paragraphs;

	public PatternReport() {
		this.paragraphs = new LinkedList<>();
	}

	public void addParagraph(PatternParagraph paragraph) {
		this.paragraphs.add(paragraph);
	}

	public Collection<Paragraph> getParagraphs()
	{
		return new LinkedList<>(paragraphs);
	}
	

	@Override
	public String getHeadline() {
		return "Report";
	}

}
