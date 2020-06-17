package detection;

import java.util.Collection;

public interface DetectionReport {

	String getHeadline();
	
	Collection<Paragraph> getParagraphs();

}
