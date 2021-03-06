package wiki.scene.shop.ui.mine;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.zhl.cbdialog.CBDialogBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import wiki.scene.loadmore.PtrClassicFrameLayout;
import wiki.scene.loadmore.PtrDefaultHandler;
import wiki.scene.loadmore.PtrFrameLayout;
import wiki.scene.loadmore.StatusViewLayout;
import wiki.scene.loadmore.utils.PtrLocalDisplay;
import wiki.scene.shop.R;
import wiki.scene.shop.adapter.ExchangeAdapter;
import wiki.scene.shop.config.AppConfig;
import wiki.scene.shop.dialog.ExtractDialog;
import wiki.scene.shop.entity.PrizeInfo;
import wiki.scene.shop.event.RefreshMineEvent;
import wiki.scene.shop.http.api.ApiUtil;
import wiki.scene.shop.itemDecoration.SpacesItemDecoration;
import wiki.scene.shop.mvp.BaseBackMvpFragment;
import wiki.scene.shop.ui.mine.mvpview.IExchangeView;
import wiki.scene.shop.ui.mine.presenter.ExchangePresenter;
import wiki.scene.shop.utils.PriceUtil;
import wiki.scene.shop.utils.UpdatePageUtils;
import wiki.scene.shop.widgets.LoadingDialog;

/**
 * 兑换
 * Created by scene on 2017/11/21.
 */

