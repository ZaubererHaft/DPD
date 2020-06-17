package pattern.builder;

import pattern.Pattern;
import pattern.PatternBuilder;
import pattern.PatternDefinition;
import pattern.Role;

public class MetricsBuilder implements PatternBuilder {

	@Override
	public Pattern build(PatternDefinition definition, Object[] data) {

		Pattern pattern = new Pattern(definition);

		Long count = (Long) data[3];
		pattern.addRole(new Role(create(data, 0), count + " method invocations"));

		return pattern;
	}

}
