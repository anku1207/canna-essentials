package com.shopify.canna.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.text.method.DigitsKeyListener;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    static int errorColor;
    static Context contextact;
    static String erroract;




    public static void  exceptionAlertDialog(Context context, String title , String msg , String buttonname, String error){

        contextact=context;
        erroract=error;

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonname, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                sendLogFile(contextact,erroract);

            }
        });
        if(!((Activity)context).isFinishing() && !alertDialog.isShowing())  alertDialog.show();


    }
    private static void sendLogFile(Context context , String error) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@anduriltechnologies.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Error reported from MyAPP");
        intent.putExtra(Intent.EXTRA_TEXT, "Log file attached."+error); // do this so some email clients don't complain about empty body.
        context.startActivity(intent);
    }

    public static void dismissDialog(Context context , Dialog dialog){
       try {
           if (!((Activity)context).isFinishing() && dialog!=null &&  dialog.isShowing()) {
               dialog.dismiss();
           }
       }catch (Exception e){
          // ExceptionsNotification.ExceptionHandling(context ,  Utility.getStackTrace(e), "0");
       }

    }
    public static Integer getVersioncode(Context context){
        Integer version =null;
        try {
            final PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                version = (int) pInfo.getLongVersionCode(); // avoid huge version numbers and you will be ok
            } else {
                //noinspection deprecation
                version = pInfo.versionCode;
            }
        } catch (Exception e) {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return version;
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
    public static Drawable GetImage(Context c, String ImageName) {
        Drawable drawable;
        try {
            String[] imagename=ImageName.split("\\.");
            int id=c.getResources().getIdentifier(imagename[0],"drawable",c.getPackageName());
            drawable= ContextCompat.getDrawable(c,id);
        }catch (Exception e){
            drawable=null;
        }
        return drawable;
    }

}

