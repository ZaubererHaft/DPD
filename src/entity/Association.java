package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Association implements Serializable {

	private static final long serialVersionUID = 2893976906743581556L;

	@Id
	@GeneratedValue
	private long id;

	@JoinColumn
	private Classifier source;

	@JoinColumn
	private Classifier target;

	@Column
	private String lowerMultiplicity;
	
	@Column
	private String upperMultiplicity;

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
	
	public String getLowerMultiplicity() {
		return lowerMultiplicity;
	}

	public void setLowerMultiplicity(String lowerMultiplicity) {
		this.lowerMultiplicity = lowerMultiplicity;
	}
	
	public String getUpperMultiplicity() {
		return upperMultiplicity;
	}

	public void setUpperMultiplicity(String upperMultiplicity) {
		this.upperMultiplicity = upperMultiplicity;
	}

	@Override
	public String toString() {
		return "Association [id=" + id + ", source=" + source + ", target=" + target + ", lowerMultiplicity="
				+ lowerMultiplicity + ", upperMultiplicity=" + upperMultiplicity + "]";
	}

}
