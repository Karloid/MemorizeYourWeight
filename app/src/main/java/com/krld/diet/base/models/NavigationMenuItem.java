package com.krld.diet.base.models;

import android.view.View;

import com.krld.diet.R;
import com.krld.diet.base.adapters.NavigationAdapter;

import rx.functions.Action0;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

public class NavigationMenuItem {

    public BehaviorSubject<Integer> notificationsObs;
    public Type type;

    public String title;
    public int imageResourceId;
    public Action0 onClick;

    public NavigationMenuItem(Type type, String title, int imageResourceId, Action0 onClick) {
        this.type = type;
        this.title = title;
        this.imageResourceId = imageResourceId;
        this.onClick = onClick;
    }

    public NavigationMenuItem(Type type) {
        this.type = type;
    }

    public NavigationMenuItem(Type type, String title, int imageResourceId, Action0 onClick, BehaviorSubject<Integer> notificationsObs) {
        this(type, title, imageResourceId, onClick);

        this.notificationsObs = notificationsObs;
    }

    public enum Type {
        TYPE_ITEM(R.layout.navigation_li, NavigationAdapter.ItemViewHolder::new),
        TYPE_HEADER(R.layout.navigation_li_header, NavigationAdapter.HeaderViewHolder::new),
        TYPE_SEPARATOR(R.layout.navigation_li_separator, NavigationAdapter.SeparatorViewHolder::new),
        TYPE_SUB_HEADER(R.layout.navigation_li, NavigationAdapter.ItemViewHolder::new);

        public int id;
        public int layoutId;
        public Func1<View, NavigationAdapter.AbstractViewHolder> createViewHolder;

        Type(int layoutId, Func1<View, NavigationAdapter.AbstractViewHolder> createViewHolder) {
            this.layoutId = layoutId;
            this.createViewHolder = createViewHolder;
            id = HackyIdHolder.id++;
        }

        public static Type getById(int id) {
            for (Type type:values()) {
                if (type.id == id){
                    return type;
                }
            }
            return null;
        }

        static private class HackyIdHolder {
            public static int id;
        }
    }
}
