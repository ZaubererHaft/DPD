package parser;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;

class UMLTypifier {
	
	
	public static boolean isClass(EObject obj)
	{
		return obj instanceof org.eclipse.uml2.uml.Class && !(obj instanceof Behavior);
	}
	
	public static boolean isInterface(EObject obj)
	{
		return obj instanceof Interface;
	}
	
	public static boolean isProperty(EObject obj)
	{
		return obj instanceof Property;
	}
	
	public static boolean isMethod(EObject obj)
	{
		return obj instanceof Operation;
	}
}
