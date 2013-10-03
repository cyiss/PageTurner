/*
 * Copyright (C) 2013 Alex Kuiper
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

package net.nightwhistler.pageturner.view.bookview;

import android.app.AlertDialog;
import android.content.Context;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.text.Html;
import com.chengyue.koala.shanbay.Shanbay;
import net.nightwhistler.pageturner.Configuration;
import net.nightwhistler.pageturner.PlatformUtil;
import com.chengyue.koalaReader.R;
import roboguice.RoboGuice;
import com.chengyue.dict.Dict;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TextSelectionActions implements ActionMode.Callback {
    private static final String TAG = "TextSelectionActions";

    private TextSelectionCallback callBack;
    private BookView bookView;

    private Context context;

    public TextSelectionActions(Context context, TextSelectionCallback callBack,
                                BookView bookView) {
        this.callBack = callBack;
        this.bookView = bookView;
        this.context = context;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        mode.finish();

        return true;
    }

    @Override
    public boolean onCreateActionMode(final ActionMode mode, Menu menu) {
        menu.removeItem(android.R.id.selectAll);

        MenuItem copyItem = menu.findItem(android.R.id.copy);

        String word = bookView.getSelectedText();
        if (Dict.getIsReady()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(Html.fromHtml(Dict.find(word)));
            Shanbay sb = Shanbay.getInstance();
            if(sb.isLoggedIn()) {
                builder.setPositiveButton("Add To Shanbay", new OkOnClickListener());
                builder.setCancelable(true);
            } else {
                builder.setCancelable(false);
            }
            builder.setNegativeButton("Dismiss", new CancelOnClickListener());
            AlertDialog dialog = builder.create();

            dialog.show();
        }


        if ( copyItem != null ) {
            copyItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    PlatformUtil.copyTextToClipboard(context, bookView.getSelectedText());
                    mode.finish();
                    return true;
                }
            });
        }

        menu.add(R.string.abs__share_action_provider_share_with)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        callBack.share(bookView.getSelectionStart(), bookView.getSelectionEnd(), bookView.getSelectedText());
                        mode.finish();
                        return true;
                    }
                }).setIcon(R.drawable.abs__ic_menu_share_holo_dark);

        menu.add(R.string.highlight)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        callBack.highLight(bookView.getSelectionStart(), bookView.getSelectionEnd(), bookView.getSelectedText());
                        mode.finish();
                        return true;
                    }
                });

        if (callBack.isDictionaryAvailable()) {
            menu.add(R.string.dictionary_lookup)
                    .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(android.view.MenuItem item) {
                            callBack.lookupDictionary(bookView.getSelectedText());
                            mode.finish();
                            return true;
                        }
                    });
        }
        menu.add(R.string.lookup_wiktionary)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        callBack.lookupWiktionary(bookView.getSelectedText());
                        mode.finish();
                        return true;
                    }
                });
        /*
        menu.add(R.string.wikipedia_lookup)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        callBack.lookupWikipedia(bookView.getSelectedText());
                        mode.finish();
                        return true;
                    }
                });
        */
        menu.add(R.string.google_lookup)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        callBack.lookupGoogle(bookView.getSelectedText());
                        mode.finish();
                        return true;
                    }
                });



        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        return true;
    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            callBack.addToShanbay(bookView.getSelectedText());
        }
    }
}
