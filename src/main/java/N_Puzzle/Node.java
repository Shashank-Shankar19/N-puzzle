package N_Puzzle;

import java.util.*;

public class Node implements Comparable<Node>{

	static private final int RANDOM_TIMES = 10000;

	static private double DEV = 0.001;
	// the default size of Puzzle is 3
	static private int SIZE = 3 * 3;
	// the state of node
	private int[] state;
	private ArrayList<Node> neighbours;
	// heuristic score
	// remember that the heuristic here means the number of conflict pairs of queens
	// so when heuristic is 0, we get the answer
	private int heuristic;

	public Node() {
		state = new int[SIZE];
		neighbours = new ArrayList<>();
		heuristic = 0;
	}

	public Node(Node n) {
		state = new int[SIZE];
		neighbours = new ArrayList<>();
		for (int i = 0; i < SIZE; i++) {
			state[i] = n.getState()[i];
		}
		heuristic = 0;
	}

	public Node(int[] tiles) {
		state = new int[SIZE];
		for (int i = 0; i < SIZE; i++) {
			state[i] = tiles[i];
		}
		neighbours = new ArrayList<>();
		heuristic = 0;
	}

	static public int getSize() {
		return SIZE;
	}

	static public void setSize(int Size) {
		SIZE = Size;
	}

	public ArrayList<Node> generateNeighbours() {

		int wide = (int)Math.sqrt(SIZE * 1.0);
		int blankPositionI = 0;
		int blankPositionJ = 0;
		for (int i = state.length - 1; i >= 0; i--) {
			if (state[i] == 0) {
				blankPositionI = i / wide;
				blankPositionJ = i % wide;
				break;
			}
		}

		if (blankPositionI > 0) {
			// can move up
			int[] tiles = new int[SIZE];
			for (int i = state.length - 1; i >= 0; i--) {
				tiles[i] = state[i];
			}
			int temp = tiles[blankPositionI * wide + blankPositionJ];
			tiles[blankPositionI * wide + blankPositionJ]
					= tiles[(blankPositionI - 1) * wide + blankPositionJ];
			tiles[(blankPositionI - 1) * wide + blankPositionJ] = temp;
			Node node = new Node(tiles);
			node.computeHeuristic();
			neighbours.add(node);
		}

		if (blankPositionI < wide - 1) {
			// can move down
			int[] tiles = new int[SIZE];
			for (int i = state.length - 1; i >= 0; i--) {
				tiles[i] = state[i];
			}
			int temp = tiles[blankPositionI * wide + blankPositionJ];
			tiles[blankPositionI * wide + blankPositionJ]
					= tiles[(blankPositionI + 1) * wide + blankPositionJ];
			tiles[(blankPositionI + 1) * wide + blankPositionJ] = temp;
			Node node = new Node(tiles);
			node.computeHeuristic();
			neighbours.add(node);
		}

		if (blankPositionJ > 0) {
			// can move left
			int[] tiles = new int[SIZE];
			for (int i = state.length - 1; i >= 0; i--) {
				tiles[i] = state[i];
			}
			int temp = tiles[blankPositionI * wide + blankPositionJ];
			tiles[blankPositionI * wide + blankPositionJ]
					= tiles[blankPositionI * wide + blankPositionJ - 1];
			tiles[blankPositionI * wide + blankPositionJ - 1] = temp;
			Node node = new Node(tiles);
			node.computeHeuristic();
			neighbours.add(node);
		}

		if (blankPositionI < wide - 1) {
			// can move right
			int[] tiles = new int[SIZE];
			for (int i = state.length - 1; i >= 0; i--) {
				tiles[i] = state[i];
			}
			int temp = tiles[blankPositionI * wide + blankPositionJ];
			tiles[blankPositionI * wide + blankPositionJ]
					= tiles[blankPositionI * wide + blankPositionJ + 1];
			tiles[blankPositionI * wide + blankPositionJ + 1] = temp;
			Node node = new Node(tiles);
			node.computeHeuristic();
			neighbours.add(node);
		}

		return neighbours;
	}

	public Node getRandomNeighbour(Node startState){
		ArrayList<Node> neighbours = generateNeighbours();

		Random random = new Random();

		Node neighbour = neighbours.get(random.nextInt(neighbours.size()));
		neighbour.computeHeuristic();

		return neighbour;
	}

	public int computeHeuristic(){

		int wide = (int)Math.sqrt(SIZE * 1.0);

		for (int i = state.length - 1; i >= 0; i--) {
			for (int j = i - 1; j >= 0; j--) {
				if (state[i] != 0 && state[j] != 0) {
					heuristic += 2 * (calculateLinearConflict(
							i / wide, i % wide,
							j / wide, j % wide,
							(state[i]) / wide, (state[i]) % wide,
							(state[j]) / wide, (state[j]) % wide) ? 1 : 0);
				}
			}
		}
		for (int i = state.length - 1; i >= 0; i--) {
			heuristic += calculateManhattanDistance(
					(state[i]) / wide, (state[i]) % wide, i / wide, i % wide);
		}

		return heuristic;
	}

	public int getHeuristic(){
		return heuristic;
	}

