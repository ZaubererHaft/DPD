package entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class MethodInvocation implements Serializable{
	
	private static final long serialVersionUID = 170139923088047413L;

	@Id
	@GeneratedValue
	private long id;

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

	@Override
	public String toString() {
		return "MethodInvocation [id=" + id + ", classifier=" + classifier + ", method=" + method + "]";
	}
	
	
}
