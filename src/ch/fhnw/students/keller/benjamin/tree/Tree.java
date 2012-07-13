package ch.fhnw.students.keller.benjamin.tree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Observable;

public class Tree extends Observable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7636873710560107200L;
	protected TreeNode rootNode;
	private TreeNode selectedNode;
	private int nodeIdCounter = 0;
	private HashMap<Integer, TreeNode> allNodes = new HashMap<Integer, TreeNode>();
	

	public Tree() {
		rootNode = new TreeNode(this);

	}
	public void dataSetChanged(){
		setChanged();
		notifyObservers();
	}
	public void addNode(TreeNode node) {
		node.getParentNode().addChild(node,
				node.getParentNode().getChildNodes().size());
		node.getParentNode().expand();

		setChanged();
		notifyObservers();
	}

	public void addNode() {
		if (selectedNode != null) {
			selectedNode.addChild(new TreeNode(-1, selectedNode), selectedNode
					.getChildNodes().size());
			selectedNode.expand();
		} else {
			rootNode.addChild(new TreeNode(-1, rootNode), rootNode
					.getChildNodes().size());
		}
		setChanged();
		notifyObservers();
	}

	public void deleteNode() {
		if (selectedNode != null) {
			allNodes.remove(selectedNode.getId());
			selectedNode.getParentNode().removeChild(selectedNode);
			if (!selectedNode.getParentNode().equals(rootNode)) {
				dataSetChanged();
			}
			unselectTreeNode();
		}
	}

	public void selectTreeNode(TreeNode node) {
		selectedNode = node;
		setChanged();
		notifyObservers();
	}

	public void unselectTreeNode() {
		selectedNode = null;
		setChanged();
		notifyObservers();
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public int newNode(TreeNode node) {
		allNodes.put(nodeIdCounter, node);
		return nodeIdCounter++;

	}
	public TreeNode getRoot(){
		return rootNode;
	}
}
