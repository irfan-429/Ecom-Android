package com.maq.ecom.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.maq.ecom.R;

import butterknife.ButterKnife;

public class ItemsFragment extends Fragment  {

//    String TAG = NotificationFragment.class.getSimpleName();
//    SessionManager sessionManager;
//    LoadingDialog loadingDialog;
//    List<Bank> arrayList = new ArrayList<>();
//    String str_amt, str_remark;
//    MultipartBody.Part imgFileSlip = null;
//    String imgFileName;
//    List<Customer> listCustomer = new ArrayList<>();
//    String selectedCustomerCode;
//    BroadcastReceiver broadcastReceiver;
//
//    /**
//     * RecyclerView
//     */
//    @BindView(R.id.sendReceiptAct_rv)
//    RecyclerView recyclerView;
//
//    /**
//     * TextView
//     */
//    @BindView(R.id.sendReceiptAct_depositDate)
//    TextView tv_depositDate;
//    @BindView(R.id.sendReceiptAct_depositTime)
//    TextView tv_depositTime;
//    @BindView(R.id.sendReceiptAct_tv_noBAnk)
//    TextView tv_noBAnk;
//    @BindView(R.id.sendReceiptAct_selectedCustomer)
//    TextView tv_selectedCustomer;
//
//    @BindView(R.id.sendReceiptAct_et_amt)
//    EditText et_amt;
//    @BindView(R.id.sendReceiptAct_et_remark)
//    EditText et_remark;
//
//    @BindView(R.id.sendReceiptAct_iv_slip)
//    AppCompatImageView iv_slip;
//
//    @OnClick(R.id.sendReceiptAct_layout_depSlip)
//    void uploadSlip() {
//        CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .start(getActivity());
//    }
//
//    @OnClick(R.id.sendReceiptAct_btn_submit)
//    void submit() {
//        validateForm();
//    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_items, container, false);
        setHasOptionsMenu(true); //toolbar item click
        ButterKnife.bind(this, root);
//        init();
//        listener();
//        fetchBanks();
//        setupBroadCast();
//
//        if (sessionManager.isAdmin()) {
//            fetchCustomers();
//            tv_selectedCustomer.setVisibility(View.VISIBLE);
//        }


        return root;
    }


