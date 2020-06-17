package pattern;

public class DefaultPatternBuilder implements PatternBuilder {

	@Override
	public Pattern build(PatternDefinition definition, Object[] data) {

		Pattern pattern = new Pattern(definition);
		pattern.addRole(new Role(create(data, 0), "unknown"));

		return pattern;
	}

}
