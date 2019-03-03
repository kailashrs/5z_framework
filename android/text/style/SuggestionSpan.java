package android.text.style;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.R.styleable;
import java.util.Arrays;
import java.util.Locale;

public class SuggestionSpan
  extends CharacterStyle
  implements ParcelableSpan
{
  public static final String ACTION_SUGGESTION_PICKED = "android.text.style.SUGGESTION_PICKED";
  public static final Parcelable.Creator<SuggestionSpan> CREATOR = new Parcelable.Creator()
  {
    public SuggestionSpan createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SuggestionSpan(paramAnonymousParcel);
    }
    
    public SuggestionSpan[] newArray(int paramAnonymousInt)
    {
      return new SuggestionSpan[paramAnonymousInt];
    }
  };
  public static final int FLAG_AUTO_CORRECTION = 4;
  public static final int FLAG_EASY_CORRECT = 1;
  public static final int FLAG_MISSPELLED = 2;
  public static final int SUGGESTIONS_MAX_SIZE = 5;
  public static final String SUGGESTION_SPAN_PICKED_AFTER = "after";
  public static final String SUGGESTION_SPAN_PICKED_BEFORE = "before";
  public static final String SUGGESTION_SPAN_PICKED_HASHCODE = "hashcode";
  private static final String TAG = "SuggestionSpan";
  private int mAutoCorrectionUnderlineColor;
  private float mAutoCorrectionUnderlineThickness;
  private int mEasyCorrectUnderlineColor;
  private float mEasyCorrectUnderlineThickness;
  private int mFlags;
  private final int mHashCode;
  private final String mLanguageTag;
  private final String mLocaleStringForCompatibility;
  private int mMisspelledUnderlineColor;
  private float mMisspelledUnderlineThickness;
  private final String mNotificationTargetClassName;
  private final String mNotificationTargetPackageName;
  private final String[] mSuggestions;
  
  public SuggestionSpan(Context paramContext, Locale paramLocale, String[] paramArrayOfString, int paramInt, Class<?> paramClass)
  {
    mSuggestions = ((String[])Arrays.copyOf(paramArrayOfString, Math.min(5, paramArrayOfString.length)));
    mFlags = paramInt;
    if (paramLocale == null)
    {
      while (paramContext != null) {
        paramLocale = getResourcesgetConfigurationlocale;
      }
      Log.e("SuggestionSpan", "No locale or context specified in SuggestionSpan constructor");
      paramLocale = null;
    }
    if (paramLocale == null) {
      paramArrayOfString = "";
    } else {
      paramArrayOfString = paramLocale.toString();
    }
    mLocaleStringForCompatibility = paramArrayOfString;
    if (paramLocale == null) {
      paramLocale = "";
    } else {
      paramLocale = paramLocale.toLanguageTag();
    }
    mLanguageTag = paramLocale;
    if (paramContext != null) {
      mNotificationTargetPackageName = paramContext.getPackageName();
    } else {
      mNotificationTargetPackageName = null;
    }
    if (paramClass != null) {
      mNotificationTargetClassName = paramClass.getCanonicalName();
    } else {
      mNotificationTargetClassName = "";
    }
    mHashCode = hashCodeInternal(mSuggestions, mLanguageTag, mLocaleStringForCompatibility, mNotificationTargetClassName);
    initStyle(paramContext);
  }
  
  public SuggestionSpan(Context paramContext, String[] paramArrayOfString, int paramInt)
  {
    this(paramContext, null, paramArrayOfString, paramInt, null);
  }
  
  public SuggestionSpan(Parcel paramParcel)
  {
    mSuggestions = paramParcel.readStringArray();
    mFlags = paramParcel.readInt();
    mLocaleStringForCompatibility = paramParcel.readString();
    mLanguageTag = paramParcel.readString();
    mNotificationTargetClassName = paramParcel.readString();
    mNotificationTargetPackageName = paramParcel.readString();
    mHashCode = paramParcel.readInt();
    mEasyCorrectUnderlineColor = paramParcel.readInt();
    mEasyCorrectUnderlineThickness = paramParcel.readFloat();
    mMisspelledUnderlineColor = paramParcel.readInt();
    mMisspelledUnderlineThickness = paramParcel.readFloat();
    mAutoCorrectionUnderlineColor = paramParcel.readInt();
    mAutoCorrectionUnderlineThickness = paramParcel.readFloat();
  }
  
  public SuggestionSpan(Locale paramLocale, String[] paramArrayOfString, int paramInt)
  {
    this(null, paramLocale, paramArrayOfString, paramInt, null);
  }
  
  private static int hashCodeInternal(String[] paramArrayOfString, String paramString1, String paramString2, String paramString3)
  {
    return Arrays.hashCode(new Object[] { Long.valueOf(SystemClock.uptimeMillis()), paramArrayOfString, paramString1, paramString2, paramString3 });
  }
  
  private void initStyle(Context paramContext)
  {
    if (paramContext == null)
    {
      mMisspelledUnderlineThickness = 0.0F;
      mEasyCorrectUnderlineThickness = 0.0F;
      mAutoCorrectionUnderlineThickness = 0.0F;
      mMisspelledUnderlineColor = -16777216;
      mEasyCorrectUnderlineColor = -16777216;
      mAutoCorrectionUnderlineColor = -16777216;
      return;
    }
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(null, R.styleable.SuggestionSpan, 17891544, 0);
    mMisspelledUnderlineThickness = localTypedArray.getDimension(1, 0.0F);
    mMisspelledUnderlineColor = localTypedArray.getColor(0, -16777216);
    localTypedArray = paramContext.obtainStyledAttributes(null, R.styleable.SuggestionSpan, 17891543, 0);
    mEasyCorrectUnderlineThickness = localTypedArray.getDimension(1, 0.0F);
    mEasyCorrectUnderlineColor = localTypedArray.getColor(0, -16777216);
    paramContext = paramContext.obtainStyledAttributes(null, R.styleable.SuggestionSpan, 17891542, 0);
    mAutoCorrectionUnderlineThickness = paramContext.getDimension(1, 0.0F);
    mAutoCorrectionUnderlineColor = paramContext.getColor(0, -16777216);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof SuggestionSpan;
    boolean bool2 = false;
    if (bool1)
    {
      if (((SuggestionSpan)paramObject).hashCode() == mHashCode) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  @Deprecated
  public String getLocale()
  {
    return mLocaleStringForCompatibility;
  }
  
  public Locale getLocaleObject()
  {
    Locale localLocale;
    if (mLanguageTag.isEmpty()) {
      localLocale = null;
    } else {
      localLocale = Locale.forLanguageTag(mLanguageTag);
    }
    return localLocale;
  }
  
  public String getNotificationTargetClassName()
  {
    return mNotificationTargetClassName;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 19;
  }
  
  public String[] getSuggestions()
  {
    return mSuggestions;
  }
  
  public int getUnderlineColor()
  {
    int i = mFlags;
    int j = 1;
    if ((i & 0x2) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    int k;
    if ((mFlags & 0x1) != 0) {
      k = 1;
    } else {
      k = 0;
    }
    if ((mFlags & 0x4) == 0) {
      j = 0;
    }
    if (k != 0)
    {
      if (i == 0) {
        return mEasyCorrectUnderlineColor;
      }
      return mMisspelledUnderlineColor;
    }
    if (j != 0) {
      return mAutoCorrectionUnderlineColor;
    }
    return 0;
  }
  
  public int hashCode()
  {
    return mHashCode;
  }
  
  public void notifySelection(Context paramContext, String paramString, int paramInt)
  {
    Intent localIntent = new Intent();
    if ((paramContext != null) && (mNotificationTargetClassName != null))
    {
      if ((mSuggestions != null) && (paramInt >= 0) && (paramInt < mSuggestions.length))
      {
        if (mNotificationTargetPackageName != null)
        {
          localIntent.setClassName(mNotificationTargetPackageName, mNotificationTargetClassName);
          localIntent.setAction("android.text.style.SUGGESTION_PICKED");
          localIntent.putExtra("before", paramString);
          localIntent.putExtra("after", mSuggestions[paramInt]);
          localIntent.putExtra("hashcode", hashCode());
          paramContext.sendBroadcast(localIntent);
        }
        else
        {
          paramContext = InputMethodManager.peekInstance();
          if (paramContext != null) {
            paramContext.notifySuggestionPicked(this, paramString, paramInt);
          }
        }
        return;
      }
      paramContext = new StringBuilder();
      paramContext.append("Unable to notify the suggestion as the index is out of range index=");
      paramContext.append(paramInt);
      paramContext.append(" length=");
      paramContext.append(mSuggestions.length);
      Log.w("SuggestionSpan", paramContext.toString());
      return;
    }
  }
  
  public void setFlags(int paramInt)
  {
    mFlags = paramInt;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    int i = mFlags;
    int j = 0;
    if ((i & 0x2) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    int k;
    if ((mFlags & 0x1) != 0) {
      k = 1;
    } else {
      k = 0;
    }
    if ((mFlags & 0x4) != 0) {
      j = 1;
    }
    if (k != 0)
    {
      if (i == 0) {
        paramTextPaint.setUnderlineText(mEasyCorrectUnderlineColor, mEasyCorrectUnderlineThickness);
      } else if (underlineColor == 0) {
        paramTextPaint.setUnderlineText(mMisspelledUnderlineColor, mMisspelledUnderlineThickness);
      }
    }
    else if (j != 0) {
      paramTextPaint.setUnderlineText(mAutoCorrectionUnderlineColor, mAutoCorrectionUnderlineThickness);
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStringArray(mSuggestions);
    paramParcel.writeInt(mFlags);
    paramParcel.writeString(mLocaleStringForCompatibility);
    paramParcel.writeString(mLanguageTag);
    paramParcel.writeString(mNotificationTargetClassName);
    paramParcel.writeString(mNotificationTargetPackageName);
    paramParcel.writeInt(mHashCode);
    paramParcel.writeInt(mEasyCorrectUnderlineColor);
    paramParcel.writeFloat(mEasyCorrectUnderlineThickness);
    paramParcel.writeInt(mMisspelledUnderlineColor);
    paramParcel.writeFloat(mMisspelledUnderlineThickness);
    paramParcel.writeInt(mAutoCorrectionUnderlineColor);
    paramParcel.writeFloat(mAutoCorrectionUnderlineThickness);
  }
}