public class ExchangeFragment extends BaseBackMvpFragment<IExchangeView, ExchangePresenter> implements IExchangeView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrLayout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    @BindView(R.id.check_all)
    TextView checkAll;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.exchange)
    TextView exchange;
    Unbinder unbinder;
    @BindView(R.id.toolbar_text)
    TextView toolbarText;

    private ExchangeAdapter adapter;
    private List<PrizeInfo> list = new ArrayList<>();

    private LoadingDialog loadingDialog;

    private boolean isCheckedAll = false;

    public static ExchangeFragment newInstance() {
        Bundle args = new Bundle();
        ExchangeFragment fragment = new ExchangeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        toolbarTitle.setText("兑换");
        toolbarText.setText("领取奖品");
        initToolbarNav(toolbar);
        initView();
        presenter.getPrizeData(true);
        UpdatePageUtils.updatePagePosition(AppConfig.POSITION_EXCHANGE, 0);
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.getPrizeData(false);
            }
        });

        adapter = new ExchangeAdapter(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(PtrLocalDisplay.dp2px(10)));
        recyclerView.setAdapter(adapter);
        //取消动画
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        adapter.setOnExchangeItemClickListener(new ExchangeAdapter.OnExchangeItemClickListener() {
            @Override
            public void onClickNumberLess(int position) {
                try {
                    int checkNumber = list.get(position).getCheckedNumber();
                    if (checkNumber > 1) {
                        checkNumber -= 1;
                        list.get(position).setCheckedNumber(checkNumber);
                        adapter.notifyItemChanged(position);
                        setToatalPrice();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onClickNumberAdd(int position) {
                try {
                    int checkNumber = list.get(position).getCheckedNumber();
                    int maxNumber = list.get(position).getNumber();
                    if (checkNumber < maxNumber) {
                        checkNumber += 1;
                        list.get(position).setCheckedNumber(checkNumber);
                        adapter.notifyItemChanged(position);
                        setToatalPrice();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClickStatus(int position) {
                try {
                    boolean state = list.get(position).isChecked();
                    list.get(position).setChecked(!state);
                    adapter.notifyItemChanged(position);
                    for (PrizeInfo info : list) {
                        if (!info.isChecked()) {
                            isCheckedAll = false;
                            break;
                        } else {
                            isCheckedAll = true;
                        }
                    }
                    checkAll.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), isCheckedAll ? R.drawable.ic_address_choosed_s : R.drawable.ic_address_choosed_d), null, null, null);
                    setToatalPrice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void showLoading(int resId) {
        try {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog.getInstance(getContext());
            }
            loadingDialog.showLoadingDialog(getString(resId));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void hideLoading() {
        try {
            if (loadingDialog != null) {
                loadingDialog.cancelLoadingDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ExchangePresenter initPresenter() {
        return new ExchangePresenter(this);
    }


    @Override
    public void showLoadingPage() {
        try {
            statusLayout.showLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showContentPage() {
        try {
            statusLayout.showContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showFailPage() {
        try {
            statusLayout.showFailed(retryListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshComplite() {
        try {
            ptrLayout.refreshComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showMessage(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void getPrizeInfoListSuccess(List<PrizeInfo> data) {
        try {
            list.clear();
            list.addAll(data);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                list.get(i).setCheckedNumber(1);
            }
            adapter.notifyDataSetChanged();
            setToatalPrice();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getPrizeIds() {
        StringBuilder idsBuilder = new StringBuilder();
        for (PrizeInfo info : list) {
            if (info.isChecked()) {
                idsBuilder.append(info.getId()).append(",");
            }
        }
        String ids = idsBuilder.toString();
        ids = ids.substring(0, ids.length() - 1);
        return ids;
    }

    @Override
    public String getNumbers() {
        StringBuilder numbersBuilder = new StringBuilder();
        for (PrizeInfo info : list) {
            if (info.isChecked()) {
                numbersBuilder.append(info.getCheckedNumber()).append(",");
            }
        }
        String numbers = numbersBuilder.toString();
        numbers = numbers.substring(0, numbers.length() - 1);
        return numbers;
    }

    @Override
    public void exchangeSuccess() {
        try {
            showOpenViewDialog("恭喜您兑换成功");
            ptrLayout.autoRefresh();
            EventBus.getDefault().post(new RefreshMineEvent());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exchangeFail() {
        try {
            showOpenViewDialog("兑换失败，请重试");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            presenter.getPrizeData(true);
        }
    };

    //显示总价
    private void setToatalPrice() {
        int total_Price = 0;
        for (PrizeInfo info : list) {
            if (info.isChecked()) {
                total_Price = total_Price + (info.getCheckedNumber() * info.getCost());
            }
        }
        totalPrice.setText(PriceUtil.getPrice(total_Price));
    }

    /**
     * 全选
     */
    @OnClick(R.id.check_all)
    public void onClickCheckAll() {
        isCheckedAll = !isCheckedAll;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            list.get(i).setChecked(isCheckedAll);
            list.get(i).setCheckedNumber(list.get(i).getNumber());
        }
        adapter.notifyDataSetChanged();
        setToatalPrice();
        checkAll.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), isCheckedAll ? R.drawable.ic_address_choosed_s : R.drawable.ic_address_choosed_d), null, null, null);
    }

    @OnClick(R.id.exchange)
    public void onClickExchange() {
        presenter.exchange();
    }

    @Override
    public void onDestroyView() {
        try {
            OkGo.getInstance().cancelTag(ApiUtil.PRIZE_LIST_TAG);
            OkGo.getInstance().cancelTag(ApiUtil.EXCHANGE_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideLoading();
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showOpenViewDialog(String message) {
        CBDialogBuilder builder = new CBDialogBuilder(getContext());
        TextView titleView = builder.getView(R.id.dialog_title);
        titleView.setSingleLine(false);
        builder.setTouchOutSideCancelable(false)
                .showCancelButton(false)
                .setTitle(message)
                .setMessage("")
                .setCustomIcon(0)
                .setConfirmButtonText("确定")
                .setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_SLID_BOTTOM)
                .setButtonClickListener(true, new CBDialogBuilder.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case BUTTON_CONFIRM:
                                dialog.cancel();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create().show();
    }

    private ExtractDialog dialog;

    @OnClick(R.id.toolbar_text)
    public void onClickToolbarText() {
        try {
            if (dialog == null) {
                ExtractDialog.Builder builder = new ExtractDialog.Builder(_mActivity);
                builder.setListener(new ExtractDialog.ExtractDialogConfirmListener() {
                    @Override
                    public void onClickConfirm() {
                        String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=170059106&version=1";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                    }
                });
                dialog = builder.create();
            }
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
