package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Class 
{
    @Id
    @GeneratedValue
    private long id;
    
    @Column
    private String name;
    
    @Column
    @Enumerated(EnumType.STRING)
    private ClassType type;

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

	public ClassType getType() {
		return type;
	}

	public void setType(ClassType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Class [id=" + id + ", name=" + name + ", type=" + type + "]";
	}
    
	
    
}
