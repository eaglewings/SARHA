package ch.fhnw.students.keller.benjamin.tree;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TreeView extends LinearLayout implements Observer {
	private TreeAdapter adapter;

	public TreeView(Context context, AttributeSet attr) {
		super(context, attr);
	}

	public void setAdapter(TreeAdapter adapter) {
		this.adapter = adapter;
		adapter.addObserver(this);
		updateViews();
	}

	private void addNodeViews(TreeNode node) {
		int i = 0;
		for (TreeNode nd : node.getChildNodes()) {
			System.out.println("addview: " + getChildCount() + "loop nr: " + i);
			NodeView v = adapter.getView(nd);
			v.leftSpaceView.setLayoutParams(new LayoutParams(
					nd.getTreeLevel() * 10, LayoutParams.MATCH_PARENT));
			addView(v);
			if (nd.isExpanded() && nd.getChildNodes().size() > 0) {
				System.out.println("add childviews");
				addNodeViews(nd);
			}
		}
	}

	private void updateViews() {
		removeAllViews();
		addNodeViews(adapter.getTree().rootNode);
		System.out.println(adapter.getTree());
		System.out.println(adapter.getTree().rootNode);
	}

	@Override
	public void update(Observable observable, Object data) {
		updateViews();
	}

}
