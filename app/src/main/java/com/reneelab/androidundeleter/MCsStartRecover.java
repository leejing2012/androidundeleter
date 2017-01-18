package com.reneelab.androidundeleter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reneelab.DataModel.PicModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class MCsStartRecover extends Fragment {
    MCsEntry main;
    Fragment Contactcontent;
    private LinearLayout sbtn;
    private LinearLayout topright;
    private Button startToscan;
    private String recoverType;
    private MCsSwitchFragment switchFragment;
    private int recordNum = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchFragment = new MCsSwitchFragment(getActivity());
        recoverType = getArguments().getString("Type");
        main = (MCsEntry) getActivity();
        main.closeSliding();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.between_interface, null);
        final Bundle bundle = new Bundle();
        switch (recoverType){
           case "photo":
               SharedPreferences preferences = getActivity().getSharedPreferences("save_pic", Context.MODE_PRIVATE);
               String pic_json = preferences.getString("pic_json", null);
               if(pic_json!=null){
                   Gson gs = new Gson();
                   Type type = new TypeToken<List<PicModel>>(){}.getType();
                   ArrayList<PicModel> pic_read_record = new ArrayList<PicModel>();
                   pic_read_record = gs.fromJson(pic_json,type);
                   recordNum = pic_read_record.size();
                   initView(view,getResources().getColor(R.color.photo), R.drawable.undelete_photo_ok,getResources().getString(R.string.recoverPhot));
               }else{
                   initView(view,getResources().getColor(R.color.photo), R.drawable.photo_scan,getResources().getString(R.string.recoverPhot));
               }

               break;
           case "sms":
               initView(view,getResources().getColor(R.color.sms), R.drawable.sms_scan,getResources().getString(R.string.recoverSms));
               break;
          /* case "txt":
               initView(view,getResources().getColor(R.color.txt), R.drawable.undelete_txt_logo,getResources().getString(R.string.recoverTxt));
               break;*/
           case "contact":
               initView(view,getResources().getColor(R.color.contacts), R.drawable.contacter_scan,getResources().getString(R.string.recoverContact));
               break;
           case "call":
               initView(view,getResources().getColor(R.color.call), R.drawable.comm_scan,getResources().getString(R.string.recoverCall));
               break;
        }
        sbtn = (LinearLayout)view.findViewById(R.id.backarea);
        main = (MCsEntry)getActivity();
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.openSliding();
                getFragmentManager().popBackStack();

            }
        });
        startToscan = (Button)view.findViewById(R.id.startTocontact);
        startToscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (recoverType){
                    case "photo":
                        final Bundle bundle = new Bundle();
                        bundle.putBoolean("record", false);
                        Contactcontent = new MCsPhoto();
                        Contactcontent.setArguments(bundle);
                        break;
                    case "sms":
                        Contactcontent = new MCsSms();
                        break;
                    case "contact":
                        Contactcontent = new MCsContacterData();
                        break;
                    case "call":
                        Contactcontent = new MCsCommData();
                        break;
                    default:
                        break;
                }
                switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()), Contactcontent);
            }
        });
        view.setClickable(true);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.isAdded()){
            getActivity().getSupportFragmentManager().putFragment(outState, "mFragment", this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private  void initView(View view,int color,int background,String title){
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.wholebackground);
        ImageView imageview = (ImageView)view.findViewById(R.id.pic);
        TextView textView = (TextView)view.findViewById(R.id.title);
        TextView record = (TextView)view.findViewById(R.id.recordNum);
        Button btnRecord = (Button)view.findViewById(R.id.startRecord);
        RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.backUp);
        topright = (LinearLayout)view.findViewById(R.id.topright);
        //topright.setBackgroundColor(color);
        //layout.setBackgroundColor(color);
        //relativeLayout.setBackgroundColor(color);
        imageview.setBackgroundResource(background);
        textView.setText(title);
        if(recordNum>0){
            record.setText(String.valueOf(recordNum)+"张图片");
            btnRecord.setVisibility(View.VISIBLE);
            final Bundle bundle = new Bundle();
            bundle.putBoolean("record", true);
            Contactcontent = new MCsPhoto();
            Contactcontent.setArguments(bundle);
            btnRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()), Contactcontent);
                }
            });
        }
    }
}
