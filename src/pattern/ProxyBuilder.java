package pattern;

import entity.Classifier;

public class ProxyBuilder implements PatternBuilder {

	@Override
	public Pattern build(PatternDefinition patternDefinition, Object[] data) {

		Pattern pattern = new Pattern(patternDefinition);

		Classifier subject = create(data, 0);
		Classifier proxy = create(data, 3);		

		pattern.addRole(new Role(subject, "Subject"));
		pattern.addRole(new Role(proxy, "Proxy"));

		return pattern;
	}

}
