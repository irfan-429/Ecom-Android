package com.maq.ecom.views.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.CustomDatePicker;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.interfaces.RetrofitRespondListener;
import com.maq.ecom.model.Statement;
import com.maq.ecom.networking.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

import static com.maq.ecom.views.activities.MainActivity.visibleFragment;

public class CartFragment extends Fragment {

//    SessionManager sessionManager;
//    LoadingDialog loadingDialog;
//    List<Statement> arrayList = new ArrayList<>();
//
//
//    String fromDate, toDate, currentDate;
//
//    /**
//     * ImageView
//     */
//    @BindView(R.id.stFrag_iv_userImg)
//    CircleImageView iv_userImg;
//
//    @OnClick(R.id.stFrag_iv_run)
//    void reloadRecord() {
//        fromDate = tv_fromDate.getText().toString();
//        toDate = tv_toDate.getText().toString();
//        fetchStatementsOffline();
//        fetchLedgerOffline();
//        Utils.showSnackBar(getActivity(), "Record fetched successfully!");
//    }
//
//    @OnClick(R.id.stFrag_iv_exportPdf)
//    void exportPdf() {
//        generatePDF();
//    }
//
//    /**
//     * RecyclerView
//     */
//    @BindView(R.id.stFrag_rv)
//    RecyclerView recyclerView;
//
//    /**
//     * TextView
//     */
//    @BindView(R.id.stFrag_tv_noOrder)
//    TextView tv_noSt;
//    @BindView(R.id.stFrag_tv_currentDate)
//    TextView tv_currentDate;
//    @BindView(R.id.stFrag_iv_userName)
//    TextView tv_userName;
//    @BindView(R.id.stFrag_iv_userMob)
//    TextView tv_userMob;
//    @BindView(R.id.stFrag_tv_fromDate)
//    TextView tv_fromDate;
//    @BindView(R.id.stFrag_tv_toDate)
//    TextView tv_toDate;
//    @BindView(R.id.stFrag_tv_opening)
//    TextView tv_opening;
//    @BindView(R.id.stFrag_tv_totalBill)
//    TextView tv_totalBill;
//    @BindView(R.id.stFrag_tv_totalReceive)
//    TextView tv_totalReceive;
//    @BindView(R.id.stFrag_tv_totalBalance)
//    TextView tv_totalBalance;
//
//    @BindView(R.id.stFrag_layout_fromDate)
//    CardView layoutFromDate;
//    @BindView(R.id.stFrag_layout_toDate)
//    CardView layoutToDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        setHasOptionsMenu(true); //toolbar item click
        ButterKnife.bind(this, root);
//        init();
//        listener();
//        fetchStatements();
//        fetchLedger();
        return root;
    }

