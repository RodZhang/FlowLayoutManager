package com.rod.uidemo.server;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rod.uidemo.Book;
import com.rod.uidemo.IBookController;
import com.rod.uidemo.R;

import java.util.List;

/**
 * @author Rod
 * @date 2019/3/8
 */
public class IPCClientFragment extends Fragment {
    public static final String TAG = "IPCClientFragment";

    private IBookController mBookController;
    private boolean mIsConnected;
    private TextView mInfoText;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIsConnected = true;
            mBookController = IBookController.Stub.asInterface(service);
            mInfoText.setText("onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsConnected = false;
            mInfoText.setText("onServiceDisconnected");
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ipc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInfoText = view.findViewById(R.id.info);
        view.findViewById(R.id.bind_btn).setOnClickListener(v -> {
            if (mIsConnected) {
                return;
            }
            Intent intent = new Intent(getActivity(), IPCService.class);
            getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        });

        view.findViewById(R.id.unbind_btn).setOnClickListener(v -> {
            if (mIsConnected) {
                mIsConnected = false;
                getActivity().unbindService(mServiceConnection);
            } else {
                mInfoText.setText("connect is disconnected");
            }
        });

        view.findViewById(R.id.get_book_btn).setOnClickListener(v -> {
            if (mIsConnected) {
                try {
                    List<Book> books = mBookController.getBooks();
                    mInfoText.setText(books.get(0).name);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                mInfoText.setText("connect service first");
            }
        });
    }
}
