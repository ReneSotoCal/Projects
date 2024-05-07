package os.projects;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class PageReplacementAlgorithms {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);//Initialize user input

		//Prompt the User until they input an exit string
		while(true){
			boolean error = false;//Controls error message for inputs
			
			//Message to prompt the user
			System.out.printf("Enter your choice of algorithm from the following list:" +
			"%n1. FIFO,%n2. Optimal Replacement,%n3. LRU Using time-of-use (Clock)" +
			"%n4. LRU Approximation Using a reference%n5. MFU. (8 pts.)%n");

			//Save the user's choice
			String choiceStr = input.nextLine();
			int choice = Integer.parseInt(choiceStr);

			//Save the user's reference string
			System.out.println("Enter your reference string: ");
			String refStr = input.nextLine();
			
			//Exit the loop if 9999 is entered
			if(refStr.equals("9999"))
				break;

			//Save the original refStr for later output and modify the string to ease manipulation
			String refStrOrig = refStr;
			refStr = refStr.replaceAll(",", "");
			String[] nums = refStr.split("[\s]");

			//Count the numbers entered to ensure they are less than 15
			if(nums.length > 15){
				System.out.println("Reference String is Larger than 15. Try Again.");
				continue;
			}
			
			refStr = "";//Clear refStr
			for(int i = 0; i < nums.length; i++){//For every number in reference string 
				if(nums[i].length() > 1){//If the number is not a single digit. Print an error message and try again 
					System.out.println("Single Digits Only. Try Again.");
					error = true;
				} else
					refStr += nums[i];//Add the number the the refStr		
			}
			
			//If there was an error detected continue with the next iteration
			if(error)
				continue;

			//Prompt the user for the frame count
			System.out.println();
			System.out.println("Enter the number of frames");
			String framesStr = input.nextLine();

			//Initialize the frame and fault count variables
			int frameCt = Integer.parseInt(framesStr);
			int faultCt = 0;

			//Switch case for the user's choice of algorithm
			switch(choice){

				case 1: //FIFO Algorithm is formatted and used to calculate the page faults 
					choiceStr = "FIFO";
					System.out.printf("%nAlgorithm: %s%nReference String: %s%nNumber of frames: %d%n"
					, choiceStr, refStrOrig, frameCt);
					faultCt = FIFO(refStr, frameCt);
					System.out.println("Number of page faults: " + faultCt);
					break;

					case 2: //Optimal Replacement Algorithm is formatted and used to calculate the page faults
					choiceStr = "Optimal Replacement";
					System.out.printf("%nAlgorithm: %s%nReference String: %s%nNumber of frames: %d%n"
					, choiceStr, refStrOrig, frameCt);
					faultCt = OptReplacement(refStr, frameCt);
					System.out.println("Number of page faults: " + faultCt);
					break;

				case 3: //LRU Using time-of-use (Clock) Algorithm is formatted and used to calculate the page faults
					choiceStr = "LRU Using time-of-use (Clock)";
					System.out.printf("%nAlgorithm: %s%nReference String: %s%nNumber of frames: %d%n"
					, choiceStr, refStrOrig, frameCt);
					faultCt = LRU(refStr, frameCt);
					System.out.println("Number of page faults: " + faultCt);
					break;

				case 4: //LRU Approximation Algorithm is formatted and used to calculate the page faults
					choiceStr = "LRU Approximation Using a referece";
					System.out.printf("%nAlgorithm: %s%nReference String: %s%nNumber of frames: %d%n"
					, choiceStr, refStrOrig, frameCt);
					faultCt = LRUApprox(refStr, frameCt);
					System.out.println("Number of page faults: " + faultCt);
					break;

				case 5: //MFU Algorithm is formatted and used to calculate the page faults
					choiceStr = "MFU.";
					System.out.printf("%nAlgorithm: %s%nReference String: %s%nNumber of frames: %d%n"
					, choiceStr, refStrOrig, frameCt);
					faultCt = MFU(refStr, frameCt);
					System.out.println("Number of page faults: " + faultCt);
					break;
			}

			System.out.println("-----------------------------------");
		}

		input.close();//Close Scanner
	}

	//Method to calculate page faults using the FIFO algorithm
	public static int FIFO(String refString, int frameCt){

		Integer[] frames = new Integer[frameCt];
		int faultCt = 0;

		//Allocates and prints frames
		System.out.println("Content of frames after each page-fault:");
		for(int i = 0; i < refString.length(); i++) {
			int refValue = Integer.parseInt(refString.substring(i, i+1));

			//If the frames are not full add a new frame and print
			if(i < frames.length){
				frames[i] = refValue;
				faultCt++;
				printFrames(frames);
			} else
				if(!containsPg(frames, refValue)){//If the page is not in the frames
					replaceFIFO(frames, refValue);//Replace page
					faultCt++;//Increment fault count
					printFrames(frames);//Print frames
				}	
		}
		

		return faultCt;//Return the fault count
	}

	//Method to replaces pages using the FIFO algorithm
	public static void replaceFIFO(Integer[] array, int num){

			//Shift the array up an element and place the new number at the end
			for(int i = 1; i < array.length; i++)
				array[i-1] = array[i];
			array[array.length-1] = num;
	}
	
	//Method to calculate page faults using the Optimal Replacement Algorithm
	public static int OptReplacement(String refString, int frameCt){

		Integer[] frames = new Integer[frameCt];
		int faultCt = 0;

		Integer[] refValues = new Integer[refString.length()];
		System.out.println("Content of frames after each page-fault:");

		for(int i = 0; i < refString.length(); i++) 	
			refValues[i] = Integer.parseInt(refString.substring(i, i+1));
		
		for(int i = 0; i < refValues.length; i++){

			if(i < frames.length){//If the frame is not full
				frames[i] = refValues[i]; //fill the frame
				faultCt++;//update the fault count
				printFrames(frames);//print the frames

			} else if(!containsPg(frames, refValues[i])){//If the page is not in the frames
				replaceOR(frames, refValues[i], i+1,refValues);//Replace frames
				faultCt++;//Increment fault count
				printFrames(frames);//print the frames
			}		
		}
		
		return faultCt;//return the fault count
	}
	
	//Method for replacing pages using the Optimal Replacement Algorithm
	public static void replaceOR(Integer[] frames, int num, int index, Integer[] references){

		Integer[] refIndexes = new Integer[frames.length];//Initialize an array for the indexes of the references
		Integer[] subarray = new Integer[references.length - index];//Cut the reference string to only be as big as needed

		for(int i = 0; i < subarray.length; i++)//Initialize the subaray elements
			subarray[i] = references[index + i];
		
		for(int i = 0; i < frames.length; i++){//For every frame
			if(containsPg(subarray, frames[i]))//If the frame is not in the remaining array

				for(int j = index; j < references.length; j++){//Move through the list of references
					if(references[j] == frames[i]){//If the frame's page matches the reference
						refIndexes[i] = j;//Set the refIndex for that frame's page to j
						break;//Exit the loop
					}
				}
			
			//If the page is not found in the remaining array, replace the frame with the desired reference.
			else{
				frames[i] = num;
				return;//Exit method
			}
		}
		
		int max = 0;
		int maxInd = 0;	

		for(int i = 0; i < refIndexes.length; i++)
			if(refIndexes[i] > max){//Update the index that is furthest
				maxInd = i;
				max = refIndexes[i];
			}

		frames[maxInd] = num; //Update the optimal frame with the desired number
	}

	//Method to calculate page faults using the LRU Algorithm
	public static int LRU(String refString, int frameCt){

		Integer[] frames = new Integer[frameCt];
		int faultCt = 0;

		ArrayList<Integer> visited = new ArrayList<>();//Array of visited references
		ArrayList<Integer> counted1 = new ArrayList<>();//Array of reference's index value at which it was visited

		System.out.println("Content of frames after each page-fault:");
		for(int i = 0; i < refString.length(); i++) {
			int refValue = Integer.parseInt(refString.substring(i, i+1));
		
			if(i < frames.length){//If the frames are not full 
				frames[i] = refValue;//Update frame
				faultCt++;//Increment fault count
				
				visited.add(refValue);//Add the value to visited
				counted1.add(i); //Add the index to counted
				printLRU(frames, counted1); // Print Frames

			} else
				if(!containsPg(frames, refValue)){//If the frames do not contain the reference
					replaceLRU(frames, refValue, visited);//Replace the frames
					visited.add(refValue);//Add the reference to visited
					ArrayList<Integer> counted2 = new ArrayList<>();//Intialize a second counted array for indexes after the frames are full

					//Adding the index of the references in the frames 
					for(int j = 0; j < frames.length; j++)
						for(int k = visited.size()-1; k >= 0; k--)
							if(visited.get(k) == frames[j]){
								counted2.add(k);
								break;
							}
						
					faultCt++;//Increment fault count
					printLRU(frames, counted2);//Print Frames

				} else//Add the reference to visited if it already exists in the frames
					visited.add(refValue);
		}

		return faultCt; //Return fault count
	}	

	//Method to replace pages in a frame using LRU Algorithm
	public static void replaceLRU(Integer[] frames, int refValue, ArrayList<Integer> visited){

		int minVal = Integer.MAX_VALUE;
		int targetInd = 0; 
		
		for(int i = 0; i < frames.length; i++){
			ArrayList<Integer> counted = new ArrayList<>();//Initialize a list for indexes
			for(int j = visited.size()-1; j >= 0; j--){

				//If the page has been visited and is not in the counted list yet
				if(visited.get(j) == frames[i] && !counted.contains(visited.get(j))){
					if(j < minVal){//Update minVal and targetInd
						counted.add(visited.get(j));//Add the reference to the counted array

						minVal = j;
						targetInd = i;
						break;	//Exit the loop

					} else
						break;//Exit the loop
				}
			}
		}

		frames[targetInd] = refValue;//Replace the frame
	}

	//Method to calculate page faults using the LRU Approximation Algorithm
	public static int LRUApprox(String refString, int frameCt){

		Integer[] frames = new Integer[frameCt];
		int faultCt = 0;
	
		HashMap<Integer, String> refBytes = new HashMap<>();
		refBytes = initializeBytes(refBytes, refString);//Initialize the bytes for the reference values
		
		System.out.println("Content of frames after each page-fault:");
		for(int i = 0; i < refString.length(); i++) {
			int refValue = Integer.parseInt(refString.substring(i, i+1));

			if(i < frames.length){//If the frame is not full, update the frames and fault count
				frames[i] = refValue;
				faultCt++;
				printLRUApprox(frames, refBytes);//Print the frames

			} else{
				if(!containsPg(frames, refValue)){//If the frames do not contain the page
					replaceLFUApprox(frames, refValue, refBytes); //Replace the page
					faultCt++;//Increment fault count
					printLRUApprox(frames, refBytes);//Print frames

				} else{//Update the reference bytes for the pages in the frames
					for(int frame : frames){
						if(frame != refValue)
							refBytes.put(frame, shift(refBytes.get(frame), false));
						else
							refBytes.put(frame, shift(refBytes.get(frame), true));
					}
				}
			}
		}
		return faultCt; //Return fault count
	}

	private static void replaceLFUApprox(Integer[] frames, int refValue, HashMap<Integer, String> refBytes) {

		int minVal = Integer.MAX_VALUE;
		int targetInd = 0; 
		
		for(int i = 0; i < frames.length; i++)
			if(binaryToDecimal(refBytes.get(frames[i])) < minVal){//Convert the bytes to decimal and compare to the other bytes in the frames
				targetInd = i;//Update target index
				minVal = binaryToDecimal(refBytes.get(frames[i]));//Update minVal 
			}
		
		//If the page in the frames is not referenced shift the bytes with a 0
		for(int i = 0; i < frames.length; i++)
			if(targetInd != refValue)
				refBytes.put(frames[i], shift(refBytes.get(frames[i]), false));
		
		//Replace the page and reinitialize it's value upon reaching the frames again or for the first time 
		frames[targetInd] = refValue;
		refBytes.put(refValue, "11111111");
	}

	//Print Frames for LRUApproximation Algorithm
	public static void printLRUApprox(Integer[] frames, HashMap<Integer, String> refBytes) {

		System.out.print("Pages:	");
		for(int i = 0; i < frames.length; i++){
			System.out.print("|");

			if(frames[i] == null)
				System.out.print("  ");

			else
				if(!refBytes.isEmpty())
					System.out.print(frames[i]);
			}

			System.out.println("|");
			System.out.print("Bytes:	");

			for(int i = 0; i < frames.length; i++){
				System.out.print("|");

				if(frames[i] == null)
					System.out.print("  ");

				else
					if(!refBytes.isEmpty())
						System.out.print(refBytes.get(frames[i]));
			}

			System.out.println("|");
			System.out.println();
	}

	//Initialize all reference bytes for each reference
	public static HashMap<Integer, String> initializeBytes(HashMap<Integer, String> refBytes, String refString){
		
		String refByte = "11111111";
		for(int i = 0; i < refString.length(); i++){
			int refVal = Integer.parseInt(String.valueOf(refString.charAt(i)));
			
			if(!refBytes.containsKey(refVal))
				refBytes.put(refVal, refByte);
		}

		return refBytes;
	}

	//Shift the bytes based on whether the reference is in the frame
	public static String shift(String refByte, boolean inFrame){
		if(inFrame)
			refByte = "1" + refByte.substring(0, refByte.length()-1);

		else
			refByte = "0" + refByte.substring(0, refByte.length()-1);
		return refByte;
	}

	//Convert the binary string to decimal
	public static Integer binaryToDecimal(String binary){

		int j = 0;
		Integer decimal = 0;

		for(int i = binary.length() - 1; i >= 0; i--)
			if(Integer.parseInt(String.valueOf(binary.charAt(i))) == 1)
				decimal += (int)Math.pow(2, j);

		return decimal;
	}

	//Method to calculate page faults using the MFU Algorithm
	public static int MFU(String refString, int frameCt){

		Integer[] frames = new Integer[frameCt];
		int faultCt = 0;
		HashMap<Integer, Integer> counter = new HashMap<>();//Counter for each value

		System.out.println("Content of frames after each page-fault:");
		for(int i = 0; i < refString.length(); i++) {
			int refValue = Integer.parseInt(refString.substring(i, i+1));

			if(!counter.containsKey(refValue))//Initialize the counters
				counter.put(refValue, 1);

			else//If it is already initialized then increment
				counter.put(refValue, counter.get(refValue) + 1);

			if(i < frames.length){//If the frames are not full
				frames[i] = refValue;//Update frame
				faultCt++;//Increment fault count
				printMFU(frames, counter);//Print frames
			
			} else
				if(!containsPg(frames, refValue)){//The frames do not have the page
					replaceMFU(frames, refValue, counter);//Replace the page
					faultCt++;//increment fault count
					printMFU(frames, counter);//Print frames
				}
			
		}
		return faultCt;//Return fault count
	}

	//Replace the page with the highest counter and set the new frame and counter
	public static void replaceMFU(Integer[] frames, int num, HashMap<Integer, Integer> counter){
			
			int maxInd = 0;
			int max = 0;

			for(int i = 0; i < frames.length; i++){
				if(counter.get(frames[i]) > max){
				
					max = counter.get(frames[i]);
					maxInd = i;
				}
			}
			
			frames[maxInd] = num;
	}

	//Print MFU algorithm
	public static void printMFU(Integer[] frames, HashMap<Integer, Integer> counter) {
		
			System.out.print("Pages:		");
			for(int i = 0; i < frames.length; i++){
				System.out.print("|");
				if(frames[i] == null)
					System.out.print(" ");
				else{
					if(!counter.isEmpty())
						System.out.print(frames[i]);
				}
			}

			System.out.println("|");
			System.out.print("Counter:	");

			for(int i = 0; i < frames.length; i++){
				System.out.print("|");

				if(frames[i] == null)
					System.out.print(" ");

				else
					if(!counter.isEmpty())
						System.out.print(counter.get(frames[i]));
			}

			System.out.println("|");
			System.out.println();
		}

	//Generic Print Frames
	public static void printFrames(Integer[] frames){

		for(int i = 0; i < frames.length; i++){
			System.out.print("|");

			if(frames[i] == null)
				System.out.print(" ");

			else
				System.out.print(frames[i]);
		}

		System.out.println("|");
	}

	//Print LRU Algorithm
	public static void printLRU(Integer[] frames, ArrayList<Integer> counted) {

		System.out.print("Pages:	");
		for(int i = 0; i < frames.length; i++){

			System.out.print("|");
			if(frames[i] == null)
				System.out.print(" ");

			else
				if(counted.size() > i)
					System.out.print(frames[i]);
		}

		System.out.println("|");
		System.out.print("Clock:	");

		for(int i = 0; i < frames.length; i++){
			System.out.print("|");

			if(frames[i] == null)
				System.out.print(" ");
			
			else
				if(counted.size() > i)
					System.out.print((counted.get(i) + 1));
		}

		System.out.println("|");
		System.out.println();
	}

	//Checks whether the array contains the number
	public static boolean containsPg(Integer[] array, int num){
		for(Integer pg : array)
			if(num == pg)
				return true;
		return false;
	}
}

