package a.team.works.u22.hal.u22teama;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.widget.ImageView;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtil {

    public static Bitmap createBitmapFromUri(Context context, Uri uri, int ori) {
        ContentResolver contentResolver = context.getContentResolver();
        InputStream inputStream = null;
        BitmapFactory.Options imageOptions;
        Bitmap imageBitmap = null;

        // メモリ上に画像を読み込まず、画像サイズ情報のみを取得する
        try {
            inputStream = contentResolver.openInputStream(uri);
            imageOptions = new BitmapFactory.Options();
            imageOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, imageOptions);
            assert inputStream != null;
            inputStream.close();
            // もし読み込む画像が大きかったら縮小して読み込む
            inputStream = contentResolver.openInputStream(uri);
            if (imageOptions.outWidth > 1000 && imageOptions.outHeight > 1000) {
                imageOptions = new BitmapFactory.Options();
                imageOptions.inSampleSize = 2;
                imageBitmap = BitmapFactory.decodeStream(inputStream, null, imageOptions);
            } else {
                imageBitmap = BitmapFactory.decodeStream(inputStream, null, null);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("error", e +"");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("error", e +"");
            e.printStackTrace();

        }
        return imageBitmap;
    }

    public static int getOrientation( Uri uri, Intent data, ContentResolver cr) {
        ExifInterface exifInterface;

        try {
            String[] columns = {MediaStore.Images.Media.DATA };
            Cursor c;
            if(data == null) {
                c = cr.query(uri, columns, null, null, null);
            }else {
                if (data.getData() != null) {
                    c = cr.query(data.getData(), columns, null, null, null);
                }else{
                    c = cr.query(uri, columns, null, null, null);
                }
            }
            c.moveToFirst();
            exifInterface = new ExifInterface(c.getString(0));
        } catch (IOException e) {
            System.out.println(e);
            return 0;
        }

        Log.e("exifR", "値 取得試す");
        int exifR = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Log.e("exifR", "値 : " + exifR);
        int orientation = 0;
        switch (exifR) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                orientation = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                orientation = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                orientation = 270;
                break;
            default:
                orientation = 0;
                break;
        }
        return orientation;
    }


    public static void setImageVew(ImageView imgView, Bitmap bitmap,int orientation, float viewWidth) {
        imgView.setScaleType(ImageView.ScaleType.MATRIX);
        imgView.setImageBitmap(bitmap);

        // 画像の幅、高さを取得
        int wOrg = bitmap.getWidth();
        int hOrg = bitmap.getHeight();
        imgView.getLayoutParams();
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) imgView.getLayoutParams();

        float factor;
        Matrix mat = new Matrix();
        mat.reset();
        switch (orientation) {
            case 1://only scaling
                factor = (float) viewWidth / (float) wOrg;
                mat.preScale(factor, factor);
                lp.width = (int) (wOrg * factor);
                lp.height = (int) (hOrg * factor);
                break;
            case 2://flip vertical
                factor = (float) viewWidth / (float) wOrg;
                mat.postScale(factor, -factor);
                mat.postTranslate(0, hOrg * factor);
                lp.width = (int) (wOrg * factor);
                lp.height = (int) (hOrg * factor);
                break;
            case 3://rotate 180
                mat.postRotate(180, wOrg / 2f, hOrg / 2f);
                factor = (float) viewWidth / (float) wOrg;
                mat.postScale(factor, factor);
                lp.width = (int) (wOrg * factor);
                lp.height = (int) (hOrg * factor);
                break;
            case 4://flip horizontal
                factor = (float) viewWidth / (float) wOrg;
                mat.postScale(-factor, factor);
                mat.postTranslate(wOrg * factor, 0);
                lp.width = (int) (wOrg * factor);
                lp.height = (int) (hOrg * factor);
                break;
            case 5://flip vertical rotate270
                mat.postRotate(270, 0, 0);
                factor = (float) viewWidth / (float) hOrg;
                mat.postScale(factor, -factor);
                lp.width = (int) (hOrg * factor);
                lp.height = (int) (wOrg * factor);
                break;
            case 6://rotate 90
                mat.postRotate(90, 0, 0);
                factor = (float) viewWidth / (float) hOrg;
                mat.postScale(factor, factor);
                mat.postTranslate(hOrg * factor, 0);
                lp.width = (int) (hOrg * factor);
                lp.height = (int) (wOrg * factor);
                break;
            case 7://flip vertical, rotate 90
                mat.postRotate(90, 0, 0);
                factor = (float) viewWidth / (float) hOrg;
                mat.postScale(factor, -factor);
                mat.postTranslate(hOrg * factor, wOrg * factor);
                lp.width = (int) (hOrg * factor);
                lp.height = (int) (wOrg * factor);
                break;
            case 8://rotate 270
                mat.postRotate(270, 0, 0);
                factor = (float) viewWidth / (float) hOrg;
                mat.postScale(factor, factor);
                mat.postTranslate(0, wOrg * factor);
                lp.width = (int) (hOrg * factor);
                lp.height = (int) (wOrg * factor);
                break;
        }
        imgView.setLayoutParams(lp);
        imgView.setImageMatrix(mat);
        imgView.invalidate();
    }

}

