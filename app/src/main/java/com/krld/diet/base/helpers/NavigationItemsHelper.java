package com.krld.diet.base.helpers;

import android.content.Context;


import com.krld.diet.BuildConfig;
import com.krld.diet.R;
import com.krld.diet.base.models.NavigationMenuItem;

import java.util.ArrayList;
import java.util.List;

public class NavigationItemsHelper {
    public static List<NavigationMenuItem> createItems(NavigationItemsCallback callback, Context context) {
        ArrayList<NavigationMenuItem> items = new ArrayList<>();


        items.add(new NavigationMenuItem(NavigationMenuItem.Type.TYPE_HEADER, null, 0, () -> callback.show(NavigationDrawerFragments.PROFILE, true)));
        items.add(new NavigationMenuItem(NavigationMenuItem.Type.TYPE_ITEM, context.getString(R.string.profile), R.drawable.ic_person_black_24dp, () -> callback.show(NavigationDrawerFragments.PROFILE, true)));
        items.add(new NavigationMenuItem(NavigationMenuItem.Type.TYPE_ITEM, context.getString(R.string.diet), R.drawable.ic_list_black_24dp, () -> callback.show(NavigationDrawerFragments.PROFILE, true)));
        items.add(new NavigationMenuItem(NavigationMenuItem.Type.TYPE_SEPARATOR));
        items.add(new NavigationMenuItem(NavigationMenuItem.Type.TYPE_ITEM, context.getString(R.string.memorize), R.drawable.ic_save_black_24dp, () -> callback.show(NavigationDrawerFragments.PROFILE, true)));
        return items;
    }

    public interface NavigationItemsCallback {
        void show(NavigationDrawerFragments fragmentEnum, boolean withAnimation);

        void showDialog(NavigationDrawerFragments debugInfo);
    }
}
