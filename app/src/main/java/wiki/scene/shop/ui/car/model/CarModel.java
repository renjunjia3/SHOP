package wiki.scene.shop.ui.car.model;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import wiki.scene.shop.entity.CartResultInfo;
import wiki.scene.shop.http.api.ApiUtil;
import wiki.scene.shop.http.base.LzyResponse;
import wiki.scene.shop.http.callback.JsonCallback;
import wiki.scene.shop.http.listener.HttpResultListener;

/**
 * Case By:购物车
 * package:wiki.scene.shop.ui.car.model
 * Author：scene on 2017/7/13 14:25
 */

public class CarModel {
    /**
     * 获取购物车列表
     */
    public void getCarDataList(HttpParams params, final HttpResultListener<CartResultInfo> resultListener) {
        OkGo.<LzyResponse<CartResultInfo>>get(ApiUtil.API_PRE + ApiUtil.CAR)
                .tag(ApiUtil.CAR_TAG)
                .params(params)
                .execute(new JsonCallback<LzyResponse<CartResultInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<CartResultInfo>> response) {
                        resultListener.onSuccess(response.body().data);
                    }

                    @Override
                    public void onError(Response<LzyResponse<CartResultInfo>> response) {
                        super.onError(response);
                        if(response.getException()!=null){
                            resultListener.onFail(response.getException().getMessage());
                        }else{
                            resultListener.onFail(response.message());
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        resultListener.onFinish();
                    }
                });
    }
}