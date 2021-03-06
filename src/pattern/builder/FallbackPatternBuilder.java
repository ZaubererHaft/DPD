package pattern.builder;

import entity.Classifier;
import pattern.Pattern;
import pattern.PatternBuilder;
import pattern.PatternDefinition;
import pattern.Role;

public class FallbackPatternBuilder implements PatternBuilder {

	@Override
	public Pattern build(PatternDefinition definition, Object[] data) {

		Pattern pattern = new Pattern(definition);
		
		String s = "";
		for (Object object : data) {
			s += data.toString() + " ";
		}
		
		Classifier  c= new Classifier();
		
		pattern.addRole(new Role(c, s));

		return pattern;
	}

}
