package com.vgaw.scaffold.util.statusbar;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.vgaw.scaffold.R;

/**
 * @author caojin
 * @date 2018/6/9
 */
public class ToolbarUtil {
    public static void enable(AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
    }

    public static void connectDrawer(AppCompatActivity activity, Toolbar toolbar,
                                     DrawerLayout drawerLayout) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity,
                drawerLayout, toolbar, R.string.base_navigation_drawer_open, R.string.base_navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public static void setTitle(AppCompatActivity activity, @StringRes int titleId) {
        activity.setTitle(titleId);
    }

    public static void displayBack(AppCompatActivity activity) {
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
