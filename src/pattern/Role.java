package pattern;

import entity.Classifier;

public class Role {
	private Classifier classifier;

	private String name;

	public Role(Classifier classifier, String name) {
		super();
		this.classifier = classifier;
		this.name = name;
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public String getName() {
		return name;
	}

}
