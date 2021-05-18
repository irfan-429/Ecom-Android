package com.maq.ecom.interfaces;

import com.maq.ecom.model.Address;
import com.maq.ecom.model.CategoryItem;

public interface CartListener {
    void onCartDelete(CategoryItem model, int position);
}



