package pattern;

public class PatternDefinition {

	private String patternName;
	private String query;

	public String getPatternName() {
		return patternName;
	}

	public void setPatternName(String patterName) {
		patternName = patterName;
	}

	public String getNamedQuery() {
		return query;
	}

	public void setNamedQuery(String query) {
		this.query = query;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((patternName == null) ? 0 : patternName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatternDefinition other = (PatternDefinition) obj;
		if (patternName == null) {
			if (other.patternName != null)
				return false;
		} else if (!patternName.equals(other.patternName))
			return false;
		return true;
	}
	

}
