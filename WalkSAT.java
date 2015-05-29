package SAT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WalkSAT {

	Random randomGenerator = new Random();

	public WalkSAT() {}
	// WalkSAT loop
	public Model loop(List<Clause> clauses, double p, int max){
		
		
		
		Model model = new Model(clauses);
		Clause c; 	// random clause from inside clauses
		int s;

		for (int i = 1; i <= max; i++){
			// If model satisfies clauses then return model
			
			if (i % 5000 == 0) System.out.println("Flips: " + i);
			
			if (satisfiable(model, clauses)) {
				System.out.println("Flips: " + i);
				printFinal(model, clauses);
				return model;
			}

			// Get a random clause and symbol from that clause
			c = getRandom(clauses, model.getList());
			s = c.getRandom();
			int chance = randomGenerator.nextInt(2);

			// flip a random symbol inside model to shake it up		
			if (chance < p) { 
				model.flipSymbol(s); 
			}
			// flip the best possible symbol to produce the maximum positive clauses
			else { 
				bestMoveBetter(model, clauses, c);
				//bestMove(model, clauses);
			}			
		}		
		return null;		
	}

	/**
	 * Tests if current model provides a solution
	 * 
	 * @param m current model
	 * @param c list of clauses
	 * @return true if cnf is true
	 */
	private boolean satisfiable(Model model, List<Clause> clauses){

		for (Clause c : clauses){
			if (!c.evaluate(model.getList())) return false;
		}		
		return true;
	}

	private void printFinal(Model model, List<Clause> clauses) {

		int a = 0;
		int b = 0;
		int c = 0;

		for (Clause cl : clauses){
			for (Integer i : model.getList()){
				if (i == cl.clause[0] || i == -cl.clause[0]) { 
					if (cl.clause[0] < 0)
						a = -i;
					else
						a = i;
				}
				if (i == cl.clause[1] || i == -cl.clause[1]) {
					if (cl.clause[1] < 0)
						b = -i;
					else
						b = i; }
				if (i == cl.clause[2] || i == -cl.clause[2]) { 
					if (cl.clause[2] < 0)
						c = -i;
					else
						c = i; }
			}

			if (a > 0 || b > 0 || c > 0) {
				//cl.printClause();
				System.out.println("(" + a + " v " + b + " v " + c + ")" + " -> True");
			}
			else {
				System.out.println("(" + a + " v " + b + " v " + c + ")" + " -> False");
			}	
		}
	}
	
	/**
	 * the actual method described in the algorithm 
	 * @param model
	 * @param clauses
	 * @param c
	 */
	private void bestMoveBetter(Model model, List<Clause> clauses, Clause c){
		
		int largestSat = 0;
		int satisfied = 0;
		int index = 0;
		int count = 0;
		Model tMod = model;
		// Go through the model and flip each symbol to see which returns the best symbol to flip

		
		for (int i : c.clause){
			tMod.flipSymbol(i);
			satisfied = 0;
			
			for (Clause cl : clauses){
				if (cl.evaluate(tMod.getList())) satisfied++;
			}		
			
			if (satisfied > largestSat) { 
				index = count;
			}
			
			count++;
			tMod.flipSymbol(i);
		}
		
		tMod.flipSymbol(c.clause[index]);
		model.setList(tMod.getList());
	}

	/**
	 * Originally miss understood the algorithm and thought to search through each symbol in the model to determine which would be best to
	 * flip, this took a very long time to complete but thought it was interesting enough to keep in the code for later use.
	 * @param model
	 * @param clauses
	 */
	private void bestMove(Model model, List<Clause> clauses){

		int largestSat = 0;
		int satisfied = 0;
		int[] m = model.getList();
		// Go through the model and flip each symbol to see which returns the best symbol to flip

		for(int i = 0; i < m.length; i++){
			// Reset satisfied
			satisfied = 0;
			// Flip the last bit back
			if (i > 0) { m[i-1] = -m[i-1]; }
			// Flip the new test bit
			m[i] = -m[i];
			// Test all clauses
			for (Clause c : clauses){
				if (c.evaluate(m)) satisfied++;
			}

			if (satisfied > largestSat) { 
				largestSat = satisfied;
				model.setList(m);
			}
		}	
	}

	private Clause getRandom(List<Clause> clauses, int[] m){			

		List<Clause> falseClauses = new ArrayList<Clause>();

		for (Clause c : clauses) {
			if (!c.evaluate(m)) falseClauses.add(c);
		}

		int index = randomGenerator.nextInt(falseClauses.size());
		Clause clause = falseClauses.get(index);		
		return clause;
	}	
}
