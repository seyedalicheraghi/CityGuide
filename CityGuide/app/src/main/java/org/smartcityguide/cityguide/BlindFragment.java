package org.smartcityguide.cityguide;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BlindFragment extends Fragment {
    private static final String EXTRA_JOB_ID = "dataFromMainActivity";
    @SuppressLint("StaticFieldLeak")
    private static View view;
    List<String> myListExample;

    ArrayAdapter<String> arrayAdapter;
    ListView listView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myListExample = new ArrayList<>();
        if (view != null) {
            Log.d("SightedFragment", "View Not Empty");
        }else{
            view = inflater.inflate(R.layout.blind_layout, container, false);
        }
        listView = view.findViewById(R.id.list_view);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(EXTRA_JOB_ID)){
            myListExample = getArguments().getStringArrayList(EXTRA_JOB_ID);
            final int nextStepToDo = Integer.valueOf(myListExample.get(myListExample.size()-1));
            myListExample.remove(myListExample.size()-1);
            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, myListExample );
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((MainActivity) getActivity()).getResult(myListExample.get(position).trim().toLowerCase(),position,nextStepToDo);
                }
            });
        }
        return view;
    }


}