package detection;

import java.util.Collection;


public interface Paragraph {
	
	String getHeader();
	
	Collection<Line> getLines();
}
