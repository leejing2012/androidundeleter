package com.reneelab.androidundeleter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reneelab.actioneffect.CustomProgressDialog;
import com.reneelab.DataModel.MCsXmlAnalysis;
import com.reneelab.DataModel.ProductType;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class MCsPay extends Fragment{
    private Button paybtn;
    private MCsEntry main;
    private static List<ProductType> buyoptions;
    private static LinearLayout box;
    private Handler  mHandler;
    private static LayoutInflater inflater1;
    public static int temp = -1;
    private static ArrayList<String> proTobuy;
    private static CustomProgressDialog dialog;
    private LinearLayout getback;
    private CheckBox hiddenBoxRight;
    private CheckBox hiddenBoxLeft;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_choose, null);
        hiddenBoxRight = (CheckBox)view.findViewById(R.id.Rightcheck);
        hiddenBoxLeft = (CheckBox)view.findViewById(R.id.Leftcheck);
        hiddenBoxRight.setVisibility(View.GONE);
        hiddenBoxLeft.setVisibility(View.GONE);
        dialog =new CustomProgressDialog(getContext(), "正在加载中",R.anim.frame);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        inflater1 = inflater;
        proTobuy = new ArrayList<String>();
        paybtn = (Button)view.findViewById(R.id.recover);
        paybtn.setText("选择支付方式");
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putStringArrayList("ProductType", proTobuy);
                if (temp == -1) {
                    Toast.makeText(getContext(), "请选择套餐类型", Toast.LENGTH_SHORT).show();
                } else {
                    Fragment to = new MCsCharge();
                    to.setArguments(bundle);
                    switchFragment(getFragmentManager().findFragmentById(getId()), to);
                }

            }
        });
        box = (LinearLayout)view.findViewById(R.id.optionbox);
        mHandler = new loadProductData(getActivity(),getActivity());
        new Thread(){
            public void run(){
                MCsXmlAnalysis xmlget = new MCsXmlAnalysis();
                InputStream prodxml =  xmlget.GetUrl("http://www.reneelab.cn/mobileapi.php?cmd=GET_PROS&protags=DR");
                buyoptions = xmlget.analysisDatal(prodxml);
                Message msg = new Message();
                if(buyoptions.size()>1){
                    mHandler.sendMessage(msg);
                }

            }
        }.start();

        getback = (LinearLayout)view.findViewById(R.id.backarea);
        getback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
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

    private void switchFragment(Fragment from,Fragment to) {
        if (getActivity() == null) {
            return;
        }
        if (getActivity() instanceof MCsEntry) {
            main = (MCsEntry) getActivity();
            main.switchFunction(from,to);
        }
    }

    static class loadProductData extends Handler{
        WeakReference mActivity;
        Activity currentActivity;
        public loadProductData(FragmentActivity activity,Activity Currentactivity){
            mActivity = new WeakReference(activity);
            currentActivity = Currentactivity;
        }
        public void handleMessage(Message message){
            for(int i=0;i<buyoptions.size();i++){
                final View view2 = inflater1.inflate(R.layout.product_options,null);
                TextView name = (TextView)view2.findViewById(R.id.optionname);
                TextView price = (TextView)view2.findViewById(R.id.optionprice);
                CheckBox choose = (CheckBox)view2.findViewById(R.id.chooseTobuy);
                name.setText(buyoptions.get(i).getName());
                price.setText("￥" + buyoptions.get(i).getPrice());
                choose.setId(i);
                choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (temp != -1) {
                                CheckBox tempButton = (CheckBox) currentActivity.findViewById(temp);
                                if (tempButton != null) {
                                    tempButton.setChecked(false);
                                }
                            }
                            int proId = buttonView.getId();
                            proTobuy.add(0, Integer.toString(buyoptions.get(proId).getId()));
                            proTobuy.add(1, buyoptions.get(proId).getName());
                            proTobuy.add(2, String.valueOf(buyoptions.get(proId).getPrice()));
                            temp = buttonView.getId();
                        } else {
                            temp = -1;
                        }
                    }
                });
                box.addView(view2);
            }
            dialog.dismiss();
            super.handleMessage(message);
        }
    }


}
