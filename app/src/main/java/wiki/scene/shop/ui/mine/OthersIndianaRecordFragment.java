package wiki.scene.shop.ui.mine;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wiki.scene.shop.R;
import wiki.scene.shop.adapter.IndiaRecordPagerFragmentAdapter;
import wiki.scene.shop.mvp.BaseBackMvpFragment;
import wiki.scene.shop.ui.mine.mvpview.IIdianaRecordView;
import wiki.scene.shop.ui.mine.presenter.IndianaRecordPresenter;

/**
 * Case By:夺宝记录主界面
 * package:wiki.scene.shop.ui.mine
 * Author：scene on 2017/7/5 14:37
 */

public class OthersIndianaRecordFragment extends BaseBackMvpFragment<IIdianaRecordView, IndianaRecordPresenter> implements IIdianaRecordView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    Unbinder unbinder;
    @BindView(R.id.toolbar_text)
    TextView toolbarText;

    private int targetUserId = 0;

    public static OthersIndianaRecordFragment newInstance(int targetUserId) {
        Bundle args = new Bundle();
        args.putInt("id", targetUserId);
        OthersIndianaRecordFragment fragment = new OthersIndianaRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            targetUserId = getArguments().getInt("id", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_indiana_record, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarText.setVisibility(View.GONE);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        toolbarTitle.setText(R.string.indiana_record);
        initToolbarNav(toolbar);
        initView();
    }

    private void initView() {
        String tabTitle[] = {getString(R.string.all), getString(R.string.win)};
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(OthersIndianaRecordTypeFragment.newInstance(OthersIndianaRecordTypeFragment.OTHERS_INDIANA_RECORD_TYPE_ALL, targetUserId));
        fragmentList.add(OthersIndianaRecordTypeFragment.newInstance(OthersIndianaRecordTypeFragment.OTHERS_INDIANA_RECORD_TYPE_WIN, targetUserId));
        tab.addTab(tab.newTab().setText(tabTitle[0]));
        tab.addTab(tab.newTab().setText(tabTitle[1]));
        viewPager.setAdapter(new IndiaRecordPagerFragmentAdapter(getChildFragmentManager(), tabTitle, fragmentList));
        viewPager.setOffscreenPageLimit(2);
        tab.setupWithViewPager(viewPager);
        tab.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tab, 60, 60);
            }
        });
    }

    @Override
    public IndianaRecordPresenter initPresenter() {
        return new IndianaRecordPresenter(this);
    }

    @Override
    public void showLoading(@StringRes int resId) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }

    }
}
