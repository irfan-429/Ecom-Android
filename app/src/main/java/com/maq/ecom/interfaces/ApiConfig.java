package com.maq.ecom.interfaces;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * This interface contains all API End Points used in Retrofit.
 */

public interface ApiConfig {

    @POST("user.php?action=login")
    Call<JsonObject> API_requestLogin(@Query("Mobile") String mobile,
                                      @Query("Password") String password,
                                      @Query("isAdmin") String isAdmin);

    @POST("user.php?action=forgetpwd")
    Call<JsonObject> API_forgotPassword(@Query("Mobile") String mobile);

    @POST("user.php?action=changepwd")
    Call<JsonObject> API_changePassword(@Query("Mobile") String mobile,
                                        @Query("Password") String password);

    @Multipart
    @POST("uploadprofileimage.php")
    Call<JsonObject> API_uploadProfileImage(@Part MultipartBody.Part profileImg);

    @Multipart
    @POST("upload_image.php")
    Call<JsonObject> API_uploadImg(@Part MultipartBody.Part img);

    @POST("user.php?action=changeimg")
    Call<JsonObject> API_changeProfileImg(@Query("Mobile") String mobile,
                                          @Query("Image") String imgName);


    @POST("user.php?action=usercategorylist")
    Call<JsonObject> API_getCategoryList(@Query("FirmId") String firmId);

    @POST("user.php?action=newcategory")
    Call<JsonObject> API_addNewCategory(@Query("FirmId") String firmId,
                                        @Query("CategoryName") String categoryName,
                                        @Query("CategoryBanner") String categoryBanner,
                                        @Query("CategoryImage") String categoryImage,
                                        @Query("Status") String status);


    @POST("user.php?action=editcategory")
    Call<JsonObject> API_editCategory(@Query("CategoryId") String categoryId,
                                      @Query("FirmId") String firmId,
                                      @Query("CategoryName") String categoryName,
                                      @Query("CategoryBanner") String categoryBanner,
                                      @Query("CategoryImage") String categoryImage,
                                      @Query("Status") String status);


    @POST("user.php?action=bannerlist")
    Call<JsonObject> API_getBannerList(@Query("FirmId") String firmId);

    @POST("user.php?action=userbannerlist")
    Call<JsonObject> API_getUserBannerList(@Query("FirmId") String firmId);


    @POST("user.php?action=newbanner")
    Call<JsonObject> API_addNewBanner(@Query("FirmId") String firmId,
                                      @Query("BannerName") String BannerName,
                                      @Query("BannerLink") String BannerLink,
                                      @Query("BannerImage") String BannerImage,
                                      @Query("Status") String status);


    @POST("user.php?action=editbanner")
    Call<JsonObject> API_editBanner(@Query("BannerId") String BannerId,
                                    @Query("FirmId") String firmId,
                                    @Query("BannerName") String BannerName,
                                    @Query("BannerLink") String BannerLink,
                                    @Query("BannerImage") String BannerImage,
                                    @Query("Status") String status);


    @POST("user.php?action=newproduct")
    Call<JsonObject> API_addNewProduct(@Query("FirmId") String FirmId,
                                       @Query("ProductCode") String ProductCode,
                                       @Query("ProductName") String ProductName,
                                       @Query("Price") String Price,
                                       @Query("Discount") String Discount,
                                       @Query("SellingPrice") String SellingPrice,
                                       @Query("CategoryId") String CategoryId,
                                       @Query("ShortDesc") String ShortDesc,
                                       @Query("Description") String Description,
                                       @Query("Status") String Status,
                                       @Query("isFeatured") String isFeatured,
                                       @Query("isNew") String isNew,
                                       @Query("isPopular") String isPopular,
                                       @Query("ProductCover") String ProductCover,
                                       @Query("Image1") String Image1,
                                       @Query("Image2") String Image2,
                                       @Query("Image3") String Image3,
                                       @Query("Image4") String Image4,
                                       @Query("Image5") String Image5,
                                       @Query("Image6") String Image6,
                                       @Query("KeyFeatures") String KeyFeatures,
                                       @Query("isSize") String isSize,
                                       @Query("Stock") String Stock
    );

    @POST("user.php?action=editproduct")
    Call<JsonObject> API_addEditProduct(@Query("FirmId") String FirmId,
                                        @Query("ProductId") String ProductId,
                                        @Query("ProductCode") String ProductCode,
                                        @Query("ProductName") String ProductName,
                                        @Query("Price") String Price,
                                        @Query("Discount") String Discount,
                                        @Query("SellingPrice") String SellingPrice,
                                        @Query("CategoryId") String CategoryId,
                                        @Query("ShortDesc") String ShortDesc,
                                        @Query("Description") String Description,
                                        @Query("Status") String Status,
                                        @Query("isFeatured") String isFeatured,
                                        @Query("isNew") String isNew,
                                        @Query("isPopular") String isPopular,
                                        @Query("ProductCover") String ProductCover,
                                        @Query("Image1") String Image1,
                                        @Query("Image2") String Image2,
                                        @Query("Image3") String Image3,
                                        @Query("Image4") String Image4,
                                        @Query("Image5") String Image5,
                                        @Query("Image6") String Image6,
                                        @Query("KeyFeatures") String KeyFeatures,
                                        @Query("isSize") String isSize,
                                        @Query("Stock") String Stock
    );


