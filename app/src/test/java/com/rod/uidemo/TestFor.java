package com.rod.uidemo;

import org.junit.Test;

/**
 * @author Rod
 * @date 2019/10/31
 */
public class TestFor {

    @Test
    public void testFor() {
        int max = 100000;
        long start = System.nanoTime();
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < 10; j++) {

            }
        }
        System.out.println("test1 cost:" + (System.nanoTime() - start));
        long start2 = System.nanoTime();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < max; j++) {

            }
        }
        System.out.println("test2 cost:" + (System.nanoTime() - start2));
    }
}
