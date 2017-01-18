package com.reneelab.androidundeleter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.reneelab.DataModel.MCsMainOptions;
import com.reneelab.DataModel.MCsGridView;

/**
 * Created by Administrator on 2016/5/24.
 */
public class MCsContentFragment extends Fragment {
    public MCsEntry main;
    private MCsGridView gridview;
    private MCsMainOptions gridViewAdapter;
    public Fragment reoverContent = null;
    private RelativeLayout intofilechoose;
    private MCsSwitchFragment switchFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.main_interface, container,false);
        ImageButton btn = (ImageButton)view.findViewById(R.id.left_menu);
        main =(MCsEntry)getActivity();
        switchFragment = new MCsSwitchFragment(getActivity());
        intofilechoose = (RelativeLayout)view.findViewById(R.id.tochoose);
        intofilechoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transmit("file");
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.changeMenu();
            }
        });

        gridViewAdapter = new MCsMainOptions(view.getContext());
        gridview = (MCsGridView)view.findViewById(R.id.gridview);
        gridview.setAdapter(new MCsMainOptions(view.getContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {
                switch (gridViewAdapter.imgs[idx]) {
                    case R.drawable.undelete_icon_photo:
                        transmit("photo");
                        break;
                    case R.drawable.undelete_icon_sms:
                        transmit("sms");
                        break;
                    case R.drawable.undelete_icon_contact:
                        transmit("contact");
                        break;
                    /* case R.drawable.undelete_icon_txt:
                        //transmit("txt");
                        break;*/
                    case R.drawable.undelete_icon_calls:
                        transmit("call");
                        break;
                    case R.drawable.undelete_icon_wifi:
                        transmit("wifi");
                        break;
                    case R.drawable.undelete_icon_whatsapp:
                        transmit("test");
                        break;
                }
            }
        });
        return  view;
    }

    private void transmit(String type){
        reoverContent = new MCsStartRecover();
        final Bundle bundle = new Bundle();
        bundle.putString("Type", type);
        reoverContent.setArguments(bundle);
        if(type.equalsIgnoreCase("wifi")){
            reoverContent = new MCsWifi();
        }else if(type.equalsIgnoreCase("test")){
            reoverContent = new MCsThirdLogin();
        }else if(type.equalsIgnoreCase("file")){
            reoverContent = new MCsFileChoose();
        }
        switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()), reoverContent);
    }


    public void menuGet(){
        reoverContent = new MCsThirdLogin();
        switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()), reoverContent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.isAdded()){
            getActivity().getSupportFragmentManager().putFragment(outState, "mFragment", this);
        }
    }

}
