package com.reneelab.androidundeleter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/7/11.
 */
public class MCsPhotoDetail extends Fragment {
    private MCsEntry main;
    private LinearLayout backarea;
    private ImageView imgScan;
    private Button recover;
    private Bitmap scanBit;
    private String iformat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_scan, null);

        imgScan = (ImageView)view.findViewById(R.id.detail_img);
        recover = (Button)view.findViewById(R.id.recover);
        final String imgUrl = getArguments().getString("Type");
        iformat = getArguments().getString("format");

        scanBit =getArguments().getParcelable("detail_bitmap");
        imgScan.setImageBitmap(scanBit);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmap(scanBit,imgUrl);
            }
        });


        backarea = (LinearLayout)view.findViewById(R.id.backarea);
        backarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return view;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
