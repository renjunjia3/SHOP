package wiki.scene.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wiki.scene.shop.R;

/**
 * Case By:商品详情土豪榜
 * package:wiki.scene.shop.adapter
 * Author：scene on 2017/7/5 11:06
 */

public class GoodsDetailTuhaoRankAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private LayoutInflater inflater;

    public GoodsDetailTuhaoRankAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TuhaoRankViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_goods_detail_tuhao_rank_item, parent, false);
            viewHolder = new TuhaoRankViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TuhaoRankViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(list.get(position));
        viewHolder.rank.setText("NO." + (position + 1));
        if (position == 0) {
            viewHolder.rank.setTextColor(Color.parseColor("#F8B551"));
        } else if (position == 1) {
            viewHolder.rank.setTextColor(Color.parseColor("#009944"));
        } else {
            viewHolder.rank.setTextColor(Color.parseColor("#2FACFF"));
        }
        return convertView;
    }

    static class TuhaoRankViewHolder {
        @BindView(R.id.rank)
        TextView rank;
        @BindView(R.id.user_avater)
        ImageView userAvater;
        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.person_times)
        TextView personTimes;

        TuhaoRankViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}