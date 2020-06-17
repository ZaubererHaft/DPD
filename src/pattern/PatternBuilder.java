package pattern;

import entity.Classifier;
import entity.ClassifierType;

public interface PatternBuilder {
	Pattern build(PatternDefinition definition, Object[] data);

	default Classifier create(Object[] data, int index) {
		Classifier c = new Classifier();

		long id = (Long) data[index];
		String name = (String) data[index + 1];
		ClassifierType type = ClassifierType.valueOf((String) data[index + 2]);

		c.setId(id);
		c.setName(name);
		c.setType(type);
		
		return c;
	}
}