//
//    @SuppressLint("SetTextI18n")
//    private void init() {
//        visibleFragment = CartFragment.class.getSimpleName();
//        loadingDialog = new LoadingDialog(getActivity());
//        sessionManager = new SessionManager(getActivity());
//
//        currentDate = toDate = Utils.formatDateTimeFromTS(Utils.getSysTimeStamp(), "yyyy-MM-dd");
//        //from date= current date - 5 days
//        fromDate = Utils.addDays(-5);
//
//        Utils.loadProfileImage(getActivity(), "image/" + sessionManager.getProfileImg(), iv_userImg);
//        tv_currentDate.setText(currentDate);
//        tv_fromDate.setText(fromDate);
//        tv_toDate.setText(toDate);
//        tv_userName.setText(sessionManager.getUserName());
//        tv_userMob.setText("Mob: " + sessionManager.getUserMobile());
//    }
//
//
//    private void listener() {
//        layoutFromDate.setOnClickListener(v -> new CustomDatePicker(getActivity(), tv_fromDate));
//        layoutToDate.setOnClickListener(v -> new CustomDatePicker(getActivity(), tv_toDate));
//
//    }
//
//    public void fetchStatements() {
//        if (!Utils.isTodaysDate(sessionManager.getStatementLastFetched())) {
//            fetchStatementsOnline();
//        } else fetchStatementsOffline();
//    }
//
//    public void fetchStatementsOnline() {
//        loadingDialog.show();
//        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getActivity()).create(ApiConfig.class)
//                .API_myStatements(sessionManager.getFirmId(), sessionManager.getUserMobile(), "2020-01-01", toDate);
//        RetrofitClient.callRetrofit(apiCall, "STATEMENTS", this);
//    }
//
//    public void fetchLedger() {
//        if (!Utils.isTodaysDate(sessionManager.getLedgerAllLastFetched())) {
//            fetchLedgerOnline();
//        } else fetchLedgerOffline();
//    }
//
//    public void fetchLedgerOnline() {
//        loadingDialog.show();
//        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getActivity()).create(ApiConfig.class)
//                .API_ledger(sessionManager.getFirmId(), sessionManager.getUserMobile());
//        RetrofitClient.callRetrofit(apiCall, "LEDGER", this);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_sync: {
//                fetchStatementsOnline();
//                return true;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onRetrofitSuccess(Response<?> response, String requestName) {
//        switch (requestName) {
//            case "STATEMENTS":
//                try {
//                    callback(response);
//                    fetchLedger();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//
//            case "LEDGER":
//                try {
//                    callbackLedger(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//
//        }
//        loadingDialog.dismiss();
//
//    }
//
//    @Override
//    public void onRetrofitFailure(String responseError, String requestName) {
//        loadingDialog.dismiss();
//        if (responseError.contains(getString(R.string.str_unable_to_resolve_host)))
//            responseError = getString(R.string.str_no_internet);
//
//        Utils.showSnackBar(getActivity(), responseError);
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void callback(Response<?> response) throws JSONException {
//        JSONObject jsonObject = new JSONObject(response.body().toString());
//        if (jsonObject.getString("error").equals("false")) {
//            if (jsonObject.has("allpayments")) {
//                JSONArray jsonArray = jsonObject.getJSONArray("allpayments");
//                if (jsonArray.length() > 0) {
//                    tv_noSt.setVisibility(View.INVISIBLE);
//                    SQLiteManager dbManager = new SQLiteManager(getActivity());
//                    dbManager.open();
//                    dbManager.deleteTableRecord(SQLiteHelper.TABLE_STATEMENT);
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        String trnNo = object.getString("TrnNo");
//                        String trnDate = object.getString("TrnDate");
//                        String recvAmount = object.getString("RecvAmount");
//                        String orderAmount = object.getString("OrderAmount");
//
////                        arrayList.add(new Statement(trnNo, trnDate, recvAmount, orderAmount, sessionManager.getUserMobile()));
//
//                        //insert to SQLite
//                        dbManager.insertStatement(trnNo, trnDate, recvAmount, orderAmount, sessionManager.getUserMobile(), fromDate, toDate);
//                    }
//                    dbManager.close();
//                    sessionManager.setStatementLastFetched(Utils.formatDateTimeFromTS(Utils.getSysTimeStamp(), "yyyy/MM/dd"));
//
//                } else tv_noSt.setVisibility(View.VISIBLE);
//                fetchStatementsOffline();
////                recyclerView.setAdapter(new StatementAdapter(getActivity(), arrayList));
//
//            }
//
//            //countets
////            if (jsonObject.has("allpayments2")) {
////                JSONArray jsonArray = jsonObject.getJSONArray("allpayments2");
////                if (jsonArray.length() > 0) {
////                    JSONObject objectFailed = jsonArray.getJSONObject(0);
////                    String opening = objectFailed.getString("Opening");
////                    String bill = objectFailed.getString("Bill");
////                    String receive = objectFailed.getString("Receive");
////                    String balance = objectFailed.getString("Balance");
////                    tv_opening.setText(getActivity().getResources().getString(R.string.IND_symbol) + opening);
////                    tv_totalBill.setText(getActivity().getResources().getString(R.string.IND_symbol) + bill);
////                    tv_totalReceive.setText(getActivity().getResources().getString(R.string.IND_symbol) + receive);
////                    totalBalance.setText(getActivity().getResources().getString(R.string.IND_symbol) + balance);
////                } else resetSummary();
////
////            }
//        }
//
//
//    }
//
////    private void resetSummary() {
////        tv_opening.setText("₹0");
////        tv_totalBill.setText("₹0");
////        tv_totalReceive.setText("₹0");
////        totalBalance.setText("₹0");
////    }
//
//    @SuppressLint("SetTextI18n")
//    private void callbackLedger(Response<?> response) throws JSONException {
//        JSONObject jsonObject = new JSONObject(response.body().toString());
//        if (jsonObject.getString("error").equals("false")) {
//            if (jsonObject.has("allledger")) {
//                JSONArray jsonArray = jsonObject.getJSONArray("allledger");
//                if (jsonArray.length() > 0) {
//                    SQLiteManager dbManager = new SQLiteManager(getActivity());
//                    dbManager.open();
//                    dbManager.deleteTableRecord(SQLiteHelper.TABLE_LEDGER);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        String trnNo = object.getString("TrnNo");
//                        String trnDate = object.getString("TrnDate");
//                        String bill = object.getString("Bill");
//                        String receive = object.getString("Receive");
//                        String customerCode = object.getString("CustomerCode");
//
//                        //insert to SQLite
//                        dbManager.insertLedger(trnNo, trnDate, bill, receive, customerCode);
//                    }
//                    dbManager.close();
//
//                }
//
//            }
//
//            if (jsonObject.has("allcrateledger")) {
//                JSONArray jsonArray = jsonObject.getJSONArray("allcrateledger");
//                if (jsonArray.length() > 0) {
//                    SQLiteManager dbManager = new SQLiteManager(getActivity());
//                    dbManager.open();
//                    dbManager.deleteTableRecord(SQLiteHelper.TABLE_LEDGER_CRT);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        String trnNo = object.getString("TrnNo");
//                        String trnDate = object.getString("TrnDate");
//                        String bill = object.getString("Bill");
//                        String receive = object.getString("Receive");
//                        String customerCode = object.getString("CustomerCode");
//
//                        //insert to SQLite
//                        dbManager.insertLedgerCrt(trnNo, trnDate, bill, receive, customerCode);
//                    }
//                    dbManager.close();
//
//                }
//
//            }
//
//            sessionManager.setLedgerAllLastFetched(Utils.formatDateTimeFromTS(Utils.getSysTimeStamp(), "yyyy/MM/dd"));
//
//
//        }
//
//    }
//
//
//
//    @SuppressLint("SetTextI18n")
//    private void resetSummary(double totalOpening, double totalBill, double totalReceive, double totalBalance) {
//        tv_opening.setText(getResources().getString(R.string.INR_symbol) + totalOpening);
//        tv_totalBill.setText(getResources().getString(R.string.INR_symbol) + totalBill);
//        tv_totalReceive.setText(getResources().getString(R.string.INR_symbol) + totalReceive);
//        tv_totalBalance.setText(getResources().getString(R.string.INR_symbol) + totalBalance);
//    }
//




}