package com.reneelab.androidundeleter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 */
public class MCsFileChoose extends Fragment {

    private Button Nextbtn;
    private MCsEntry main;
    private Fragment switchcontent;
    private LinearLayout backarea;
    private Fragment backcurrent;
    private LinearLayout img,c_video,c_mp3,c_word,zip_option,apk_option;
    private CheckBox img_check,c_video_check,c_mp3_check,c_word_check,c_zip,c_apk;
    private ScrollView box;
    private List<CheckBox> checkBoxs;
    private  ArrayList file_type;
    private MCsSwitchFragment switchFragment;
    private ArrayList<Integer> cs = new ArrayList<Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backcurrent = new MCsContentFragment();
        checkBoxs=new ArrayList<CheckBox>();
        file_type = new ArrayList();
        switchFragment = new MCsSwitchFragment(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.file_cho, null);
        box = (ScrollView)view.findViewById(R.id.file_c);
        Nextbtn = (Button)view.findViewById(R.id.Next);
        Nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getValues();
               // DialogView(getContext());
                //Fragment n = new MCsFileList();
                //switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()), n);
            }
        });
        backarea = (LinearLayout)view.findViewById(R.id.backarea);
        backarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()), backcurrent);
                //main.openSliding();
                getFragmentManager().popBackStack();
            }
        });
        initView(inflater);
        return view;
    }

    public void DialogView(Context con){
        final AlertDialog builder = new AlertDialog.Builder(getContext()).create();
        builder.show();
        LayoutInflater toc = LayoutInflater.from(getContext());
        View view2 = toc.inflate(R.layout.dialogview, null);
        RelativeLayout c2s = (RelativeLayout)view2.findViewById(R.id.change2save);
        /*c2s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"改变空间触摸有效",Toast.LENGTH_SHORT).show();
            }
        });*/
        c2s.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    v.setBackgroundResource(R.color.line);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    v.setBackgroundResource(R.color.white);
                    //Toast.makeText(getContext(),"改变空间触摸有效",Toast.LENGTH_SHORT).show();
                    builder.dismiss();
                    DialogView2(getContext());
                }
              return false;
            }
        });

        builder.getWindow().setContentView(view2);
    }

    public void DialogView2(Context con){
        AlertDialog builder = new AlertDialog.Builder(getContext()).create();
        builder.show();
        LayoutInflater toc = LayoutInflater.from(getContext());
        View view2 = toc.inflate(R.layout.dialog_save_choose, null);


        builder.getWindow().setContentView(view2);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(LayoutInflater inflater){
        View view2 = inflater.inflate(R.layout.file_cho_opt,null);
        /*img = (LinearLayout)view2.findViewById(R.id.img_option);
        img.setTag(1);*/
        c_video = (LinearLayout)view2.findViewById(R.id.video_option);
        c_video.setTag(2);
        c_mp3 = (LinearLayout)view2.findViewById(R.id.mp3_option);
        c_mp3.setTag(3);
        c_word = (LinearLayout)view2.findViewById(R.id.word_option);
        c_word.setTag(4);

       /* img_check = (CheckBox)view2.findViewById(R.id.c_img);
        img_check.setTag(1);*/
        c_video_check = (CheckBox)view2.findViewById(R.id.c_video);
        c_video_check.setTag(2);
        c_mp3_check = (CheckBox)view2.findViewById(R.id.c_mp3);
        c_mp3_check.setTag(3);
        c_word_check = (CheckBox)view2.findViewById(R.id.c_word);
        c_word_check.setTag(4);


        /*img.setOnClickListener(listener);*/
        c_video.setOnClickListener(listener);
        c_mp3.setOnClickListener(listener);
        c_word.setOnClickListener(listener);


        box.addView(view2);

        /*checkBoxs.add(img_check);*/
        checkBoxs.add(c_video_check);
        checkBoxs.add(c_mp3_check);
        checkBoxs.add(c_word_check);
        /*checkBoxs.add(c_zip);
        checkBoxs.add(c_apk);*/
    }

    public void getValues() {

        String content = "";

        int scanType = 0;
        for (CheckBox cbx : checkBoxs) {
            if (cbx.isChecked()) {
                content = "ok";
                int i = (int)cbx.getTag();
                cs.add(i);
            }
        }

        if ("".equals(content)) {
            Toast.makeText(getContext(),"请至少选择一项",Toast.LENGTH_SHORT).show();
        }else{
            scanType = scanType(cs);
            final Bundle bundle = new Bundle();
            bundle.putInt("Type", 1);
            bundle.putInt("scanType", scanType);
            switchcontent =new  MCsEqAnalyse();
            switchcontent.setArguments(bundle);
            switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()),switchcontent);
        }
    }

    private int scanType(ArrayList<Integer> arr){
        int a = arr.size();
        switch (a){
            case 1:
                if(arr.get(0) == 1)     return 1;
                else if(arr.get(0)==2) return 4;
                else if(arr.get(0)==3) return 2;
                else if(arr.get(0)==4) return 8;
                break;
            case 2:
                if(arr.get(0)==1){
                  if(arr.get(1)==2)         return 5;
                  else if (arr.get(1)==3)  return 3;
                  else if(arr.get(1)==4)   return 9;
                }else if(arr.get(0)==2){
                    if(arr.get(1)==3)        return 6;
                    else if(arr.get(1)==4)   return 12;
                }else if(arr.get(0)==3){
                    if(arr.get(1)==4)        return 10;
                }
            case 3 :
                if(arr.get(0)==1){
                    if(arr.get(1)==2){
                        if(arr.get(2)==3) return 7;
                        else if(arr.get(2)==4) return 13;
                    }else if(arr.get(1)==3){
                      return  11;
                    }
                }else if(arr.get(0)==2){
                   return 14;
                }
            case 4:
                return 15;

        }
        return 0;
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             int type =(Integer) v.getTag();
            switch (type){
                case 1:
                    if(img_check.isChecked())
                        img_check.setChecked(false);
                    else
                        img_check.setChecked(true);
                    break;
                case 2:
                    if (c_video_check.isChecked())
                        c_video_check.setChecked(false);
                    else
                        c_video_check.setChecked(true);
                    break;
                case 3:
                    if (c_mp3_check.isChecked())
                        c_mp3_check.setChecked(false);
                    else
                        c_mp3_check.setChecked(true);
                    break;
                case 4:
                    if (c_word_check.isChecked())
                        c_word_check.setChecked(false);
                    else
                        c_word_check.setChecked(true);
                    break;
               /* case 5:
                    if(c_zip.isChecked())
                        c_zip.setChecked(false);
                    else
                        c_zip.setChecked(true);
                    break;
                case 6:
                    if(c_apk.isChecked())
                        c_apk.setChecked(false);
                    else
                        c_apk.setChecked(true);
                    break;*/
            }
        }
    };



}
