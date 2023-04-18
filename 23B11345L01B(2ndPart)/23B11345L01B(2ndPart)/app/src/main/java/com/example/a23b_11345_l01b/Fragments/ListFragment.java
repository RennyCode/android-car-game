package com.example.a23b_11345_l01b.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a23b_11345_l01b.R;
import com.google.android.material.button.MaterialButton;
import com.example.a23b_11345_l01b.Interfaces.CallBack_SendClick;

public class ListFragment extends Fragment {
    private MaterialButton list_BTN_send;
    private AppCompatEditText list_ET_name;

    private CallBack_SendClick callBack_SendClick;

    public void setCallBack_SendClick(CallBack_SendClick callBack_SendClick) {
        this.callBack_SendClick = callBack_SendClick;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        list_BTN_send = view.findViewById(R.id.list_BTN_send);
        list_ET_name = view.findViewById(R.id.list_ET_name);
    }

    public void sendClicked() {
        if(callBack_SendClick != null)
            callBack_SendClick.userNameChosen(list_ET_name.getText().toString());
    }
}