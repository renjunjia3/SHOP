package wiki.scene.shop.ui.mine.presenter;

import com.lzy.okgo.model.HttpParams;

import wiki.scene.shop.http.listener.HttpResultListener;
import wiki.scene.shop.ui.mine.model.MineInfoModel;
import wiki.scene.shop.ui.mine.mvpview.IMineInfoView;
import wiki.scene.shop.mvp.BasePresenter;

/**
 * Case By:个人资料
 * package:wiki.scene.shop.fragment.mine.presenter
 * Author：scene on 2017/6/29 17:47
 */

public class MineInfoPresenter extends BasePresenter<IMineInfoView> {
    private IMineInfoView mineInfoView;
    private MineInfoModel model;

    public MineInfoPresenter(IMineInfoView mineInfoView) {
        this.mineInfoView = mineInfoView;
        model = new MineInfoModel();
    }

    public void updateInfo() {
        if (mineInfoView != null) {
            if (mineInfoView.getNickName().isEmpty()) {
                mineInfoView.showFail("请填写昵称");
                return;
            }
            HttpParams params = new HttpParams();
            params.put("nickname", mineInfoView.getNickName());
            params.put("sex", mineInfoView.getSex());
            mineInfoView.showLoading("加载中...");
            model.updateUserInfo(params, new HttpResultListener<String>() {
                @Override
                public void onSuccess(String data) {
                    mineInfoView.showFail(data);
                }

                @Override
                public void onFail(String message) {
                    if (mineInfoView != null)
                        mineInfoView.showFail(message);
                }

                @Override
                public void onFinish() {
                    if(mineInfoView!=null)
                        mineInfoView.hideLoading();
                }
            });
        }

    }
}
