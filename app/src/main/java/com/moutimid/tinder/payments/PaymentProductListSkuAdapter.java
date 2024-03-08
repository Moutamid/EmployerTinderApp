package com.moutimid.tinder.payments;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.ProductDetails;
import com.moutamid.tinder.R;
import com.moutimid.tinder.helpers.Config;


import java.util.List;


public class PaymentProductListSkuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mActivity;
    private ItemViewHolder.OnItemSelectedListener itemSelectedListener;

    private List<ProductDetails> mPaymentProductModels;

    private static int lastSelectedPosition = 0;

    private static final int NORMAL_ITEM = 0;
    private static final int FOOTER_VIEW = 1;

    public PaymentProductListSkuAdapter(Activity activity, ItemViewHolder.OnItemSelectedListener itemSelectedListener, List<ProductDetails> paymentProductModelList) {
        this.mActivity = activity;
        this.itemSelectedListener = itemSelectedListener;
        this.mPaymentProductModels = paymentProductModelList;
    }

    private boolean isHeader(int position) {
        return position == mPaymentProductModels.size() - 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return isHeader(position) ? FOOTER_VIEW : NORMAL_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;
/*
        if (viewType == FOOTER_VIEW) {
            view = inflater.inflate(R.layout.payment_terms_item, parent, false);
            return new HeaderViewHolder(view);

        } else {*/

            view = inflater.inflate(R.layout.payment_product_package_list_item, parent, false);
            return new ItemViewHolder(view, itemSelectedListener);



    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ProductDetails productModel = mPaymentProductModels.get(position);

     /*   if (viewHolder.getItemViewType() == FOOTER_VIEW) {

            HeaderViewHolder headerViewHolder = ((HeaderViewHolder) viewHolder);

            SpannableString termsText = new SpannableString(mActivity.getString(R.string.terms_and_conditions));
            termsText.setSpan(new UnderlineSpan(), 0, termsText.length(), 0);
            headerViewHolder.mTermsnAndConditions.setText(termsText);
            headerViewHolder.mTermsnAndConditions.setOnClickListener(v -> QuickHelp.goToActivityWithNoClean(mActivity, WebUrlsActivity.class, WebUrlsActivity.WEB_URL_TYPE, Config.TERMS_OF_USE_IN_APP));

        } else */if (viewHolder.getItemViewType() == NORMAL_ITEM) {

            ItemViewHolder holder = ((ItemViewHolder) viewHolder);

            holder.selectedProduct.setChecked(lastSelectedPosition == position);

            switch (productModel.getProductId()) {
                case Config.SUBS_1_YEAR:

                    holder.ItemName.setText("1 year/29.99 USD");
                    holder.ItemPrice.setText("Unlock messages for 12 months");
                    holder.savePercent.setText("Save 75%");
                    break;
                case Config.SUBS_1_WEEK:
                    holder.ItemName.setText("1 week/5.99 USD");
                    holder.ItemPrice.setText("Unlock messages for one week");
                    holder.savePercent.setText("");

                    break;
                case Config.SUBS_1_MONTH:
                    holder.ItemName.setText("1 month/9.99 USD");
                    holder.ItemPrice.setText("Unlock messages for one month");
                    holder.savePercent.setText("");
                    break;
            }


            holder.mItem = productModel;
        }

    }



    @Override
    public int getItemCount() {
        return mPaymentProductModels.size()-1;
    }
        public void setSelected(ProductDetails paymentProductModel) {

        itemSelectedListener.onItemSelected(paymentProductModel);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ProductDetails mItem;
        OnItemSelectedListener itemSelectedListener;

        RelativeLayout mItemLayout;
        RadioButton selectedProduct;

        TextView ItemName;
        TextView ItemPrice;


        TextView savePercent;

        ItemViewHolder(View v, OnItemSelectedListener itemSelected) {
            super(v);

            itemSelectedListener = itemSelected;

            mItemLayout = v.findViewById(R.id.productListItem_paymentBox);

            selectedProduct = v.findViewById(R.id.productListItem_radio);
            ItemName = v.findViewById(R.id.productListItem_name);
            ItemPrice = v.findViewById(R.id.productListItem_totalCost);
            savePercent = v.findViewById(R.id.productListItem_details2);

            selectedProduct.setOnClickListener(view -> {

                lastSelectedPosition = getAdapterPosition();
                itemSelectedListener.onItemSelected(mItem);
            });

            mItemLayout.setOnClickListener(view -> {
                lastSelectedPosition = getAdapterPosition();
                itemSelectedListener.onItemSelected(mItem);
            });
        }

        public interface OnItemSelectedListener {

            void onItemSelected(ProductDetails item);
            void onRadioSelected(ProductDetails item);
        }
    }

}