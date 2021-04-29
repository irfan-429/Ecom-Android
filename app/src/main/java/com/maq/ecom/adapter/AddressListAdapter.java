package com.maq.ecom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.maq.ecom.R;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.DeleteAddress;
import com.maq.ecom.model.Address;
import com.maq.ecom.views.activities.AddAddressActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * developed by irfan A.
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {

    Context context;
    List<Address> arrayList;
    List<Address> arrayListFull;
    DeleteAddress listener;
    private int mCheckedPostion = -1;

    public AddressListAdapter(Context context, List<Address> arrayList, DeleteAddress listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListFull = new ArrayList<>(arrayList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Address model = arrayList.get(position);

        //bind data
        holder.tv_name.setText(model.getAddress());
        holder.tv_city.setText(model.getCity() + ", " + model.getState() + ", " + model.getPin());
        holder.tv_gst.setText("GST " + model.getGstNo());
        holder.tv_phone.setText("Phone No: " + model.getAltMobile());


        if (mCheckedPostion == -1) {
            if (model.getIsDefault().equals("1"))
            holder.radio.setChecked(true);
        } else
            holder.radio.setChecked(position == mCheckedPostion);

        holder.btn_edit.setOnClickListener(v ->
                context.startActivity(new Intent(context, AddAddressActivity.class)
                        .putExtra("address", arrayList.get(position))
                        .putExtra("address_edit", true)
                )
        );

        holder.btn_del.setOnClickListener(v ->
                listener.onDeleteAddress(arrayList.get(position), position)
        );

        holder.btn_select.setOnClickListener(v -> {
//                selectedBankId = arrayList.get(position).getBankId();
                    mCheckedPostion = position;
                    notifyDataSetChanged();
                }
        );
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.addrItem_tv_address)
        TextView tv_name;
        @BindView(R.id.addrItem_tv_city)
        TextView tv_city;
        @BindView(R.id.addrItem_tv_gst)
        TextView tv_gst;
        @BindView(R.id.addrItem_tv_phone)
        TextView tv_phone;

        @BindView(R.id.addrItem_btn_edit)
        Button btn_edit;
        @BindView(R.id.addrItem_btn_del)
        Button btn_del;
        @BindView(R.id.addrItem_btn_select)
        Button btn_select;

        @BindView(R.id.addrItem_radio)
        MaterialRadioButton radio;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
