package com.reneelab.androidundeleter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/5/27.
 */
public class MCsAbout extends Fragment {

   private MCsEntry main;
    private LinearLayout back;
    private Fragment base;
    private MCsSwitchFragment switchFragment;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.about, null);
        back = (LinearLayout)view.findViewById(R.id.backarea);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base = new MCsContentFragment();
                //switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()),base);
                //getFragmentManager().popBackStack();
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
