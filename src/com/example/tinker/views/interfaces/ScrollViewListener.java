package com.example.tinker.views.interfaces;

import com.example.tinker.views.HybridScrollView;

/**
 * Created by root on 7/29/14.
 */
public interface ScrollViewListener {
    void onScrollChanged(HybridScrollView scrollView, int x, int y, int oldx, int oldy);
}
