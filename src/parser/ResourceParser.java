package parser;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;

class ResourceParser {

	private final Resource resource;
	private final ResourceVisitor visitor;

	public ResourceParser(Resource resource, ResourceVisitor visitor) {
		this.resource = resource;
		this.visitor = visitor;
	}

	// ToDo: improve complexity
	/**
	 * parses the UML resources. by three steps <br />
	 * 1. parse interface 2. parse classes 3. parse relationships between classes
	 */
	public void parse() {

		Collection<EObject> contents = new ArrayList<>();
		resource.getAllContents().forEachRemaining(e -> contents.add(e));

		contents.stream().filter(obj -> UMLTypifier.isInterface(obj)).forEach(obj -> {
			visitor.visit((Interface) obj);
		});

		contents.stream().filter(obj -> UMLTypifier.isClass(obj)).forEach(obj -> {
			visitor.visit((org.eclipse.uml2.uml.Class) obj);
		});

		contents.stream().filter(o -> !UMLTypifier.isClass(o) && !UMLTypifier.isInterface(o)).forEach(obj -> {
			if (obj instanceof Property) {
				visitor.visit((Property) obj);
			} else if (obj instanceof Generalization) {
				visitor.visit((Generalization) obj);
			} else if (obj instanceof InterfaceRealization) {
				visitor.visit((InterfaceRealization) obj);
			} else if (obj instanceof Operation) {
				visitor.visit((Operation) obj);
			} else if (obj instanceof Behavior) {
				visitor.visit((Behavior) obj);
			}
		});
	}
}
