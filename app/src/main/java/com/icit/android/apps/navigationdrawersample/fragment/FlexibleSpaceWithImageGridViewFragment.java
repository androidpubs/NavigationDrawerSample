/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.icit.android.apps.navigationdrawersample.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.icit.android.apps.navigationdrawersample.R;
import com.icit.android.apps.navigationdrawersample.base.FlexibleSpaceWithImageBaseFragment;

public class FlexibleSpaceWithImageGridViewFragment extends FlexibleSpaceWithImageBaseFragment<ObservableGridView> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flexiblespacewithimagegridview, container, false);

        final ObservableGridView gridView = (ObservableGridView) view.findViewById(R.id.scroll);
        // Set padding view for GridView. This is the flexible space.
        View paddingView = new View(getActivity());
        final int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_offset);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, flexibleSpaceImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);

        gridView.addHeaderView(paddingView);
        setDummyData(gridView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridView.setNestedScrollingEnabled(true);
        }
        // TouchInterceptionViewGroup should be a parent view other than ViewPager.
        // This is a workaround for the issue #117:
        // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
        gridView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));

        // Scroll to the specified offset after layout
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_SCROLL_Y)) {
            final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
            ScrollUtils.addOnGlobalLayoutListener(gridView, new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    int offset = scrollY % flexibleSpaceImageHeight;
                    setSelectionFromTop(gridView, 0, -offset);
                }
            });
            updateFlexibleSpace(scrollY, view);
        } else {
            updateFlexibleSpace(0, view);
        }

        gridView.setScrollViewCallbacks(this);

        updateFlexibleSpace(0, view);

        return view;
    }

    @SuppressWarnings("NewApi")
    @Override
    public void setScrollY(int scrollY, int threshold) {

    }

    @Override
    public void updateFlexibleSpace(int scrollY, View view) {

    }

    /*
     * setSelectionFromTop method has been moved from ListView to AbsListView since API level 21,
     * so for API level 21-, we need to use other method to scroll with offset.
     * smoothScrollToPositionFromTop seems to work, but it's from API level 11.
     * We can't use GridView for Gingerbread.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setSelectionFromTop(ObservableGridView gridView, int position, int offset) {
        if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
            gridView.setSelectionFromTop(position, offset);
        } else if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
            gridView.smoothScrollToPositionFromTop(position, offset, 0);
        }
    }
}
