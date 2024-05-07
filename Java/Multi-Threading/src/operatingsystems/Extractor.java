package operatingsystems;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor implements Runnable {

	//Declaring variables
	private ArrayList<String> symbols = new ArrayList<>();
	private ArrayList<String> passwSymbols = new ArrayList<>();
	private ArrayList<Integer> nums = new ArrayList<>();
	private ArrayList<Character> letters = new ArrayList<>();
	private ArrayList<Integer> singleDigits = new ArrayList<>();

	private Set<Integer> new1;
	private Set<Integer> new2;
	private Set<Integer> appendedSet;

	private String section;
	private String password;

	private Integer max;
	private Integer min;






	public Extractor(String section3) {

		setSection(section3);//setting the section
		populateNums(getSection());//Populating the nums ArrayList
		setSymbols(symbols);//Set the symbols for the password
		
		setMax(findMax(nums));//Find the Max in the nums ArrayList
		setMin(findMin(nums));//Find the Min in the nums ArrayList
		setNew1(generateNew1(getMax(), nums));//Setting the values of set new1
		
		setNew2(generateNew2(getMin(), nums));//Setting the values of set new2
		setNew2(removeDuplicates(getNew1(), getNew2()));//removing the duplicates between the 2 sets
		setAppendedSet(appendSets(getNew1(), getNew2()));//Append the 2 sets
		
		setLetters(convertToLetters(getAppendedSet()));//Convert the appended set into Letters for the password
		setPasswSymbols(convertToPasswSymbols(getAppendedSet()));//Convert the appended set into symbols from the symbols ArrayList
		setPassword(generatePassword(getPasswSymbols(), getLetters()));//Generate and set the password
	}

	//Method that runs when this thread is started
	@Override
	public void run() {

			System.out.printf("SECTION THREE IS: %s%nNUMBER OF VALID ELEMENTS in P or Q: %d%nPASSWORD IS: %s%n%n",
				getSection(), getLetters().size(), getPassword());
	}

	//Populating the nums ArrayList
	public void populateNums(String section) {

		//1st Capture group captures 1-5 digits
		//Non-capture group to select but not capture commas and surrounding whitespace
		//Second capturing group selects future digits or its the end of the line
		Pattern numRegex = Pattern.compile("(-?[0-9]{1,5})(?:\s*,\s*(?=-?[0-9])|$)");
		Matcher matchNums = numRegex.matcher(section);//Matches regex to the section

		while(matchNums.find()) {//While matches are found

			//Add non-negative integers to the nums ArrayList
			if(Integer.parseInt(matchNums.group(1)) >= 0)
				nums.add(Integer.parseInt(matchNums.group(1)));
		}
	}

	//Finding the max value in ArrayList Nums
	public Integer findMax(ArrayList<Integer> nums) {
		
		Integer max = nums.get(0);
		
		for(int i = 1; i < nums.size(); i++) 
			if(nums.get(i) > max)
				max = nums.get(i);
		
		return max;
	}

	//Finding the min value in ArrayList nums
	public Integer findMin(ArrayList<Integer> nums) {
		
		Integer min = nums.get(0);
		
		for(int i = 1; i < nums.size(); i++) 
			if(nums.get(i) < min)
				min = nums.get(i);
		
		return min;
	}

	//Subtracting values in nums from the max value to formulate new1
	public Set<Integer> generateNew1(Integer max, ArrayList<Integer> nums){

		Set<Integer> new1 = new LinkedHashSet<Integer>();
		
		for(int i = 0; i < nums.size(); i++) 
			new1.add(max - nums.get(i));
		
		return new1;
	}

	//Subtract the min value from every value in nums to generate new2
	public Set<Integer> generateNew2(Integer max, ArrayList<Integer> nums){
		
		Set<Integer> new2 = new LinkedHashSet<Integer>();
		
		for(int i = 0; i < nums.size(); i++) 
			new2.add(nums.get(i) - min);
		
		return new2;
	}

	//Take 2 sets and append them
	public Set<Integer> appendSets(Set<Integer> new1, Set<Integer> new2){
		
		for(Integer num : new2) 
			new1.add(num);
		
		return new1;
	}

	//Remove the duplicates from both sets
	public Set<Integer> removeDuplicates(Set<Integer> new1, Set<Integer> new2) {

		ArrayList<Integer> duplicates = new ArrayList<>();
		ArrayList<Integer> tempList = new ArrayList<>(new2);
		
		for(Integer num : tempList) 
			if(new1.contains(num)) 
				duplicates.add(num);
			
		for(Integer num : duplicates)  
			tempList.remove(num);
		
		return new LinkedHashSet<Integer>(tempList);//Return the new set
	}

	//Converting the appendedSet into letters
	public ArrayList<Character> convertToLetters(Set<Integer> appendedSet){
		
		ArrayList<Character> letters = new ArrayList<>();
		Character[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
				'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
		
		//Takes the mod of values from num and uses it as an index for the alphabet array
		for(Integer num : appendedSet) 
			letters.add(alphabet[num % 26]);
		
		return letters;
	}

	//Converting the appendedSet into password symbols
	public ArrayList<String> convertToPasswSymbols(Set<Integer> appendedSet){

		ArrayList<Integer> digits = new ArrayList<>();
		ArrayList<String> passwordSymbols = new ArrayList<>();
		
		//adding the new index values into digits to access the symbols array
		for(Integer num : appendedSet) 
			digits.add((num % 26) % 10);
		
		//Adding the corresponding symbol from symbols based on the nums saved in digits
		for(Integer num : digits) 
			passwordSymbols.add(getSymbols().get(num));
		
		return passwordSymbols;
	}

	//Generate a password with symbols and letters
	public String generatePassword(ArrayList<String> passwSymbols, ArrayList<Character> letters) {
		
		StringBuilder password = new StringBuilder();//StringBuilder to append letters
		
		//Iterate through both ArrayLists and take the element at index i for each and append it to password
		for(int i = 0; i < letters.size(); i++) {
			//letters must be before symbols while appending
			password.append(letters.get(i));
			password.append(passwSymbols.get(i));
		}

		return password.toString();
	}

	//Getters and Setters
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public ArrayList<String> getPasswSymbols() {
		return passwSymbols;
	}
	
	public void setPasswSymbols(ArrayList<String> passwSymbols) {
		this.passwSymbols = passwSymbols;
	}
	
	public ArrayList<String> getSymbols() {
		return symbols;
	}
	
	public void setSymbols(ArrayList<String> symbols) {
		symbols.add("!");
		symbols.add("@");
		symbols.add("#");
		symbols.add("4");
		symbols.add("5");
		symbols.add("%");
		symbols.add("8");
		symbols.add("9");
		symbols.add("6");
		symbols.add("7");
		this.symbols = symbols;
	}
	
	public ArrayList<Character> getLetters() {
		return letters;
	}
	
	public void setLetters(ArrayList<Character> letters) {
		this.letters = letters;
	}
	
	public ArrayList<Integer> getSingleDigits() {
		return singleDigits;
	}
	
	public void setSingleDigits(ArrayList<Integer> singleDigits) {
		this.singleDigits = singleDigits;
	}
	
	public Set<Integer> getAppendedSet() {
		return appendedSet;
	}

	public void setAppendedSet(Set<Integer> appendedSet) {
		this.appendedSet = new LinkedHashSet<Integer>(appendedSet);
	}

	public Set<Integer> getNew1() {
		return new1;
	}
	
	public void setNew1(Set<Integer> new1) {
		this.new1 = new LinkedHashSet<Integer>(new1);
	}
	
	public Set<Integer> getNew2() {
		return new2 = new LinkedHashSet<Integer>(new2);
	}
	
	public void setNew2(Set<Integer> new2) {
		this.new2 = new2;
	}
	
	public String getSection() {
		return section;
	}
	
	public void setSection(String section) {
		this.section = section;
	}
	
	public Integer getMax() {
		return max;
	}
	
	public void setMax(Integer max) {
		this.max = max;
	}
	
	public Integer getMin() {
		return min;
	}
	
	public void setMin(Integer min) {
		this.min = min;
	}
}