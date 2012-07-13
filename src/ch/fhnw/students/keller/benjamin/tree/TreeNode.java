package ch.fhnw.students.keller.benjamin.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

public class TreeNode extends Observable implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9009407318516629609L;
	public static final int NO_CHILDREN=0;
	public static final int INFINITE_CHILDREN=-1;
	private ArrayList<TreeNode> childNodes = new ArrayList<TreeNode>();
	private TreeNode parentNode;
	private boolean expanded;
	private int childrenAllowed;
	private int id;
	private Tree tree;
	
	public TreeNode(Tree tree){
		
		childrenAllowed=INFINITE_CHILDREN;
		id=tree.newNode(this);
		this.tree= tree;
		expanded=true;
		
	}
	
	public TreeNode(int childrenAllowed, TreeNode parentNode){
		this.childrenAllowed=childrenAllowed;
		this.parentNode=parentNode;
		tree=parentNode.getTree();
		id=tree.newNode(this);
		
	}
	
	public int getId(){
		return id;
	}
	public Tree getTree(){
		return tree;
	}
	public void expand(){
		expanded=true;
		setChanged();
		notifyObservers();
		tree.dataSetChanged();
	}
	public void collapse(){
		expanded=false;
		if(tree.getSelectedNode()!=null){
			if(!tree.getSelectedNode().isVisible()){
				tree.unselectTreeNode();
			}
				
		}
		setChanged();
		notifyObservers();
		tree.dataSetChanged();
	}
	public boolean isExpanded(){
		return expanded;
	}
	public boolean isVisible(){
		if (parentNode!=null){
			if(parentNode.isExpanded()){
				return parentNode.isVisible();
			}
			else{
				return false;
			}
		}else{
			return true;
		}
		
	}
	public void setParentNode(TreeNode parentNode) {
		this.parentNode = parentNode;
	}
	public TreeNode getParentNode() {
		return parentNode;
	}
	public void setChildrenAllowed(int childrenAllowed) {
		this.childrenAllowed = childrenAllowed;
	}
	public int getChildrenAllowed() {
		return childrenAllowed;
	}
	public void setChildNodes(ArrayList<TreeNode> childNodes) {
		this.childNodes = childNodes;
	}
	public ArrayList<TreeNode> getChildNodes() {
		return childNodes;
	}
	
	public int getTreeLevel() {
		if (parentNode==null){
			return 0;
		}
		else{
			return parentNode.getTreeLevel()+1;
		}
	}
	
	public int addChild(TreeNode child, int pos){
		System.out.println("addchild pos: "+pos);
		if (pos>childNodes.size() || pos ==-1){
			pos=childNodes.size();
		}
		childNodes.add(pos, child);
		System.out.println("addchild pos: "+pos);
		System.out.println("children: "+childNodes.size());
		return pos;
	}
	public void removeChild(TreeNode child){
		
		childNodes.remove(child);
		
	}
	public boolean canHaveMoreChildren(){
		if(childrenAllowed==INFINITE_CHILDREN){
			return true;
		}
		else if(childNodes.size()<childrenAllowed){
			return true;
		}
		return false;
	}

	public boolean isSelected() {
		
		return this.equals(tree.getSelectedNode());
	}
	
	
	

}
