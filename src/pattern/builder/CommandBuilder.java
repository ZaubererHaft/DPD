package pattern.builder;

import pattern.Pattern;
import pattern.PatternBuilder;
import pattern.PatternDefinition;
import pattern.Role;

public class CommandBuilder implements PatternBuilder {
	
	@Override
	public Pattern build(PatternDefinition definition, Object[] data) {
		Pattern pattern = new Pattern(definition);

		pattern.addRole(new Role(create(data, 0), "Client"));
		pattern.addRole(new Role(create(data, 3), "Command Interface"));
		pattern.addRole(new Role(create(data, 6), "Receiver of implementation"));
		
		return pattern;
	}
}
