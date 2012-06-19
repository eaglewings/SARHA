package ch.fhnw.students.keller.benjamin.sarha.remote.ui;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import ch.fhnw.students.keller.benjamin.sarha.R;
import ch.fhnw.students.keller.benjamin.sarha.comm.DeviceModel;
import ch.fhnw.students.keller.benjamin.sarha.config.Config;
import ch.fhnw.students.keller.benjamin.sarha.config.IO;
import ch.fhnw.students.keller.benjamin.sarha.config.IOs;

public class ConfigRemoteAdapter extends BaseAdapter {
	private DeviceModel deviceModel;
	private Config config;
	private Context context;
	private enum Viewtypes {
		DO, DI, AO, AI, HEADER, // Use same order as in IO.Type!
	};

	private LayoutInflater inflater;

	public ConfigRemoteAdapter(Context context, DeviceModel device) {
		this.deviceModel = device;
		this.config = device.getConfig();
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {

		return config.countH();
	}

	@Override
	public Object getItem(int position) {
		int counter = position;
		for (IO.Type type : config.types) {
			if (config.count(type) == 0) {
				continue;
			}

			if (counter == 0) {
				// its a Header
				return IO.typeHeaders.get(type);
			}
			if (config.count(type) >= counter) {
				// its an item
				return config.ios.get(type).get(counter - 1);
			} else {
				counter = counter - config.countH(type);
			}

		}
		/*
		 * for (IO.Type type : IO.Type.values()) { if (position == 0) { return
		 * IO.typeHeaders.get(type); } if (config.count(type) >= position) {
		 * return config.ios.get(type).get(position - 1); } else { position =
		 * position - config.countH(type); }
		 * 
		 * }
		 */

		return null;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		Object item = getItem(position);
		if (item instanceof IOs) {
			return true;
		}

		return false;

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		IOs io = null;
		if (getItem(position) instanceof IOs) {
			io = (IOs) getItem(position);
		}
		holder = new ViewHolder();
		Viewtypes type = Viewtypes.values()[getItemViewType(position)];

		if (convertView == null) {
			
			holder.position = position;
			switch (type) {
			case DO:
				convertView = inflater.inflate(R.layout.remote_listviewitem_digital_out, null);
			case DI:
				convertView = inflater.inflate(R.layout.remote_listviewitem_digital_in, null);
				break;
			case AO:
				convertView = inflater.inflate(R.layout.remote_listviewitem_analog_out, null);
				break;
			case AI:
				convertView = inflater.inflate(R.layout.remote_listviewitem_analog_in, null);

				break;
			case HEADER:
				convertView = inflater.inflate(R.layout.remote_listviewitem_header, null);
				break;
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		switch (type) {
		case DO:
			holder.toggleButton = (ToggleButton) convertView
					.findViewById(R.id.toggleButton1);
			holder.toggleButton.setOnClickListener(holder.toggleClickListener);
			holder.ivDO = (ImageView) convertView.findViewById(R.id.imageView2);
			break;
		case DI:

			holder.toggleButton = (ToggleButton) convertView
					.findViewById(R.id.toggleButton1);
			holder.toggleButton.setOnClickListener(holder.toggleClickListener);
			holder.ivDI = (ImageView) convertView.findViewById(R.id.imageView2);
			break;
		case AO:
			holder.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar1);
			holder.seekBar.setOnSeekBarChangeListener(holder.seekBarListener);
			holder.seekBar.setThumb(holder.thumb);
			holder.seekBar.setThumbOffset(12);
			break;
		case AI:
			holder.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar1);
			holder.seekBar.setOnSeekBarChangeListener(holder.seekBarListener);
			holder.seekBar.setThumb(holder.thumb);
			holder.seekBar.setThumbOffset(12);

			break;
		}
		holder.textView = (TextView) convertView.findViewById(R.id.textView1);
		if (io != null) {

			holder.ivOverride = (ImageView) convertView
					.findViewById(R.id.imageView1);
			holder.ivOverride.setOnClickListener(holder.clickListener);
			if (deviceModel.isDebug() && deviceModel.isProgramRunning()) {
				if (io.isOverridden()) {
					holder.ivOverride.setImageResource(R.drawable.remote_override_enabled);
				} else {
					holder.ivOverride
							.setImageResource(R.drawable.remote_override_disabled);
				}
			} else {
				holder.ivOverride.setImageDrawable(null);
			}
			io.addObserver(holder);
			deviceModel.addObserver(holder);
			holder.io = io;
			holder.view = convertView;
			holder.textView.setText(io.name);
			holder.update(null, null);
		} else {
			holder.textView.setText("text");
		}

		return convertView;

	}

	@Override
	public int getViewTypeCount() {
		return Viewtypes.values().length;
	}

	@Override
	public int getItemViewType(int position) {
		Object item = getItem(position);
		if (item instanceof IOs) {
			return ((IOs) item).type.ordinal();
		} else {
			return Viewtypes.HEADER.ordinal();
		}
	}

	public class ViewHolder implements Observer {
		public View view;
		public IOs io;
		public TextView textView;
		public ToggleButton toggleButton;
		public SeekBar seekBar;
		public ImageView ivOverride, ivDI, ivDO;
		public int position;
		public Drawable thumb = context.getResources().getDrawable(
				R.drawable.remote_seekbar_thumb_analog_in);

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
				t.setText("clicked on: " + ((IOs) getItem(position)).name);
				t.show();
				IOs io = (IOs) getItem(position);
				if (io.isOverridden()) {
					deviceModel.setIOreal(io);
				} else {
					deviceModel.setIOoverride(io);
				}
			}
		};
		public OnClickListener toggleClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				IOs io = (IOs) getItem(position);
				t.setText(((IOs) getItem(position)).name + ": "
						+ ((ToggleButton) v).isChecked());
				t.show();
				if (deviceModel.isProgramRunning()) {
					deviceModel.setIOoverride(io);
					deviceModel.setIOoverrideValue(io,
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
				IOs io = (IOs) getItem(position);
				t.setText("set;io;" + io.address + ";" + progress);
				t.show();
				if (deviceModel.isProgramRunning()) {
					deviceModel.setIOoverrideValue(io, seekBar.getProgress());
				}

				switch (io.type) {
				case AO:

					break;
				case AI:
					io.override();
					// ivOverride.setAlpha(255);
					break;

				default:
					break;
				}

			}
		};

