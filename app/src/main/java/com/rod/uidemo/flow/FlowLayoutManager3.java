package com.rod.uidemo.flow;

import android.support.v7.widget.RecyclerView;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author Rod
 * @date 2018/8/20
 */
public class FlowLayoutManager3 extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
    }
}
