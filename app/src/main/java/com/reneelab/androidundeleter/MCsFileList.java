package com.reneelab.androidundeleter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.reneelab.DataModel.FileAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */
public class MCsFileList extends Fragment{

    private TextView mPath;
    //存放显示的文件列表的名称
    private List<String> mFileName = null;
    //存放显示的文件列表的相对应的路径
    private List<String> mFilePaths = null;

    private ListView fileList;
    private FileAdapter fileAdapter;
    private ImageView backFile;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mFileName = new ArrayList<String>();
        mFilePaths = new ArrayList<String>();
    }

    public void onActivityCreated(Bundle savedInstanceState){
      super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.file_list, null);
        mPath = (TextView)view.findViewById(R.id.currentpath);
        backFile = (ImageView)view.findViewById(R.id.backfile);
        fileList = (ListView)view.findViewById(R.id.fileList);

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intoFileListInfo(mFilePaths.get(position));
            }
        });

        backFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intoFileListInfo();
            }
        });

        initFileListInfo("/storage/emulated/legacy");
        return view;
    }

    public void onDestroyView(){
        super.onDestroyView();
    }

    public void onDestroy(){
        super.onDestroy();
    }

    private void initFileListInfo(String filePath){

        mFilePaths.clear();
        mFileName.clear();
        //显示当前的路径
        mPath.setText(filePath);
        File mFile = new File(filePath);
        //遍历出该文件夹路径下的所有文件/文件夹
        File[] mFiles = mFile.listFiles();
        if(mFile.length()>1){
            /*将所有文件信息添加到集合中*/
            for(File mCurrentFile:mFiles){
                mFileName.add(mCurrentFile.getName());
                mFilePaths.add(mCurrentFile.getPath());
            }

    	/*适配数据*/
            fileAdapter =  new FileAdapter(getContext(),mFileName,mFilePaths);
            fileList.setAdapter(fileAdapter);
        }

    }

    private void intoFileListInfo(String filePath){
        //显示当前的路径
        mPath.setText(filePath);
        File mFile = new File(filePath);
        //遍历出该文件夹路径下的所有文件/文件夹
        File[] mFiles = mFile.listFiles();
        System.err.println();
        //只要当前路径不是手机根目录或者是sd卡根目录则显示“返回根目录”和“返回上一级”
    	/*将所有文件信息添加到集合中*/
        mFileName.clear();
        mFilePaths.clear();
        try {
          for(File mCurrentFile:mFiles){
              mFileName.add(mCurrentFile.getName());
              mFilePaths.add(mCurrentFile.getPath());
          }
          fileAdapter.notifyDataSetChanged();
        }catch (Exception e){

        }

    }
}
