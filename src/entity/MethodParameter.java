package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class MethodParameter {
	@Id
	@GeneratedValue
	private long id;

	@Column
	private String name;

	@JoinColumn
	private Classifier classifier;

	@JoinColumn
	private Method method;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MethodParameter [id=" + id + ", name=" + name + ", classifier=" + classifier + ", method=" + method
				+ "]";
	}

}
