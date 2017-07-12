package wiki.scene.shop.ui.mine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import wiki.scene.shop.R;
import wiki.scene.shop.ShopApplication;
import wiki.scene.shop.activity.LoginActivity;
import wiki.scene.shop.event.ChooseAvaterResultEvent;
import wiki.scene.shop.event.RegisterSuccessEvent;
import wiki.scene.shop.event.StartBrotherEvent;
import wiki.scene.shop.event.UserInfoUpdateSuccessEvent;
import wiki.scene.shop.mvp.BaseMainMvpFragment;
import wiki.scene.shop.ui.mine.mvpview.IMineView;
import wiki.scene.shop.ui.mine.presenter.MinePresenter;
import wiki.scene.shop.utils.SharedPreferencesUtil;

/**
 * Case By:我的
 * package:wiki.scene.shop.fragment.indiana
 * Author：scene on 2017/6/26 14:13
 */
public class MineFragment extends BaseMainMvpFragment<IMineView, MinePresenter> implements IMineView {

    @BindView(R.id.user_avater)
    ImageView userAvater;
    @BindView(R.id.image_level)
    TextView imageLevel;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.level)
    TextView level;
    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.coin_number)
    TextView coinNumber;

    private ProgressDialog progressDialog;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        progressDialog = new ProgressDialog(_mActivity);
        progressDialog.setMessage("正在上传");

        if (ShopApplication.hasLogin) {
            hasLogin();
        } else {
            hasNoLogin();
        }

    }

    @OnClick(R.id.user_avater)
    public void onClickUserAvater() {
        if (ShopApplication.hasLogin) {
            startActivity(new Intent(_mActivity, MineInfoActivity.class));
        } else {
            enterLogin();
        }
    }

    /**
     * 充值
     */
    @OnClick(R.id.recharge)
    public void onClickRecharge() {
        presenter.recharge();
    }

    /**
     * 夺宝记录
     */
    @OnClick(R.id.indiana_record)
    public void onClickIndianaRecord() {
        presenter.clickIndianaRecord();
    }

    @OnClick(R.id.win_record)
    public void onClickWinRecord() {
        presenter.clickWinRecord();
    }

    @OnClick(R.id.mine_share)
    public void onClickMyShareOrder() {
        presenter.clickMineShare();
    }

    @OnClick(R.id.receiver_address)
    public void onClickReceiverAddress() {
        presenter.clickMineReceiverAddress();
    }

    @OnClick(R.id.mine_red)
    public void onClickMineRed() {
        presenter.clickMineRed();
    }

    @OnClick(R.id.setting)
    public void onClickSetting() {
        presenter.clickSetting();
    }

    @Override
    public void showLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    @Override
    public MinePresenter initPresenter() {
        return new MinePresenter(this);
    }


    /**
     * 登录
     */
    @Override
    public void enterLogin() {
        startActivity(new Intent(_mActivity, LoginActivity.class));
    }

    /**
     * 夺宝记录
     */
    @Override
    public void enterIndianaRecord() {
        EventBus.getDefault().post(new StartBrotherEvent(IndianaRecordFragment.newInstance()));
    }

    /**
     * 中奖记录
     */
    @Override
    public void enterWinRecord() {
        EventBus.getDefault().post(new StartBrotherEvent(WinRecordFragment.newInstance()));
    }

    /**
     * 夺宝攻略
     */
    @Override
    public void enterIndianaRaiders() {

    }

    /**
     * 晒单
     */
    @Override
    public void enterMineShare() {
        EventBus.getDefault().post(new StartBrotherEvent(MyShareOrderFragment.newInstance()));
    }

    /**
     * 我的红包
     */
    @Override
    public void enterMineRed() {
        EventBus.getDefault().post(new StartBrotherEvent(MineRedFragment.newInstance()));
    }

    /**
     * 我的收货地址
     */
    @Override
    public void enterReceiverAddress() {
        EventBus.getDefault().post(new StartBrotherEvent(MineReceiverAddressFragment.newInstance()));
    }

    /**
     * 服务中心
     */
    @Override
    public void enterServiceCenter() {

    }

    /**
     * 分享app
     */
    @Override
    public void enterShareApp() {

    }

    /**
     * 设置
     */
    @Override
    public void enterSetting() {
        EventBus.getDefault().post(new StartBrotherEvent(SettingFragment.newInstance()));
    }

    @Override
    public void hasNoLogin() {
        level.setVisibility(View.GONE);
        score.setVisibility(View.GONE);
        username.setText(R.string.please_login);
        Glide.with(_mActivity).load(R.drawable.ic_default_avater).bitmapTransform(new CropCircleTransformation(_mActivity)).into(userAvater);
        imageLevel.setText(String.valueOf(1));
        coinNumber.setText(String.format(getString(R.string.coin_number), 0));
    }

    @Override
    public void hasLogin() {
        level.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        level.setText(String.format(getString(R.string.mine_level), ShopApplication.userInfo.getLevel()));
        score.setText(String.format(getString(R.string.mine_score), ShopApplication.userInfo.getScore()));
        imageLevel.setText(String.valueOf(ShopApplication.userInfo.getLevel()));
        username.setText(ShopApplication.userInfo.getNickname().isEmpty() ? ShopApplication.userInfo.getMobile() : ShopApplication.userInfo.getNickname());
        Glide.with(_mActivity).load(ShopApplication.userInfo.getAvatar()).bitmapTransform(new CropCircleTransformation(_mActivity)).error(R.drawable.ic_default_avater).into(userAvater);
        coinNumber.setText(String.format(getString(R.string.coin_number), ShopApplication.userInfo.getMoney()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeUserAvater(ChooseAvaterResultEvent event) {
        Glide.with(this).load("file://" + event.avaterPath)
                .bitmapTransform(new CropCircleTransformation(_mActivity))
                .into(userAvater);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterSuccess(RegisterSuccessEvent event) {
        if (event != null) {
            SharedPreferencesUtil.putString(_mActivity, ShopApplication.USER_INFO_KEY, new Gson().toJson(event.getUserInfo()));
            ShopApplication.userInfo = event.getUserInfo();
            ShopApplication.hasLogin = true;
            hasLogin();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}