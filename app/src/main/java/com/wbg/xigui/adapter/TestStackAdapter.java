package com.wbg.xigui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;
import com.wbg.xigui.R;
import com.wbg.xigui.bean.BankBean;

public class TestStackAdapter extends StackAdapter<BankBean> {

    public TestStackAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindView(BankBean data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof ColorItemViewHolder) {
            ColorItemViewHolder h = (ColorItemViewHolder) holder;
            h.onBind(data, position);
        }
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view;
        view = getLayoutInflater().inflate(R.layout.item_bank_card, parent, false);
        return new ColorItemViewHolder(view);

    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_bank_card;
    }

     class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        TextView mTextTitle;
        TextView textView;

        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            textView = (TextView) view.findViewById(R.id.tv_number);
        }

        @Override
        public void onItemExpand(boolean b) {

        }

        public void onBind(BankBean data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data.getColor()), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(data.getBankName());
            textView.setText(data.getAccountNo());
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicklistener.click(position);
                }
            });
        }

    }

   public interface Clicklistener{
        void click(int position);
    }

    public Clicklistener clicklistener;

    public void setClicklistener(Clicklistener clicklistener) {
        this.clicklistener = clicklistener;
    }

}
