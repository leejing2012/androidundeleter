package com.reneelab.DataModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reneelab.androidundeleter.R;

public class MCsMainOptions extends BaseAdapter {
    private Context mContext;

    public String[] img_text = {
            "恢复照片", "恢复短信", "恢复联系人",/* "恢复TXT",*/
            "恢复WIFI","恢复通信录",/*"恢复微信" ,"恢复WhatsApp"*/};
    public int[] imgs = { R.drawable.undelete_icon_photo,
            R.drawable.undelete_icon_sms,
            R.drawable.undelete_icon_contact,
          /*  R.drawable.undelete_icon_txt,*/
            R.drawable.undelete_icon_wifi,
            R.drawable.undelete_icon_calls,
          /*  R.drawable.undelete_icon_wechat,
            R.drawable.undelete_icon_whatsapp*/

    };

    public MCsMainOptions(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return img_text.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.main_options, parent, false);
        }
        TextView tv = MCsBaseViewHolder.get(convertView, R.id.tv_item);
        ImageView iv = MCsBaseViewHolder.get(convertView, R.id.iv_item);
        iv.setBackgroundResource(imgs[position]);
        tv.setText(img_text[position]);

        return convertView;
    }
}