	public int compareTo(Node n){
		if (this.heuristic < n.getHeuristic()) {
			return -1;
		} else if (this.heuristic > n.getHeuristic()) {
			return 1;
		} else {
			return 0;
		}
	}

	public void setState(int[] s) {
		for(int i = 0; i < SIZE; i++){
			state[i]= s[i];
		}
	}

	public int[] getState(){
		return state;
	}

	public String toString(){

		int wide = (int)Math.sqrt(SIZE * 1.0);

		String result = "";
		String[][] board = new String[wide][wide];

		for (int i = 0; i < wide; i++)
			for(int j = 0; j < wide; j++)
				board[i][j] = String.valueOf(state[i * wide + j]);

		for (int i = 0; i < SIZE; i++){
			for (int j = 0; j < SIZE; j++){
				result += board[i][j];
			}
			result += "\n";
		}
		
		return result;
	}

	private int calculateManhattanDistance(int ai, int aj, int bi, int bj) {
		return Math.abs(ai - bi) + Math.abs(aj - bj);
	}

	private boolean calculateLinearConflict(int ai, int aj, int bi, int bj,
								 int aGoalI, int aGoalJ, int bGoalI, int bGoalJ) {
		double k, b;
		// first, judge whether the 4 points are in one line
		// second, judege whether the vector(ai - bi, aj - bj)
		// and the vector(aGoalI - bGoalI, aGoalJ - bGoalJ) is in contrast
		// i = k * j + b
		if (aj != bj) {
			k = (1.0 * ai - bi) / (aj - bj);
			b = ai - aj * k;
			if (isTheSame(aGoalI - aGoalJ * k, b) && isTheSame(bGoalI - bGoalJ * k, b)) {
				// yes, the 4 points are in one line
				double v1i = ai - bi, v1j = aj - bj;
				double v2i = aGoalI - bGoalI, v2j = aGoalJ - bGoalJ;
				return isTheSame(
						(v1i * v2i + v1j * v2j) /
								Math.sqrt((v1i * v1i + v1j * v1j) * (v2i * v2i + v2j * v2j)), -1);

			}
		}
		else {
			if (aGoalJ == aj && bGoalJ == bj) {
				// yes, the 4 points are in one line
				double v1i = ai - bi, v1j = aj - bj;
				double v2i = aGoalI - bGoalI, v2j = aGoalJ - bGoalJ;
				return isTheSame(
						(v1i * v2i + v1j * v2j) /
								Math.sqrt((v1i * v1i + v1j * v1j) * (v2i * v2i + v2j * v2j)), -1);
			}
		}
		return false;
	}

	private boolean isTheSame(double a, double b) { return Math.abs(a - b) <= DEV;}

	static public void randomMove(int[] state) {

		for (int r = 0; r < RANDOM_TIMES; r++) {

			int wide = (int)Math.sqrt(SIZE * 1.0);

			int blankPositionI = 0, blankPositionJ = 0;
			for (int i = state.length - 1; i >= 0; i--) {
				if (state[i] == 0) {
					blankPositionI = i / wide;
					blankPositionJ = i % wide;
					break;
				}
			}

			ArrayList<Integer> choices = new ArrayList<>();
			if (blankPositionI > 0) {
				// can move up
				choices.add(0);
			}
			if (blankPositionI < wide - 1) {
				// can move down
				choices.add(1);
			}
			if (blankPositionJ > 0) {
				// can move left
				choices.add(2);
			}
			if (blankPositionJ < wide - 1) {
				// can move right
				choices.add(3);
			}

			Random random = new Random();

			int randomChoice = random.nextInt(choices.size());

			if (blankPositionI > 0 && choices.get(randomChoice) == 0) {
				// move up
				int temp = state[blankPositionI * wide + blankPositionJ];
				state[blankPositionI * wide + blankPositionJ]
						= state[(blankPositionI - 1) * wide + blankPositionJ];
				state[(blankPositionI - 1) * wide + blankPositionJ] = temp;
			}
			if (blankPositionI < wide - 1 && choices.get(randomChoice) == 1) {
				// move down
				int temp = state[blankPositionI * wide + blankPositionJ];
				state[blankPositionI * wide + blankPositionJ]
						= state[(blankPositionI + 1) * wide + blankPositionJ];
				state[(blankPositionI + 1) * wide + blankPositionJ] = temp;
			}
			if (blankPositionJ > 0 && choices.get(randomChoice) == 2) {
				// can move left
				int temp = state[blankPositionI * wide + blankPositionJ];
				state[blankPositionI * wide + blankPositionJ]
						= state[blankPositionI * wide + blankPositionJ - 1];
				state[blankPositionI * wide + blankPositionJ - 1] = temp;
			}
			if (blankPositionJ < wide - 1 && choices.get(randomChoice) == 3) {
				// can move right
				int temp = state[blankPositionI * wide + blankPositionJ];
				state[blankPositionI * wide + blankPositionJ]
						= state[blankPositionI * wide + blankPositionJ + 1];
				state[blankPositionI * wide + blankPositionJ + 1] = temp;
			}
		}
	}
}
