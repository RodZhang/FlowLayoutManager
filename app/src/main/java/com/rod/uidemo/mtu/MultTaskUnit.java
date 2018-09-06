package com.rod.uidemo.mtu;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a series of tasks
 * Each task define it's func to deal with his result
 * Each task define it's start & deal_func thread(main or background thread)
 * Define task execute sequence(serial or concurrence)
 * Has a callback handle all tasks result
 *
 * @author Rod
 * @date 2018/9/6
 */
public class MultTaskUnit<T> {

    private Order mSequence;
    private List<Task> mTasks = new ArrayList<>();
    private Callback<T> mCallback;

    public MultTaskUnit addTask(Task task) {
        mTasks.add(task);
        return this;
    }

    public MultTaskUnit executeOrder(Order order) {
        mSequence = order;
        return this;
    }

    public MultTaskUnit<T> setCallback(Callback<T> callback) {
        mCallback = callback;
        return this;
    }

    public void start() {

    }

    public void cancel() {

    }

    public interface Callback<T> {
        T onFinish();
    }

    public enum Order {
        SERIAL,
        CONCURRENCE
    }
}
