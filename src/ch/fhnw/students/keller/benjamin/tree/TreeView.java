package ch.fhnw.students.keller.benjamin.tree;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TreeView extends LinearLayout implements Observer {
	private Tree tree;

	

	public TreeView(Context context, AttributeSet attr) {
		super(context, attr);
		System.out.println("constructor");
	}


	public void setTree(Tree tree) {

		this.tree = tree;
		this.tree.addObserver(this);
		this.tree.view=this;
		updateViews();
		
	}


	private void addNodeViews(TreeNode node) {
		System.out
				.println("addNodeViews: size: " + node.getChildNodes().size());
		int i = 0;
		for (TreeNode nd : node.getChildNodes()) {
			System.out.println("addview: " + getChildCount() + "loop nr: " + i);
			nd.view.leftSpaceView.setLayoutParams(new LayoutParams(nd.getTreeLevel()*10, LayoutParams.MATCH_PARENT));
			addView(nd.view);
			if (nd.isExpanded() && nd.getChildNodes().size() > 0) {
				System.out.println("add childviews");
				addNodeViews(nd);
			}
		}
	}

	private void updateViews() {
		removeAllViews();
		addNodeViews(tree.rootNode);
	}

	
		
	

	@Override
	public void update(Observable observable, Object data) {
		updateViews();

	}

}
