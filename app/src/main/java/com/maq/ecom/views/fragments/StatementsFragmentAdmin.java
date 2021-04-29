//package com.maq.ecom.views.fragments;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.cardview.widget.CardView;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.gson.JsonObject;
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Chunk;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.pdf.draw.LineSeparator;
//import com.maq.sstrading.R;
//import com.maq.sstrading.adapter.CustomerSpinnerAdapter;
//import com.maq.sstrading.adapter.StatementAdapter;
//import com.maq.sstrading.database.SQLiteHelper;
//import com.maq.sstrading.database.SQLiteManager;
//import com.maq.sstrading.database.SessionManager;
//import com.maq.sstrading.helper.CustomDatePicker;
//import com.maq.sstrading.helper.LoadingDialog;
//import com.maq.sstrading.helper.Utils;
//import com.maq.sstrading.interfaces.ApiConfig;
//import com.maq.sstrading.interfaces.RetrofitRespondListener;
//import com.maq.sstrading.model.Customer;
//import com.maq.sstrading.model.Statement;
//import com.maq.sstrading.networking.RetrofitClient;
//import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import de.hdodenhof.circleimageview.CircleImageView;
//import retrofit2.Call;
//import retrofit2.Response;
//
//import static com.maq.sstrading.views.activities.MainActivity.visibleFragment;
//
//public class StatementsFragmentAdmin extends Fragment implements RetrofitRespondListener {
//
//    SessionManager sessionManager;
//    LoadingDialog loadingDialog;
//    List<Statement> arrayList = new ArrayList<>();
//    List<Customer> listCustomer = new ArrayList<>();
//    String selectedCustomerCode = "All";
//    String fromDate, toDate, currentDate;
//    double selectedCustomerOpeningBal = 0.0;
//    String selectedCustomerName = "", selectedCustomerAddress = "";
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
//    @BindView(R.id.stFrag_selectedCustomer)
//    TextView tv_selectedCustomer;
//
//    @BindView(R.id.stFrag_layout_fromDate)
//    CardView layoutFromDate;
//    @BindView(R.id.stFrag_layout_toDate)
//    CardView layoutToDate;
//
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_statements_admin, container, false);
//        setHasOptionsMenu(true); //toolbar item click
//        ButterKnife.bind(this, root);
//        init();
//        listener();
//        fetchCustomers();
//        fetchStatements();
//        fetchLedger();
//        return root;
//    }
//
////    @Override
////    public void setMenuVisibility(final boolean visible) {
////        super.setMenuVisibility(visible);
////        if (visible) ((MainActivity)getActivity()).menuItemAdd.setVisible(true);
////    }
//
//    @SuppressLint("SetTextI18n")
//    private void init() {
//        visibleFragment = StatementsFragment.class.getSimpleName();
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
//                .API_myStatements(sessionManager.getFirmId(), "All", "2020-01-01", toDate);
//        RetrofitClient.callRetrofit(apiCall, "STATEMENTS", this);
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
//    public void fetchLedger() {
//        if (!Utils.isTodaysDate(sessionManager.getLedgerAllLastFetched())) {
//            fetchLedgerOnline();
//        } else fetchLedgerOffline();
//    }
//
//    public void fetchLedgerOnline() {
//        loadingDialog.show();
//        Call<JsonObject> apiCall = RetrofitClient.getRetrofitInstance(getActivity()).create(ApiConfig.class)
//                .API_ledger(sessionManager.getFirmId(), "All");
//        RetrofitClient.callRetrofit(apiCall, "LEDGER", this);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_sync: {
//                fetchStatementsOnline();
//                fetchLedgerOnline();
//                fetchCustomersOnline();
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
//            case "LEDGER":
//                try {
//                    callbackLedger(response);
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
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        String trnNo = object.getString("TrnNo");
//                        String trnDate = object.getString("TrnDate");
//                        String recvAmount = object.getString("RecvAmount");
//                        String orderAmount = object.getString("OrderAmount");
//                        String customerCode = object.getString("CustomerCode");
//
//                        //insert to SQLite
//                        dbManager.insertStatement(trnNo, trnDate, recvAmount, orderAmount, customerCode, fromDate, toDate);
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
//    }
//
//
//    private void fetchStatementsOffline() {
//        arrayList = new ArrayList<>();
//        arrayList.clear();
//
//        SQLiteManager sqLiteManager = new SQLiteManager(getActivity());
//        sqLiteManager.open();
//        Cursor cursor = sqLiteManager.fetchStatements();
//
//        arrayList.clear(); //clear array
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String trnNo = cursor.getString(cursor.getColumnIndex(SQLiteHelper.ST_TRANS_NO));
//            String trnDate = cursor.getString(cursor.getColumnIndex(SQLiteHelper.ST_TRANS_DATE));
//            String recvAmount = cursor.getString(cursor.getColumnIndex(SQLiteHelper.ST_RECV_AMT));
//            String orderAmount = cursor.getString(cursor.getColumnIndex(SQLiteHelper.ST_ORD_AMT));
//            String customerCode = cursor.getString(cursor.getColumnIndex(SQLiteHelper.ST_CUS_CODE));
////             fromDate = cursor.getString(cursor.getColumnIndex(SQLiteHelper.FROM_DATE));
////             toDate = cursor.getString(cursor.getColumnIndex(SQLiteHelper.TO_DATE));
////            tv_fromDate.setText(fromDate);
////            tv_toDate.setText(toDate);
//
//            if (Utils.isDateAfter(this.fromDate, trnDate) && Utils.isDateBefore(this.toDate, trnDate)) {
//                if (selectedCustomerCode.equals(customerCode)) {
//                    arrayList.add(new Statement(trnNo, trnDate, recvAmount, orderAmount, customerCode));
////                    updateSummary(status, recvAmount);
//                }
//            }
//
//            cursor.moveToNext();
//        }
//
//        if (arrayList.size() > 0) {
//            tv_noSt.setVisibility(View.GONE);
//        } else tv_noSt.setVisibility(View.VISIBLE);
//
//        recyclerView.setAdapter(new StatementAdapter(getActivity(), arrayList));
////        resetSummary();
//    }
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
//                        if (customerCode.equals("CUS00002")) {
//                            Log.e("TAG=", "callbackLedger: " + trnDate);
//                            Log.e("TAG=", "callback: "+trnNo );
//                        }
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
//            fetchLedgerOffline();
//
//
//        }
//
//    }
//
//
//    private void fetchLedgerOffline() {
//
//        double totalOpening = 0.0;
//        double totalBill = 0.0;
//        double totalReceive = 0.0;
//        double totalBalance = 0.0;
//
//        double previousBills = 0.0;
//        double previousReceiving = 0.0;
//
//        SQLiteManager sqLiteManager = new SQLiteManager(getActivity());
//        sqLiteManager.open();
//        Cursor cursor = sqLiteManager.fetchLedgers();
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String trnNo = cursor.getString(cursor.getColumnIndex(SQLiteHelper.LGR_TRANS_NO));
//            String trnDate = cursor.getString(cursor.getColumnIndex(SQLiteHelper.LGR_TRANS_DATE));
//            String bill = cursor.getString(cursor.getColumnIndex(SQLiteHelper.LGR_BILL));
//            String receive = cursor.getString(cursor.getColumnIndex(SQLiteHelper.LGR_RECV));
//            String customerCode = cursor.getString(cursor.getColumnIndex(SQLiteHelper.LGR_CUS_CODE));
//
//            if (selectedCustomerCode.equals(customerCode)) {
//                if (Utils.isDateAfter(this.fromDate, trnDate) && Utils.isDateBefore(this.toDate, trnDate)) {
//                    totalBill = totalBill + Double.parseDouble(bill);
//                    totalReceive = totalReceive + Double.parseDouble(receive);
//                }
//
//                if (Utils.isDateBeforeFromCurrent(this.fromDate, trnDate)) {
//                    previousBills = previousBills + Double.parseDouble(bill);
//                    previousReceiving = previousReceiving + Double.parseDouble(receive);
//                }
//            }
//            cursor.moveToNext();
//        }
//
//
//        totalOpening = selectedCustomerOpeningBal + previousBills - previousReceiving;
//        totalBalance = totalOpening + totalBill - totalReceive;
//
//        resetSummary(totalOpening, totalBill, totalReceive, totalBalance);
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void resetSummary(double totalOpening, double totalBill, double totalReceive, double totalBalance) {
//        tv_opening.setText(getResources().getString(R.string.INR_symbol) + totalOpening);
//        tv_totalBill.setText(getResources().getString(R.string.INR_symbol) + totalBill);
//        tv_totalReceive.setText(getResources().getString(R.string.INR_symbol) + totalReceive);
//        tv_totalBalance.setText(getResources().getString(R.string.INR_symbol) + totalBalance);
//    }
//
//    private void callbackCustomer(Response<?> response) throws JSONException {
//        JSONObject jsonObject = new JSONObject(response.body().toString());
//        if (jsonObject.getString("error").equals("false")) {
//            if (jsonObject.has("allcustomers")) {
//                listCustomer.clear();
//                JSONArray jsonArray = jsonObject.getJSONArray("allcustomers");
//                if (jsonArray.length() > 0) {
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
//                        listCustomer.add(new Customer(customerCode, customerName, openingBalance, openingCrt, address, null));
//
//                        //insert to SQLite
//                        dbManager.insertCustomer(customerCode, customerName, openingBalance, openingCrt, address);
//                    }
//                    dbManager.close();
//                    sessionManager.setCustomerLastFetched(Utils.formatDateTimeFromTS(Utils.getSysTimeStamp(), "yyyy/MM/dd"));
//
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
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
//                .setPositiveButton("OK", (dialog, id) -> {
//                    Customer model = (Customer) spinner.getSelectedItem();
//                    selectedCustomerCode = model.getCustomerCode();
//                    selectedCustomerName = model.getCustomerName();
//                    selectedCustomerAddress = model.getAddress();
//                    tv_selectedCustomer.setText(model.getCustomerName());
//                    selectedCustomerOpeningBal = Double.parseDouble(model.getOpeningBalance());
//                }).show();
//
//    }
//
//    private void generatePDF() {
//        String str_openingBal = tv_opening.getText().toString().replace("₹", "");
//        String str_closingBal = tv_totalBalance.getText().toString().replace("₹", "");
//
//        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
//        Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
//        Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD | Font.UNDERLINE);
//        Font subFontBold = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
//        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
//        Font smallNormal = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
//
//        try {
//            //init document
//            Document document = new Document(PageSize.A4);
//
//            //set local storage path
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFs";
//            File dir = new File(path);
//            if (!dir.exists())
//                dir.mkdirs();
//
//            File file = new File(dir, "Account Statement.pdf");
//
//            //start PDF writing
//            PdfWriter.getInstance(document, new FileOutputStream(file));
//
//            //close document here
//            document.open();
//            loadingDialog.show();
//
//            PdfPTable pdfPTable;
//            PdfPCell pdfPCell;
//
//            /** row 1*/
//            //firm name
//            pdfPTable = new PdfPTable(1);
//            pdfPTable.setWidthPercentage(100f);
//            pdfPCell = new PdfPCell(new Paragraph(sessionManager.getFirmName(), catFont));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            pdfPCell.setPadding(5f);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            document.add(pdfPTable);
//
//            /** row 2*/
//            //firm address
//            pdfPTable = new PdfPTable(1);
//            pdfPTable.setWidthPercentage(100f);
//            pdfPCell = new PdfPCell(new Paragraph(sessionManager.getFirmAddress(), smallBold));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            pdfPCell.setPadding(5f);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            document.add(pdfPTable);
//
//            /** row 3*/
//            //pdf name
//            pdfPTable = new PdfPTable(1);
//            pdfPTable.setWidthPercentage(100f);
//            pdfPCell = new PdfPCell(new Paragraph("CUSTOMER ACCOUNT STATEMENT", subFont));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            pdfPCell.setPadding(5f);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            document.add(pdfPTable);
//
//            /** row 4*/
//            //date
//            pdfPTable = new PdfPTable(1);
//            pdfPTable.setWidthPercentage(100f);
//            pdfPCell = new PdfPCell(new Paragraph("From " + fromDate + " To " + toDate, smallNormal));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            pdfPCell.setPadding(5f);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            document.add(pdfPTable);
//
//            //line break
//            document.add(new Paragraph("\n"));
//
//            /** row 5*/
//            //customer name
//            pdfPTable = new PdfPTable(2);
//            pdfPTable.setWidthPercentage(100f);
//            pdfPTable.setWidths(new int[]{2, 6});
//            pdfPCell = new PdfPCell(new Phrase("Customer Name:", smallBold));
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            pdfPCell = new PdfPCell(new Phrase(selectedCustomerName, smallNormal));
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            document.add(pdfPTable);
//
//
//            /** row 6*/
//            //customer address
//            pdfPTable = new PdfPTable(3);
//            pdfPTable.setWidthPercentage(100f);
//            pdfPTable.setWidths(new int[]{2, 4, 2});
//            pdfPCell = new PdfPCell(new Phrase("Customer Address:", smallBold));
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            pdfPCell = new PdfPCell(new Phrase(selectedCustomerAddress, smallNormal));
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            pdfPCell = new PdfPCell(new Phrase("Opening Balance: ₹" + str_openingBal, smallBold));
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            pdfPTable.addCell(pdfPCell);
//            document.add(pdfPTable);
//
//
//            //add straight line
//            document.add(new Chunk(new LineSeparator()));
//
//            /** row 7*/
//            //table header
//            pdfPTable = new PdfPTable(6);
//            pdfPTable.setWidthPercentage(100f);
//            pdfPTable.setWidths(new int[]{1, 3, 2, 2, 2, 2});
//            pdfPCell = new PdfPCell(new Phrase("Sr.", smallBold));
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            pdfPCell = new PdfPCell(new Phrase("TrnNo", smallBold));
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            pdfPCell = new PdfPCell(new Phrase("TrnDate", smallBold));
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            pdfPCell = new PdfPCell(new Phrase("Bill Amount", smallBold));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            pdfPCell = new PdfPCell(new Phrase("Recv Amount", smallBold));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            pdfPCell = new PdfPCell(new Phrase("Balance", smallBold));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
////            pdfPTable.setHeaderRows(1);
//            document.add(pdfPTable);
//
//            //add straight line
//            document.add(new Chunk(new LineSeparator()));
//
//            /** row 8*/
//            //table body
//            pdfPTable = new PdfPTable(6);
//            pdfPTable.setWidthPercentage(100f);
//            pdfPTable.setWidths(new int[]{1, 3, 2, 2, 2, 2});
//
//            double billAmtSum = 0, recvAmtSum = 0;
//            double balanceTemp = 0;
//
//            for (int i = 0; i < arrayList.size(); i++) {
//                //get data
//                Statement model = arrayList.get(i);
//                pdfPCell = new PdfPCell(new Phrase(String.valueOf(i + 1), smallNormal));
//                pdfPCell.setBorder(Rectangle.NO_BORDER);
//                pdfPTable.addCell(pdfPCell);
//                pdfPCell = new PdfPCell(new Phrase(model.getTrnNo(), smallNormal));
//                pdfPCell.setBorder(Rectangle.NO_BORDER);
//                pdfPTable.addCell(pdfPCell);
//                pdfPCell = new PdfPCell(new Phrase(Utils.formatDateTimeFromString(model.getTrnDate(), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm"), smallNormal));
//                pdfPCell.setBorder(Rectangle.NO_BORDER);
//                pdfPTable.addCell(pdfPCell);
//                pdfPCell = new PdfPCell(new Phrase(model.getOrderAmount(), smallNormal));
//                pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                pdfPCell.setBorder(Rectangle.NO_BORDER);
//                pdfPTable.addCell(pdfPCell);
//                pdfPCell = new PdfPCell(new Phrase(model.getRecvAmount(), smallNormal));
//                pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                pdfPCell.setBorder(Rectangle.NO_BORDER);
//                pdfPTable.addCell(pdfPCell);
//                double balance;
//                {
//                    if (i == 0) {
//                        balance = Double.parseDouble(str_openingBal) + Double.parseDouble(model.getOrderAmount()) - Double.parseDouble(model.getRecvAmount());
//                    } else
//                        balance = balanceTemp + Double.parseDouble(model.getOrderAmount()) - Double.parseDouble(model.getRecvAmount());
//
//                    balanceTemp = balance;
//                }
//                pdfPCell = new PdfPCell(new Phrase(String.valueOf(balance), smallNormal));
//                pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                pdfPCell.setBorder(Rectangle.NO_BORDER);
//                pdfPTable.addCell(pdfPCell);
//
//                //calculate sums
//                billAmtSum = billAmtSum + Double.parseDouble(model.getOrderAmount());
//                recvAmtSum = recvAmtSum + Double.parseDouble(model.getRecvAmount());
//            }
//            document.add(pdfPTable);
//
//
//            //line break
//            document.add(new Paragraph("\n"));
//            document.add(new Paragraph("\n"));
//            document.add(new Paragraph("\n"));
//            document.add(new Paragraph("\n"));
//            document.add(new Paragraph("\n"));
//
//            //add straight line
//            document.add(new Chunk(new LineSeparator()));
//
//            /** row 9*/
//            //table total
//            pdfPTable = new PdfPTable(4);
//            pdfPTable.setWidthPercentage(100f);
//            pdfPTable.setWidths(new int[]{6, 2, 2, 2});
//
//            pdfPCell = new PdfPCell(new Phrase("Total", smallNormal));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            pdfPCell = new PdfPCell(new Phrase("₹" + billAmtSum, smallNormal));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            pdfPCell = new PdfPCell(new Phrase("₹" + recvAmtSum, smallNormal));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            pdfPCell = new PdfPCell(new Phrase("", smallNormal));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            document.add(pdfPTable);
//
//            //add straight line
//            document.add(new Chunk(new LineSeparator()));
//
//            /** row */
//            //closing balance
//            pdfPTable = new PdfPTable(1);
//            pdfPTable.setWidthPercentage(100f);
//            pdfPCell = new PdfPCell(new Phrase("Closing Balance: ₹" + str_closingBal, smallBold));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            pdfPCell.setBorder(Rectangle.NO_BORDER);
//            pdfPTable.addCell(pdfPCell);
//            document.add(pdfPTable);
//
//            //close document here
//            document.close();
//            loadingDialog.dismiss();
//
//            //share pdf
//            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//            Uri screenshotUri = Uri.parse(file.getPath());
//            sharingIntent.setType("*/*");
//            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//            startActivity(Intent.createChooser(sharingIntent, "Share using"));
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}