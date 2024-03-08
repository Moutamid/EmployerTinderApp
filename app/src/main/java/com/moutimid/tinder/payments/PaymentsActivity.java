package com.moutimid.tinder.payments;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.common.collect.ImmutableList;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.moutamid.tinder.R;
import com.moutimid.tinder.helpers.Config;
import com.moutimid.tinder.model.UserModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaymentsActivity extends AppCompatActivity implements PaymentProductListSkuAdapter.ItemViewHolder.OnItemSelectedListener {


    TextView mContinueBtn;
    BottomSheetDialog sheetDialog;

    private List<ProductDetails> mPaymentProductModels;
    PaymentProductListSkuAdapter mPaymentProductListAdapter;
    public RecyclerView mRecyclerView;
    ImageView playPlanImage,backImageView;

    private ProductDetails productModel;
    private BillingClient mBillingClient;
    private ProgressBar progressBar;

    private UserModel mCurrentUser;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payments_product_list);

//        mCurrentUser = (User) ParseUser.getCurrentUser();
        handler = new Handler();

        mPaymentProductModels = new ArrayList<ProductDetails>();
        mPaymentProductListAdapter = new PaymentProductListSkuAdapter(this, this, mPaymentProductModels);

        backImageView = findViewById(R.id.backImageView);
        playPlanImage = findViewById(R.id.playPlanImage);
        progressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.productList_productPackageList);
        mContinueBtn = findViewById(R.id.payments_purchaseButton);

        initComponent();
    }


    private void initComponent() {


        mRecyclerView.setAdapter(mPaymentProductListAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);

        mRecyclerView.setItemViewCacheSize(12);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setBackgroundResource(R.color.white);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRecyclerView.setLayoutManager(layoutManager);

        mContinueBtn.setOnClickListener(v -> initPurchaseFlow());
        backImageView.setOnClickListener(v -> finish());
        playPlanImage.setOnClickListener(v -> {
            registerSheetDialoge();
        });

        initPurchase();
    }


    @Override
    public void onItemSelected(ProductDetails item) {
        productModel = item;

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mPaymentProductListAdapter.notifyDataSetChanged();
            }
        });
        mContinueBtn.setEnabled(true);

    }

    @Override
    public void onRadioSelected(ProductDetails item) {

    }


    private void initPurchase() {
        progressBar.setVisibility(View.VISIBLE);
        mBillingClient = BillingClient
                .newBuilder(this)
                .enablePendingPurchases()
                .setListener(
                        new PurchasesUpdatedListener() {
                            @Override
                            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                                    for (Purchase purchase : list) {

                                        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                            switch (purchase.getProducts().get(0)) {

                                                case Config.SUBS_1_WEEK:
                                                    acknowledgePurchaseSubs(7, purchase);
                                                    break;
                                                case Config.SUBS_1_MONTH:
                                                    acknowledgePurchaseSubs(30, purchase);
                                                    break;
                                                case Config.SUBS_1_YEAR:
                                                    acknowledgePurchaseSubs(365, purchase);
                                                    break;
                                            }

                                        }
                                    }
                                }
                            }
                        }
                ).build();

        establishConnection();


    }


    void establishConnection() {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    initSKUPurchase();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                establishConnection();
            }
        });
    }


    public void initSKUPurchase() {
        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(Config.SUBS_1_WEEK)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(Config.SUBS_1_MONTH)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(Config.SUBS_1_YEAR)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build());

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder().setProductList(productList).build();
        mBillingClient.queryProductDetailsAsync(params, new ProductDetailsResponseListener() {
                    public void onProductDetailsResponse(BillingResult billingResult, List<ProductDetails> productDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            if (productDetailsList != null && productDetailsList.size() > 0) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        createList(productDetailsList, productDetailsList.get(0));
                                    }
                                },2000);

                            } else {
                                progressBar.setVisibility(View.GONE);
                                //todo need to add subscription ids
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            //todo error
                        }

                    }
                }
        );

    }

    private void createList(List<ProductDetails> skuDetailsList, ProductDetails skuDetails) {

        mPaymentProductModels.clear();
        mPaymentProductModels.addAll(skuDetailsList);
        mPaymentProductModels.add(skuDetails);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mPaymentProductListAdapter.notifyDataSetChanged();
            }
        });
        productModel = mPaymentProductModels.get(0);
        mContinueBtn.setEnabled(true);

    }

    public void registerSheetDialoge(){
        sheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        sheetDialog.setContentView(R.layout.subscription_bottom_sheet);
        sheetDialog.setCancelable(true);
        sheetDialog.setCanceledOnTouchOutside(true);
        Button gotItButton = sheetDialog.findViewById(R.id.gotItButton);

        if (gotItButton != null) {
            gotItButton.setOnClickListener(v -> {
                if (sheetDialog.isShowing()){
                    sheetDialog.dismiss();
                }
            });
        }
        if (sheetDialog != null && !sheetDialog.isShowing()){
            sheetDialog.show();
        }
    }



    public void initPurchaseFlow() {
        if (productModel != null) {
            ImmutableList productDetailsParamsList = ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productModel).setOfferToken(productModel.getSubscriptionOfferDetails().get(0).getOfferToken()).build());
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList).build();
            mBillingClient.launchBillingFlow(PaymentsActivity.this, billingFlowParams);
        } else {
            mContinueBtn.setEnabled(false);
        }


    }


    public void acknowledgePurchaseSubs(int days, Purchase purchase) {
        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
        AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = billingResult1 -> {
            if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                addPremiumLimitDate(days);
            }
        };

        mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
    }

    public void addPremiumLimitDate(int days) {

        Calendar calendar = Calendar.getInstance();

        if (days == 7) {
            calendar.add(Calendar.DAY_OF_WEEK, 7);
        } else if (days == 30) {
            calendar.add(Calendar.DAY_OF_MONTH, 30);
        } else if (days == 365) {
            calendar.add(Calendar.MONTH, 12);
        }

//        mCurrentUser.setPremium(calendar.getTime());
//        mCurrentUser.saveEventually(e -> {
//            if (e == null) {
//                QuickHelp.showNotification(PaymentsActivity.this, Application.getInstance().getString(R.string.you_are_premium), false);
//            }
//        });
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}