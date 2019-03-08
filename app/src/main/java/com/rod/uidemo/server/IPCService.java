package com.rod.uidemo.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.rod.uidemo.Book;
import com.rod.uidemo.IBookController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rod
 * @date 2019/3/8
 */
public class IPCService extends Service {

    private final IBookController.Stub mBinder = new IBookController.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            return new ArrayList<Book>() {{
                add(new Book("hehe"));
            }};
        }

        @Override
        public int addBook(Book book) throws RemoteException {
            return 8;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
