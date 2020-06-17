package report.builder;

import pattern.Pattern;
import pattern.Role;
import report.Line;

class Classline implements Line {

	private Role role;
	private Pattern pattern;

	public Classline(Pattern pattern, Role role) {
		this.pattern = pattern;
		this.role = role;
	}

	@Override
	public String asText() {
		return pattern.getName() + " (role: " + role.getName() + ")";
	}

}
