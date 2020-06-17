package pattern;

import java.lang.reflect.Constructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties()
public class PatternDefinition {

	private String patternName;
	private String query;
	private String builder;

	public String getPatternName() {
		return patternName;
	}

	public void setPatternName(String patternName) {
		this.patternName = patternName;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getBuilder() {
		return builder;
	}

	public void setBuilder(String builder) {
		this.builder = builder;
	}

	public PatternBuilder createBuilder() {

		if (builder == null) {
			return new DefaultPatternBuilder();
		}

		try {
			Constructor<?> constructor = Class.forName(builder).getConstructor();
			return (PatternBuilder) constructor.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}

}