//    private void init() {
//        loadingDialog = new LoadingDialog(getActivity());
//        sessionManager = new SessionManager(getActivity());
//
//        tv_depositDate.setText(Utils.formatDateTimeFromTS(Utils.getSysTimeStamp(), "yyyy-MM-dd"));
//        tv_depositTime.setText(Utils.formatDateTimeFromTS(Utils.getSysTimeStamp(), "HH:mm"));
//    }
//
//    private void listener() {
//        tv_depositDate.setOnClickListener(v -> new CustomDatePicker(getActivity(), tv_depositDate));
//        tv_depositTime.setOnClickListener(v -> new CustomTimePicker(getActivity(), tv_depositTime));
//    }
//
//    public void fetchBanks() {
//        if (!Utils.isTodaysDate(sessionManager.getBankLastFetched())) {
//            fetchBankOnline();
//        } else fetchBankOffline();
//
//    }
//
//    private void fetchBankOnline() {
//        if (!loadingDialog.isShowing()) loadingDialog.show();
//        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getActivity()).create(ApiConfig.class)
//                .API_fetchBanks(sessionManager.getFirmId());
//        RetrofitClient.callRetrofit(apiCall, "BANKS", this);
//    }
//
//    private void fetchCustomers() {
//        if (!Utils.isTodaysDate(sessionManager.getCustomerLastFetched())) {
//            fetchCustomersOnline();
//        } else fetchCustomersOffline();
//    }
//
//    private void fetchCustomersOnline() {
//        loadingDialog.show();
//        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getActivity()).create(ApiConfig.class)
//                .API_customers(sessionManager.getFirmId());
//        RetrofitClient.callRetrofit(apiCall, "CUSTOMERS", this);
//    }
//
//    private void validateForm() {
//        str_amt = et_amt.getText().toString().trim();
//        str_remark = et_remark.getText().toString().trim();
//        if (TextUtils.isEmpty(str_amt)) {
//            Utils.showToast(getActivity(), "Amount must be filled");
//        } else if (imgFileSlip == null) {
//            Utils.showToast(getActivity(), "No slip uploaded");
//        } else uploadNewReceiptImg();
//    }
//
//    private void uploadNewReceiptImg() {
//        loadingDialog.show(); //show loader
//        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getActivity()).create(ApiConfig.class)
//                .API_uploadImg(imgFileSlip);
//        RetrofitClient.callRetrofit(apiCall, "NEW_RECEIPT_IMG", this);
//    }
//
//    private void requestNewReceipt() {
//        loadingDialog.show(); //show loader
//
//        String customerCode;
//        if (sessionManager.isAdmin()) {
//            customerCode = selectedCustomerCode;
//        } else customerCode = sessionManager.getUserMobile();
//
//        String date = tv_depositDate.getText() + " " + tv_depositTime;
//
//        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getActivity()).create(ApiConfig.class)
//                .API_requestNewReceipt(sessionManager.getFirmId(), date, customerCode, str_amt, str_remark, imgFileName, BanksAdapter.selectedBankId, "Pending");
//        RetrofitClient.callRetrofit(apiCall, "NEW_RECEIPT", this);
//    }
//
//
//    private void setupBroadCast() {
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent i) {
//                Uri resultUri = Uri.parse(i.getStringExtra("URI"));
//                iv_slip.setImageURI(resultUri);
//                File file = new File(resultUri.getPath());
//                imgFileSlip = Utils.ImageToMultipartBody("file", Utils.compressImage(file)); //get file to submit
//                imgFileName = file.getName();
//            }
//        };
//
//        getContext().registerReceiver(broadcastReceiver, new IntentFilter("getCroppedImgURI"));
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.action_sync) {
//            fetchBankOnline();
//            if (sessionManager.isAdmin()) {
//                fetchCustomersOnline();
//                tv_selectedCustomer.setVisibility(View.VISIBLE);
//            }
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onRetrofitSuccess(Response<?> response, String requestName) {
//        switch (requestName) {
//            case "BANKS":
//                try {
//                    callback(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//
//            case "NEW_RECEIPT_IMG":
//                try {
//                    callbackNewReceiptImg(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//
//            case "NEW_RECEIPT":
//                try {
//                    callbackNewReceipt(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//
//            case "CUSTOMERS":
//                try {
//                    callbackCustomer(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//        loadingDialog.dismiss();
//
//    }
//
//    @Override
//    public void onRetrofitFailure(String responseError, String requestName) {
//        loadingDialog.dismiss();
//        if (responseError.contains(getActivity().getString(R.string.str_unable_to_resolve_host)))
//            responseError = getActivity().getString(R.string.str_no_internet);
//
//        Utils.showSnackBar(getActivity(), responseError);
//    }
//
//    private void callback(Response<?> response) throws JSONException {
//        int responseCode = response.code();
//        if (responseCode == Utils.HTTP_OK) {
//            JSONObject jsonObject = new JSONObject(response.body().toString());
//            if (jsonObject.getString("error").equals("false")) {
//                if (jsonObject.has("allbanks")) {
//                    arrayList.clear();
//                    JSONArray jsonArray = jsonObject.getJSONObject("allbanks").getJSONArray("allbanks");
//                    if (jsonArray.length() > 0) {
//                        tv_noBAnk.setVisibility(View.GONE);
//                        //sqlite
//                        SQLiteManager dbManager = new SQLiteManager(getActivity());
//                        dbManager.open();
//                        dbManager.deleteTableRecord(SQLiteHelper.TABLE_BANK); //del and add new record
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//                            String bankId = object.getString("BankId");
//                            String bankName = object.getString("BankName");
//                            String bankImage = object.getString("BankImage");
//
//                            arrayList.add(new Bank(bankId, bankName, bankImage));
//                            //insert to SQLite
//                            dbManager.insertBank(bankId, bankName, bankImage);
//
//
//                        }
//
//                        dbManager.close();
//                        sessionManager.setBankLastFetched(Utils.formatDateTimeFromTS(Utils.getSysTimeStamp(), "yyyy/MM/dd"));
//
//                    } else tv_noBAnk.setVisibility(View.VISIBLE);
//
//                    recyclerView.setAdapter(new BanksAdapter(getActivity(), arrayList));
//
//                }
//
//            }
//        } else Utils.showToast(getActivity(), String.valueOf(responseCode));
//    }
//
//    private void fetchBankOffline() {
//        SQLiteManager sqLiteManager = new SQLiteManager(getActivity());
//        sqLiteManager.open();
//        Cursor cursor = sqLiteManager.fetchBanks();
//
//        arrayList.clear(); //clear array
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String bankId = cursor.getString(cursor.getColumnIndex(SQLiteHelper.BANK_ID));
//            String bankName = cursor.getString(cursor.getColumnIndex(SQLiteHelper.BANK_NAME));
//            String bankImage = cursor.getString(cursor.getColumnIndex(SQLiteHelper.BANK_IMG));
//
//            arrayList.add(new Bank(bankId, bankName, bankImage));
//            cursor.moveToNext();
//        }
//
//        if (arrayList.size() > 0) {
//            tv_noBAnk.setVisibility(View.GONE);
//            recyclerView.setAdapter(new BanksAdapter(getActivity(), arrayList));
//        }
//    }
//
//    private void callbackNewReceiptImg(Response<?> response) throws JSONException {
//        int responseCode = response.code();
//        if (responseCode == Utils.HTTP_OK) {
//            JSONObject jsonObject = new JSONObject(response.body().toString());
//            if (jsonObject.getString("success").equals("true")) {
//                requestNewReceipt();
//            } else Utils.showToast(getActivity(), jsonObject.getString("success"));
//        } else Utils.showToast(getActivity(), String.valueOf(responseCode));
//    }
//
//    private void callbackNewReceipt(Response<?> response) throws JSONException {
//        int responseCode = response.code();
//        if (responseCode == Utils.HTTP_OK) {
//            JSONObject jsonObject = new JSONObject(response.body().toString());
//            if (jsonObject.getString("error").equals("false")) {
//                Utils.showToast(getActivity(), jsonObject.getString("error_msg"));
//                if (getActivity() instanceof SendReceiptPage) getActivity().finish();
//            } else Utils.showToast(getActivity(), jsonObject.getString("error_msg"));
//
//        } else Utils.showToast(getActivity(), String.valueOf(responseCode));
//    }
//
//    private void callbackCustomer(Response<?> response) throws JSONException {
//        JSONObject jsonObject = new JSONObject(response.body().toString());
//        if (jsonObject.getString("error").equals("false")) {
//            if (jsonObject.has("allcustomers")) {
//                listCustomer.clear();
//                JSONArray jsonArray = jsonObject.getJSONArray("allcustomers");
//                if (jsonArray.length() > 0) {
////                    listCustomer.add(new Customer("All", "All", "CustomerCode"));
//                    //sqlite
//                    SQLiteManager dbManager = new SQLiteManager(getActivity());
//                    dbManager.open();
//                    dbManager.deleteTableRecord(SQLiteHelper.TABLE_CUSTOMER);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        String customerCode = object.getString("CustomerCode");
//                        String customerName = object.getString("CustomerName");
//                        String openingBalance = object.getString("OpeningBalance");
//                        String openingCrt = object.getString("OpeningCrt");
//                        String address = object.getString("Address");
//
//                        listCustomer.add(new Customer(customerCode, customerName, openingBalance, openingCrt, address, "CustomerCode"));
//                        //insert to SQLite
//                        dbManager.insertCustomer(customerCode, customerName, openingBalance, openingCrt, address);
//
//                    }
//                    dbManager.close();
//                    sessionManager.setCustomerLastFetched(Utils.formatDateTimeFromTS(Utils.getSysTimeStamp(), "yyyy/MM/dd"));
//
//                    setupCustomerSpinner(listCustomer);
//                }
//            }
//
//
//        }
//
//    }
//
//    private void fetchCustomersOffline() {
//        SQLiteManager sqLiteManager = new SQLiteManager(getActivity());
//        sqLiteManager.open();
//        Cursor cursor = sqLiteManager.fetchCustomers();
//
//        listCustomer.clear(); //clear array
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String id = cursor.getString(cursor.getColumnIndex(SQLiteHelper.ID));
//            String customerCode = cursor.getString(cursor.getColumnIndex(SQLiteHelper.CUSTOMER_CODE));
//            String customerName = cursor.getString(cursor.getColumnIndex(SQLiteHelper.CUSTOMER_NAME));
//            String openingBalance = cursor.getString(cursor.getColumnIndex(SQLiteHelper.CUSTOMER_OPENING_BALANCE));
//            String openingCrt = cursor.getString(cursor.getColumnIndex(SQLiteHelper.CUSTOMER_OPENING_CRT));
//            String address = cursor.getString(cursor.getColumnIndex(SQLiteHelper.CUSTOMER_ADDRESS));
//
//            listCustomer.add(new Customer(customerCode, customerName, openingBalance, openingCrt, address, null));
//            cursor.moveToNext();
//        }
//
//        setupCustomerSpinner(listCustomer);
//    }
//
//
//    private void setupCustomerSpinner(List<Customer> listCustomer) {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
//        final View formElementsView = inflater.inflate(R.layout.dynamic_spinner, null, false);
//        // Set up spinner BEFORE launching dialog
//        final SearchableSpinner spinner = formElementsView.findViewById(R.id.dynamic_spinner);
//
//        spinner.setTitle("Select One");
//        spinner.setPositiveButton("Close");
//        spinner.setAdapter(new CustomerSpinnerAdapter(getActivity(), R.layout.item_spinner, listCustomer, null));
//
//        // the alert dialog
//        new AlertDialog.Builder(getActivity())
//                .setView(formElementsView)
//                .setTitle("Select Customer")
//                .setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Customer model = (Customer) spinner.getSelectedItem();
//                        selectedCustomerCode = model.getCustomerCode();
//                        tv_selectedCustomer.setText(model.getCustomerName());
//                    }
//
//                }).show();
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        getContext().unregisterReceiver(broadcastReceiver);
//    }

}