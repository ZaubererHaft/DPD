package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Realization {
    @Id
    @GeneratedValue
    private long id;
    
    @JoinColumn
    private Class source;
    
    @JoinColumn
    private Interface target;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	public Class getSource() {
		return source;
	}

	public void setSource(Class source) {
		this.source = source;
	}

	public Interface getTarget() {
		return target;
	}

	public void setTarget(Interface target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "Realization [id=" + id + ", source=" + source + ", target=" + target + "]";
	}
    
}
