package detection;

class Paragraph {
	private final String pattern;
	private final int count;

	public Paragraph(String pattern, int count) {
		this.pattern = pattern;
		this.count = count;
	}

	public String asText() {
		return pattern + " pattern: " + count;
	}
	
}
