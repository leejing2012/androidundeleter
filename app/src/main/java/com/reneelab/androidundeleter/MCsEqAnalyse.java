package com.reneelab.androidundeleter;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 */
public class MCsEqAnalyse extends Fragment {
    public MCsEntry main;
    private LinearLayout backarea;
    private Fragment backcurrent,MultiplefilesScan;
    private ImageView imageroot;
    private ScrollView box_check;
    private Button change2save,Nextbtn;
    private int saveType=0 ;
    private int scanFlag =0;

    private final static int kSystemRootStateUnknow = -1;
    private final static int kSystemRootStateDisable = 0;
    private final static int kSystemRootStateEnable = 1;
    private static int systemRootState = kSystemRootStateUnknow;
    private MCsSwitchFragment switchFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveType = getArguments().getInt("Type");
        scanFlag = getArguments().getInt("scanType");
        switchFragment = new MCsSwitchFragment(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        backcurrent = new MCsContentFragment();
        MultiplefilesScan = new MCsRecoverFile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eq_analyse, null);
        box_check = (ScrollView)view.findViewById(R.id.check_box);

        Nextbtn = (Button)view.findViewById(R.id.Next);
        Nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putInt("scanType", scanFlag);
                MultiplefilesScan.setArguments(bundle);
                switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()),MultiplefilesScan);
            }
        });


        backarea = (LinearLayout)view.findViewById(R.id.backarea);
        backarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        isRootSystem();
        initView(inflater);
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initView(LayoutInflater inflater){
        View view2 =  inflater.inflate(R.layout.eq_check, null);
        change2save = (Button)view2.findViewById(R.id.c2s);
        imageroot = (ImageView)view2.findViewById(R.id.root_check);
        change2save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Fragment setting = new MCsSavesetting();
                switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()),setting);
            }
        });
        if(isRootSystem()){
            imageroot.setImageResource(R.drawable.recovery_checker_success);
        }
        if(saveType == 1){
            TextView savepath = (TextView)view2.findViewById(R.id.savepath);
            savepath.setText("外置存储2");
        }else if(saveType == 2){
            TextView savepath = (TextView)view2.findViewById(R.id.savepath);
            savepath.setText("内置存储");
        }

        box_check.addView(view2);
    }

    public static boolean isRootSystem() {
        if (systemRootState == kSystemRootStateEnable) {
            return true;
        } else if (systemRootState == kSystemRootStateDisable) {
            return false;
        }
        File f = null;
        final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/" };
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()) {
                    systemRootState = kSystemRootStateEnable;
                    return true;
                }
            }
        } catch (Exception e) {
        }
        systemRootState = kSystemRootStateDisable;
        return false;
    }

    /**
     * 获取内置SD卡路径
     * @return
     */
    public String getInnerSDCardPath() {
       // return Environment.getExternalStorageDirectory().getPath();
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取外置SD卡路径
     * @return  应该就一条记录或空
     */
    public List<String> getExtSDCardPath()
    {
        List<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard"))
                {
                    String [] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory())
                    {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }


}
