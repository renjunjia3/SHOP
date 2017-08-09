package wiki.scene.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wiki.scene.shop.R;
import wiki.scene.shop.ShopApplication;
import wiki.scene.shop.entity.ListGoodsInfo;
import wiki.scene.shop.widgets.RatioImageView;

/**
 * Case By:夺宝的adapter
 * package:wiki.scene.shop.adapter
 * Author：scene on 2017/6/28 15:31
 */

public class IndianaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ListGoodsInfo> list;

    public IndianaAdapter(Context context, List<ListGoodsInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndianaViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_indiana_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IndianaViewHolder viewHolder = (IndianaViewHolder) holder;
        ListGoodsInfo info = list.get(position);
        viewHolder.goodsName.setText(info.getTitle());
        Glide.with(context).load(ShopApplication.configInfo.getFile_domain() + info.getThumb()).into(viewHolder.goodsImage);
        int precent = info.getCurrent_source() * 100 / info.getNeed_source();
        viewHolder.progressText.setText(precent + "%");
        viewHolder.progressBar.setProgress(precent);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    static class IndianaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goods_image)
        RatioImageView goodsImage;
        @BindView(R.id.goods_name)
        TextView goodsName;
        @BindView(R.id.progress_text)
        TextView progressText;
        @BindView(R.id.immddiately_indiana)
        TextView immddiatelyIndiana;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        IndianaViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
