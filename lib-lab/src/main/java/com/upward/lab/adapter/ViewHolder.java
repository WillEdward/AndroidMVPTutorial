package com.upward.lab.adapter;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.upward.lab.R;
import com.upward.lab.util.UPUtils;

public class ViewHolder {

    private Context context;
	private SparseArray<View> mViews;
	private View mConvertView;

	private ViewHolder(Context context, ViewGroup parent, int layoutId) {
        this.context = context;
		mViews = new SparseArray<>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId);
		}
		return (ViewHolder) convertView.getTag();
	}

	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public void setText(int viewId, String text) {
		TextView textView = getView(viewId);
		textView.setText(text);
	}

    public void setImage(int viewId, int imgRes) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(imgRes);
    }

    public void setImage(int viewId, String imgUrl) {
        ImageView imageView = getView(viewId);
        if (!TextUtils.isEmpty(imgUrl)) {
            Picasso.with(context)
                    .load(imgUrl)
                    .resize(UPUtils.dp2px(80), UPUtils.dp2px(80))
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.placeholder);
        }
    }

    public void setImage(int viewId, String imgUrl, int placeholder) {
        ImageView imageView = getView(viewId);
        if (!TextUtils.isEmpty(imgUrl)) {
            Picasso.with(context)
                    .load(imgUrl)
                    .resize(UPUtils.dp2px(80), UPUtils.dp2px(80))
                    .centerCrop()
                    .placeholder(placeholder)
                    .into(imageView);
        } else {
            imageView.setImageResource(placeholder);
        }
    }

	public View getConvertView() {
		return mConvertView;
	}

    public void setVisibility(int viewId, boolean visible) {
        getView(viewId).setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
    }

}
