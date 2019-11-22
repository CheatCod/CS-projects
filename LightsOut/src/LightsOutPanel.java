import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class LightsOutPanel extends JPanel implements MouseListener {
	static int[][] lights; // Stores board lights pattern
	static int[][] copyOfLights; //
	static int[][] ansArray;
	static int[][] copyOfAnsArray;
	static int[][] ansArrayReset;
	static String leaderboardUnSorted[] = new String[50];
	static String leaderboardSorted[] = new String[50];
	static boolean displayAnswer = false;
	static int gameState = 0; // 0 = generated board, 1 = custome file
	static int numOfLightsOn = 10;
	static int clickCount;
	static int ansClick;
	static int copyOfAnsClick;
	static int ansClickFin;
	static int copyOfCountClick;
	static int counter = 0, callCount = 0;
	static JLabel lable1 = new JLabel();
	static JFrame parent = new JFrame();
	static JFrame frame = new JFrame();
	static String name = JOptionPane.showInputDialog(parent, "What is your name?", null);
	static JButton ansButton = new JButton();

	public static void main(String[] args) throws Exception {
		JOptionPane.showMessageDialog(frame, "Hello " + name
				+ "! Here is a guide for the buttons\nAns: On/Off - Toggle this button to either show or hide the solotion\nSave - Creates a save point\nLoad - Load to the latest save point\nNew - Generate a random map (SAVE POINT IN THE PREVIOUS MAP WILL BE LOST)\nRead - Read the board from a file\nWrite - write the current board to a file");
		setNumOfLights();
		parent.pack();
		parent.setVisible(true);
		LightsOutPanel sm = new LightsOutPanel();
		setJFrame(frame);
		frame.add(lable1);
		lable1.setBounds(125, 575, 400, 100);
		button(frame, sm);
		LightsOutPanel panel = new LightsOutPanel();
		setPanel(frame, panel);

	}

	private static void setPanel(JFrame frame, LightsOutPanel panel) {
		panel.addMouseListener(panel);
		panel.setPreferredSize(new Dimension(501, 701));
		panel.setMinimumSize(new Dimension(501, 701));

		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(panel, BorderLayout.CENTER);
		frame.pack();
	}

	public static void setJFrame(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("LightsOut! Player name: " + name);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public static void button(JFrame frame, LightsOutPanel sm) {
		JButton ldFileButton = new JButton();
		JButton saveButton = new JButton();
		JButton loadButton = new JButton();
		JButton randoButton = new JButton();
		JButton writeFileButton = new JButton();
		JButton debugButton = new JButton();
		JButton resetButton = new JButton();

		// frame.setSize(500, 500);
		frame.add(ldFileButton);
		frame.add(writeFileButton);
		frame.add(saveButton);
		frame.add(loadButton);
		frame.add(randoButton);
		frame.add(ansButton);
		frame.add(resetButton);
		frame.add(debugButton);

		debugButton.setVisible(false);

		ansButton.setBounds(0, 500, 100, 100);
		saveButton.setBounds(100, 500, 100, 100);
		loadButton.setBounds(200, 500, 100, 100);
		randoButton.setBounds(300, 500, 100, 100);
		ldFileButton.setBounds(400, 500, 100, 100);
		writeFileButton.setBounds(0, 600, 100, 100);
		resetButton.setBounds(400, 600, 100, 100);
		debugButton.setBounds(0, 600, 100, 100);

		ansButton.setText("Ans: Off");
		saveButton.setText("Save");
		loadButton.setText("Load");
		randoButton.setText("New");
		ldFileButton.setText("Read");
		writeFileButton.setText("Write");
		resetButton.setText("Reset");
		ansButton.setEnabled(false);
		ansButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (displayAnswer == true) {
					displayAnswer = false;
					ansButton.setText("Ans: Off");
					frame.repaint();
				} else {
					displayAnswer = true;
					ansButton.setText("Ans: On");
					frame.repaint();
				}

			}
		});
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyArray();
				copyOfCountClick = clickCount;
				copyOfAnsClick = ansClick;
				sortArray();

			}
		});
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ansCount();
				for (int x = 0; x < 5; x++) {
					for (int y = 0; y < 5; y++) {
						if ((ansArray[x][y]) - (copyOfAnsArray[x][y]) != 0) {
							toggle(x, y);
							toggleAnswer(x, y);
						}
					}
				}
				clickCount = copyOfCountClick;
				ansClick = copyOfAnsClick;
				frame.repaint();
			}
		});
		randoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] options = { "Yes", "No" };
				// Integer[] options = {1, 3, 5, 7, 9, 11};
				// Double[] options = {3.141, 1.618};
				// Character[] options = {'a', 'b', 'c', 'd'};
				int x = JOptionPane.showOptionDialog(null, "New Player?", "New Player", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				setNumOfLights();
				if (x == 0) {
					ansButton.setEnabled(true);
					// System.out.println("Yes");
					name = JOptionPane.showInputDialog(parent, "What is your name?", null);
					frame.setTitle("LightsOut! Player name: " + name);
					sm.rand();
					sortArray();
					clickCount = 0;
					gameState = 0;
					frame.repaint();

				} else {
					clickCount = 0;
					sm.rand();
					ansButton.setEnabled(true);
					gameState = 0;
					frame.repaint();
				}

			}
		});
		ldFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filePath = JOptionPane.showInputDialog(parent, "Enter file path ", null);
				decode(filePath);
				displayAnswer = false;
				gameState = 1;
				frame.repaint();

			}
		});
		writeFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filePath = JOptionPane.showInputDialog(parent, "Enter file path ", null);
				try {
					encode(filePath);
				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
		});
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ansCount();
				for (int x = 0; x < 5; x++) {
					for (int y = 0; y < 5; y++) {
						if ((ansArray[x][y]) - (ansArrayReset[x][y]) != 0) {
							toggle(x, y);
							toggleAnswer(x, y);
						}
					}
				}
				clickCount = 0;
				ansClick = 0;
				frame.repaint();
			}
		});

	}

	public static void checkIf5() {
		if (clickCount == 5) {
			System.out.println(
					"Hey! Seems like you are having some trouble\nThe magic button that toggles the answers has been enabled");
			ansButton.setEnabled(true);
		}
	}

	public static void setNumOfLights() {
		String temp;
		temp = (JOptionPane.showInputDialog(parent, "How many lights would you like to turn on? (Default 10)"));
		if (temp.matches("[0-9]+")) {
			numOfLightsOn = Integer.parseInt(temp);
		}
	}

	public static void sortArray() throws NumberFormatException {
		int counter = 0;
		for (int i = 0; i < 50; i++) {
			if (leaderboardUnSorted[i] != null) {
				leaderboardSorted[counter] = (leaderboardUnSorted[i]);
				counter++;
			}
			if (leaderboardSorted[i] == null) {
				leaderboardSorted[i] = "";
			}
		}
	}

	public static void copyArray() {
		ansArrayReset = new int[5][5];
		copyOfAnsArray = new int[5][5];
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				copyOfAnsArray[x][y] = ansArray[x][y];
				ansArrayReset[x][y] = ansArray[x][y];
			}
		}
	}

	public LightsOutPanel() {
		lights = new int[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				lights[i][j] = 1;
			}
		}
		rand();
	}

	// unused methods
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void paintComponent(Graphics g) {
		lable1.setText("Click Count:" + clickCount + "                        Min ans count " + ansClick);
		checkIfComplete();
		int boxWidth = 100;
		int boxHeight = 100;
		int y = 0;

		for (int row = 0; row < 5; row++) {
			int x = 0;
			for (int col = 0; col < 5; col++) {
				if (ansArray[row][col] == 1 && displayAnswer == true) {
					g.setColor(Color.RED);
				} else if (lights[row][col] == 1) {
					g.setColor(Color.WHITE);
				} else {
					g.setColor(Color.BLACK);
				}
				g.fillRect(x, y, boxWidth, boxHeight);

				g.setColor(Color.BLUE);
				g.drawRect(x, y, boxWidth, boxHeight);
				x += boxWidth;
			}
			y += boxHeight;
		}
	}

	public void rand() {
		fillAnsArray();
		clearLightsArray();
		int rand3, randBi, total = 0;
		boolean isTotal10 = false;
		rand3 = (int) Math.round(Math.random() * 10);
		while (isTotal10 == false) {
			for (int randLoop = 0; randLoop < rand3; randLoop++) {
				for (int x = 0; x < 5; x++) {
					for (int y = 0; y < 5; y++) {
						randBi = (int) Math.round(Math.random());
						if (randBi == 1) {
							total = 0;
							for (int totalx = 0; totalx < 5; totalx++) {
								for (int totaly = 0; totaly < 5; totaly++) {
									total = lights[totalx][totaly] + total;
								}
							}
							if (total == numOfLightsOn) {
								isTotal10 = true;
							} else {
								toggle(x, y);
								toggleAnswer(x, y);
							}
						}
					}
				}
			}
		}
		ansCount();
		copyArray();
		ansClickFin = ansClick;
	}

	public static void decode(String filePath) {
		int temp[][] = new int[11][5];
		parent.pack();
		parent.setVisible(true);
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			for (int i = 0; i < 11; i++) {
				for (int j = 0; j < 5; j++) {
					temp[i][j] = Integer.parseInt(br.readLine());
				}
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		if (temp[5][0] == 9) {// if file is created by program
			for (int x = 0; x < 5; x++) {
				for (int y = 0; y < 5; y++) {
					lights[x][y] = temp[x][y];
				}
			}
			for (int x = 6; x < 11; x++) {
				for (int y = 0; y < 5; y++) {
					ansArray[x - 6][y] = temp[x][y];
				}
			}
		} else {// if not created by program
			for (int x = 0; x < 5; x++) {
				for (int y = 0; y < 5; y++) {
					lights[x][y] = temp[x][y];
				}
			}
			clearAnsArray();
			ansButton.setEnabled(false);
		}

		ansClick = 0;
		clickCount = 0;
	}

	public static void encode(String filePath) throws IOException {
		int temp[][] = new int[11][5];
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				temp[x][y] = lights[x][y];
			}
		}
		for (int y = 0; y < 5; y++) {
			temp[5][y] = 9;
		}
		for (int x = 6; x < 11; x++) {
			for (int y = 0; y < 5; y++) {
				temp[x][y] = ansArray[x - 6][y];
			}
		}
		PrintWriter output = new PrintWriter(new FileWriter(filePath));

		for (int x = 0; x < 11; x++) {
			for (int y = 0; y < 5; y++) {
				output.println(temp[x][y]);
			}
		}
		output.close();
	}

	public void mousePressed(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		int boxWidth = 100;
		int boxHeight = 100;

		int col = mouseX / boxWidth;
		int row = mouseY / boxHeight;
		toggle(row, col);
		toggleAnswer(row, col);
		ansCount();
		countClick();
		repaint();
		checkIf5();
	}

	public static String inputString() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		return br.readLine();
	}

	public void countClick() {
		clickCount = clickCount + 1;
	}

	public static void ansCount() {
		ansClick = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				ansClick = ansClick + ansArray[i][j];
			}
		}

	}

	public void fillAnsArray() {
		ansArray = new int[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				ansArray[i][j] = 0;
			}
		}
	}

	public static void clearLightsArray() {
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				lights[x][y] = 0;
			}
		}
	}

	public static void clearAnsArray() {
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				ansArray[x][y] = 0;
			}
		}
	}

	public static void toggleAnswer(int x, int y) {
		if (ansArray[x][y] == 1) {
			ansArray[x][y] = 0;
		} else {
			ansArray[x][y] = 1;
		}
	}

	public static void toggle(int row, int col) {
		if (lights[row][col] == 1) {
			lights[row][col] = 0;
		} else {
			lights[row][col] = 1;
		}
		if (row - 1 >= 0) {
			if (lights[row - 1][col] == 0) {
				lights[row - 1][col] = 1;
			} else {
				lights[row - 1][col] = 0;
			}
		}
		if (row + 1 <= 4) {
			if (lights[row + 1][col] == 0) {
				lights[row + 1][col] = 1;
			} else
				lights[row + 1][col] = 0;
		}
		if (col - 1 >= 0) {
			if (lights[row][col - 1] == 0) {
				lights[row][col - 1] = 1;
			} else
				lights[row][col - 1] = 0;
		}
		if (col + 1 <= 4) {
			if (lights[row][col + 1] == 0) {
				lights[row][col + 1] = 1;
			} else
				lights[row][col + 1] = 0;
		}
	}

	public static void checkIfComplete() {
		int total = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				total = total + lights[i][j];
			}
		}
		if (total == 0) {
			postAnalysis();
		}
	}

	public static void postAnalysis() {
		callCount++;
		if (callCount == 2) {
			callCount = 0;
			leaderboardUnSorted[clickCount] = " Player: " + name + ", moves : " + clickCount;
			sortArray();
			JOptionPane.showMessageDialog(frame, "                               GAME COMPLETE!!" + "\n You took "
					+ clickCount + " steps to complete, you needed minimum of " + ansClickFin + " steps" + "\n#1"
					+ leaderboardSorted[0] + "\n#2" + leaderboardSorted[1] + "\n#3" + leaderboardSorted[2] + "\n#4"
					+ leaderboardSorted[3] + "\n#5" + leaderboardSorted[4] + "\n#6" + leaderboardSorted[5] + "\n#7"
					+ leaderboardSorted[6] + "\n#8" + leaderboardSorted[7] + "\n#9" + leaderboardSorted[8] + "\n#10"
					+ leaderboardSorted[9] + "\n This map is now completed, you can either:"
					+ "\n -Load the map from a checkpoint you saved" + "\n -Reset the map" + "\n -Generate a new map");

		}
	}
}