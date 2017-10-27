import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class UnitTests {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	@Test
	public void testClassAInterfaces() {
	    System.setOut(new PrintStream(outContent));
		try
	    {
			Inspector inspector = new Inspector();
			Object a = new ClassA();
			inspector.InspectInterfaces(a.getClass());
	    }
		catch(Exception e)
	    {		
			System.out.println("ERROR: " + e.getMessage());
			System.out.println("Exiting test driver");
	    }
		String expected = "Name of Implemented Interfaces: " + 
				    		" java.io.Serializable" + 
				    		" java.lang.Runnable";
		expected = expected.replaceAll("\\s", "");
		String result = outContent.toString().replaceAll("\\s","");
		result = result.replaceAll("\n", "");
		expected = expected.replaceAll("\n", "");
		result = result.replaceAll("\t", "");
		expected = expected.replaceAll("\t", "");
		
	    assertEquals(expected, result);
	    System.setOut(null);
	}
	
	@Test
	public void testClassAConstructors() {
	    System.setOut(new PrintStream(outContent));
		try
	    {
			Inspector inspector = new Inspector();
			Object a = new ClassA();
			inspector.InspectConstructors(a.getClass());
	    }
		catch(Exception e)
	    {		
			System.out.println("ERROR: " + e.getMessage());
			System.out.println("Exiting test driver");
	    }
		String expected = "Constructions the Class Declares:" + 
				"         Constructor Name: ClassA" + 
				"         Parameter Types:" + 
				"                 None" + 
				"         Modifiers: public" + 
				"         Constructor Name: ClassA" + 
				"         Parameter Types:" + 
				"                 int" + 
				"         Modifiers: public";
		expected = expected.replaceAll("\\s", "");
		String result = outContent.toString().replaceAll("\\s","");
		result = result.replaceAll("\n", "");
		expected = expected.replaceAll("\n", "");
		result = result.replaceAll("\t", "");
		expected = expected.replaceAll("\t", "");
		
	    assertEquals(expected, result);
	    System.setOut(null);
	}

	@Test
	public void testClassAFields() {
	    System.setOut(new PrintStream(outContent));
		try
	    {
			Inspector inspector = new Inspector();
			Object a = new ClassA();
			inspector.InspectFields(a.getClass());
	    }
		catch(Exception e)
	    {		
			System.out.println("ERROR: " + e.getMessage());
			System.out.println("Exiting test driver");
	    }
		String expected = "Fields the Class Declares:" + 
				"         Field Name: val" + 
				"         Type: int" + 
				"         Modifiers: private" + 
				"         Field Name: val2" + 
				"         Type: double" + 
				"         Modifiers: private" + 
				"         Field Name: val3" + 
				"         Type: boolean" + 
				"         Modifiers: private" + 
				"         Field Name: val4" + 
				"         Type: class [I" + 
				"         Modifiers: private";
		expected = expected.replaceAll("\\s", "");
		String result = outContent.toString().replaceAll("\\s","");
		result = result.replaceAll("\n", "");
		expected = expected.replaceAll("\n", "");
		result = result.replaceAll("\t", "");
		expected = expected.replaceAll("\t", "");
		
	    assertEquals(expected, result);
	    System.setOut(null);
	}

	@Test
	public void testClassAFieldsValues() {
	    System.setOut(new PrintStream(outContent));
		try
	    {
			Inspector inspector = new Inspector();
			Object a = new ClassA();
			inspector.InspectFieldValues(a.getClass(), a, true);
	    }
		catch(Exception e)
	    {		
			System.out.println("ERROR: " + e.getMessage());
			System.out.println("Exiting test driver");
	    }
		String expected = "Current Value of Each Field:" + 
				"         val = 3" + 
				"         val2 = 0.2" + 
				"         val3 = true" + 
				"         val4 = Array" + 
				"                 Component Type: int" + 
				"                 Length: 4" + 
				"                 Contents: [1,2,3,4]";
		expected = expected.replaceAll("\\s", "");
		String result = outContent.toString().replaceAll("\\s","");
		result = result.replaceAll("\n", "");
		expected = expected.replaceAll("\n", "");
		result = result.replaceAll("\t", "");
		expected = expected.replaceAll("\t", "");
		
	    assertEquals(expected, result);
	    System.setOut(null);
	}
}
