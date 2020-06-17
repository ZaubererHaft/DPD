package pattern.builder;

import pattern.Pattern;
import pattern.PatternBuilder;
import pattern.PatternDefinition;
import pattern.Role;

public class AbstractFactoryBuilder implements PatternBuilder {

	@Override
	public Pattern build(PatternDefinition definition, Object[] data) {
		Pattern p = new Pattern(definition);
		p.addRole(new Role(create(data, 0), "Factory")); 
		return p;
	}

}
