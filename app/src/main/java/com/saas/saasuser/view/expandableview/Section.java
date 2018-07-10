package com.saas.saasuser.view.expandableview;

import java.util.ArrayList;
import java.util.List;



public class Section<P, C> {
    public boolean expanded;
    public P parent;
    public List<C> children;

    public Section() {
        children = new ArrayList<>();
        expanded = false;
    }
}
