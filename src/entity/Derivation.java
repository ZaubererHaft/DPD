package entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Derivation implements Serializable {

	private static final long serialVersionUID = 798444406265080395L;

	@Id
	@GeneratedValue
	private long id;

	@JoinColumn
	private Classifier source;

	@JoinColumn
	private Classifier target;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Classifier getSource() {
		return source;
	}

	public void setSource(Classifier source) {
		this.source = source;
	}

	public Classifier getTarget() {
		return target;
	}

	public void setTarget(Classifier target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "Subclass [id=" + id + ", source=" + source + ", target=" + target + "]";
	}

}
