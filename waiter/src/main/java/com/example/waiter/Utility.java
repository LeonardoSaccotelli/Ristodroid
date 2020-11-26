package com.example.waiter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.util.Base64;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class Utility {
    public static Bitmap byteToBitmap(byte[] blob) {
        blob = Base64.decode(blob, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(blob, 0, blob.length);
    }

    public static <T> String listToStringWithDelimiter (List<T> list, String delimiter) {
        return list.stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }

    public static String priceToString(double price) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(price);
    }

    public static String convertResourceIdToString (int resourceId, Context context){
        return context.getString(resourceId);
    }

    public static SpannableString createIndentedText(String text, int marginFirstLine, int marginNextLines) {
        SpannableString result=new SpannableString(text);
        result.setSpan(new LeadingMarginSpan.Standard(marginFirstLine, marginNextLines),0,text.length(),0);
        return result;
    }
}
