package com.example.myapplication.calendar.pager;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.calendar.Article;
import com.example.myapplication.calendar.ArticleAdapter;
import com.example.myapplication.calendar.base.fragment.BaseFragment;
import com.example.myapplication.calendar.group.GroupItemDecoration;
import com.example.myapplication.calendar.group.GroupRecyclerView;


public class PagerFragment extends BaseFragment {

    private GroupRecyclerView mRecyclerView;


    static PagerFragment newInstance() {
        return new PagerFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pager;
    }

    @Override
    protected void initView() {
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(new ArticleAdapter(mContext));
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    boolean isScrollTop() {
        return mRecyclerView != null && mRecyclerView.computeVerticalScrollOffset() == 0;
    }
}
