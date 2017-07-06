package wiki.scene.shop.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import wiki.scene.loadmore.utils.SceneLogUtil;
import wiki.scene.shop.R;
import wiki.scene.shop.activity.presenter.Register1Presenter;
import wiki.scene.shop.activity.mvpview.IRegister1View;
import wiki.scene.shop.event.RegisterSuccessEvent;
import wiki.scene.shop.mvp.BaseMvpActivity;
import wiki.scene.shop.utils.ToastUtils;

/**
 * Case By:注册第一步
 * package:wiki.scene.shop.activity
 * Author：scene on 2017/6/27 14:19
 */

public class Register1Activity extends BaseMvpActivity<IRegister1View, Register1Presenter> implements IRegister1View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.verification)
    EditText verification;
    @BindView(R.id.get_verification)
    TextView getVerification;
    @BindView(R.id.agree)
    CheckBox agree;

    private Unbinder unbinder;

    private ProgressDialog progressDialog;

    private int countDowntime = 60;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        unbinder = ButterKnife.bind(this);
        initToolbar();
        initView();
    }

    @Override
    public Register1Presenter initPresenter() {
        return new Register1Presenter(this);
    }

    private void initToolbar() {
        toolbarTitle.setText("注册");
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        progressDialog = new ProgressDialog(Register1Activity.this);
        progressDialog.setMessage(getString(R.string.is_get_verification));
    }

    @OnClick(R.id.next_step)
    public void onClickNextStep() {
        presenter.checkCode();
    }

    @OnClick(R.id.get_verification)
    public void onClickGetVerification() {
        presenter.getVerificationCode();
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
    public void showFail(String message) {
        ToastUtils.getInstance(Register1Activity.this).showToast(message);
    }

    @Override
    public void showFail(@StringRes int resId) {
        ToastUtils.getInstance(Register1Activity.this).showToast(resId);
    }

    @Override
    public void showCountDownTimer() {
        getVerification.setClickable(false);
        getVerification.setText(String.format(getString(R.string.retry_xx), 60));
        getVerification.setTextColor(getResources().getColor(R.color.bg_color_gray));
        getVerification.setBackgroundColor(getResources().getColor(R.color.text_color_title));
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (countDowntime > 1) {
                                countDowntime -= 1;
                                getVerification.setText(String.format(getString(R.string.retry_xx), countDowntime));
                            } else {
                                getVerification.setClickable(true);
                                getVerification.setText(R.string.get_verification);
                                getVerification.setTextColor(getResources().getColor(R.color.white));
                                getVerification.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                if (timer != null) {
                                    timer.cancel();
                                    timer = null;
                                }
                                if (timerTask != null) {
                                    timerTask.cancel();
                                    timerTask = null;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            SceneLogUtil.e("抛出View空异常");
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    @Override
    public void showLoading(String str) {
        if (progressDialog != null) {
            progressDialog.setMessage(str);
            progressDialog.show();
        }
    }

    @Override
    public void showLoading(@StringRes int resId) {
        if (progressDialog != null) {
            progressDialog.setMessage(getString(resId));
            progressDialog.show();
        }
    }

    @Override
    public void enterNextStep() {
        Intent intent = new Intent(Register1Activity.this, Register2Activity.class);
        intent.putExtra("phone", getPhoneNumber());
        intent.putExtra("code", getCode());
        startActivity(intent);
    }

    @Override
    public String getCode() {
        return verification.getText().toString().trim();
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber.getText().toString().trim();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterSuccess(RegisterSuccessEvent event) {
        if (event != null) {
            onBackPressed();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
        unbinder.unbind();
    }
}
