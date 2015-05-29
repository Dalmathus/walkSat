package SAT;

import java.util.*;

public class Model {

	// Model containing a truth value for each possible variable in a potentially satisfiable problem
	
	private int[] model;	// list containing truth values for each symbol
	private Random ran = new Random();
	
	public Model(int Size) {
		model = new int[Size];
	}
	
	/**
	 * Creates a list with c initial indexes
	 * @param c; number of clauses
	 */
	public Model(List<Clause> clauses) {
		List<Integer> test = new ArrayList<Integer>();
		
		for (Clause c : clauses){
			
			if (!test.contains(c.clause[0]) 
					&& !test.contains(-c.clause[0])) test.add(c.clause[0]);
			if (!test.contains(c.clause[1]) 
					&& !test.contains(-c.clause[1])) test.add(c.clause[1]);
			if (!test.contains(c.clause[2]) 
					&& !test.contains(-c.clause[2])) test.add(c.clause[2]);
		}
		
		model = new int[test.size()];
		
		for (int i = 0; i < model.length; i++){
			model[i] = test.get(i);
		}
		
		for (Integer j : model) {
			if (ran.nextInt(2) > 1) j = -j;
		}
	}
	
	/**
	 * Flip a symbols truth value inside a model
	 */
	public void flipSymbol(Integer s) {
		for (int i = 0; i < model.length; i++){
			if(model[i] == s || -model[i] == s) {
				model[i] = -(model[i]);
			}
		}
	}
	
	public void printModel(){
		System.out.println("True");
		for (int i : model){
			if (i > 0) System.out.println(i);
		}
		System.out.println("False");
		for (int i : model){
			if (i < 0) System.out.println(i);
		}
	}
	
	public void setList(int[] newL){
		model = newL;
	}
	
	public int[] getList(){
		return model;
	}
}