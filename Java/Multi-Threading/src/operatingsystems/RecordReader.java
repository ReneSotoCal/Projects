package operatingsystems;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.*;
import java.io.*;
import java.util.ArrayList;

public class RecordReader {

	public static void main(String[] args) {

		//Instantiating the individual sections
		String section1 = "";
		String section2 = "";
		String section3 = "";
		ArrayList<String> records = new ArrayList<>();// ArrayList for individual records

		File file = new File("src/xyz.txt"); // Finding the file xyz.txt and placing it in a File object
		
		// Try-with-resources for the Input stream of the file
		try( 
			InputStream input = new BufferedInputStream(new FileInputStream(file));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
			){

			while(bufferedReader.ready()) {// Continues reading the file until the file is empty

				records.add(bufferedReader.readLine());	
			}

			Pattern pattern = Pattern.compile("(\\*+)([^\\*]+)");//Captures asterisks in group 1 and captures any character not an asterisk in group 2

			for(String record : records) {//Iterate through each record in the the records list

				Matcher matcher = pattern.matcher(record);

				while(matcher.find()) {
					
					String asterisks = matcher.group(1);//Storing the asterisks

					//Initializing the sections based on asterisk count
					if(asterisks.equals("*"))
						section1 = matcher.group(2);
					else if(asterisks.equals("**"))
						section2 = matcher.group(2);
					else if(asterisks.equals("***"))
						section3 = matcher.group(2);
				}


				//Checking for an empty section to declare a record invalid
				if(!section1.isEmpty() && !section2.isEmpty() && !section3.isEmpty()) {
					
					//Initializing and starting the revealer, expose, and extractor threads
					Thread revealer = new Thread(new Revealer(section1));
					Thread exposer = new Thread(new Exposer(section2));
					Thread extractor = new Thread(new Extractor(section3));
					revealer.start();
					exposer.start();
					extractor.start();
				} else {
					System.out.printf("Record is invalid%n%n");
				}
				
				//Reset the values of sections for the next record
				section1 = "";
				section2 = "";
				section3 = "";
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}