package ch.fhnw.students.keller.benjamin.sarha.fsm.ui;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnLongClickListener;
import ch.fhnw.students.keller.benjamin.sarha.fsm.Condition;
import ch.fhnw.students.keller.benjamin.tree.NodeView;
import ch.fhnw.students.keller.benjamin.tree.Tree;
import ch.fhnw.students.keller.benjamin.tree.TreeAdapter;
import ch.fhnw.students.keller.benjamin.tree.TreeNode;

public class ConditionAdapter extends TreeAdapter {

	public ConditionAdapter(Tree tree, Context context) {
		super(tree, context);
	}

	@Override
	public NodeView getView(final TreeNode node) {
		ConditionView v = new ConditionView(context, (Condition) node);
		v.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				getTree().selectTreeNode(node);
				DialogFragment newFragment = new ConditionDialog(context,
						(Condition) ((ConditionView) v).node, false);
				newFragment.show(((TransitionActivity)context).getSupportFragmentManager(), "dialog");
				return true;
			}

		});
		return v;
	}

}
