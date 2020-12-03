package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;

public class ConfigUtil {
    public static SpaceNavigationView spaceNavigationView;

    public static void initSpaceNavigationView(SpaceNavigationView spaceNavigationView, final Bundle savedInstanceState, final Context context) {
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("明细", R.drawable.account));
        spaceNavigationView.addSpaceItem(new SpaceItem("图表", R.drawable.chart));
        spaceNavigationView.addSpaceItem(new SpaceItem("社区", R.drawable.compass));
        spaceNavigationView.addSpaceItem(new SpaceItem("我的", R.drawable.personal));
        spaceNavigationView.shouldShowFullBadgeText(false);
        spaceNavigationView.setCentreButtonIcon(R.drawable.add2);
        spaceNavigationView.showIconOnly();
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Log.d("onCentreButtonClick ", "onCentreButtonClick");
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex){
                    case 1:

                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Log.d("onItemReselected ", "" + itemIndex + " " + itemName);
            }
        });

    }

    /**
     * 解决嵌套listview不显示的问题
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);

    }
}
