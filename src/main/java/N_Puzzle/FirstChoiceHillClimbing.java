package N_Puzzle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by 伟平 on 2015/11/6.
 */
public class FirstChoiceHillClimbing {

    private int[] startState;
    // start state
    private Node start;
    private int nodesGenerated;

    public FirstChoiceHillClimbing() {
        start = new Node();
        startState = new int[Node.getSize()];
        startState();
        nodesGenerated=0;
    }

    public FirstChoiceHillClimbing(int[] s) {
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

            boolean existBetter = false;

            for (int i = 0; i < successors.size(); i++) {
                if (successors.get(i).compareTo(currentNode) < 0) {
                    currentNode = successors.get(i);
                    existBetter = true;
                    break;
                }
            }

            if(!existBetter) {
                return currentNode;
            }

        }
    }

    public Node getStartNode(){
        return start;
    }

    public int getNodesGenerated(){
        return nodesGenerated;
    }

}
