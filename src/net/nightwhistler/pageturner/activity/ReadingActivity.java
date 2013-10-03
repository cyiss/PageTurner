/*
 * Copyright (C) 2012 Alex Kuiper
 * 
 * This file is part of PageTurner
 *
 * PageTurner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PageTurner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PageTurner.  If not, see <http://www.gnu.org/licenses/>.*
 */
package net.nightwhistler.pageturner.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ExpandableListView;
import com.google.inject.Inject;
import net.nightwhistler.pageturner.Configuration;
import com.chengyue.koalaReader.R;
import net.nightwhistler.pageturner.view.NavigationCallback;
import roboguice.inject.InjectFragment;

import java.util.ArrayList;
import java.util.List;

public class ReadingActivity extends PageTurnerActivity {

    @InjectFragment(R.id.fragment_reading)
    private ReadingFragment readingFragment;

    @Inject
    private Configuration config;

    private int tocIndex = -1;
    private int highlightIndex = -1;
    private int searchIndex = -1;

    @Override
    protected int getMainLayoutResource() {
        return R.layout.activity_reading;
    }

    @Override
    public void onDrawerClosed(View view) {
        getSupportActionBar().setTitle(R.string.app_name);
        super.onDrawerClosed(view);
    }

    @Override
    protected void initDrawerItems() {
        super.initDrawerItems();

        if ( this.readingFragment != null ) {

            List<NavigationCallback> tocCallbacks =
                    this.readingFragment.getTableOfContents();

            if ( tocCallbacks != null && ! tocCallbacks.isEmpty() ) {
                getAdapter().setChildren(this.tocIndex, tocCallbacks );
            }

            List<NavigationCallback> highlightCallbacks =
                    this.readingFragment.getHighlights();

            if ( highlightCallbacks != null && ! highlightCallbacks.isEmpty() ) {
                getAdapter().setChildren(this.highlightIndex, highlightCallbacks);
            }

            List<NavigationCallback> searchCallbacks =
                    this.readingFragment.getSearchResults();

            if ( searchCallbacks != null && !searchCallbacks.isEmpty() ) {
                getAdapter().setChildren(this.searchIndex, searchCallbacks);
            }

        }

    }

    protected String[] getMenuItems( Configuration config ) {

        List<String> menuItems = new ArrayList<String>();

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && config.isFullScreenEnabled() ) {
            menuItems.add("");
        }

        menuItems.add( getString(R.string.library));
        menuItems.add( getString(R.string.download));
        menuItems.add( config.getLastReadTitle() );

        if ( this.readingFragment != null ) {

            if ( this.readingFragment.hasTableOfContents() ) {
                menuItems.add( getString(R.string.toc_label));
                this.tocIndex = menuItems.size() - 1;
            }

            if ( this.readingFragment.hasHighlights() ) {
                menuItems.add( getString(R.string.highlights));
                this.highlightIndex = menuItems.size() - 1;
            }

            if ( this.readingFragment.hasSearchResults() ) {
                menuItems.add( getString(R.string.search_results));
                this.searchIndex = menuItems.size() - 1;
            }

        }

        return menuItems.toArray(new String[menuItems.size()]);
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

        int correctedIndex = getCorrectIndex(i);

        if ( correctedIndex == 2 || i == tocIndex || i == highlightIndex  || i == searchIndex) {
            return false;
        }

        return super.onGroupClick(expandableListView, view, correctedIndex, l);
    }

    private int getCorrectIndex( int i ) {

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && config.isFullScreenEnabled() ) {
            return i - 1;
        } else {
            return i;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {

        super.onChildClick(expandableListView, view, i, i2, l );

        NavigationCallback childItem = getAdapter().getChild( i, i2 );

        childItem.onClick();

        return false;
    }

    @Override
    protected int getTheme(Configuration config) {
        int theme = config.getTheme();

        if ( config.isFullScreenEnabled() ) {
            if (config.getColourProfile() == Configuration.ColourProfile.NIGHT) {
                theme = R.style.DarkFullScreen;
            } else {
                theme = R.style.LightFullScreen;
            }
        }

        return theme;
    }

    @Override
    protected void onCreatePageTurnerActivity(Bundle savedInstanceState) {

        Class<? extends PageTurnerActivity> lastActivityClass = config.getLastActivity();

        if ( !config.isAlwaysOpenLastBook() && lastActivityClass != null
                && lastActivityClass != ReadingActivity.class
                && getIntent().getData() == null ) {
            Intent intent = new Intent(this, lastActivityClass);

            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onSearchRequested() {
        readingFragment.onSearchRequested();
        return true;
    }

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		readingFragment.onWindowFocusChanged(hasFocus);
	}

	public void onMediaButtonEvent(View view) {
		this.readingFragment.onMediaButtonEvent(view.getId());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return readingFragment.onTouchEvent(event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

        if ( readingFragment.dispatchKeyEvent(event) ) {
            return true;
        }

        return super.dispatchKeyEvent(event);
	}

    @Override
    protected void beforeLaunchActivity() {
        readingFragment.saveReadingPosition();
        readingFragment.getBookView().releaseResources();
    }

}