    @POST("user.php?action=productlist")
    Call<JsonObject> API_getProductList(@Query("FirmId") String FirmId);


    @POST("user.php?action=updatestock")
    Call<JsonObject> API_updateStock(@Query("FirmId") String FirmId,
                                     @Query("Qty") String Qty,
                                     @Query("ProductId") String ProductId);

    @POST("user.php?action=allitems")
    Call<JsonObject> API_getCategoryItems(@Query("FirmId") String FirmId,
                                          @Query("Category") String Category);


    @POST("user.php?action=allitemtab")
    Call<JsonObject> API_getTabItems(@Query("FirmId") String firmId);

    @POST("user.php?action=newuser")
    Call<JsonObject> API_registerNewUser(@Query("FirmId") String FirmId,
                                         @Query("UserName") String UserName,
                                         @Query("EmailId") String EmailId,
                                         @Query("MobileNo") String MobileNo,
                                         @Query("Password") String Password
    );

    @POST("user.php?action=newaddress")
    Call<JsonObject> API_addNewAddress(@Query("UserId") String UserId,
                                       @Query("Address") String Address,
                                       @Query("City") String City,
                                       @Query("State") String State,
                                       @Query("PIN") String PIN,
                                       @Query("AddressType") String AddressType,
                                       @Query("AltMobile") String AltMobile,
                                       @Query("Latitude") String Latitude,
                                       @Query("Longtitude") String Longtitude,
                                       @Query("isDefault") String isDefault,
                                       @Query("GSTNo") String GSTNo,
                                       @Query("AadharNo") String AadharNo
    );

    @POST("user.php?action=editaddress")
    Call<JsonObject> API_editAddress(@Query("UserId") String UserId,
                                     @Query("Address") String Address,
                                     @Query("City") String City,
                                     @Query("State") String State,
                                     @Query("PIN") String PIN,
                                     @Query("AddressType") String AddressType,
                                     @Query("AltMobile") String AltMobile,
                                     @Query("Latitude") String Latitude,
                                     @Query("Longtitude") String Longtitude,
                                     @Query("isDefault") String isDefault,
                                     @Query("GSTNo") String GSTNo,
                                     @Query("AadharNo") String AadharNo,
                                     @Query("AddressId") String AddressId
    );

    @POST("user.php?action=addresslist")
    Call<JsonObject> API_getAddressList(@Query("UserId") String UserId
    );

    @POST("user.php?action=removeaddress")
    Call<JsonObject> API_delAddress(@Query("AddressId") String AddressId
    );


    @POST("user.php?action=msglist")
    Call<JsonObject> API_fetchMarque(@Query("FirmId") String firmId);

    @POST("user.php?action=updatepayment")
    Call<JsonObject> API_updateTransaction(@Query("RecvNo") String firmId,
                                           @Query("CustomerCode") String customerCode,
                                           @Query("Status") String status
    );

    @POST("user.php?action=mystatement")
    Call<JsonObject> API_myStatements(@Query("FirmId") String firmId,
                                      @Query("CustomerCode") String customerCode,
                                      @Query("FromDate") String fromDate,
                                      @Query("ToDate") String toDate
    );


    @POST("user.php?action=mycratestatement")
    Call<JsonObject> API_myCrateStatements(@Query("FirmId") String firmId,
                                           @Query("CustomerCode") String customerCode,
                                           @Query("FromDate") String fromDate,
                                           @Query("ToDate") String toDate);

    @POST("user.php?action=allcratereport")
    Call<JsonObject> API_allCrateReport(@Query("FirmId") String firmId,
                                        @Query("CustomerCode") String customerCode,
                                        @Query("FromDate") String fromDate,
                                        @Query("ToDate") String toDate,
                                        @Query("Status") String status
    );


    @POST("user.php?action=allcustomers")
    Call<JsonObject> API_customers(@Query("FirmId") String firmId);

    @POST("user.php?action=allroute")
    Call<JsonObject> API_allRoute(@Query("FirmId") String firmId);

    @POST("user.php?action=orderexist")
    Call<JsonObject> API_checkOrderExists(@Query("CustomerCode") String customerCode,
                                          @Query("FirmId") String firmId,
                                          @Query("OrderDate") String fromDate
    );

    @POST("user.php?action=receiptdetail")
    Call<JsonObject> API_myReceiptItems(@Query("RecvId") String RecvId,
                                        @Query("CustomerCode") String CustomerCode);

    @POST("user.php?action=crtreceiptdetail")
    Call<JsonObject> API_myReceiptCrtItems(@Query("RecvId") String RecvId,
                                           @Query("CustomerCode") String CustomerCode);

    @POST("user.php?action=ledger")
    Call<JsonObject> API_ledger(@Query("FirmId") String FirmId,
                                @Query("CustomerCode") String CustomerCode);

    @POST("user.php?action=dashboard")
    Call<JsonObject> API_summary(@Query("FirmId") String FirmId,
                                 @Query("CustomerCode") String CustomerCode,
                                 @Query("Date") String Date,
                                 @Query("Route") String Route);


}
