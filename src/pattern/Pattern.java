package pattern;

import java.util.Collection;
import java.util.LinkedList;


public class Pattern {

	private Collection<Role> roles;

	private PatternDefinition definition;

	public Pattern(PatternDefinition definition) {
		super();
		this.definition = definition;
		this.roles = new LinkedList<Role>();
	}

	public void addRole(Role role) {
		this.roles.add(role);
	}

	public String getName() {
		return definition.getPatternName();
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public String asText() {
		
		return roles.stream().map(r -> r.getName() + ":" + r.getClassifier().getName()).reduce((a,b) -> a + ", " + b).get();
	}

}
