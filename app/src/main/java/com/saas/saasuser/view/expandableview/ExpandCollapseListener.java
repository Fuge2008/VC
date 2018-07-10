package com.saas.saasuser.view.expandableview;

import android.view.View;


public interface ExpandCollapseListener<P,C> {

    interface ExpandListener<P> {
        void onExpanded(int parentIndex, P parent, View view);
    }

    interface CollapseListener<P> {
        void onCollapsed(int parentIndex, P parent, View view);
    }

}
