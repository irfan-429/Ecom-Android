package com.maq.ecom.interfaces;

import com.maq.ecom.model.CategoryItem;

public interface OrderStatusListener {
    void onStatusUpdate(String status, int position);
}



