package pattern.builder;

import pattern.Pattern;
import pattern.PatternBuilder;
import pattern.PatternDefinition;
import pattern.Role;

public class BridgeBuilder implements PatternBuilder{

	@Override
	public Pattern build(PatternDefinition definition, Object[] data) {
		Pattern pattern = new Pattern(definition);

		pattern.addRole(new Role(create(data, 0), "Abstraction"));
		pattern.addRole(new Role(create(data, 3), "Implementation"));

		return pattern;
	}

}
