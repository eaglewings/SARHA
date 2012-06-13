package ch.fhnw.students.keller.benjamin.tree;

import java.util.Observable;
import java.util.Observer;

import ch.fhnw.students.keller.benjamin.sarha.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NodeView extends LinearLayout implements Observer{
	
	public TextView tv1,tv2;
	public Button bt;
	public TreeNode node;
	public View leftSpaceView;
	private OnClickListener expandClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(node.isExpanded()){
				node.collapse();
				
			}
			else{
				node.expand();
			}
		}
	};
	
	public NodeView(Context context, TreeNode node){
		super(context);
		System.out.println(node);
		this.node=node;
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(NodeView.this.node.isSelected()){
					NodeView.this.node.getTree().unselectTreeNode();
				}
				else{
					NodeView.this.node.getTree().selectTreeNode(NodeView.this.node);
				}
				
			}
		});
		View.inflate(context, R.layout.tree_node_view, this);
		tv1= (TextView) findViewById(R.id.textView1);
		tv2= (TextView) findViewById(R.id.textView2);
		bt= (Button) findViewById(R.id.button1);
		bt.setOnClickListener(expandClickListener);
		leftSpaceView = findViewById(R.id.view2);
		update(null,null);
		
	}
	public NodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
		
	}
	@Override
	public void update(Observable observable, Object data) {
		if(node.isSelected()){
			setBackgroundColor(Color.GREEN);
		}
		else{
			setBackgroundColor(Color.BLACK);
		}
		if(node.isExpanded()){
			bt.setText("-");
		}
		else{
			bt.setText("+");
		}
		if(node.getChildNodes().size()==0){
			bt.setVisibility(INVISIBLE);
		}
		else{
			bt.setVisibility(VISIBLE);
		}
		
	}

}
