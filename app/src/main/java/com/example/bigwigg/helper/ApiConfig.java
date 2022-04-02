package com.example.bigwigg.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bigwigg.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@SuppressWarnings("deprecation")
public class ApiConfig extends Application {

    public static final String TAG = ApiConfig.class.getSimpleName();
    static ApiConfig mInstance;
    static boolean isDialogOpen = false;
    RequestQueue mRequestQueue;

    public static String VolleyErrorMessage(VolleyError error) {
        String message = "";
        try {
            if (error instanceof NetworkError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (error instanceof ServerError) {
                message = "The server could not be found. Please try again after some time!";
            } else if (error instanceof AuthFailureError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (error instanceof ParseError) {
                message = "Parsing error! Please try again after some time!";
            } else if (error instanceof TimeoutError) {
                message = "Connection TimeOut! Please check your internet connection.";
            } else
                message = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }



    @SuppressLint("DefaultLocale")
    public static String StringFormat(String number) {
        return String.format("%.2f", Double.parseDouble(number));
    }

    public static ArrayList<String> getDates(String startDate, String endDate) {
        ArrayList<String> dates = new ArrayList<>();
        @SuppressLint("SimpleDateFormat")
        DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(startDate);
            date2 = df1.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        assert date1 != null;
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        assert date2 != null;
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.get(Calendar.DATE) + "-" + (cal1.get(Calendar.MONTH) + 1) + "-" + cal1.get(Calendar.YEAR) + "-" + cal1.get(Calendar.DAY_OF_WEEK));
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }



    public static void RequestToVolley(final VolleyCallback callback, final Activity activity, final String url, final Map<String, String> params, final boolean isProgress) {
        if (ProgressDisplay.mProgressBar != null) {
            ProgressDisplay.mProgressBar.setVisibility(View.GONE);
        }
        final ProgressDisplay progressDisplay = new ProgressDisplay(activity);
        progressDisplay.hideProgress();

            if (isProgress)
                progressDisplay.showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                if (ApiConfig.isConnected(activity))
                    callback.onSuccess(true, response);
                if (isProgress)
                    progressDisplay.hideProgress();
            }, error -> {
                if (isProgress)
                    progressDisplay.hideProgress();
                if (ApiConfig.isConnected(activity))
                        callback.onSuccess(false, "");
                String message = VolleyErrorMessage(error);
                if (!message.equals(""))
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params1 = new HashMap<>();
                    params1.put(Constant.AUTHORIZATION, "Bearer " + createJWT("eKart", "eKart Authentication"));
                    return params1;
                }

                @Override
                protected Map<String, String> getParams() {
                    params.put(Constant.AccessKey, Constant.AccessKeyVal);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
            ApiConfig.getInstance().getRequestQueue().getCache().clear();
            ApiConfig.getInstance().addToRequestQueue(stringRequest);
    }

    public static void RequestToVolley(final VolleyCallback callback, final Activity activity, final String url, final Map<String, String> params, final Map<String, String> fileParams) {
        if(isConnected(activity)) {
            VolleyMultiPartRequest multipartRequest = new VolleyMultiPartRequest(url,
                    response -> callback.onSuccess(true, response),
                    error -> callback.onSuccess(false, "")) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params1 = new HashMap<>();
                    params1.put(Constant.AUTHORIZATION, "Bearer " + createJWT("eKart", "eKart Authentication"));
                    return params1;
                }

                @Override
                public Map<String, String> getDefaultParams() {
                    params.put(Constant.AccessKey, Constant.AccessKeyVal);
                    return params;
                }

                @Override
                public Map<String, String> getFileParams() {
                    return fileParams;
                }
            };

            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            getInstance().getRequestQueue().getCache().clear();
            getInstance().addToRequestQueue(multipartRequest);
        }
    }

    public static String toTitleCase(String str) {
        if (str == null) {
            return null;
        }
        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    public static String createJWT(String issuer, String subject) {
        try {
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            byte[] apiKeySecretBytes = Constant.JWT_KEY.getBytes();
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
            JwtBuilder builder = Jwts.builder()
                    .setIssuedAt(now)
                    .setSubject(subject)
                    .setIssuer(issuer)
                    .signWith(signatureAlgorithm, signingKey);

            return builder.compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean CheckValidation(String item, boolean isMailValidation, boolean isMobileValidation) {
        boolean result = false;
        if (item.length() == 0) {
            result = true;
        } else if (isMailValidation) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(item).matches()) {
                result = true;
            }
        } else if (isMobileValidation) {
            if (!android.util.Patterns.PHONE.matcher(item).matches()) {
                result = true;
            }
        }
        return result;
    }

    @SuppressLint("DefaultLocale")
    public static String GetDiscount(double OriginalPrice, double DiscountedPrice) {
        return String.format("%.0f", Double.parseDouble("" + (((((OriginalPrice - DiscountedPrice) + OriginalPrice) / OriginalPrice) - 1) * 100))) + "%";
    }

    public static synchronized ApiConfig getInstance() {
        return mInstance;
    }

    public static Boolean isConnected(final Activity activity) {
        boolean check = false;
        try {
            ConnectivityManager ConnectionManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                check = true;
            } else {
                try {
                    if (!isDialogOpen) {
                        @SuppressLint("InflateParams") View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_no_internet, null);
                        ViewGroup parentViewGroup = (ViewGroup) sheetView.getParent();
                        if (parentViewGroup != null) {
                            parentViewGroup.removeAllViews();
                        }

                        final Dialog mBottomSheetDialog = new Dialog(activity);
                        mBottomSheetDialog.setContentView(sheetView);
                        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        mBottomSheetDialog.show();
                        isDialogOpen = true;
                        Button btnRetry = sheetView.findViewById(R.id.btnRetry);
                        mBottomSheetDialog.setCancelable(false);

                        btnRetry.setOnClickListener(view -> {
                            if (isConnected(activity)) {
                                isDialogOpen = false;
                                mBottomSheetDialog.dismiss();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FirebaseApp.initializeApp(this);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}