package com.reneelab.androidundeleter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reneelab.DataModel.BarProgross;
import com.reneelab.DataModel.MCsAudioListView;
import com.reneelab.DataModel.MCsDocumentListView;
import com.reneelab.DataModel.MCsVideoListView;
import com.reneelab.DataModel.MutilFileScanProgressPar;
import com.reneelab.DataModel.MyPagerAdapter;
import com.reneelab.DataModel.audioModel;
import com.reneelab.DataModel.documentModel;
import com.reneelab.DataModel.videoModel;
import com.reneelab.jni.CallBack;
import com.reneelab.jni.initJni;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/10/17.
 */
public class MCsRecoverFile extends Fragment implements CallBack {
    private  int muti_file_flag = 0;
    private LinearLayout getback;
    private MCsSwitchFragment switchFragment;
    private Fragment main;
    private int scan_file_num = 0,pause=0;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View view1, view2, view3, view4, view5;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private ListView listViewAudio,listViewVideo,listViewDocument;
    private MCsAudioListView adapterAudio;
    private MCsVideoListView adapterVideo;
    private MCsDocumentListView adapterDocument;
    private  ArrayList<audioModel> audio_list,audio_recover_list;
    private  ArrayList<videoModel> video_list,video_recover_list;
    private  ArrayList<documentModel> document_list,document_recover_list;
    private TextView num;
    private MCsRecoverFile k;
    private ImageView undelete_pause;
    private Button recoverbtn;
    ExecutorService fixedThreadPool;
    private BarProgross mutilprogress;
    private MutilFileScanProgressPar asyncTask;
    private boolean scanFinish = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        muti_file_flag = getArguments().getInt("scanType");
        switchFragment = new MCsSwitchFragment(getActivity());
        main = new MCsContentFragment();
        audio_list = new ArrayList<audioModel>();
        audio_recover_list = new ArrayList<audioModel>();
        video_list = new ArrayList<videoModel>();
        video_recover_list = new ArrayList<videoModel>();
        document_list = new ArrayList<documentModel>();
        document_recover_list =  new ArrayList<documentModel>();
        fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scan_file, null);
        k = this;
        num =(TextView)view.findViewById(R.id.t_middle);
        undelete_pause = (ImageView) view.findViewById(R.id.undelete_pause);
        recoverbtn = (Button)view.findViewById(R.id.recover_file_mutil);
        mutilprogress = (BarProgross) view.findViewById(R.id.pb_progressbarf);
        initViewPager(view);
        getback = (LinearLayout)view.findViewById(R.id.backarea);

        getback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initJni.getInstance().stopScan();
                //switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()),main);
                getFragmentManager().popBackStackImmediate("com.reneelab.androidundeleter.MCsContentFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                scanFinish = true;
            }
        });

        undelete_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(pause == 0){
                pause = 1;
                  undelete_pause.setImageResource(R.drawable.recover_file_button_pressed);
                  initJni.getInstance().suspendedScan();
                  //initJni.getInstance().stopScan();
              }else{
                pause=0;
                  undelete_pause.setImageResource(R.drawable.recover_file_button);
                  initJni.getInstance().ContinueScan();
              }
            }
        });

        recoverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!audio_recover_list.isEmpty()){
                    try {
                        for (int i = 0;i<audio_recover_list.size();i++){
                            final byte[] buff =new byte[(int)audio_recover_list.get(i).getSize()];
                            final int j=i;
                            final int length_a = (int)audio_recover_list.get(i).getSize();
                            fixedThreadPool.execute(new Runnable() {
                                @Override
                                public void run() {
                                        int a = initJni.getInstance().readbuffer(audio_recover_list.get(j).getNumber(),0,(int)audio_recover_list.get(j).getSize(),buff);
                                        if(a == length_a){
                                            recoverFileToPhone(buff,2,audio_recover_list.get(j).getAudio_name());
                                            for(int r=0;r<audio_list.size();r++){
                                                if(audio_list.get(r).getNumber() == audio_recover_list.get(j).getNumber()){
                                                    audio_list.get(r).setFlag(1);
                                                }
                                            }
                                        }
                                }
                            });
                        }
                    }catch (IndexOutOfBoundsException e){
                        return;
                    }finally {
                        Toast.makeText(MCsRecoverFile.this.getActivity(),"音频成功恢复到 '/storage/emulated/0/都叫兽数据恢复/音频'",Toast.LENGTH_SHORT).show();
                        listViewAudio.setAdapter(adapterAudio);
                        adapterAudio.notifyDataSetChanged();
                    }
                }

               if(!video_recover_list.isEmpty()){
                   if (!video_recover_list.isEmpty()){
                       for (int i = 0;i<video_recover_list.size();i++){
                           try {
                               final byte[] buff =new byte[(int)video_recover_list.get(i).getSize()];
                               final int j=i;
                               final int length_a = (int)video_recover_list.get(i).getSize();
                               fixedThreadPool.execute(new Runnable() {
                                   @Override
                                   public void run() {
                                       int a = initJni.getInstance().readbuffer(video_recover_list.get(j).getNumber(),0,(int)video_recover_list.get(j).getSize(),buff);
                                       if(a == length_a){
                                           recoverFileToPhone(buff,1,video_recover_list.get(j).getVideo_name());
                                           for(int r=0;r<video_list.size();r++){
                                               if(video_list.get(r).getNumber() == video_recover_list.get(j).getNumber()){
                                                   video_list.get(r).setFlag(true);
                                               }
                                           }
                                       }

                                   }
                               });
                           }catch (IndexOutOfBoundsException e){
                               return;
                           }finally {
                               Toast.makeText(getContext(),"视频成功恢复到 '/storage/emulated/0/都叫兽数据恢复/视频'",Toast.LENGTH_SHORT).show();
                               listViewVideo.setAdapter(adapterVideo);
                               adapterVideo.notifyDataSetChanged();
                           }
                       }
                   }
               }

                if(document_recover_list.size()>0){
                    if (!document_recover_list.isEmpty()){
                        for (int i = 0;i<document_recover_list.size();i++){
                            try {
                                final byte[] buff =new byte[(int)document_recover_list.get(i).getSize()];
                                final int j=i;
                                final int length_a = (int)document_recover_list.get(i).getSize();
                                fixedThreadPool.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        int a = initJni.getInstance().readbuffer(document_recover_list.get(j).getNumber(),0,(int)document_recover_list.get(j).getSize(),buff);

                                        if(a == length_a){
                                            recoverFileToPhone(buff,3,document_recover_list.get(j).getDocument_name());
                                            for(int r=0;r<document_recover_list.size();r++){
                                                if(document_recover_list.get(r).getNumber() == document_recover_list.get(j).getNumber()){
                                                    document_recover_list.get(r).setFlag(true);
                                                }
                                            }
                                        }
                                    }
                                });
                            }catch (IndexOutOfBoundsException e){
                                return;
                            }finally {
                                Toast.makeText(getContext(),"文件成功恢复到 '/storage/emulated/0/都叫兽数据恢复/文件'",Toast.LENGTH_SHORT).show();
                                listViewDocument.setAdapter(adapterDocument);
                                adapterDocument.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        });
        asyncTask = new MutilFileScanProgressPar(mutilprogress,this);
        asyncTask.execute(0);

        Thread mThread = new Thread(runnable_logic);
        mThread.start();
        return view;
    }

    Runnable runnable_logic = new Runnable() {
        @Override
        public void run() {
            initJni.getInstance().recover_type(getContext(),k,1,muti_file_flag);
        }
    };


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void initViewPager(View view){
        mViewPager = (ViewPager)view.findViewById(R.id.vp_view);
        mTabLayout = (TabLayout)view.findViewById(R.id.tabs);
        mInflater = LayoutInflater.from(getContext());
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        switch (muti_file_flag){
            case 1:
                view1 = mInflater.inflate(R.layout.test_viewpager, null);
                mViewList.add(view1);
                mTitleList.add("图片");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                break;
            case 2:
                view1 = mInflater.inflate(R.layout.mp3_recover_layout, null);
                mViewList.add(view1);
                mTitleList.add("音频");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                initAudioView(view1);
                break;
            case 3:
                view1 = mInflater.inflate(R.layout.test_viewpager, null);
                view2 = mInflater.inflate(R.layout.test_viewpager, null);
                mViewList.add(view1);
                mViewList.add(view2);
                mTitleList.add("图片");
                mTitleList.add("音频");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
                break;
            case 4:
                view1 = mInflater.inflate(R.layout.avi_recover_layout, null);
                mViewList.add(view1);
                mTitleList.add("视频");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                initVideoView(view1);
                break;
            case 5:
                view1 = mInflater.inflate(R.layout.test_viewpager, null);
                view2 = mInflater.inflate(R.layout.test_viewpager, null);
                mViewList.add(view1);
                mViewList.add(view2);
                mTitleList.add("图片");
                mTitleList.add("视频");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
                break;
            case 6:
                view1 = mInflater.inflate(R.layout.mp3_recover_layout, null);
                view2 = mInflater.inflate(R.layout.avi_recover_layout, null);
                mViewList.add(view1);
                mViewList.add(view2);
                mTitleList.add("音频");
                mTitleList.add("视频");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
                initAudioView(view1);
                initVideoView(view2);
                break;
            case 7:
                view1 = mInflater.inflate(R.layout.test_viewpager, null);
                view2 = mInflater.inflate(R.layout.test_viewpager, null);
                view3 = mInflater.inflate(R.layout.test_viewpager, null);
                mViewList.add(view1);
                mViewList.add(view2);
                mViewList.add(view3);
                mTitleList.add("图片");
                mTitleList.add("视频");
                mTitleList.add("音频");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
                break;
            case 8:
                view1 = mInflater.inflate(R.layout.document_recover_layout, null);
                mViewList.add(view1);
                mTitleList.add("文件");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                initDocumentView(view1);
                break;
            case 9:
                view1 = mInflater.inflate(R.layout.test_viewpager, null);
                view2 = mInflater.inflate(R.layout.test_viewpager, null);
                mViewList.add(view1);
                mViewList.add(view2);
                mTitleList.add("图片");
                mTitleList.add("文件");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
                break;
            case 10:
                view1 = mInflater.inflate(R.layout.mp3_recover_layout, null);
                view2 = mInflater.inflate(R.layout.document_recover_layout, null);
                mViewList.add(view1);
                mViewList.add(view2);
                mTitleList.add("音频");
                mTitleList.add("文件");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
                initAudioView(view1);
                initDocumentView(view2);
                break;
            case 11:
                view1 = mInflater.inflate(R.layout.test_viewpager, null);
                view2 = mInflater.inflate(R.layout.test_viewpager, null);
                view3 = mInflater.inflate(R.layout.test_viewpager, null);
                mViewList.add(view1);
                mViewList.add(view2);
                mViewList.add(view3);
                mTitleList.add("图片");
                mTitleList.add("音频");
                mTitleList.add("文件");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
                break;
            case 12:
                view1 = mInflater.inflate(R.layout.avi_recover_layout, null);
                view2 = mInflater.inflate(R.layout.document_recover_layout, null);
                mViewList.add(view1);
                mViewList.add(view2);
                mTitleList.add("视频");
                mTitleList.add("文件");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
                initVideoView(view1);
                initDocumentView(view2);
                break;
            case 13:
                view1 = mInflater.inflate(R.layout.test_viewpager, null);
                view2 = mInflater.inflate(R.layout.test_viewpager, null);
                view3 = mInflater.inflate(R.layout.test_viewpager, null);
                mViewList.add(view1);
                mViewList.add(view2);
                mViewList.add(view3);
                mTitleList.add("图片");
                mTitleList.add("视频");
                mTitleList.add("文件");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
                break;
            case 14:
                view1 = mInflater.inflate(R.layout.mp3_recover_layout, null);
                view2 = mInflater.inflate(R.layout.avi_recover_layout, null);
                view3 = mInflater.inflate(R.layout.document_recover_layout, null);
                mViewList.add(view1);
                mViewList.add(view2);
                mViewList.add(view3);
                mTitleList.add("音频");
                mTitleList.add("视频");
                mTitleList.add("文件");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
                initAudioView(view1);
                initVideoView(view2);
                initDocumentView(view3);
                break;
            case 15:
                view1 = mInflater.inflate(R.layout.test_viewpager, null);
                view2 = mInflater.inflate(R.layout.test_viewpager, null);
                view3 = mInflater.inflate(R.layout.test_viewpager, null);
                view4 = mInflater.inflate(R.layout.test_viewpager, null);
                mViewList.add(view1);
                mViewList.add(view2);
                mViewList.add(view3);
                mViewList.add(view4);
                mTitleList.add("图片");
                mTitleList.add("音频");
                mTitleList.add("视频");
                mTitleList.add("文件");
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
                mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(3)));
                break;
        }

        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList,mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
    }

    private void initAudioView(View view){
        listViewAudio = (ListView)view.findViewById(R.id.listMp3);
        adapterAudio = new MCsAudioListView((MCsEntry)getActivity(),audio_list,k);
        listViewAudio.setAdapter(adapterAudio);
        listViewAudio.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void initVideoView(View view){
        listViewVideo = (ListView)view.findViewById(R.id.listAvi);
        adapterVideo = new MCsVideoListView((MCsEntry)getActivity(),video_list,k);
        listViewVideo.setAdapter(adapterVideo);
        listViewVideo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void initDocumentView(View view){
        listViewDocument = (ListView)view.findViewById(R.id.listDocument);
        adapterDocument = new MCsDocumentListView((MCsEntry)getActivity(),document_list,k);
        listViewDocument.setAdapter(adapterDocument);
        listViewDocument.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public void addVideoList(videoModel v){
        video_recover_list.add(v);
    }

    public void delVideoList(videoModel v){
        for(int i=0;i<video_recover_list.size();i++){
           if(video_recover_list.get(i).getVideo_name()==v.getVideo_name()){
               video_recover_list.remove(i);
           }
        }
    }

    public void addAviList(audioModel v){
        audio_recover_list.add(v);
    }

    public void delAviList(audioModel v){
        for(int i=0;i<audio_recover_list.size();i++){
            if(audio_recover_list.get(i).getAudio_name()==v.getAudio_name()){
                audio_recover_list.remove(i);
            }
        }
    }

    public void addDocumentList(documentModel d){
        document_recover_list.add(d);
    }

    public void delDocumentList(documentModel d){
        for(int i=0;i<document_recover_list.size();i++){
            if(document_recover_list.get(i).getDocument_name()==d.getDocument_name()){
                document_recover_list.remove(i);
            }
        }
    }

    public void doSomething(String str){}
    public void show_sms(long phone,String body,String date){}
    public void show_comm(long phone,String date,int duration){}
    public void ex_file(String out, int no, long size,String format){
        scan_file_num++;
        if(format.equalsIgnoreCase("mp3")||format.equalsIgnoreCase("wav")){
            Message msg1 = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("audioName", out);
            bundle.putInt("audioId",no);
            bundle.putLong("audioSize",size);
            msg1.setData(bundle);
            handle_audio.sendMessage(msg1);
        }else if(format.equalsIgnoreCase("mp4")||format.equalsIgnoreCase("mov")||format.equalsIgnoreCase("swf")){
            Message msg2 = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("videoName", out);
            bundle.putInt("videoId",no);
            bundle.putLong("videoSize",size);
            msg2.setData(bundle);
            handle_video.sendMessage(msg2);
        }else if(format.equalsIgnoreCase("log")||format.equalsIgnoreCase("zip")||format.equalsIgnoreCase("txt")||format.equalsIgnoreCase("pdf")||format.equalsIgnoreCase("doc")){
            Message msg3 = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("documentName", out);
            bundle.putInt("documentId",no);
            bundle.putLong("documentSize",size);
            msg3.setData(bundle);
            handle_document.sendMessage(msg3);
        }

        Message msg_num = new Message();
        Bundle bundle_num = new Bundle();
        bundle_num.putInt("num",scan_file_num);
        msg_num.setData(bundle_num);
        handle.sendMessage(msg_num);

    }
    public void callback_getAccess(String path){}
    public void callback_percent(int percent){
        if (percent == 100)scanFinish = true;
    }
    public void show_contacter(String phone,String name){}

    Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Bundle bundle_n = msg.getData();
            final int snum = bundle_n.getInt("num");
            num.setText(String.valueOf(snum));
        }
    };

    Handler handle_audio = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Bundle bundle = msg.getData();
            final int audioId = bundle.getInt("audioId");
            final  String audioName = bundle.getString("audioName");
            final long audioSize = bundle.getLong("audioSize");
            audioModel au = new audioModel();
            au.setNumber(audioId);
            au.setAudio_name(audioName);
            au.setSize(audioSize);
            audio_list.add(au);
            listViewAudio.setAdapter(adapterAudio);
            adapterAudio.notifyDataSetChanged();
        }
    };

    Handler handle_video = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Bundle bundle = msg.getData();
            final int videoId = bundle.getInt("videoId");
            final  String videoName = bundle.getString("videoName");
            final long videoSize = bundle.getLong("videoSize");
            videoModel vi = new videoModel();
            vi.setNumber(videoId);
            vi.setVideo_name(videoName);
            vi.setSize(videoSize);
            video_list.add(vi);
            listViewVideo.setAdapter(adapterVideo);
            adapterVideo.notifyDataSetChanged();
        }
    };


    Handler handle_document = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Bundle bundle = msg.getData();
            final int documentId = bundle.getInt("documentId");
            final  String documentName = bundle.getString("documentName");
            final long documentSize = bundle.getLong("documentSize");
            documentModel vi = new documentModel();
            vi.setNumber(documentId);
            vi.setVideo_name(documentName);
            vi.setSize(documentSize);
            document_list.add(vi);
            listViewDocument.setAdapter(adapterDocument);
            adapterDocument.notifyDataSetChanged();
        }
    };

    public void recoverFileToPhone(byte[] bfile,int type,String fileName){
        String filePath = "";
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        switch (type){
            case 1:
                filePath = "/storage/emulated/0/都叫兽数据恢复/视频";
                break;
            case 2:
                filePath = "/storage/emulated/0/都叫兽数据恢复/音频";
                break;
            case 3:
                filePath = "/storage/emulated/0/都叫兽数据恢复/文件";
                break;
        }

        try {
            File dir = new File(filePath);
            if(!dir.exists()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"/"+fileName);
            file.createNewFile();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    public void clearAviList(){
        audio_recover_list.clear();
    }

    public boolean scanFinish(){
        if(scanFinish) return false;
        else return true;
    }

}
