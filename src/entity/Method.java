package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Method implements Serializable {

	private static final long serialVersionUID = 2828270059312094308L;

	@Id
	@GeneratedValue
	private long id;

	@Column
	private String name;

	@Column
	private boolean isStatic;

	@JoinColumn
	private Classifier classifier;

	@Column
	@Enumerated(EnumType.STRING)
	private Visibility visibility;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	@Override
	public String toString() {
		return "Method [id=" + id + ", name=" + name + ", isStatic=" + isStatic + ", classifier=" + classifier + "]";
	}

}
