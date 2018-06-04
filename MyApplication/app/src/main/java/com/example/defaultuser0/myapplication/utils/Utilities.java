package com.example.defaultuser0.myapplication.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Region;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.defaultuser0.myapplication.R;
import com.example.defaultuser0.myapplication.webservice.Services;
import com.github.mrengineer13.snackbar.SnackBar;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    Context mContext;

    public Utilities(Context mContext) {
        this.mContext = mContext;
    }

    public void setServices(Services mServices) {
        this.mServices = mServices;
    }

    Services mServices;

    public static void writeLog(String mMessage) {
        Log.i("MyApplication Log ", mMessage);
    }


    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


    public String convertDate(String date) {
        // to convert date from YYYY-mm-DD to DD-mm-YYYY
        String[] dateArray = date.split("-");
        String convertedDate;
        convertedDate = dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0];

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = df.parse(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy");

        String newDate = df1.format(d);
        return newDate;
    }

    public String convertNewDate(String date) {
        // to convert date from YYYY-mm-DD to DD-mm-YYYY
//        String[] dateArray = date.split(" ");
//        String convertedDate;
//        convertedDate = dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0];

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df1 = new SimpleDateFormat("dd-mm-yyyy");

        String newDate = df1.format(d);
        return newDate;
    }

    public String convertTo24Hour(String mTimeSlot) {

        SimpleDateFormat readFormat = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat writeFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = readFormat.parse(mTimeSlot);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            String formattedDate = writeFormat.format(date);
            return formattedDate;
        }

        return mTimeSlot;
    }

    public String convertTo12Hour(String mTimeSlot) {

        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat parseFormat;
            if (mTimeSlot.split(Pattern.quote(":")).length == 3) {
                parseFormat = new SimpleDateFormat("HH:mm:ss");
            } else {
                parseFormat = new SimpleDateFormat("HH:mm");
            }
            Date date = parseFormat.parse(mTimeSlot);
            return (displayFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return mTimeSlot;
    }

    public String getCurrentTime() {
        Calendar mCal = Calendar.getInstance();
        int mMinute = mCal.get(Calendar.MINUTE);
        int mHour = mCal.get(Calendar.HOUR_OF_DAY);
        return mHour + ":" + mMinute;
    }

    public String getCurrentDate() {
        Calendar mCal = Calendar.getInstance();
        int day = mCal.get(Calendar.DATE);
        int month = mCal.get(Calendar.MONTH) + 1;
        int year = mCal.get(Calendar.YEAR);
        return day + "-" + month + "-" + year;
    }

    public String getFormattedDate(String date) {
        Date mDateObject = null;
        try {
            mDateObject = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String mFormattedDate = new SimpleDateFormat("dd MMM yyyy").format(mDateObject);
        return mFormattedDate;
    }

    public String getNewFormattedDate(String date) {
        Date mDateObject = null;
        try {
            mDateObject = new SimpleDateFormat("dd-mm-yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String mFormattedDate = new SimpleDateFormat("yyyy-mm-dd").format(mDateObject);
        return mFormattedDate;
    }



    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);


                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public void showSnackBar(Activity mActivity, String mMessage, SnackBarTypes mSnackBarTypes) {
        try {
            SnackBar.Builder mBuilder =
                    new SnackBar.Builder(mActivity)
                            .withMessage(mMessage)
                            .withDuration(SnackBar.LONG_SNACK);
            switch (mSnackBarTypes) {
                case TYPE_ERROR:
                    mBuilder.withActionIconId(R.drawable.ic_snack_cancel);
                    mBuilder.withActionMessage("");
                    break;
                case TYPE_INFORMATION:
                    mBuilder.withActionIconId(R.drawable.ic_snack_info);
                    mBuilder.withActionMessage("");
                    break;
                case TYPE_SUCCESS:
                    mBuilder.withActionIconId(R.drawable.ic_snack_success);
                    mBuilder.withActionMessage("");
                    break;
            }
            mBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSnackBar(Context mContext, View mRootView, String mMessage, SnackBarTypes mSnackBarTypes) {
        try {
            SnackBar.Builder mBuilder =
                    new SnackBar.Builder(mContext, mRootView)
                            .withMessage(mMessage)
                            .withDuration(SnackBar.LONG_SNACK);
            switch (mSnackBarTypes) {
                case TYPE_ERROR:
                    mBuilder.withActionIconId(R.drawable.ic_snack_cancel);
                    mBuilder.withActionMessage("");
                    break;
                case TYPE_INFORMATION:
                    mBuilder.withActionIconId(R.drawable.ic_snack_info);
                    mBuilder.withActionMessage("");
                    break;
                case TYPE_SUCCESS:
                    mBuilder.withActionIconId(R.drawable.ic_snack_success);
                    mBuilder.withActionMessage("");
                    break;
            }
            mBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logOutUser() {
        try {
            ObscuredSharedPreferences prefs = new ObscuredSharedPreferences(mContext, mContext.getSharedPreferences("My_Application", Context.MODE_PRIVATE));
            prefs.edit().clear().commit();
//            Intent mIntent = new Intent(mContext, LoginActivity.class);
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(mIntent);
//            ((BaseActivity) mContext).finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSearchValid(String s) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        return !m.find();
    }

    private String encodeBase64(String mData) {
        try {
            byte[] data = mData.getBytes("ASCII");
            return Base64.encodeToString(data, Base64.DEFAULT).replaceAll(Pattern.quote("="), "").
                    replaceAll(Pattern.quote("+"), "-").replaceAll(Pattern.quote("/"), "_");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String dayOf(String date) {

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;

        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat df1 = new SimpleDateFormat("dd");
        String stringDate = df1.format(d);
        String newDate = stringDate.substring(stringDate.length() - 1);
        if ((Integer.parseInt(stringDate) >= 11) && (Integer.parseInt(stringDate) <= 20)) {

            return stringDate + "<sup><small>th</small></sup>";

        } else {
            if (newDate.equals("1")) {

                return stringDate + "<sup><small>st</small></sup>";

            } else if (newDate.equals("2")) {

                return stringDate + "<sup><small>nd</small></sup>";

            } else if (newDate.equals("3")) {

                return stringDate + "<sup><small>rd</small></sup>";

            } else {

                return stringDate + "<sup><small>th</small></sup>";

            }
        }


    }

    public boolean checkDate(String time) {
        //get your today date as string
        String today = (String) android.text.format.DateFormat.format(
                "yyyy-MM-dd", new Date());
        //Convert the two time string to date formate
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(time);
            date2 = sdf.parse(today);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //do the comparison
        if (date1.after(date2)) {
            //do something
            return true;
        } else if (date1.before(date2)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pastTime(String time) {
        //get your today date as string
        String today = (String) android.text.format.DateFormat.format(
                "dd-MM-yyyy", new Date());
        //Convert the two time string to date formate
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(time);
            date2 = sdf.parse(today);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //do the comparison
        if (date1.before(date2)) {
            //do something
            return true;
        } else {
            return false;
        }
    }

    public String getMonth(String date) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df1 = new SimpleDateFormat("MMM");

        String newMonth = df1.format(d);
        return newMonth;
    }

    public String getYear(String date) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");

        String newYear = df1.format(d);
        return newYear;
    }

    public String getWeek(String week) {

        if (week.equals("1")) {
            return "1st Week";
        } else if (week.equals("2")) {
            return "2nd Week";
        } else if (week.equals("3")) {
            return "3rd Week";
        } else {
            return week + "th Week";
        }

    }


    public enum SnackBarTypes {
        TYPE_ERROR,
        TYPE_INFORMATION,
        TYPE_SUCCESS
    }

    private String encodeBase64(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT).replaceAll(Pattern.quote("="), "").
                replaceAll(Pattern.quote("+"), "-").replaceAll(Pattern.quote("/"), "_");
    }

    public boolean hasSpecialCharacter(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isLetterOrDigit(value.charAt(i)))
                return true;
        }
        return false;
    }

    public String getCertificateSHA1Fingerprint() {
        PackageManager pm = mContext.getPackageManager();
        String packageName = mContext.getPackageName();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return (hexString);
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }


    public boolean isEmailValid(CharSequence input) {
        boolean isValid = false;

//        String expression="(^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$)(;|,|; | ;| ; | , | ,){1}+\"^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
//        String expression= "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        String expression = "(([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4}))" +
                "(((;|,|; |, | ;| ; | , | ,){1}"
                + "([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4}))*)";


        CharSequence inputStr = input;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String upperCaseAllFirst(String value) {
        if (!value.equals(null)) {
            value = value.toLowerCase();
            char[] array = value.toCharArray();
            // Uppercase first letter.
            array[0] = Character.toUpperCase(array[0]);
            // Uppercase all letters that follow a whitespace character.
            for (int i = 1; i < array.length; i++) {
                if (Character.isWhitespace(array[i - 1])) {
                    array[i] = Character.toUpperCase(array[i]);
                }
            }
            // Result.
            return new String(array);
        } else {
            return null;
        }
    }


    public boolean isValidPhoneNumber(CharSequence mPhoneNumber) {

        if (mPhoneNumber.length() == 10 && Pattern.matches("^[789]\\d{9}$", mPhoneNumber)) {
            return true;
        } else if (mPhoneNumber.length() > 9 && mPhoneNumber.length() <= 11 && mPhoneNumber.charAt(0) == '0' &&
                Pattern.matches("^([0|\\+[0-9]{1,5}])?((0|7|8|9)[0-9]{9,})$", mPhoneNumber)) {
            return true;
        } else {
            if (mPhoneNumber.length() > 9 && mPhoneNumber.length() < 11 && mPhoneNumber.charAt(0) == '0' &&
                    Pattern.matches("^([0|+[0-9]{1,5}])?((0|7|8|9)[0-9]{9,})$", mPhoneNumber)) {
                return true;
            }
        }
        return false;

    }

    public static String changeTimeFormat(String myTime) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = df.parse(myTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String newTime = df.format(cal.getTime());
        return newTime;
    }

    public static boolean notnull(String data) {
        if (data != null) {
            if (data.isEmpty() || data.trim().isEmpty() || data.equals("null") || data.equals("")) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public String createImageFromBitmap(Bitmap bitmap, String mImageName) {

        String fileName = Environment.getExternalStorageDirectory() + File.separator + mImageName;//no .png or .jpg needed

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            File mOutFile = new File(fileName);
            FileOutputStream fo = new FileOutputStream(mOutFile);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public String compressImage(File mFile) {
        String filePath = mFile.getAbsolutePath();
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = actualWidth / actualHeight;


        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {

            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                scaledBitmap.recycle();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return filename;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "QKClinic/Gallery");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


}