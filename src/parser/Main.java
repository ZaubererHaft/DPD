package parser;

public class Main {

	public static void main(String[] args) {
		UMLParser parser = new UMLParser("/home/ludwig/Repositories/Study/Seminar/code/workspace-papyrus/Program/test.uml");
		parser.parse();
	}

}
