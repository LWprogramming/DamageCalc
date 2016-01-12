/**
 * LW_Programming
 * 11/2/2015
 * http://pastebin.com/T3mnAamV explains it all. basically take some parameters and see what is the chance of the 
 * multiple hits knocking the 'mon out? good for late-game execution.
 * 
Replay: http://replay.pokemonshowdown.com/ou-288044208 turn 20.
252 Atk Mega Lopunny Return vs. 0 HP / 4 Def Mega Gyarados: 160-189 (48.3 - 57%) -- guaranteed 2HKO after Stealth Rock
252+ Atk Choice Band Huge Power Azumarill Aqua Jet vs. 0 HP / 4 Def Mega Gyarados: 55-66 (16.6 - 19.9%) -- possible 5HKO after Stealth Rock
252+ Atk Iron Plate Technician Scizor Bullet Punch vs. 0 HP / 4 Def Mega Gyarados: 60-71 (18.1 - 21.4%) -- guaranteed 5HKO after Stealth Rock
worst case scenario: i get 95.5% damage.
Best case scenario: i get 110.8% damage
what are the chances of getting the KO? it's really a fine distinction so it's hard without calcing.


One last note: the way I implement it has O(n^3), which is really terrible in general. However, since at most this is at most 16^6 
calculations because there are only 6 pokemon on a team, the computer shouldn't really have too much issue.

Test run, using 252+ SpA Abomasnow Blizzard vs. 252 HP / 0 SpD Abomasnow: 178-211 (46.3 - 54.9%) -- 65.6% chance to 2HKO: 

Number of Pokemon?
2
Roll: 0
Type in the rolls. Formatting example: 
178, 181, 183, 186, 187, 189, 192, 193, 196, 198, 199, 202, 204, 207, 208, 211
178, 181, 183, 186, 187, 189, 192, 193, 196, 198, 199, 202, 204, 207, 208, 211
Roll: 1
Type in the rolls. Formatting example: 
178, 181, 183, 186, 187, 189, 192, 193, 196, 198, 199, 202, 204, 207, 208, 211
178, 181, 183, 186, 187, 189, 192, 193, 196, 198, 199, 202, 204, 207, 208, 211
Amount of HP opposing Pokemon has? Need exact value, no percents
383
Your probability of KO is: 65.625%

 */

import java.util.Scanner;

public class Multihit {
	public final static int NUM_ROLLS = 16;// For each attack there are 16
											// possible rolls.
	private Scanner keyboard = new Scanner(System.in);
	private int[][] rolls;//each row represents a pokemon, each column has the rolls.
	private int enemyHP;
	
	//creates new calc.
	public Multihit()
	{
		inputRolls();
		inputEnemyHP();
		System.out.println("Your probability of KO is: " + getProbabilityKO() + "%");
	}
	
	public void inputRolls() {
//		System.out.println("To clear calc at any point, type -1");
//		System.out.println("To clear the previous Pokemon's rolls, type -2");

		System.out.println("Number of Pokemon?");
		int numMons = keyboard.nextInt();
		keyboard.nextLine();
		
		while (tooSmallInput(1, numMons)) {
			System.out.println("too small");
			numMons = keyboard.nextInt();
			keyboard.nextLine();
		}
		
		rolls = new int[numMons][NUM_ROLLS];// rows are the individual
													// mon's list of rolls,
													// columns are rolls for
													// individual mon.

		// inputting rolls here.
//		System.out.println("Just a reminder:");
//		System.out.println("To clear entire calc at any point, type -1");
//		System.out.println("To clear the previous Pokemon's rolls, type -2");
//
//		int i = 0;
//		int j = 0;
//		int input;

		String input = null;
		String[] inputArray;
//		String filler = null;
				
		for (int i = 0 ; i < numMons; i++)
		{
			System.out.println("Roll: " + i);
			System.out.println("Type in the rolls. Formatting example: ");
			System.out.println("178, 181, 183, 186, 187, 189, 192, 193, 196, 198, 199, 202, 204, 207, 208, 211");
			input = keyboard.nextLine();
//			keyboard.nextLine();//after typing enter to have the scanner consume the extra line.
			input = input.replaceAll("\\s", "");//take out the commas
			inputArray = input.split(",");
			
			for (int j = 0; j < inputArray.length; j ++)
			{
//				System.out.println(inputArray[j]);
				rolls[i][j] = Integer.parseInt(inputArray[j]);
			}
			
//			System.out.println(rolls[0][15]);
		}
	}

