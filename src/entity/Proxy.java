package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

public class Proxy {
	
	
	private Classifier subject;
	

	private Classifier proxy;
	

	private Classifier realSubject;
}
