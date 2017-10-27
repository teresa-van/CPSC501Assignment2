/*
 * CPSC 501 Assignment #2
 * Written by: Teresa Van (10149274)
 * This class is a reflection object inspector that does a complete introspection of an object at runtime.
 * Created: October 26th, 2017
 * Last Modified: October 26th, 2017
 */

import java.util.*;
import java.lang.reflect.*;

public class Inspector {
	
	private static Queue<Class> ClassesToInspect = new ArrayDeque<Class>();
	private static Queue<Object> ObjectsToInspect = new ArrayDeque<Object>();
	private static ArrayList<Class> Inspected = new ArrayList<Class>();
	
	//Wrapper Class Check, Credit: https://stackoverflow.com/questions/709961/determining-if-an-object-is-of-primitive-type
	private static final Set<Class> WRAPPER_TYPES = new HashSet(Arrays.asList(
		    Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class));
	
	public static boolean isWrapperType(Class c) {
	    return WRAPPER_TYPES.contains(c);
	}
	//
	
	public Inspector() { }
	
	public void inspect(Object obj, boolean recursive)
	{
		Inspected.add(obj.getClass());
		
		Class objClass = obj.getClass();

		System.out.println("Name of declaring class: " + objClass.getName());
		System.out.println("Name of Immediate Superclass: " + objClass.getSuperclass().getName());
		if (!Inspected.contains(objClass.getSuperclass()) && !ClassesToInspect.contains(objClass.getSuperclass())) ClassesToInspect.add(objClass.getSuperclass());
		
		System.out.println("Name of Interfaces: ");
		Class[] interfaces = objClass.getInterfaces();
		if (interfaces.length == 0) System.out.println("\t No interfaces are implemented by this class.\n");
		else for (Class i : interfaces) System.out.println("\t " + i.getName());
		
		System.out.println("Methods the Class Declares: ");
		Method[] methods = objClass.getDeclaredMethods();
		if (methods.length == 0) System.out.println("\t No methods declared.\n");
		else
		{
			for (Method m : methods)
			{
				System.out.println("\t Method Name: " + m.getName());
				
				System.out.println("\t Exceptions Thrown: ");
				Class[] exceptions = m.getExceptionTypes();
				if (exceptions.length == 0) System.out.println("\t\t None");
				else for (Class e : exceptions) System.out.println("\t\t" + e.getName());
				
				System.out.println("\t Parameter Types: ");
				Parameter[] parameters = m.getParameters();
				if (parameters.length == 0) System.out.println("\t\t None");
				else for (Parameter p : parameters) System.out.println("\t\t" + p.getType().toString());
				
				System.out.println("\t Return Type: " + m.getReturnType());
				int mod = m.getModifiers();
				System.out.println("\t Modifiers: " + Modifier.toString(mod));
				System.out.println("\n");
			}
		}
		System.out.println("Constructions the Class Declares: ");
		Constructor[] constructors = objClass.getDeclaredConstructors();
		if (constructors.length == 0) System.out.println("\t No constructors declared.\n");
		else
		{
			for (Constructor c : constructors)
			{
				System.out.println("\t Constructor Name: " + c.getName());

				System.out.println("\t Parameter Types: ");
				Parameter[] parameters = c.getParameters();
				if (parameters.length == 0) System.out.println("\t\t None");
				else for (Parameter p : parameters) System.out.println("\t\t" + p.getType().toString());

				int mod = c.getModifiers();
				System.out.println("\t Modifiers: " + Modifier.toString(mod));
				System.out.println("\n");
			}
		}
		
		System.out.println("Fields the Class Declares: ");
		Field[] fields = objClass.getDeclaredFields();
		if (fields.length == 0) System.out.println("\t No fields declared.\n");
		else
		{
			for (Field f : fields)
			{
				System.out.println("\t Field Name: " + f.getName());

				System.out.println("\t Type: " + f.getType().toString());
				int mod = f.getModifiers();
				System.out.println("\t Modifiers: " + Modifier.toString(mod));
				System.out.println("\n");
			}
		}
		
		System.out.println("Current Value of Each Field: ");
		if (fields.length == 0) System.out.println("\t No fields.\n");
		else
		{
			for (Field f : fields)
			{
				try 
				{
					f.setAccessible(true);
					Object value = f.get(obj);
					try
					{
						if (value.getClass().isPrimitive() || isWrapperType(value.getClass())) 
							System.out.println("\t " + f.getName() + " = " + f.get(obj).toString());
						else if (value.getClass().isArray())
						{
							System.out.println("\t " + f.getName() + " = Array");
							System.out.println("\t\t Component Type: " + value.getClass().getComponentType());
							System.out.println("\t\t Length: " + Array.getLength(value));
							System.out.print("\t\t Contents: [");
							for (int i = 0; i < Array.getLength(value); i++)
							{
								if (i == Array.getLength(value)-1) System.out.print(Array.get(value, i));
								else System.out.print(Array.get(value, i) + ",");
							}
							System.out.println("]\n");
						}
						else
						{
							System.out.println("\t " + value.getClass().getName() + " " + f.hashCode());
							if (recursive) 
							{
								if (!Inspected.contains(value.getClass()) && !ObjectsToInspect.contains(value) && !ClassesToInspect.contains(value.getClass()))
								{
									ObjectsToInspect.add(value);
									Inspected.add(value.getClass());
								}
							}
						}
					}
					catch (NullPointerException e) { System.out.println("\t " + f.getName() + " = Null"); }
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
		System.out.println("\n\n\n");
		
		while (!ClassesToInspect.isEmpty())
		{
			if (ClassesToInspect.element().getName().equals("java.lang.Class") || 
					ClassesToInspect.element().getName().equals("java.lang.Object")) ClassesToInspect.remove();
			else
			{
				try
				{
					inspectClass(ClassesToInspect.remove(), recursive);
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
		while (!ObjectsToInspect.isEmpty())
		{
			if (ObjectsToInspect.element().getClass().getName().equals("java.lang.Class") || 
					ObjectsToInspect.element().getClass().getName().equals("java.lang.Object")) ObjectsToInspect.remove();
			else
			{
				try
				{
					inspect(ObjectsToInspect.remove(), recursive);
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
	}
	
	
	public void inspectClass(Class objClass, boolean recursive)
	{
		Inspected.add(objClass);
		
		System.out.println("Name of declaring class: " + objClass.getName());
		System.out.println("Name of Immediate Superclass: " + objClass.getSuperclass().getName());
		if (!Inspected.contains(objClass.getSuperclass()) && !ClassesToInspect.contains(objClass.getSuperclass())) ClassesToInspect.add(objClass.getSuperclass());
		
		System.out.println("Name of Interfaces: ");
		Class[] interfaces = objClass.getInterfaces();
		if (interfaces.length == 0) System.out.println("\t No interfaces are implemented by this class.\n");
		else for (Class i : interfaces) System.out.println("\t " + i.getName());
		
		System.out.println("Methods the Class Declares: ");
		Method[] methods = objClass.getDeclaredMethods();
		if (methods.length == 0) System.out.println("\t No methods declared.\n");
		else
		{
			for (Method m : methods)
			{
				System.out.println("\t Method Name: " + m.getName());
				
				System.out.println("\t Exceptions Thrown: ");
				Class[] exceptions = m.getExceptionTypes();
				if (exceptions.length == 0) System.out.println("\t\t None");
				else for (Class e : exceptions) System.out.println("\t\t" + e.getName());
				
				System.out.println("\t Parameter Types: ");
				Parameter[] parameters = m.getParameters();
				if (parameters.length == 0) System.out.println("\t\t None");
				else for (Parameter p : parameters) System.out.println("\t\t" + p.getType().toString());
				
				System.out.println("\t Return Type: " + m.getReturnType());
				int mod = m.getModifiers();
				System.out.println("\t Modifiers: " + Modifier.toString(mod));
				System.out.println("\n");
			}
		}
		System.out.println("Constructions the Class Declares: ");
		Constructor[] constructors = objClass.getDeclaredConstructors();
		if (constructors.length == 0) System.out.println("\t No constructors declared.\n");
		else
		{
			for (Constructor c : constructors)
			{
				System.out.println("\t Constructor Name: " + c.getName());

				System.out.println("\t Parameter Types: ");
				Parameter[] parameters = c.getParameters();
				if (parameters.length == 0) System.out.println("\t\t None");
				else for (Parameter p : parameters) System.out.println("\t\t" + p.getType().toString());

				int mod = c.getModifiers();
				System.out.println("\t Modifiers: " + Modifier.toString(mod));
				System.out.println("\n");
			}
		}
		
		System.out.println("Fields the Class Declares: ");
		Field[] fields = objClass.getDeclaredFields();
		if (fields.length == 0) System.out.println("\t No fields declared.\n");
		else
		{
			for (Field f : fields)
			{
				System.out.println("\t Field Name: " + f.getName());

				System.out.println("\t Type: " + f.getType().toString());
				int mod = f.getModifiers();
				System.out.println("\t Modifiers: " + Modifier.toString(mod));
				System.out.println("\n");
			}
		}
		System.out.println("\n\n\n");
	}
}
