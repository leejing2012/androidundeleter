package com.reneelab.DataModel;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.LruCache;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reneelab.androidundeleter.MCsPhoto;
import com.reneelab.androidundeleter.R;
import com.reneelab.jni.initJni;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import libcore.io.DiskLruCache;
import libcore.io.DiskLruCache.Snapshot;

/**
 * Created by Administrator on 2016/6/21.
 */
public class PhotoWallAdapter extends ArrayAdapter<String> implements Serializable{
    /**
     * 记录所有正在下载或等待下载的任务。
     */
    private Set<BitmapWorkerTask> taskCollection;

    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private LruCache<String, Bitmap> mMemoryCache;

    /**
     * 图片硬盘缓存核心类。
     */
    private DiskLruCache mDiskLruCache;

    /**
     * GridView的实例
     */
    private GridView mPhotoWall;

    private MCsPhoto main;

    public Fragment photo_wall;

    public ArrayList<PicModel> s;

    private SparseBooleanArray mCheckStateArray;



    /**
     * 记录每个子项的高度。
     */
    private int mItemHeight = 0;

    public PhotoWallAdapter(Context context, int textViewResourceId, ArrayList objects, GridView photoWall, Fragment photo_scan, MCsPhoto v) {
        super(context, textViewResourceId, objects);
        s=objects;
        photo_wall = photo_scan;
        main = v;
        mPhotoWall = photoWall;
        taskCollection = new HashSet<BitmapWorkerTask>();
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        // 设置图片缓存大小为程序最大可用内存的1/8
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
        try {
            // 获取图片缓存路径
            File cacheDir = getDiskCacheDir(context, "thumb");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            // 创建DiskLruCache实例，初始化缓存数据
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mCheckStateArray = new SparseBooleanArray();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //final String url = getItem(position);
        final  String url =s.get(position).getPic_name();
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.photo_view, null);
        } else {
            view = convertView;
        }
        final TextView recover_flag = (TextView)view.findViewById(R.id.recover_flag);
        final ImageView imageView = (ImageView) view.findViewById(R.id.photo_window);
        if (imageView.getLayoutParams().height != mItemHeight) {
            imageView.getLayoutParams().height = mItemHeight;
        }

        /*checkbox add arraylist*/
        final CheckBox pcheckbox  = (CheckBox)view.findViewById(R.id.pcheckbox);
        pcheckbox.setTag(url);

        pcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setChecked(position,isChecked);
                if(isChecked){
                    main.addRecoverList(s.get(position));
                }else{
                    main.deleteReoverList(s.get(position));
                }
            }
        });

       pcheckbox.setChecked(isChecked(position));

        if(s.get(position).getFlag()==1){
            recover_flag.setVisibility(View.VISIBLE);
            pcheckbox.setVisibility(View.INVISIBLE);
        }else {
            recover_flag.setVisibility(View.INVISIBLE);
           // pcheckbox.setVisibility(View.VISIBLE);
        }

        // 给ImageView设置一个Tag，保证异步加载图片时不会乱序
        imageView.setTag(url);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.get(position).getFlag()==1){
                    Toast.makeText(getContext(),"此图片已经恢复!",Toast.LENGTH_SHORT).show();
                }else {
                    main.previewDetail(url,s.get(position).getFormat());
                }

            }
        });
        imageView.setImageResource(R.drawable.empty_photo);
        loadBitmaps(imageView, url,position);
        return view;
    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @param bitmap
     *            LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
     * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
     */
    public void loadBitmaps(ImageView imageView, String imageUrl,int picPosition) {
        try {
            Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
            if (bitmap == null) {
                BitmapWorkerTask task = new BitmapWorkerTask();
                taskCollection.add(task);
                task.execute(picPosition);
            } else {
                if (imageView != null && bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void cancelAllTasks() {
        if (taskCollection != null) {
            for (BitmapWorkerTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址。
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取当前应用程序的版本号。
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 设置item子项的高度。
     */
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        notifyDataSetChanged();
    }

    /**
     * 使用MD5算法对传入的key进行加密并返回。
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * 将缓存记录同步到journal文件中。
     */
    public void fluchCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public Bitmap getDiskCach(String url){
        Bitmap bitmap_d = null;
        FileDescriptor fileDescriptor_d = null;
        FileInputStream fileInputStream_d = null;
        Snapshot snapShot_detail = null;
            // 生成图片URL对应的key\
        try {
            final String key = hashKeyForDisk(url);
            // 查找key对应的缓存
            snapShot_detail = mDiskLruCache.get(key);
            if (snapShot_detail != null) {
                fileInputStream_d = (FileInputStream) snapShot_detail.getInputStream(0);
                fileDescriptor_d = fileInputStream_d.getFD();
            }
            // 将缓存数据解析成Bitmap对象
            if (fileDescriptor_d != null) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(fileDescriptor_d,null,opts);
                int inSampleSize = cacluateInSampleSize(opts);
                opts.inSampleSize = inSampleSize;
                opts.inJustDecodeBounds = false;
                bitmap_d = BitmapFactory.decodeFileDescriptor(fileDescriptor_d,null,opts);
            }

        }catch (IOException ex){

        }
        return bitmap_d;
    }

    private  int cacluateInSampleSize(BitmapFactory.Options opts)
    {
        if (opts == null)
            return 1;

        int inSampleSize = 1;
        int realWidth = opts.outWidth;
        int realHeight = opts.outHeight;
        int reqWidth = 90;
        int reqHeight= 90;

        if (realHeight > reqHeight || realWidth > reqWidth)
        {
            int heightRatio = realHeight / reqHeight;
            int widthRatio = realWidth / reqWidth;

            inSampleSize = (heightRatio > widthRatio) ? widthRatio
                    : heightRatio;
        }
        return inSampleSize;
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStateArray.put(position, isChecked);
    }

    public boolean isChecked(int position) {
        return mCheckStateArray.get(position);
    }



    /**
     * 异步下载图片的任务。
     *
     * @author guolin
     */
    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        /**
         * 图片的URL地址
         */
        private String imageUrl;

        @Override
        protected Bitmap doInBackground(Integer... params) {
            imageUrl = s.get(params[0]).getPic_name();
            int pic_p = params[0];
            FileDescriptor fileDescriptor = null;
            FileInputStream fileInputStream = null;
            Snapshot snapShot = null;
            try {
                // 生成图片URL对应的key
                final String key = hashKeyForDisk(imageUrl);
                // 查找key对应的缓存
                snapShot = mDiskLruCache.get(key);
                if (snapShot == null) {
                    // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        if (downloadUrlToStream(imageUrl, outputStream,pic_p)){
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    }
                    // 缓存被写入后，再次查找key对应的缓存
                    snapShot = mDiskLruCache.get(key);
                }
                if (snapShot != null) {
                    fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                    fileDescriptor = fileInputStream.getFD();
                }
                // 将缓存数据解析成Bitmap对象
                Bitmap bitmap = null;
                if (fileDescriptor != null) {
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    BitmapFactory.decodeFileDescriptor(fileDescriptor,null,opts);
                    int inSampleSize = cacluateInSampleSize(opts);
                    opts.inSampleSize = inSampleSize;
                    opts.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor,null,opts);
                    //bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                }
                if (bitmap != null) {
                    // 将Bitmap对象添加到内存缓存当中
                    addBitmapToMemoryCache(imageUrl, bitmap);
                }
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileDescriptor == null && fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
            ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            taskCollection.remove(this);
        }


        private boolean downloadUrlToStream(String urlString, OutputStream outputStream,int position_pic) {
            BufferedOutputStream out = null;
            BufferedInputStream in = null;
            try {
                byte[] buff =new byte[(int)s.get(position_pic).getPic_size()];
                int bb = initJni.getInstance().readbuffer(s.get(position_pic).getPic_id(),0,(int)s.get(position_pic).getPic_size(),buff);
                //int bb =   ubj.read(s.get(position_pic).getPic_id(),0,(int)s.get(position_pic).getPic_size(),buff);
                System.err.println("----read-----------------"+buff.length+"---"+bb+"------"+urlString);
                if(bb ==(int)s.get(position_pic).getPic_size()){
                    ByteArrayInputStream is = new ByteArrayInputStream(buff);
                    in = new BufferedInputStream(/*urlConnection.getInputStream()*/is, 8 * 1024);
                    out = new BufferedOutputStream(outputStream, 8 * 1024);
                    int b;
                    while ((b = in.read()) != -1) {
                        out.write(b);
                    }
                    return true;
                }else{
                    return false;
                }

            } catch (final IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

    }

}
