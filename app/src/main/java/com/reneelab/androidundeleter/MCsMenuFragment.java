package com.reneelab.androidundeleter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MCsMenuFragment extends Fragment {
	private RelativeLayout login;
	private RelativeLayout about;
	private Fragment cFragment;
	private Fragment loginfrg;
	private Fragment abofrg;
    public MCsEntry main;
	public SharedPreferences mySharedPreferences;
	private TextView loginame;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left_menu, null);
		mySharedPreferences= getActivity().getSharedPreferences("test", Activity.MODE_PRIVATE);
		String uname = mySharedPreferences.getString("username","null");
		loginame = (TextView)view.findViewById(R.id.phoneType);
		if(uname.equalsIgnoreCase("null")){

		}else{
           loginame.setText(uname);
		}

		cFragment = new MCsContentFragment();
		login = (RelativeLayout)view.findViewById(R.id.thirLogin);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loginfrg = new MCsThirdLogin();
                switchFragment(cFragment,loginfrg,"login");
			}
		});
		about = (RelativeLayout)view.findViewById(R.id.about);
		about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				abofrg = new MCsAbout();
				switchFragment(cFragment,abofrg,"about");
			}
		});
		return view;
	}

	public void switchFragment(Fragment from,Fragment to,String menuType) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MCsEntry) {
			main = (MCsEntry) getActivity();
			main.test(from, to,menuType);
		}
	}

}

