package com.rod.uidemo.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rod.uidemo.R;
import com.rod.uidemo.common.DataFactory;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * @author Rod
 * @date 2018/9/26
 */
public class RefreshFragment extends Fragment {
    public static final String TAG = "RefreshFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pull_to_refresh, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1, DataFactory.Companion.getStringList(50));
        ListView listView = view.findViewById(R.id.list_view);
        ViewGroup parent = (ViewGroup) listView.getParent();
        int index = parent.indexOfChild(listView);
        parent.removeView(listView);
        SmartRefreshLayout layout = new SmartRefreshLayout(getActivity());
        layout.addView(listView);
        parent.addView(layout, index);


        listView.setAdapter(adapter);
    }
}
