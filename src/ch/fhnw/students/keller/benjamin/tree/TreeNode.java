package ch.fhnw.students.keller.benjamin.tree;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class TreeNode extends Observable implements Observer{
	
	public static final int NO_CHILDREN=0;
	public static final int INFINITE_CHILDREN=-1;
	private ArrayList<TreeNode> childNodes = new ArrayList<TreeNode>();
	private TreeNode parentNode;
	private boolean expanded;
	private boolean selected=false;
	private int childrenAllowed;
	private int id;
	private Tree tree;
	public NodeView view;
	
	public TreeNode(Tree tree){
		
		childrenAllowed=INFINITE_CHILDREN;
		id=tree.newNode(this);
		this.tree= tree;
		this.tree.addObserver(this);
		expanded=true;
		
	}
	
	public TreeNode(int childrenAllowed, TreeNode parentNode){
		this.childrenAllowed=childrenAllowed;
		this.parentNode=parentNode;
		tree=parentNode.getTree();
		id=tree.newNode(this);
		this.tree.addObserver(this);
		view = new NodeView(tree.view.getContext(), this);
		addObserver(view);
		
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
		tree.view.update(null, null);
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
		tree.view.update(null, null);
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

	@Override
	public void update(Observable observable, Object data) {
		boolean oldselect=selected;
		if(this.equals(tree.getSelectedNode())){
			selected=true;
		}
		else{
			selected=false;
		}
		if(oldselect!=selected){
			setChanged();
			notifyObservers();
		}
	}

	public boolean isSelected() {
		
		return selected;
	}
	
	
	

}
