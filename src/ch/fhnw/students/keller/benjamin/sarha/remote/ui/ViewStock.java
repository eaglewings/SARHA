package ch.fhnw.students.keller.benjamin.sarha.remote.ui;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.comm.DeviceModel;
import ch.fhnw.students.keller.benjamin.sarha.comm.Protocol;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;
import ch.fhnw.students.keller.benjamin.sarha.config.IOs;

public class ViewStock {
	private ArrayList<View> views;
	private Config config;
	private LayoutInflater inflater;
	private Context context;
	private DeviceModel deviceModel;
	private Handler handler;
	private Protocol protocol;

	public ViewStock(Context context, DeviceModel deviceModel) {
	}

	public ViewStock(Context context, DeviceModel deviceModel, Protocol protocol) {
		this.protocol = protocol;
		views = new ArrayList<View>();
		System.out.println("views");
		this.deviceModel = deviceModel;
		config = deviceModel.getConfig();
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		System.out.println("inflater");
		handler = new Handler();
		for (IO.Type type : config.types) {
			if(type!=IO.Type.TMR){
			for (IOs io : config.ios.get(type)) {
				System.out.println(type);
				views.add(createView(io, type));
			}}
		}
		System.out.println("viewstock Constructor end");
	}

	public void addViews(ViewGroup parent) {
		parent.removeAllViews();
		int i = 0;
		for (View view : views) {
			parent.addView(view, i);
			i++;
		}
	}

	public View getView(int pos) {
		return views.get(pos);

	}

	public IOs getItem(int pos) {
		return ((Tag) views.get(pos).getTag()).io;
	}

	private View createView(IOs io, IO.Type type) {
		View view = null;

		Tag tag = new Tag();
		System.out.println("newtag");
		switch (type) {

		case DO:

			view = inflater.inflate(R.layout.remote_listviewitem_digital_out,
					null);

			tag.toggleButton = (ToggleButton) view
					.findViewById(R.id.toggleButton);

			tag.toggleButton.setOnClickListener(tag.toggleClickListener);
			tag.ivDO = (ImageView) view.findViewById(R.id.imageView2);

			break;
		case DI:
			view = inflater.inflate(R.layout.remote_listviewitem_digital_in,
					null);
			tag.toggleButton = (ToggleButton) view
					.findViewById(R.id.toggleButton1);
			tag.toggleButton.setOnClickListener(tag.toggleClickListener);
			tag.ivDI = (ImageView) view.findViewById(R.id.imageView2);
			break;
		case AO:
			view = inflater.inflate(R.layout.remote_listviewitem_analog_out,
					null);

			tag.seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
			tag.seekBar.setOnSeekBarChangeListener(tag.seekBarListener);
			tag.seekBar.setThumb(tag.thumbao);
			tag.seekBar.setThumbOffset(0);
			break;
		case AI:
			view = inflater.inflate(R.layout.remote_listviewitem_analog_in,
					null);

			tag.seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
			tag.seekBar.setOnSeekBarChangeListener(tag.seekBarListener);
			tag.seekBar.setThumb(tag.thumbai);
			tag.seekBar.setThumbOffset(0);
			tag.seekBar.setMax(4095);

			break;
		}
		tag.ivOverride = (ImageView) view.findViewById(R.id.imageView1);
		tag.ivOverride.setOnClickListener(tag.clickListener);
		tag.textView = (TextView) view.findViewById(R.id.textView1);
		tag.io = io;
		tag.view = view;
		deviceModel.addObserver(tag);
		io.addObserver(tag);
		tag.textView.setText(io.name);
		tag.update(null, null);
		view.setTag(tag);
		return view;
	}

	public class Tag implements Observer {

		public View view;
		public IOs io;
		public TextView textView;
		public ToggleButton toggleButton;
		public SeekBar seekBar;
		public ImageView ivOverride, ivDI, ivDO;
		public int position;
		public Drawable thumbao = context.getResources().getDrawable(
				R.drawable.remote_seekbar_thumb_analog_out);
		public Drawable thumbai = context.getResources().getDrawable(
				R.drawable.remote_seekbar_thumb_analog_in);
		public Drawable seekBarDebug = context.getResources().getDrawable(
				R.drawable.remote_seekbar_debug);
		public Drawable seekBarNormal = context.getResources().getDrawable(
				R.drawable.remote_seekbar_normal);

