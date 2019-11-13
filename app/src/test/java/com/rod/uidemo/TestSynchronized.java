package com.rod.uidemo;

import org.junit.Test;

/**
 * @author Rod
 * @date 2019/10/15
 */
public class TestSynchronized {

    @Test
    public void test1() {
        Inner inner = new Inner();
        new Thread(inner::normalSync1).start();
        new Thread(inner::normalSync2).start();
        new Thread(Inner::staticSync).start();
        Inner inner1 = new Inner();
        new Thread(inner1::normalSync1).start();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class Inner {

        public static synchronized void staticSync() {
            System.out.println("staticSync, enter");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("staticSync, exit");
        }

        public synchronized void normalSync1() {
            System.out.println("normalSync1, enter");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("normalSync1, exit");
        }

        public synchronized void normalSync2() {
            System.out.println("normalSync2, enter");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("normalSync2, exit");
        }
    }
}
