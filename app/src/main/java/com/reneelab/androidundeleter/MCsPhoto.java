package com.reneelab.androidundeleter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reneelab.DataModel.PhotoWallAdapter;
import com.reneelab.DataModel.PicModel;
import com.reneelab.jni.CallBack;
import com.reneelab.jni.initJni;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MCsPhoto extends Fragment implements CallBack{
    private TextView title,stop_text,scan_type_text;
    private LinearLayout stopScan,photoHead;
    private RelativeLayout backarea;
    public MCsEntry main;

    private GridView mPhotoWall;
    private  Animation operatingAnim;
    private PhotoWallAdapter mAdapter;
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    Fragment f;
    private MCsSwitchFragment switchFragment;
    private Button pRecover;
    public ArrayList<PicModel> test,recoverList;
    boolean scanFlag = true;
    private TextView picNum,spercent;
    private int scan_num=0;
    private  int fresh_num = 0,srollState_num=0;
    private ImageView infoOperatingIV;
    private MCsPhoto k;
    private AlertDialog dialog_stop,dialog_save;
    private boolean scanFinish = false, noScan = false;
    private boolean recordFlag;
    boolean deep_scan =false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MCsEntry) getActivity();
        operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.tip);//进度圆圈
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        switchFragment = new MCsSwitchFragment(getActivity());
        test= new ArrayList<PicModel>();
        recoverList = new ArrayList<PicModel>();
        recordFlag = getArguments().getBoolean("record");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        k = this;
        f =getFragmentManager().findFragmentById(getId());
        View view = inflater.inflate(R.layout.photo_layout, null);
        initWidget(view);
        initDialog();
        ReadPic(view);
        return view;
    }

    Runnable runnable_logic = new Runnable() {
        @Override
        public void run() {
            initJni.getInstance().recover_type(getContext(),k,1,1);
        }
    };

    @Override
    public void ex_file(String out, int no, long size,String format) {
        // TODO Auto-generated method stub
            scan_num++;
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("fileName", out);
            bundle.putInt("fileId",no);
            bundle.putLong("fileSize",size);
            bundle.putInt("scanNum",scan_num);
            bundle.putString("format",format);
            msg.setData(bundle);
            handle_pic.sendMessage(msg);
    }


    Handler handle_pic = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Bundle bundle = msg.getData();
            final int pic_id = bundle.getInt("fileId");
            final  String pic_format = bundle.getString("format");
            //scan_num = bundle.getInt("scanNum");
            picNum.setText(String.valueOf(scan_num));
            PicModel pic1 = new PicModel();
            pic1.setPic_id(pic_id);
            pic1.setPic_name(bundle.getString("fileName"));
            pic1.setPic_size(bundle.getLong("fileSize"));
            pic1.setFormat(pic_format);
            test.add(pic1);
            fresh_num++;
                if(fresh_num < 50){
                    mPhotoWall.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }else if(fresh_num>50&&fresh_num==500){
                    mPhotoWall.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    fresh_num = 50;
                }
            if(scan_num>100){
                int per_err = (int)scan_num/100;
                spercent.setText(per_err+"%");
            }
        }
    };

    Handler handle_percent = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int per = msg.arg1;
            spercent.setText(per+"%");

            if(per==100&&deep_scan){
                infoOperatingIV.clearAnimation();
                scanFinish = true;
                scan_type_text.setText("全盘扫描结束！");
                stop_text.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(),"扫描结束",Toast.LENGTH_SHORT);
            }else {
                if(per==100&&!deep_scan){
                    scan_type_text.setText("深度扫描中.....");
                    initJni.getInstance().pic_deep_scan(1);
                    deep_scan = true;
                }
            }
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        mAdapter.fluchCache();
    }

    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.cancelAllTasks();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mAdapter.cancelAllTasks();
        execShell("chmod 777 /data/local/setenforce*");
        execShell("sh /data/local/setenforce1.sh");
    }

    public void previewDetail(String url,String format){
        Fragment n = new MCsPhotoDetail();
        final Bundle bundle = new Bundle();
        bundle.putString("Type", url);
        Bitmap scanBit;
        if(mAdapter.getBitmapFromMemoryCache(url) == null){
            scanBit =   mAdapter.getDiskCach(url);
        }else{
            scanBit = mAdapter.getBitmapFromMemoryCache(url);
        }
        bundle.putParcelable("detail_bitmap",scanBit);
        bundle.putString("format",format);
        n.setArguments(bundle);
        switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()),n);
    }

    @Override
    public void doSomething(String str){}

    public void show_sms(long phone,String body,String date){}

    @Override
    public void callback_getAccess(String path){}

    public void execShell(String cmd){
        try{
            Process p = Runtime.getRuntime().exec("su");
            OutputStream outputStream = p.getOutputStream();
            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }

    public void show_comm(long phone,String date,int duration){}

    public void callback_percent(int percent){
        Message msg_percent = new Message();
        msg_percent.arg1 = percent;
        handle_percent.sendMessage(msg_percent);
    }

    public void show_contacter(String phone,String name){}

    public void addRecoverList(PicModel pi){
       recoverList.add(pi);
    }

    public void deleteReoverList(PicModel pidel){
        for(int i = 0;i<recoverList.size();i++){
            if(pidel.getPic_name() == recoverList.get(i).getPic_name()){
                recoverList.remove(i);
            }
        }
    }

    public void saveBitmap(Bitmap bmp,String name) {
        File file = new File("/storage/emulated/0/都叫兽数据恢复/图片");
        if (!file.exists())
            file.mkdir();
        String newFilePath = "/storage/emulated/0/都叫兽数据恢复/图片" + "/" + name;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void initDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("停止扫描");
        builder.setMessage("请您确定是否停止扫描？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initJni.getInstance().stopScan();
                main.openSliding();
                scanFinish = true;
                infoOperatingIV.clearAnimation();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_stop = builder.create();

        AlertDialog.Builder builder_save = new AlertDialog.Builder(getContext());
        builder_save.setTitle("温馨提示");
        builder_save.setMessage("是否保存扫描结果？");
        builder_save.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SavePic();
                scan_num=0;
                getFragmentManager().popBackStackImmediate("com.reneelab.androidundeleter.MCsContentFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        builder_save.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scan_num=0;
                getFragmentManager().popBackStackImmediate("com.reneelab.androidundeleter.MCsContentFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        dialog_save = builder_save.create();
    }

    private void initWidget(View view){
        title = (TextView)view.findViewById(R.id.title);

        title.setText(getString(R.string.recoverPhot));
        stop_text = (TextView)view.findViewById(R.id.stop_text);
        backarea = (RelativeLayout) view.findViewById(R.id.ptback);
        stopScan = (LinearLayout)view.findViewById(R.id.stopScan);
        picNum = (TextView)view.findViewById(R.id.t_middle);
        spercent = (TextView)view.findViewById(R.id.Spercent);
        photoHead = (LinearLayout)view.findViewById(R.id.photoHead);
        infoOperatingIV= (ImageView)view.findViewById(R.id.photo_anim);
        if (operatingAnim != null) {
            infoOperatingIV.startAnimation(operatingAnim);
        }

        scan_type_text = (TextView)view.findViewById(R.id.down_font);

        backarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noScan){
                    getFragmentManager().popBackStackImmediate("com.reneelab.androidundeleter.MCsContentFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else {
                    if (scanFinish){
                        dialog_save.show();
                    }else {
                        dialog_stop.show();
                    }
                }
            }
        });

        stopScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scanFlag){
                    initJni.getInstance().suspendedScan();
                    stop_text.setText("继续扫描");
                    scanFlag = false;
                    infoOperatingIV.clearAnimation();
                }
                else{
                    initJni.getInstance().ContinueScan();
                    stop_text.setText("暂停扫描");
                    scanFlag = true;
                    infoOperatingIV.startAnimation(operatingAnim);
                }
            }
        });

        pRecover = (Button)view.findViewById(R.id.recover);
        pRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j<recoverList.size();j++){
                    Bitmap scanBit;
                    String tagurl = recoverList.get(j).getPic_name();
                    recoverList.get(j).setFlag(1);
                    if(mAdapter.getBitmapFromMemoryCache(tagurl) == null){
                        scanBit =   mAdapter.getDiskCach(tagurl);
                    }else{
                        scanBit = mAdapter.getBitmapFromMemoryCache(tagurl);
                    }
                    saveBitmap(scanBit,tagurl);
                }
                Toast.makeText(getActivity(),"图片恢复完成！",Toast.LENGTH_SHORT).show();
                mPhotoWall.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initPhotoWall(View view){
        mImageThumbSize = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);
        mPhotoWall = (GridView)view.findViewById(R.id.photo_wall);
        mAdapter = new PhotoWallAdapter(getContext(), 0,test,mPhotoWall,getFragmentManager().findFragmentById(getId()),this);
        mPhotoWall.setAdapter(mAdapter);
        mPhotoWall.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int numColumns = (int) Math.floor(mPhotoWall.getWidth() / (mImageThumbSize + mImageThumbSpacing));
                if (numColumns > 0) {
                    int columnWidth = (mPhotoWall.getWidth() / numColumns) - mImageThumbSpacing;
                    mAdapter.setItemHeight(columnWidth);
                    mPhotoWall.getViewTreeObserver() .removeGlobalOnLayoutListener(this);
                }
            }
        });

        mPhotoWall.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                srollState_num = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
               /*if(firstVisibleItem==0||srollState_num==0){
                   photoHead.setVisibility(View.VISIBLE);
               }else{
                   photoHead.setVisibility(View.GONE);
               }*/
                if (firstVisibleItem>4){
                    photoHead.setVisibility(View.GONE);
                }else {
                    photoHead.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void SavePic(){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("save_pic", Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(test);
        editor.putString("pic_json", json);
        editor.commit();
    }

    private void ReadPic(View view){
        SharedPreferences preferences = getActivity().getSharedPreferences("save_pic", Context.MODE_PRIVATE);
        String pic_json = preferences.getString("pic_json", null);
        if(pic_json!=null&&recordFlag){
            Gson gs = new Gson();
            Type type = new TypeToken<List<PicModel>>(){}.getType();
            ArrayList<PicModel> pic_read_record = new ArrayList<PicModel>();
            pic_read_record = gs.fromJson(pic_json,type);
            test = pic_read_record;
            initPhotoWall(view);
            noScan = true;
            infoOperatingIV.clearAnimation();
            spercent.setText("100%");
            picNum.setText(String.valueOf(test.size()));
        }else {
            initPhotoWall(view);
            Thread mThread = new Thread(runnable_logic);
            mThread.start();
            noScan = false;
        }
    }
}
