package operatingsystems;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Revealer implements Runnable {

		//Declaring LocalDate variables to calculate the current age
		private LocalDate today = LocalDate.now();
		private LocalDate instanceDate;
		
		//Declaring age related integers: month, day, year
		private Integer birthMonth;
		private Integer birthYear;
		private Integer birthDay;
		
		//Declaring section to be set with the inputted section and initializing all other variables to "Missing"
		private String section;
		private String age = "Missing";
		private String first = "Missing";
		private String last = "Missing";
		private String middle = "Missing";
		
		//Convenience constructor for the Revealer class that takes the section as a parameter
		protected Revealer(String section) {
		
			setSection(section);	//Setting the current section
			validateFirstName(getSection());//Validate whether the first name is valid
			validateMiddleInitial(getSection());//Validate whether the middle initial is valid
			validateLastName(getSection());//Validate whether the last name is valid
			validateAge(getSection());//Validate whether the age is valid
		}

		//Method that runs when the thread, Revealer, is started in the RecordReader Class 
		@Override
		public void run() {
		
				System.out.printf("SECTION ONE IS: %s%nFIRST NAME IS: %s%nMIDDLE INITIAL IS: %s%nLAST NAME IS: %s%nAGE IS: %s%n%n",
					getSection(),getFirst() ,getMiddle(),getLast() ,getAge());
		}
		
		//Method to test the validity of the first name is it exists
		public void validateFirstName(String section) {
			
			//Looks for a "F" followed by 1 capital letter and at least one lowercase letter
			Pattern firstNameRegex =Pattern.compile("F([A-Z])([a-z]+)");
			Matcher matchFirst = firstNameRegex.matcher(section);
			
			while(matchFirst.find()) {
				setFirst(matchFirst.group(1).concat(matchFirst.group(2)));//Sets the first name if match is found
			}
		}
		
		//Validating the middle initial
		public void validateMiddleInitial(String section) {
			
			//Matches a capital M followed by a capital letter
			Pattern midInitialRegex =Pattern.compile("M([A-Z])");
			Matcher matchMI = midInitialRegex.matcher(section);
			
			if(matchMI.find()) 
				setMiddle(matchMI.group(1));//Set the middle initial if a match is found
		}

		//Validating the last name in the section
		public void validateLastName(String section) {
		
			//Matches a L followed by one capital letter and at least 
			Pattern lastNameRegex =Pattern.compile("L([A-Z])([a-z]+)");
			Matcher matchLast = lastNameRegex.matcher(section);
			
			while(matchLast.find()) {
				setLast(matchLast.group(1).concat(matchLast.group(2)));//Set the Last name if found
			}
		}
		
		//Validating the age
		public void validateAge(String section) {
			String matched;
			Pattern ageRegex =Pattern.compile("B([0-9])");//Matches B with a number following it 
			Matcher matchAge = ageRegex.matcher(section);
			
			while(matchAge.find()) {

				matched = matchAge.group(1);
				int index = section.indexOf(matched); // Grab the index of the first matched number
				matched = section.substring(index, index + 8);//Save the next characters in a substring starting from the saved index
				
				try {//Try to set the age parameters
					
					setBirthMonth(Integer.parseInt(matched.substring(0,2)));
					setBirthDay(Integer.parseInt(matched.substring(2,4)));
					setBirthYear(Integer.parseInt(matched.substring(4, 8)));
					setInstanceDate(getBirthMonth(), getBirthDay(), getBirthYear());
					setAge(getInstanceDate(), today);
				
				}catch(Exception ex) {//Set age to invalid if there was an error setting the age.
					age = "Invalid";
				}
			}	
		}

		//Getters and Setters
		//Formats how the age should be outputted
		public void setAge(LocalDate instanceDate, LocalDate today) {
		
			Period period = Period.between(instanceDate, today);
			this.age = period.getYears() + " years, " + period.getMonths() + " months, and " + period.getDays() + " days.";
		}

		public String getAge() {
			return this.age;
		}
		
		//Checks the arguments for validity before setting them as the instance date.
		public void setInstanceDate(Integer birthMonth, Integer birthDay, Integer birthYear) {
			if(birthMonth <= 12 && birthMonth > 0) {
				if(birthYear > 1900 && birthYear < today.getYear()) {
					if(birthDay <= 31) {
						this.instanceDate = LocalDate.of(birthYear, birthMonth, birthDay);
					}
				}
			}
		}
		
		public LocalDate getInstanceDate() {
			return this.instanceDate;
		}
		
		public void setBirthYear(Integer birthYear) {
			this.birthYear = birthYear;
		}
		
		public Integer getBirthYear() {
			return this.birthYear;
		}

		public void setBirthMonth(Integer birthMonth) {
			this.birthMonth = birthMonth;
		}
		
		public Integer getBirthMonth() {
			return this.birthMonth;
		}

		public void setBirthDay(Integer birthDay) {
			this.birthDay = birthDay;
		}
		
		public Integer getBirthDay() {
			return this.birthDay;
		}

		public String getSection() {
			return this.section;
		}
		
		public void setSection(String section) {
				this.section = section;
		}
		
		public void setFirst(String first) {
			this.first = first;
		}
		
		public String getFirst() {
			return this.first;
		}
		
		public void setLast(String Last) {
			this.last= Last;
		}
		
		public String getLast() {
			return this.last;
		}
		
		public void setMiddle(String middle) {
			this.middle = middle;
		}
		
		public String getMiddle() {
			return this.middle;
		}
}
