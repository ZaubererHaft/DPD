package detection;

import java.util.LinkedList;
import java.util.List;

public class DetectionReport {

	private List<Paragraph> paragraphs;

	public DetectionReport() {
		this.paragraphs = new LinkedList<>();
	}

	public void addParagraph(Paragraph paragraph) {
		this.paragraphs.add(paragraph);
	}

	public String asText() {
		return paragraphs.stream().map(p -> p.asText()).reduce("", (a, b) -> a + "\n" + b);
	}

}
