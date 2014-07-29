package com.example.tinker.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import com.example.tinker.views.interfaces.ScrollViewListener;

/**
 * Created by root on 7/29/14.
 */
public class HybridScrollView extends ScrollView {

    private ScrollViewListener svl = null;

    public HybridScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public HybridScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HybridScrollView(Context context) {
        super(context);
    }

    public void setScrollViewListener(ScrollViewListener svl) {
        this.svl = svl;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(svl != null) {
            svl.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
