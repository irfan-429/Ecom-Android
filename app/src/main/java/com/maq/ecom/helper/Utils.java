package com.maq.ecom.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.maq.ecom.R;
import com.maq.ecom.views.activities.LoginActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.BuildConfig;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by irfan A. on 17/07/2020.
 */

public class Utils {
    public static String BASE_URL = "http://blue.maqsolution.com/";
    public static String IMAGE_COLLECTION = "http://blue.maqsolution.com/uploads/";

    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_NO_CONTENT = 204;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_SERVER_ERROR = 500;

    static boolean showPwdFlag = true;

    public static String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z-]+\\.+[a-z]+";
    public static final int SPLASH_TIME_OUT = 2000;
    public static final String COUNTRY_RESTRICTION = "pk";
    public static final float ZOOM_MAP_BY = 16f;

    public static int POLYLINE_CLR = Color.BLACK, POLYLINE_WIDTH = 10;
    public static int PADDING_MARKERS_FIT_TO_MAP = 200; // Space (in px) between bounding box edges and view edges (applied to all four sides of the bounding box)

    public static final String[] APP_PERMISSIONS = {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};
    public static final int PERMISSION_RC = 10;


    /**
     * show toast
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * show snackBar
     *
     * @param context
     * @param msg
     */
    public static void showSnackBar(Context context, String msg) {
        View view = ((Activity) context).findViewById(android.R.id.content);
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * navigate to desire activity
     *
     * @param context
     * @param destination
     */
    public static void navigateTo(Context context, Class<?> destination) {
        context.startActivity(new Intent(context, destination));
    }

    /**
     * navigate to desire activity with NEW_TASK
     *
     * @param context
     * @param destination
     */
    public static void navigateClearTo(Context context, Class<?> destination) {
        context.startActivity(new Intent(context, destination)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    /**
     * show/hide password on tap
     *
     * @param editTextID
     * @param iv_placeHolder
     */
    public static void showHidePwd(EditText editTextID, AppCompatImageView iv_placeHolder) {
        if (showPwdFlag) {
            editTextID.setTransformationMethod(null); //readable
            iv_placeHolder.setImageResource(R.drawable.ic_eye);
            showPwdFlag = false;
        } else {
            editTextID.setTransformationMethod(new PasswordTransformationMethod()); //dots
            iv_placeHolder.setImageResource(R.drawable.ic_eye_hide);
            showPwdFlag = true;
        }
    }


    /**
     * hide keypad
     *
     * @param activity
     */
    public static void hideKeypad(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) view = new View(activity);

        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * return multipart form data to post an image
     *
     * @param key
     * @param file
     */
    public static MultipartBody.Part getMultipartBody(String key, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(key, file.getName(), requestFile); // {key, name ,val}

        return body;
    }

    /**
     * populate image
     *
     * @param context
     * @param imgURL
     * @param placeHolder
     */
    @SuppressLint("CheckResult")
    public static void loadImage(Context context, String imgURL, AppCompatImageView placeHolder) {
        CircularProgressDrawable progressDrawable = new CircularProgressDrawable(context);
        progressDrawable.setStrokeWidth(4f);
        progressDrawable.setCenterRadius(20f);
        progressDrawable.start();

        if (imgURL == null) return;

        Glide.with(context)
                .load(IMAGE_COLLECTION + imgURL)
                .placeholder(progressDrawable)
                .error(R.drawable.no_image_available)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                .override(100, 200)
//                .centerCrop()
//                .fitCenter() // scale to fit entire image within ImageView
                .into(placeHolder);
    }


    /**
     * populate profile image
     *
     * @param context
     * @param imgURL
     * @param placeHolder
     */
    @SuppressLint("CheckResult")
    public static void loadProfileImage(Context context, String imgURL, CircleImageView placeHolder) {
        if (imgURL == null) return;

        Glide.with(context)
                .load(IMAGE_COLLECTION + imgURL)
                .placeholder(R.drawable.gif_loading)
                .error(R.drawable.no_image_available)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .override(100, 200)
//                .centerCrop()
//                .fitCenter() // scale to fit entire image within ImageView
                .into(placeHolder);
    }

    /**
     * get system time stamp
     *
     * @return
     */
    public static long getSysTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * format time stamp
     *
     * @param timestamp
     * @param outputFormat
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDateTimeFromTS(long timestamp, String outputFormat) {
        Date time = new Date(timestamp);
        DateFormat outPut = new SimpleDateFormat(outputFormat);
        //Hear Define your returning date formate
        return outPut.format(time);
    }

    /**
     * format input date to desire output format
     *
     * @param inputDate
     * @param inputDateFormat
     * @param outputFormat
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDateTimeFromString(String inputDate, String inputDateFormat, String outputFormat) {
        SimpleDateFormat format = new SimpleDateFormat(inputDateFormat);
        Date newDate = null;
        try {
            newDate = format.parse(inputDate);

        } catch (
                ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat(outputFormat);

        return format.format(newDate);
    }

    /**
     * do capital every 1st letter of the word
     *
     * @param text
     * @return
     */
    public static String getCapsSentence(String text) {
        String[] splits = text.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.length; i++) {
            String eachWord = splits[i];
            if (i > 0 && eachWord.length() > 0) {
                sb.append(" ");
            }
            String cap = eachWord.substring(0, 1).toUpperCase() + eachWord.substring(1);
            sb.append(cap);
        }
        return sb.toString();
    }

    /**
     * get address by LatLng
     *
     * @param context
     * @param latitude
     * @param longitude
     * @return address
     */
    public static String getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> address;
        String locationAddress = null;
        try {
            geocoder = new Geocoder(context, Locale.getDefault());
            address = geocoder.getFromLocation(latitude, longitude, 1);
            if (address != null && address.size() > 0) { // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String myAddress = address.get(0).getAddressLine(0); // If any additional getAddress line present than only, check with max available getAddress lines by getMaxAddressLineIndex()
                String city = address.get(0).getLocality();
                String state = address.get(0).getAdminArea();
                String country = address.get(0).getCountryName();
                String postalCode = address.get(0).getPostalCode();

                locationAddress = myAddress;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationAddress;
    }


    /**
     * set view to full screen
     *
     * @param activity
     */
    public static void setToFullScreen(Activity activity) { //set to full screen before showing to user
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * change status bar color
     *
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(Activity activity, int color) {
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(color));
    }

    /**
     * change system nav bar color
     *
     * @param activity
     * @param color
     */
    public static void setSysNavigationBarColor(Activity activity, int color) {
        activity.getWindow().setNavigationBarColor(activity.getResources().getColor(color));
    }

    /**
     * share app
     *
     * @param context
     */
    public static void shareApp(Context context, String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        String shareMessage = text + "\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        context.startActivity(Intent.createChooser(shareIntent, "Choose One"));
    }

    /**
     * make phone call on local number
     *
     * @param context
     * @param number
     */
    public static void makePhoneCall(Context context, String number) {
        if (number == null) return;
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        context.startActivity(callIntent);
    }

    /**
     * whatsApp intent
     *
     * @param context
     * @param number
     */
    public static void makeWhatsApp(Context context, String number) {
        Activity activity = (Activity) context;
//        String number = "+923044819330"; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    /**
     * compose email to 'n' addresses
     *
     * @param context
     * @param addresses
     */
    public static void composeEmail(Context context, String[] addresses) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
//        intent.putExtra(Intent.EXTRA_TEXT, body + "\n\n\nFrom: " + fullName + "\n\nPh: " + mobile);

        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
        else
            Toast.makeText(context, "There are no email app installed.", Toast.LENGTH_SHORT).show();
    }

    /**
     * underline TextView
     */
    public static void underlineTextView(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     * create notification
     *
     * @param context
     */
    public static void createNotification(Context context) {
        Uri alertTune = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0); //add action on tap on createNotification

        String CHANNEL_ID = "1234";
        String CHANNEL_NAME = "channel_01";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH); //create createNotification cahnnel for Oreo and above
            channel.setLightColor(Color.GRAY);
            channel.enableLights(true);
            channel.setDescription("");
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(alertTune, audioAttributes);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        //create notification
        NotificationCompat.Builder status = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOnlyAlertOnce(true)
                .setContentTitle(context.getString(R.string.app_name))
//                .setContentText(context.getString(R.string.str_noti_desc))
                .setVibrate(new long[]{0, 500, 1000})
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSound(alertTune)
//                .setStyle(new NotificationCompat.InboxStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        int NOTIFICATION_ID = 1; // Causes to update the same notification over and over again.
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, status.build());
        }
    }

    /**
     * return multipart form data to post an image
     *
     * @param key
     * @param file
     */
    public static MultipartBody.Part ImageToMultipartBody(String key, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(key, file.getName(), requestFile); // {key, name ,val}

        return body;
    }

    public static RequestBody ImageToRequestBody(File file) { //for image file to request body
        return RequestBody.create(MediaType.parse("multipart/form-data"), file);
    }

    public static String addDays(int days) {
        String dateInString = formatDateTimeFromTS(System.currentTimeMillis(), "yyyy-MM-dd");  // current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, days);
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date resultDate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultDate);

        return dateInString;
    }

    public static String addDays(int days, String outputFormat) {
        String dateInString = formatDateTimeFromTS(System.currentTimeMillis(), "yyyy-MM-dd HH:mm");  // current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, days);
        sdf = new SimpleDateFormat(outputFormat, Locale.ENGLISH);
        Date resultDate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultDate);

        return dateInString;
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isTodaysDate(String selectedDate) {
        if (selectedDate == null) return false;

//        String endDate = "21-10-2019";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

        Date currentSysDate = Calendar.getInstance().getTime(); //get current sys date
        String currentDate = dateFormatter.format(currentSysDate); //first format and get String val
//        String selectedFormattedDate = dateFormatter.format(selectedDate); //first format and get String val

        boolean dateFlag = false;


        try {
            // If selected date is after the current date.
            if (dateFormatter.parse(selectedDate).before(dateFormatter.parse(currentDate)))
                dateFlag = false;  // If selected date is before current date.
            else dateFlag = dateFormatter.parse(selectedDate).equals(dateFormatter.parse(currentDate));  // If two dates are equal.

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateFlag;
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isDateAfter(String fromDate, String receivingDate) {
        if (receivingDate == null) return false;

        fromDate = Utils.formatDateTimeFromString(fromDate, "yyyy-MM-dd", "yyyy/MM/dd");
        receivingDate = Utils.formatDateTimeFromString(receivingDate, "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd");

//        String endDate = "21-10-2019";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

//        Date currentSysDate = Calendar.getInstance().getTime(); //get current sys date
//        String currentDate = dateFormatter.format(currentSysDate); //first format and get String val
//        String selectedFormattedDate = dateFormatter.format(selectedDate); //first format and get String val

        boolean dateFlag = false;


        try {
            if (dateFormatter.parse(receivingDate).after(dateFormatter.parse(fromDate)) ||
                    dateFormatter.parse(receivingDate).equals(dateFormatter.parse(fromDate)))
                dateFlag = true;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateFlag;
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isDateBefore(String toDate, String receivingDate) {
        if (receivingDate == null) return false;

        toDate = Utils.formatDateTimeFromString(toDate, "yyyy-MM-dd", "yyyy/MM/dd");
        receivingDate = Utils.formatDateTimeFromString(receivingDate, "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd");


//        String endDate = "21-10-2019";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

//        Date currentSysDate = Calendar.getInstance().getTime(); //get current sys date
//        String currentDate = dateFormatter.format(currentSysDate); //first format and get String val
//        String selectedFormattedDate = dateFormatter.format(selectedDate); //first format and get String val

        boolean dateFlag = false;


        try {
            if (dateFormatter.parse(receivingDate).before(dateFormatter.parse(toDate)) ||
                    dateFormatter.parse(receivingDate).equals(dateFormatter.parse(toDate)))
                dateFlag = true;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateFlag;
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isDateBeforeFromCurrent(String fromDate, String checkingDate) {
        if (checkingDate == null) return false;

        fromDate = Utils.formatDateTimeFromString(fromDate, "yyyy-MM-dd", "yyyy/MM/dd");
        checkingDate = Utils.formatDateTimeFromString(checkingDate, "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd");


//        String endDate = "21-10-2019";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

//        Date currentSysDate = Calendar.getInstance().getTime(); //get current sys date
//        String currentDate = dateFormatter.format(currentSysDate); //first format and get String val
//        String selectedFormattedDate = dateFormatter.format(selectedDate); //first format and get String val

        boolean dateFlag = false;


        try {
            if (dateFormatter.parse(checkingDate).before(dateFormatter.parse(fromDate)))
                dateFlag = true;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateFlag;
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//Compression quality, here 100 means no compression, the storage of compressed data to baos
        int options = 90;
        while (baos.toByteArray().length / 1024 > 400) {  //Loop if compressed picture is greater than 400kb, than to compression
            baos.reset();//Reset baos is empty baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//The compression options%, storing the compressed data to the baos
            options -= 10;//Every time reduced by 10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//The storage of compressed data in the baos to ByteArrayInputStream
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//The ByteArrayInputStream data generation
        return bitmap;
    }


    public static File compressImage(File file) {
        //it reduces the image size to nearly 200 KB
        try {
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Checks for granted permission but by a single string (single permission)
     *
     * @param permission
     * @return boolean
     */
    public static boolean checkPermissionsGranted(Context context, String permission) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Check for permissions by a given list
     *
     * @param permissions
     * @return boolean
     */
    public static boolean checkPermissionsGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (!checkPermissionsGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check for permissions by a given list
     *
     * @param grantResults
     * @return boolean
     */
    public static boolean checkPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void askForPermission(FragmentActivity activity, String permission) {
        askForPermissions(activity, new String[]{permission});
    }

    /**
     * Asks for permissions by a given list
     *
     * @param activity
     * @param permissions
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void askForPermissions(FragmentActivity activity, String[] permissions) {
        activity.requestPermissions(permissions, PERMISSION_RC);
    }

    public static String RoundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return twoDForm.format(d);
    }

    public static float extractFloatPart(String value) {
        return Float.parseFloat(value.replaceAll(".*?([\\d.]+).*", "$1"));

    }

}
