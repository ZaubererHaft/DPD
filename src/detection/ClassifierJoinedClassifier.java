package detection;

import java.util.LinkedList;
import java.util.List;

import entity.Classifier;

public class ClassifierJoinedClassifier {
	private Classifier parent;

	private List<Classifier> children;

	public ClassifierJoinedClassifier() {
		this.children = new LinkedList<Classifier>();
	}

	public Classifier getParent() {
		return parent;
	}

	public void setParent(Classifier parent) {
		this.parent = parent;
	}

	public List<Classifier> getChildren() {
		return new LinkedList<Classifier>(children);
	}

	public void addChild(Classifier child) {
		this.children.add(child);
	}

	@Override
	public String toString() {
		return "ClassifierJoinedClassifier [parent=" + parent + ", children=" + children + "]";
	}
	

}
