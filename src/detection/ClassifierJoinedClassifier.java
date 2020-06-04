package detection;

import java.util.LinkedList;
import java.util.List;

import entity.Classifier;

public class ClassifierJoinedClassifier {

	private List<Classifier> children;

	public ClassifierJoinedClassifier() {
		this.children = new LinkedList<Classifier>();
	}

	public List<Classifier> getChildren() {
		return new LinkedList<Classifier>(children);
	}

	public void addChild(Classifier child) {
		this.children.add(child);
	}


}
