package N_Puzzle;

import java.util.*;

public class StochasticHillClimbing {

	private int[] startState;
	// start state
	private Node start;
	private int nodesGenerated;

	public StochasticHillClimbing() {
		start = new Node();
		startState = new int[Node.getSize()];
		startState();
		nodesGenerated = 0;
	}

	public StochasticHillClimbing(int[] s) {
		start = new Node();
		startState = new int[Node.getSize()];
		for (int i = 0; i < s.length; i++){
			startState[i] = s[i];
		}
		start.setState(startState);
		start.computeHeuristic();
		
		nodesGenerated = 0;
	}

	public void startState() {
		for (int i = 0; i < Node.getSize(); i++) {
			startState[i] = i;
		}
		Node.randomMove(startState);
		start.setState(startState);
		start.computeHeuristic();
	}

	public Node hillClimbing() {

		Node currentNode = start;
		
		while (true) {
			ArrayList<Node> successors = currentNode.generateNeighbours();
			nodesGenerated += successors.size();
			
			Node nextNode = null;

			ArrayList<Node> betterSuccessors = new ArrayList<>();

			for (int i = 0; i < successors.size(); i++) {
				if (successors.get(i).compareTo(currentNode) < 0) {
					betterSuccessors.add(successors.get(i));
				}
			}

			if (!betterSuccessors.isEmpty()) {
				Random random = new Random();
				nextNode = betterSuccessors.get(random.nextInt(betterSuccessors.size()));
			}
			
			if(nextNode == null) {
				return currentNode;
			}

			currentNode = nextNode;
		}
	}

	public Node getStartNode(){
		return start;
	}

	public int getNodesGenerated(){
		return nodesGenerated;
	}
}