		public OnTouchListener seekbarDisableTouchListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return true;
			}
		};

		Toast t = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		public OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				t.setText("clicked on: " + io.name);
				t.show();

				if (io.isOverridden()) {

					deviceModel.setIOreal(io);
					if (protocol != null) {
						protocol.setAddressReal(io);
					}
				} else {
					deviceModel.setIOoverride(io);
					if (protocol != null) {
						protocol.setAddressValue(io,
								(toggleButton.isChecked()) ? 1 : 0);
					}
				}
			}
		};
		public OnClickListener toggleClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				t.setText(io.name + ": " + ((ToggleButton) v).isChecked());
				t.show();
				if (deviceModel.isProgramRunning()) {
					deviceModel.setIOoverrideValue(io,
							(((ToggleButton) v).isChecked()) ? 1 : 0);
					deviceModel.setIOoverride(io);

					System.out.println("onclick" + io.getOverrideValue());
				}
				
					if (protocol != null) {
						protocol.setAddressValue(io,
								(((ToggleButton) v).isChecked()) ? 1 : 0);
					}

			}
		};
		public OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					update(null, null);
					t.setText("set;io;" + io.address + ";" + progress);
					t.show();
					if (deviceModel.isProgramRunning() && deviceModel.isDebug()) {
						deviceModel.setIOoverrideValue(io, progress);
						deviceModel.setIOoverride(io);

					} 
					
						if (protocol != null) {
							protocol.setAddressValue(io, progress);
						}
					

					switch (io.type) {
					case AO:

						break;
					case AI:

						break;
					}
				}

			}
		};

		@Override
		public void update(Observable arg0, Object arg1) {
			handler.post(updateRunnable);

		}

		private Runnable updateRunnable = new Runnable() {

			@Override
			public void run() {
				int prog;
				switch (io.type) {
				case DO:
					if (deviceModel.isProgramRunning()) {
						if (deviceModel.isDebug()) {
							toggleButton.setVisibility(View.VISIBLE);
							toggleButton
									.setChecked((io.getOverrideValue() >= 1) ? true
											: false);
						} else {
							toggleButton.setVisibility(View.INVISIBLE);
						}
						if (io.getValue() > 0) {
							ivDO.setImageResource(R.drawable.dion);
						} else {
							ivDO.setImageResource(R.drawable.dioff);
						}

					} else {
						toggleButton.setVisibility(View.VISIBLE);
						toggleButton
								.setChecked((io.getOverrideValue() >= 1) ? true
										: false);
						ivDO.setImageDrawable(null);
					}

					break;

				case DI:
					if (deviceModel.isProgramRunning() && deviceModel.isDebug()) {

						toggleButton.setVisibility(View.VISIBLE);
						toggleButton
								.setChecked((io.getOverrideValue() >= 1) ? true
										: false);
					} else {
						toggleButton.setVisibility(View.INVISIBLE);
					}
					if (io.getValue() > 0) {
						ivDI.setImageResource(R.drawable.dioff);
					} else {
						ivDI.setImageResource(R.drawable.dion);
					}
					break;

				case AO:
					prog = seekBar.getProgress();
					if (deviceModel.isProgramRunning()) {
						if (deviceModel.isDebug()) {

							seekBar.setOnTouchListener(null);
							seekBar.setProgress(io.getOverrideValue());
							seekBar.setProgressDrawable(seekBarDebug);
							thumbao.setAlpha(255);
							seekBar.setSecondaryProgress(io.getValue());
						} else {
							thumbao.setAlpha(0);
							seekBar.setProgress(io.getValue());
							seekBar.setOnTouchListener(seekbarDisableTouchListener);
							seekBar.setProgressDrawable(seekBarNormal);

						}

					} else {

						seekBar.setOnTouchListener(null);
						seekBar.setProgressDrawable(seekBarNormal);
						seekBar.setProgress(io.getValue());
						thumbao.setAlpha(255);

					}
					seekBar.setProgress(seekBar.getMax());
					// seekBar.invalidate();
					seekBar.setProgress(prog);
					break;
				case AI:

					if (deviceModel.isDebug() && deviceModel.isProgramRunning()) {

						thumbai.setAlpha(255);
						seekBar.setOnTouchListener(null);
						seekBar.setProgress(io.getOverrideValue());
						seekBar.setProgressDrawable(seekBarDebug);
						seekBar.setSecondaryProgress(io.getValue());
					} else {
						seekBar.setProgressDrawable(seekBarNormal);
						thumbai.setAlpha(0);
						seekBar.setOnTouchListener(seekbarDisableTouchListener);
						seekBar.setProgress(io.getValue());

					}

					break;
				}
				if (deviceModel.isDebug() && deviceModel.isProgramRunning()) {
					if (io.isOverridden()) {
						ivOverride
								.setImageResource(R.drawable.remote_override_enabled);
					} else {
						ivOverride
								.setImageResource(R.drawable.remote_override_disabled);
					}
				} else {
					ivOverride.setImageDrawable(null);
				}

			}

		};
	}

}