		@Override
		public void update(Observable observable, Object data) {
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
					toggleButton.setChecked((io.getOverrideValue() >= 1) ? true
							: false);
					ivDO.setImageDrawable(null);
				}

				break;

			case DI:
				if (deviceModel.isProgramRunning() && deviceModel.isDebug()) {

					toggleButton.setVisibility(View.VISIBLE);
					toggleButton.setChecked((io.getOverrideValue() >= 1) ? true
							: false);
				} else {
					toggleButton.setVisibility(View.INVISIBLE);
				}
				if (io.getValue() > 0) {
					ivDI.setImageResource(R.drawable.dion);
				} else {
					ivDI.setImageResource(R.drawable.dioff);
				}
				break;

			case AO:

				if (deviceModel.isProgramRunning()) {
					if (deviceModel.isDebug()) {
						thumb.setAlpha(255);
						seekBar.setOnTouchListener(null);
						seekBar.setProgress(io.getOverrideValue());
					} else {
						thumb.setAlpha(0);
						seekBar.setOnTouchListener(seekbarDisableTouchListener);
						seekBar.setSecondaryProgress(0);
					}

				} else {
					thumb.setAlpha(255);
					seekBar.setOnTouchListener(null);
					seekBar.setSecondaryProgress(0);
				}

				break;
			case AI:

				seekBar.setProgress(io.getValue());
				break;

			}
			view.bringToFront();
		}

	}

}
