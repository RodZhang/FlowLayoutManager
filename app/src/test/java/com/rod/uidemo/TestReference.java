package com.rod.uidemo;

import org.junit.Test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * @author Rod
 * @date 2019/10/21
 */
public class TestReference {

    @Test
    public void testReference() {
        ReferenceQueue<ReflectBean> refQueue = new ReferenceQueue<>();
        SoftReference<ReflectBean> softRef = new SoftReference<>(new ReflectBean(), refQueue);
//        WeakReference<ReflectBean> weakRef = new WeakReference<>(new ReflectBean(), refQueue);
//        PhantomReference<ReflectBean> phantomRef = new PhantomReference<>(obj, refQueue);

//        System.out.println(obj);
        System.out.println(softRef.get());
//        System.out.println(weakRef.get());
//        System.out.println(phantomRef.get());

        Thread thread = new Thread(() -> {
            try {
                int cnt = 0;
                System.out.println("start");
                Reference<? extends ReflectBean> k;
                while((k = refQueue.remove()) != null) {
                    System.out.println((cnt++) + "回收了:" + k);
                }
            } catch(InterruptedException e) {
                //结束循环
            }
        });
        System.gc();
        thread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(softRef.get());
//        System.out.println(weakRef.get());
//        System.out.println(phantomRef.get());
    }

}