	//lets user type enemy hp directly into calc, no parameters bc they're user-set.
	//probably not the best approach but it does what it has to do.
	public void inputEnemyHP()
	{
		System.out.println("Amount of HP opposing Pokemon has? Need exact value, no percents");
		// no silliness with Showdown percents!
																				
		enemyHP = keyboard.nextInt();
		keyboard.nextLine();
		while (tooSmallInput(1, enemyHP)) {
			System.out.println("too small");
			enemyHP = keyboard.nextInt();
			keyboard.nextLine();
		}
	}
	
	/*
	 * Returns array with list of all potential roll outcomes. 
	 * I did it recursively so as to handle any number of attackers from 2-6.
	 */
	public int[] combineDamages(int[] array)
	{
		int[] toReturn;
		if (array.length % NUM_ROLLS != 0)
		{
			System.out.println("the number of rolls should be a multiple of " + NUM_ROLLS);
			return null;//probably bad idea but it should never happen.
		}
		/*
		 * Base case
		 */
		if(array.length == 2 * NUM_ROLLS)//if we're combining rolls of just two pokemon
		{
			toReturn = new int[array.length/2 * array.length/2];
			for (int i = 0; i < array.length/2; i++)
			{
				for (int j = array.length/2; j < array.length; j++)
				{
					toReturn[i*array.length/2 + (j-(array.length/2))] = array[i] + array[j];//fill in values.
				}
			}
		}
		else //if more than two pokemon, then split the first mon off (so make an array with 16 rolls.
		{
			int[] individualPokemonRolls = new int[NUM_ROLLS];
			int[] restOfTheRolls = new int[array.length-NUM_ROLLS];
			for (int i = 0; i < NUM_ROLLS; i++)
			{
				individualPokemonRolls[i] = array[i];
			}
			for (int j = 0; j < array.length-NUM_ROLLS; j++)
			{
				restOfTheRolls[j] = array[NUM_ROLLS+j];
			}//create the two arrays first
			
			//then do the recursion
			return multiplyArrays(individualPokemonRolls, combineDamages(restOfTheRolls));
		}
//		for (int i = 0; i < toReturn.length; i++)
//		{
//			System.out.println(toReturn[i]);
//		}
		return toReturn;
	}
	
	/*
	 * Used to "multiply" two arrays. 
	 * ASSUMPTION: array1 is of length NUM_ROLLS.
	 */
	private int[] multiplyArrays(int[] array1, int[] array2)
	{
		if(array1.length != NUM_ROLLS)
		{
			System.out.println("array1's length is not equal to NUM_ROLLS");
			return null;//see above, should not happen but whatever.
		}
		
		int[] toReturn = new int[array1.length* array2.length];
		
		for (int i = 0; i < array1.length; i++)
		{
			for (int j = 0; j < array2.length; j++)
			{
				toReturn[i*array2.length + j] = array1[i] + array2[j];//fill in the values.
			}
		}
		return toReturn;
	}
	
	private int[] toOneDimensionalArray(int[][] twoDimensionalArray)
	{
		int[] toReturn = new int[twoDimensionalArray.length * twoDimensionalArray[0].length];
		for (int i = 0; i < twoDimensionalArray.length; i++)
		{
			for (int j = 0; j < twoDimensionalArray[0].length; j++)
			{
				toReturn[i*twoDimensionalArray[0].length + j] = twoDimensionalArray[i][j];
			}
		}
		return toReturn;
	}
	
	/*
	 * Determine probability that given rolls do enough damage. 
	 */
	public double getProbabilityKO()
	{
//		for (int i = 0; i < rolls.length; i++)
//		{
//			for (int j = 0; j < rolls[0].length; j++)
//			{
//				System.out.println(rolls[i][j]);
//			}
//		}
		
		int[] rollsOneDimensional = toOneDimensionalArray(rolls);
		int[] combinedRolls = combineDamages(rollsOneDimensional);
		
		double total = 0.0;
		for (int i = 0; i < combinedRolls.length; i++)
		{
//			System.out.println(combinedRolls[i]);
			if (combinedRolls[i] > enemyHP)
				total ++;
		}
		return (total / (double) combinedRolls.length) * (double) 100;
	}
	
	private static boolean tooSmallInput(int threshold, int param) {
		return (threshold >= param);
	}
	
	public static void main(String [] args)
	{
		Multihit test = new Multihit();//using the example replay given. 
		
	}
}