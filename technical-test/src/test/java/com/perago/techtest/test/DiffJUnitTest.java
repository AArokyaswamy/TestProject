package com.perago.techtest.test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.perago.techtest.Diff;
import com.perago.techtest.DiffEngine;
import com.perago.techtest.DiffException;
import com.perago.techtest.DiffRenderer;
import com.perago.techtest.DiffRendererService;
import com.perago.techtest.DiffService;

public class DiffJUnitTest {

	@Test
	public void test() {

		DiffEngine diffEngine = new DiffService();
		DiffRenderer diffRenderer = new DiffRendererService();
		try {

			// Create 1st Person object
			Person p1 = createPerson1();

			// Create 2nd Person Object

			Person p2 = createPerson2();

			// Updated Scenario
			System.out.println(" ===================================== (1) Updated Scenario ===================================");
			Diff<Person> p3 = diffEngine.calculate(p1, p2);

			System.out.println("Object p3(from calculate  method) is Diff of Original Object p1 and Modified Object p2 ");

			Diff<Person> p4 = diffEngine.apply(p1, (Diff) p3);
			assertEquals( p2,p4 );
			if (p2.equals(p4)) {
				System.out.println("Object p4(from Apply  method)  is same as Modified Object p2 ");

			}

			// Diff Renderer
			String diffRenderString = diffRenderer.render(p3);
			assertEquals( "updated", diffRenderString );

			displayObjectsCompare(p1, p2, p3, diffRenderString);

			// Created Scenario
			System.out.println("  ");
			System.out.println(" ===================================== (2) Created Scenario ===================================");
			p1 = null;
			Diff<Person> p5 = diffEngine.calculate(p1, p2);

			System.out
					.println("Diff Object p5(from calculate  method) is Diff of Original Object p1(null) and Modified Object p2 ");

			Diff<Person> p6 = diffEngine.apply(p1, (Diff) p5);
			
			if (p1 == null && p6.equals(p2)) {
				System.out
						.println("Object p6(from Apply  method)  is same as Modified Object p2 ");

			}

			// Diff Renderer
			diffRenderString = diffRenderer.render(p5);
			assertEquals( "created", diffRenderString );

			displayObjectsCompare(p1, p2, p5, diffRenderString);
			
			// Deleted Scenario
			System.out.println("  ");
			System.out.println(" ===================================== (3) Deleted Scenario ===================================");
			p1 = createPerson1(); //as p1 assigned null previously
			p2=null;
			Diff<Person> p7 = diffEngine.calculate(p1, p2);
			diffRenderString = diffRenderer.render(p7);
			assertEquals( "deleted", diffRenderString );

	        System.out.println("Diff Object p7(from calculate  method) is Diff of Original Object p1 and Modified Object p2(null) ");
	


			Diff<Person> p8 = diffEngine.apply(p1,p7);
			
			if (p2 == null && p8==null) {
				System.out.println("Object p8(from Apply  method)  is same as Modified Object p2 ");
		
			}



			displayObjectsCompare(p1, p2, p7, diffRenderString);


		} catch (DiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	private Person createPerson1() {
		Person friend1 = new Person();
		friend1.setFirstName("Harry");
		friend1.setSurname("Smith");

		Set<String> nickNames1 = new HashSet();
		String[] array = { "MrA", "MrB", "MrC" };
		Collections.addAll(nickNames1, array);

		Person p1 = new Person();
		p1.setFirstName("Damir");
		p1.setSurname("Smith");
		p1.setFriend(friend1);
		p1.setNickNames(nickNames1);
		return p1;
	}
	
	private Person createPerson2() {
		Set<String> nickNames2 = new HashSet();
		String[] array2 = { "MrD", "MrE", "MrF" };
		Collections.addAll(nickNames2, array2);

		Person friend2 = new Person();
		friend2.setFirstName("Jimmy");
		friend2.setSurname("kheer");

		Person p2 = new Person();
		p2.setFirstName("Chris");
		p2.setSurname("Steyn");
		p2.setFriend(friend2);
		p2.setNickNames(nickNames2);
		return p2;
	}

	private void displayObjectsCompare(Diff<Person> p1, Diff<Person> p2,
			Diff<Person> p3, String diffRenderString) {
		System.out.println("");
		System.out.println("                   ----------------------------------------------------------          ");
		System.out.println("                   " + "First Name   |" + " Suraname  |"+ "     Nick Names  |" + "        Friend ");
		System.out.println("                   ----------------------------------------------------------          ");
		
		if (p1 == null)
			System.out.println("Original Object      NULL   ");
		else if (p1 != null)
			System.out.println("Original object      " + ((Person) p1).getFirstName()
					+ "         " + ((Person) p1).getSurname() + "       "
					+ ((Person) p1).getNickNames() + "     "
					+ ((Person) p1).getFriend().getFirstName() + " "
					+ ((Person) p1).getFriend().getSurname());
		
		if (p2 == null)
			System.out.println("Modified Object      NULL   ");
		else if (p2 != null)
			System.out.println("Modified Object      " + ((Person) p2).getFirstName()
					+ "         " + ((Person) p2).getSurname() + "       "
					+ ((Person) p2).getNickNames() + "     "
					+ ((Person) p2).getFriend().getFirstName() + " "
					+ ((Person) p2).getFriend().getSurname());
		
		if (p3 == null)
			System.out.println("Diff Object          NULL   ");
		if (p3 != null && diffRenderString.equals("deleted"))
			System.out.println("Diff Object          NULL   ");
		else if (p3 != null)
			System.out.println("Diff Object(" + diffRenderString + ") "
					+ ((Person) p3).getFirstName() + "         "
					+ ((Person) p3).getSurname() + "       "
					+ ((Person) p3).getNickNames() + "     "
					+ ((Person) p3).getFriend().getFirstName() + " "
					+ ((Person) p3).getFriend().getSurname());

	}

}
