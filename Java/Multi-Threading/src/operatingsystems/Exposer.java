package operatingsystems;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exposer implements Runnable {

	//Declaring the variables used in the class and initializing values of alphabet part1 and part2
	String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	String section;
	String part1 = "Invalid";
	String part2 = "Invalid";
	String deciphered;
	ArrayList<Integer> tokens = new ArrayList<>(); 
	
	//Convenience constructor for the Exposer class
	public Exposer(String section) {
		
		setSection(section);//Setting the section to the current one
		validatePart1(section);//Validating whether part one is a valid entry
		validatePart2(section);//Validating whether part two is a valid entry
		
		//Checks whether both parts are valid
		if(!getPart2().equals("Invalid") && !tokens.isEmpty()) {
			
			StringBuilder newLetters = new StringBuilder();//StringBuilder to append new letters
			
			//Save the result of the deciphered matrix of each pair of letters and the token array
			for(int i = 2; i <= getPart1().length(); i+=2) {
				Matrix decipheredMat = decipherMatrix(getPart1().substring(i-2, i), tokens);
				
				//Iterating through the matrix to find the new letters and append them to newLetters
				for(int j = 0; j < decipheredMat.getRows(); j++) {
					for(int k = 0; k < decipheredMat.getCols(); k++) {
						Integer val = decipheredMat.getValue(j, k);
						Character newChar = this.alphabet.charAt(val);
						newLetters.append(newChar);
					}
				}
			}
			setDeciphered(newLetters.toString());//Set the new Deciphered Code 
		}
	}

	//Printing the information about the section when the thread has started running
	@Override
	public void run() {
			
			if(!getPart1().equals("Invalid") && !getPart2().equals("Invalid")) 
				System.out.printf("SECTION TWO IS: %s%nPART1 IS: %s%nPART2 IS: %s%nDECIPHERED CODE IS: %s%n%n",
						getSection(), getPart1(), getPart2(), getDeciphered());
			else
				System.out.printf("SECTION TWO IS: %s%nPART1 IS: %s%nPART2 IS: %s%n%n",
						getSection(), getPart1(), getPart2());	
	}
	
	//Validating part1
	public void validatePart1(String section) {

		Pattern part1Regex =Pattern.compile("([A-Z]+)");//Capture any capitol letters
		Matcher matchFirst = part1Regex.matcher(section);
		
		while(matchFirst.find()) {
			if(matchFirst.group(1).length() % 2 == 0)//If the amount of letters found is even
				setPart1(matchFirst.group(1));//Set Part1 to the letters found
		}
	}
	
	//Validating part2
	public void validatePart2(String section) {

		Pattern invalRegex = Pattern.compile("([0-9]+[.][0-9])");//checking for decimal numbers
		Pattern validRegex = Pattern.compile("[0-9]+");//Checking for whole numbers
		Pattern numsRegex = Pattern.compile("[0-9].*");//Match everything after the first number
		
		Matcher matchNums = numsRegex.matcher(section);
		Matcher matchDecimal = invalRegex.matcher(section);
		Matcher validMatches = validRegex.matcher(section);
		
		
		while(!matchDecimal.find() && validMatches.find()) {//While no decimal numbers are found and valid matches are
			tokens.add(Integer.parseInt(validMatches.group(0)));//Add the numbers into the tokens ArrayList
		}
		
		if(tokens.size() == 4) //If there are only four valid entries
			if(matchNums.find())//Match the content from the first number on
				setPart2(matchNums.group());//Set part 2
		
	}
	
	//Decipher the code with an alphabet string and numbers for a matrix
	public Matrix decipherMatrix(String letters, ArrayList<Integer> tokens) {
		
		//Variables for the letters and indexes of the letters in the alphabet
		String firstLetter = letters.substring(0, 1);
		String secondLetter = letters.substring(1, 2);
		Integer firstLetterVal = alphabet.indexOf(firstLetter);
		Integer secondLetterVal = alphabet.indexOf(secondLetter);
		
		Matrix letterMatrix = new Matrix(2, 1);//Create a new matrix for the letter's index values
		
		//Populating the Matrix
		letterMatrix.setValue(0, 0, firstLetterVal);
		letterMatrix.setValue(1, 0, secondLetterVal);
		
		Matrix tokenMatrix = new Matrix (2, 2);//Create a new matrix for the token ArrayList's content

		//Populate the Matrix
		tokenMatrix.setValue(0, 0, tokens.get(0));
		tokenMatrix.setValue(0, 1, tokens.get(1));
		tokenMatrix.setValue(1, 0, tokens.get(2));
		tokenMatrix.setValue(1, 1, tokens.get(3));
		
		//Return the result of the (multiplication of the token and letters matrix) modular 26
		return tokenMatrix.multiplyMatrix(letterMatrix).modMatrix(26);
	}
	
	//Getters and Setters
	public void setDeciphered(String deciphered) {
		this.deciphered = deciphered;
	}
	
	public String getDeciphered() {
		return this.deciphered;
	}
	
	public void setPart1(String part1) {
		this.part1 = part1;
	}
	
	public String getPart1() {
		return this.part1;
	}
	
	public void setPart2(String part2) {
		this.part2 = part2;
	}
	
	public String getPart2() {
		return this.part2;
	}
	
	public void setSection(String section) {
		this.section = section;
	}
	
	public String getSection() {
		return this.section;
	}
}
