package ch.fhnw.students.keller.benjamin.tree;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;

public class TreeAdapter extends Observable implements Observer{
	
	private Tree tree;
	protected Context context;
	private TreeNode selectedNode;
	
	public TreeAdapter(Tree tree, Context context){
		this.setTree(tree);
		tree.addObserver(this);
		this.context = context;
	}

	
	public NodeView getView(TreeNode node){
		
		
		return new NodeView(context, node);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
		
	}


	public Tree getTree() {
		return tree;
	}


	public void setTree(Tree tree) {
		this.tree = tree;
	}


	public TreeNode getSelectedNode() {
		return selectedNode;
	}


	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

}
