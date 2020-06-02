package parser;

import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;

public interface ResourceVisitor {
	
	public void visit(Property umlProperty);
	
	public void visit(Generalization umnlGeneralization);
	
	public void visit(org.eclipse.uml2.uml.Class umlClass);
	
	public void visit(Interface umlInterface);
	
	public void visit(InterfaceRealization umInterfaceRealization);

	public void visit(Operation operation);

	public void visit(OpaqueBehavior behavior);
}
