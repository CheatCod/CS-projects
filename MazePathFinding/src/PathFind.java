import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PathFind {
	static Maze newMaze = new Maze(12, 8); //Call constructor to initialize object newMaze with length = 12, width = 8
	static String maze[][] = new String[newMaze.width][newMaze.length];//initialize global object for maze layout
	static String shortestArray[][] = new String[newMaze.width][newMaze.length];//initialize global object to keep track of the shortest path
	/**
	 * main method
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException { 
		String mouseToCheese[][] = new String[newMaze.width][newMaze.length];
		String cheeseToExit[][] = new String[newMaze.width][newMaze.length];
		readFile("C:\\Users\\28920\\Desktop\\Maze.txt");
		copyArray(maze, mouseToCheese);
		findPath(getPosY("M"), getPosX("M"), 0, Integer.MAX_VALUE, mouseToCheese, "C");
		copyArray(shortestArray, mouseToCheese);
		copyArray(maze, cheeseToExit);
		findPath(getPosY("C"), getPosX("C"), 0, Integer.MAX_VALUE, cheeseToExit, "X");
		copyArray(shortestArray, cheeseToExit);
		output(combine(mouseToCheese, cheeseToExit)); 
	}
	
	/**
	 * takes in 2 arrays and combine the cells which indicates path
	 * @param tempArray1
	 * @param tempArray2
	 * @return combined array
	 */
	private static String[][] combine(String[][] tempArray1, String[][] tempArray2) {
		for (int row = 0; row < newMaze.width; row++) {
			tempArray1[getPosY("X")][getPosX("X")] = "X";
			for (int colume = 0; colume < newMaze.length; colume++) {
				if (tempArray2[row][colume].equals("PATH!!!")) {
					tempArray1[row][colume] = "PATH!!!";
				}
			}
		}
		return tempArray1;

	}

	/**
	 * @param goal
	 * @return y position of the parameter in maze array
	 */
	private static int getPosY(String goal) {
		for (int row = 0; row < newMaze.width; row++) {
			for (int colume = 0; colume < newMaze.length; colume++) {
				if (maze[row][colume].equals(goal)) {
					return row;
				}
			}
		}
		return -1;
	}

	/**
	 * @param goal
	 * @return x position of the parameter in maze array
	 */
	private static int getPosX(String goal) {

		for (int row = 0; row < newMaze.width; row++) {
			for (int colume = 0; colume < newMaze.length; colume++) {
				if (maze[row][colume].equals(goal)) {
					return colume;
				}
			}
		}
		return -1;
	}

	/** functional method which outputs the array to the console
	 * @param outputArray
	 */
	private static void output(String[][] outputArray) {

		for (int row = 0; row < newMaze.width; row++) {
			System.out.println();
			for (int colume = 0; colume < newMaze.length; colume++) {
				System.out.print(outputArray[row][colume] + "\t");
			}
		}
	}

	/**read a file in the location of the parameter 
	 * @param filePath
	 * @throws IOException
	 */
	private static void readFile(String filePath) throws IOException {

		BufferedReader readFile = new BufferedReader(new FileReader(filePath));
		String line = readFile.readLine();
		for (int row = 0; row < newMaze.width; row++) {
			maze[row] = line.split(" ");
			line = readFile.readLine();
		}
	}

	/**check if array at y, x is legal, or has been explored recently
	 * @param y
	 * @param x
	 * @param tempArray
	 * @return true or false
	 */
	private static boolean isValid(int y, int x, String[][] tempArray) {

		if (y >= newMaze.width || y < 0 || x >= newMaze.length || x < 0) {
			return false;
		}
		if (maze[y][x].equals("B")) {
			return false;
		}
		if (tempArray[y][x].equals("PATH!!!")) {
			return false;
		}
		return true;
	}

	/**recursive path-finding method which takes in the starting position with end goal
	 * then records the shortest path in global array: shortestArray
	 * @param y
	 * @param x
	 * @param currentDis - the distance to reach the goal in this iteration 
	 * @param minDis - the minimum distance taken to reach the goal in every iteration
	 * @param tempArray - a placeholder array to keep track of all the potential solution
	 * @param goal - what to path-find to
	 * @return
	 */
	private static  int findPath(int y, int x, int currentDis, int minDis, String[][] tempArray, String goal) {// inspired																								// by
																												// errick
		if (maze[y][x].equals(goal)) {
			if (currentDis < minDis) {
				shortestArray = copyArray(tempArray, shortestArray);
			}
			return Integer.min(currentDis, minDis);
		}
		tempArray[y][x] = "PATH!!!";
		if (isValid(y + 1, x, tempArray) == true) {
			minDis = findPath(y + 1, x, currentDis + 1, minDis, tempArray, goal);
		}
		if (isValid(y, x + 1, tempArray) == true) {
			minDis = findPath(y, x + 1, currentDis + 1, minDis, tempArray, goal);
		}
		if (isValid(y - 1, x, tempArray) == true) {
			minDis = findPath(y - 1, x, currentDis + 1, minDis, tempArray, goal);
		}
		if (isValid(y, x - 1, tempArray) == true) {
			minDis = findPath(y, x - 1, currentDis + 1, minDis, tempArray, goal);
		}
		tempArray[y][x] = ".";
		return minDis;
	}

	/**copy one array's content to another
	 * @param toBeCopied
	 * @param destination
	 * @return 
	 */
	private static String[][] copyArray(String[][] toBeCopied, String[][] destination) {
		for (int i = 0; i < newMaze.width; i++) {
			for (int j = 0; j < newMaze.length; j++) {
				destination[i][j] = toBeCopied[i][j];
			}
		}
		return destination;
	}
}
