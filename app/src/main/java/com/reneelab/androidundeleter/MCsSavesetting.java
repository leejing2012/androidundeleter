package com.reneelab.androidundeleter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reneelab.androidundeleter.utils.FileSizeUtil;

/**
 * Created by Administrator on 2016/7/6.
 */
public class MCsSavesetting extends Fragment {
    private MCsEntry main;
    private CheckBox echeckbox,icheckbox;
    private TextView extra_save,inter_save;
    private LinearLayout backarea;
    private FileSizeUtil fileutil;
    private int save_path=0;
    private MCsSwitchFragment switchFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileutil = new FileSizeUtil();
        switchFragment = new MCsSwitchFragment(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.save_choose, null);
        echeckbox = (CheckBox)view.findViewById(R.id.externalStorge);
        icheckbox = (CheckBox)view.findViewById(R.id.innerStorge);
        backarea = (LinearLayout)view.findViewById(R.id.backarea);
        extra_save = (TextView)view.findViewById(R  .id.extra_save);
        inter_save = (TextView)view.findViewById(R.id.inter_save);

        extra_save.setText(fileutil.formatFileSize(fileutil.getTotalExternalMemorySize(),true));
        inter_save.setText(fileutil.formatFileSize(fileutil.getTotalInternalMemorySize(),true));

        backarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Fragment sure_save = new MCsEqAnalyse();
                final Bundle bundle = new Bundle();
                bundle.putInt("Type", save_path);
                sure_save.setArguments(bundle);
                switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()),sure_save);
            }
        });
        echeckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    save_path = 1;
                    icheckbox.setChecked(false);
                }

            }
        });

        icheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   save_path = 2;
                   echeckbox.setChecked(false);
               }
            }
        });
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
