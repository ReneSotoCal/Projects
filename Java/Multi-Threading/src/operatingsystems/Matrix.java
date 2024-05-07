package operatingsystems;

//Matrix class
public class Matrix {
	
	//Variables to instantiate a matrix
	private int rows;
	private int cols;
	private Integer[][] matrix;
	
	//Convenience Constructor to create a Matrix
	public Matrix(int rows, int cols) {
		
		//Setting the size of the matrix
		setRows(rows);
		setCols(cols);
		createMatrix(new Integer[rows][cols]);
	}
	
//	Taking the mod of every number in a matrix
	public Matrix modMatrix(int mod) {
		
		Matrix result = new Matrix(this.getRows(), this.getCols());//Create a matrix of the size used in the instance
		
		//Iterate through the matrix to set the value of the matrix to the new values that were modded
		for(int i = 0; i < this.getRows(); i++) 
			for(int j = 0; j < this.getCols(); j++) 
				result.setValue(i, j, this.getValue(i, j) % mod);

		return result; // Return the modded matrix
	}

	//Multiply two matrixes together 
	public Matrix multiplyMatrix(Matrix other) {
		
		//Local variables for the dimensions of the two matrixes
		int rowSize1 = this.getRows();
		int rowSize2 = other.getRows();
		int colSize1 = this.getCols();
		int colSize2 = other.getCols();
		
		Matrix resultMatrix = new Matrix(rowSize1, colSize2);//Creates the resultant matrix
		
		//checks whether the column size of the instance matrix is the same as the row size of the other matrix
		if(colSize1 == rowSize2) {
			
			
			for(int i = 0; i < rowSize1; i++) {
				for(int j = 0; j < colSize2; j++) {
					int sum = 0;//Reset the sum to 0 when a new row and column are being multiplied
					for(int k = 0; k < colSize1; k++) {
						
						// Sums the values from multiplying the first matrix's row with the second matrix's column 
						sum += this.getValue(i, k) * other.getValue(k, j);
					}
					resultMatrix.setValue(i, j, sum);// Places the sum in the resulting matrix
				}
			}
		}
		return resultMatrix;//Return the resultant matrix
	}
	
	//Getters and Setters
	public Integer getValue(int row, int col) {
		return this.matrix[row][col];
	}
	
	public void setValue(int row, int col, int value) {
		this.matrix[row][col] = value;
	}
	
	public int getRows() {
		return this.rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return this.cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public Integer[][] getMatrix() {
		return this.matrix;
	}

	public void createMatrix(Integer[][] matrix) {
		this.matrix = matrix;
	}
}
