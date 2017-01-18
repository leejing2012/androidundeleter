package com.reneelab.DataModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reneelab.androidundeleter.R;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */
public class FileAdapter extends BaseAdapter {
    private Bitmap mImage;
    private Bitmap mAudio;
    private Bitmap mRar;
    private Bitmap mVideo;
    private Bitmap mFolder;
    private Bitmap mApk;
    private Bitmap mOthers;
    private Bitmap mTxt;

    private Context mContext;
    //文件名列表
    private List<String> mFileNameList;
    //文件对应的路径列表
    private List<String> mFilePathList;


    public FileAdapter(Context context,List<String> fileName,List<String> filePath){
        mContext = context;
        mFileNameList = fileName;
        mFilePathList = filePath;
        //初始化图片资源
        //图片文件对应的icon
        mImage = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.image);
        //音频文件对应的icon
        mAudio = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.audio);
        //视频文件对应的icon
        mVideo = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.video);
        //可执行文件对应的icon
        mApk = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.apk);
        //文本文档对应的icon
        mTxt = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.txt);
        //其他类型文件对应的icon
        mOthers = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.others);
        //文件夹对应的icon
        mFolder = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.filetype_icon_folder);
        //zip文件对应的icon
        mRar = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.zip_icon);
    }

    public int getCount() {
        return mFilePathList.size();
    }
    //获得当前位置对应的文件名
    public Object getItem(int position) {
        return mFileNameList.get(position);
    }
    //获得当前的位置
    public long getItemId(int position) {
        return position;
    }
    //获得视图
    public View getView(int position, View convertView, ViewGroup viewgroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mLI = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //初始化列表元素界面
            convertView = mLI.inflate(R.layout.file_list_child, null);
            //获取列表布局界面元素
            viewHolder.mIV = (ImageView)convertView.findViewById(R.id.image_list_childs);
            viewHolder.mTV = (TextView)convertView.findViewById(R.id.text_list_childs);
            //将每一行的元素集合设置成标签
            convertView.setTag(viewHolder);
        } else {
            //获取视图标签
            viewHolder = (ViewHolder) convertView.getTag();
        }
        File mFile = new File(mFilePathList.get(position).toString());
        //如果
        if(mFileNameList.get(position).toString().equals("BacktoRoot")){
            //添加返回根目录的按钮
          /*  viewHolder.mIV.setImageBitmap(mBackRoot);
            viewHolder.mTV.setText("返回根目录");*/
        }else if(mFileNameList.get(position).toString().equals("BacktoUp")){
            //添加返回上一级菜单的按钮
          /*  viewHolder.mIV.setImageBitmap(mBackUp);
            viewHolder.mTV.setText("返回上一级");*/
        }else if(mFileNameList.get(position).toString().equals("BacktoSearchBefore")){
            //添加返回搜索之前目录的按钮
          /*  viewHolder.mIV.setImageBitmap(mBackRoot);
            viewHolder.mTV.setText("返回搜索之前目录");*/
        }else{
            String fileName = mFile.getName();
            viewHolder.mTV.setText(fileName);
            if(mFile.isDirectory()){
                viewHolder.mIV.setImageBitmap(mFolder);
            }else{
                String fileEnds = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()).toLowerCase();//取出文件后缀名并转成小写
                if(fileEnds.equals("m4a")||fileEnds.equals("mp3")||fileEnds.equals("mid")||fileEnds.equals("xmf")||fileEnds.equals("ogg")||fileEnds.equals("wav")){
                    viewHolder.mIV.setImageBitmap(mVideo);
                }else if(fileEnds.equals("3gp")||fileEnds.equals("mp4")){
                    viewHolder.mIV.setImageBitmap(mAudio);
                }else if(fileEnds.equals("jpg")||fileEnds.equals("gif")||fileEnds.equals("png")||fileEnds.equals("jpeg")||fileEnds.equals("bmp")){
                    viewHolder.mIV.setImageBitmap(mImage);
                }else if(fileEnds.equals("apk")){
                    viewHolder.mIV.setImageBitmap(mApk);
                }else if(fileEnds.equals("txt")){
                    viewHolder.mIV.setImageBitmap(mTxt);
                }else if(fileEnds.equals("zip")||fileEnds.equals("rar")){
                    viewHolder.mIV.setImageBitmap(mRar);
                }else {
                    viewHolder.mIV.setImageBitmap(mOthers);
                }
            }
        }
        return convertView;
    }
    //用于存储列表每一行元素的图片和文本
    class ViewHolder {
        ImageView mIV;
        TextView mTV;
    }
}
