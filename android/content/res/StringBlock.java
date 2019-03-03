package android.content.res;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.text.Annotation;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineHeightSpan.WithDensity;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.SparseArray;

final class StringBlock
{
  private static final String TAG = "AssetManager";
  private static final boolean localLOGV = false;
  private final long mNative;
  private final boolean mOwnsNative;
  private SparseArray<CharSequence> mSparseStrings;
  private CharSequence[] mStrings;
  StyleIDs mStyleIDs = null;
  private final boolean mUseSparse;
  
  StringBlock(long paramLong, boolean paramBoolean)
  {
    mNative = paramLong;
    mUseSparse = paramBoolean;
    mOwnsNative = false;
  }
  
  public StringBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    mNative = nativeCreate(paramArrayOfByte, paramInt1, paramInt2);
    mUseSparse = paramBoolean;
    mOwnsNative = true;
  }
  
  public StringBlock(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    mNative = nativeCreate(paramArrayOfByte, 0, paramArrayOfByte.length);
    mUseSparse = paramBoolean;
    mOwnsNative = true;
  }
  
  private static void addParagraphSpan(Spannable paramSpannable, Object paramObject, int paramInt1, int paramInt2)
  {
    int i = paramSpannable.length();
    int j = paramInt1;
    if (paramInt1 != 0)
    {
      j = paramInt1;
      if (paramInt1 != i)
      {
        j = paramInt1;
        if (paramSpannable.charAt(paramInt1 - 1) != '\n')
        {
          j = paramInt1;
          do
          {
            paramInt1 = j - 1;
            j = paramInt1;
            if (paramInt1 <= 0) {
              break;
            }
            j = paramInt1;
          } while (paramSpannable.charAt(paramInt1 - 1) != '\n');
          j = paramInt1;
        }
      }
    }
    int k = paramInt2;
    if (paramInt2 != 0)
    {
      k = paramInt2;
      if (paramInt2 != i)
      {
        k = paramInt2;
        if (paramSpannable.charAt(paramInt2 - 1) != '\n')
        {
          do
          {
            paramInt1 = paramInt2 + 1;
            k = paramInt1;
            if (paramInt1 >= i) {
              break;
            }
            paramInt2 = paramInt1;
          } while (paramSpannable.charAt(paramInt1 - 1) != '\n');
          k = paramInt1;
        }
      }
    }
    paramSpannable.setSpan(paramObject, j, k, 51);
  }
  
  private CharSequence applyStyles(String paramString, int[] paramArrayOfInt, StyleIDs paramStyleIDs)
  {
    if (paramArrayOfInt.length == 0) {
      return paramString;
    }
    paramString = new SpannableString(paramString);
    for (int i = 0; i < paramArrayOfInt.length; i += 3)
    {
      int j = paramArrayOfInt[i];
      if (j == boldId) {
        paramString.setSpan(new StyleSpan(1), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
      }
      for (;;)
      {
        break;
        if (j == italicId)
        {
          paramString.setSpan(new StyleSpan(2), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
        }
        else if (j == underlineId)
        {
          paramString.setSpan(new UnderlineSpan(), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
        }
        else if (j == ttId)
        {
          paramString.setSpan(new TypefaceSpan("monospace"), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
        }
        else if (j == bigId)
        {
          paramString.setSpan(new RelativeSizeSpan(1.25F), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
        }
        else if (j == smallId)
        {
          paramString.setSpan(new RelativeSizeSpan(0.8F), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
        }
        else if (j == subId)
        {
          paramString.setSpan(new SubscriptSpan(), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
        }
        else if (j == supId)
        {
          paramString.setSpan(new SuperscriptSpan(), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
        }
        else if (j == strikeId)
        {
          paramString.setSpan(new StrikethroughSpan(), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
        }
        else if (j == listItemId)
        {
          addParagraphSpan(paramString, new BulletSpan(10), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1);
        }
        else if (j == marqueeId)
        {
          paramString.setSpan(TextUtils.TruncateAt.MARQUEE, paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 18);
        }
        else
        {
          String str1 = nativeGetString(mNative, j);
          if (str1.startsWith("font;"))
          {
            String str2 = subtag(str1, ";height=");
            if (str2 != null) {
              addParagraphSpan(paramString, new Height(Integer.parseInt(str2)), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1);
            }
            str2 = subtag(str1, ";size=");
            if (str2 != null) {
              paramString.setSpan(new AbsoluteSizeSpan(Integer.parseInt(str2), true), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
            }
            str2 = subtag(str1, ";fgcolor=");
            if (str2 != null) {
              paramString.setSpan(getColor(str2, true), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
            }
            str2 = subtag(str1, ";color=");
            if (str2 != null) {
              paramString.setSpan(getColor(str2, true), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
            }
            str2 = subtag(str1, ";bgcolor=");
            if (str2 != null) {
              paramString.setSpan(getColor(str2, false), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
            }
            str1 = subtag(str1, ";face=");
            if (str1 != null) {
              paramString.setSpan(new TypefaceSpan(str1), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
            }
          }
          else if (str1.startsWith("a;"))
          {
            str1 = subtag(str1, ";href=");
            if (str1 != null) {
              paramString.setSpan(new URLSpan(str1), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
            }
          }
          else if (str1.startsWith("annotation;"))
          {
            int k = str1.length();
            for (int m = str1.indexOf(';'); m < k; m = j)
            {
              int n = str1.indexOf('=', m);
              if (n < 0) {
                break;
              }
              int i1 = str1.indexOf(';', n);
              j = i1;
              if (i1 < 0) {
                j = k;
              }
              paramString.setSpan(new Annotation(str1.substring(m + 1, n), str1.substring(n + 1, j)), paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)] + 1, 33);
            }
          }
        }
      }
    }
    return new SpannedString(paramString);
  }
  
  private static CharacterStyle getColor(String paramString, boolean paramBoolean)
  {
    int i = -16777216;
    int j = i;
    if (!TextUtils.isEmpty(paramString)) {
      if (paramString.startsWith("@"))
      {
        Resources localResources = Resources.getSystem();
        int k = localResources.getIdentifier(paramString.substring(1), "color", "android");
        j = i;
        if (k != 0)
        {
          paramString = localResources.getColorStateList(k, null);
          if (paramBoolean) {
            return new TextAppearanceSpan(null, 0, 0, paramString, null);
          }
          j = paramString.getDefaultColor();
        }
      }
      else
      {
        try
        {
          j = Color.parseColor(paramString);
        }
        catch (IllegalArgumentException paramString)
        {
          j = -16777216;
        }
      }
    }
    if (paramBoolean) {
      return new ForegroundColorSpan(j);
    }
    return new BackgroundColorSpan(j);
  }
  
  private static native long nativeCreate(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  private static native void nativeDestroy(long paramLong);
  
  private static native int nativeGetSize(long paramLong);
  
  private static native String nativeGetString(long paramLong, int paramInt);
  
  private static native int[] nativeGetStyle(long paramLong, int paramInt);
  
  private static String subtag(String paramString1, String paramString2)
  {
    int i = paramString1.indexOf(paramString2);
    if (i < 0) {
      return null;
    }
    i += paramString2.length();
    int j = paramString1.indexOf(';', i);
    if (j < 0) {
      return paramString1.substring(i);
    }
    return paramString1.substring(i, j);
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      super.finalize();
      return;
    }
    finally
    {
      if (mOwnsNative) {
        nativeDestroy(mNative);
      }
    }
  }
  
  public CharSequence get(int paramInt)
  {
    try
    {
      int i;
      if (mStrings != null)
      {
        localObject1 = mStrings[paramInt];
        if (localObject1 != null) {
          return localObject1;
        }
      }
      else if (mSparseStrings != null)
      {
        localObject1 = (CharSequence)mSparseStrings.get(paramInt);
        if (localObject1 != null) {
          return localObject1;
        }
      }
      else
      {
        i = nativeGetSize(mNative);
        if ((mUseSparse) && (i > 250))
        {
          localObject1 = new android/util/SparseArray;
          ((SparseArray)localObject1).<init>();
          mSparseStrings = ((SparseArray)localObject1);
        }
        else
        {
          mStrings = new CharSequence[i];
        }
      }
      String str = nativeGetString(mNative, paramInt);
      Object localObject1 = str;
      int[] arrayOfInt = nativeGetStyle(mNative, paramInt);
      if (arrayOfInt != null)
      {
        if (mStyleIDs == null)
        {
          localObject1 = new android/content/res/StringBlock$StyleIDs;
          ((StyleIDs)localObject1).<init>();
          mStyleIDs = ((StyleIDs)localObject1);
        }
        for (i = 0; i < arrayOfInt.length; i += 3)
        {
          int j = arrayOfInt[i];
          if ((j != mStyleIDs.boldId) && (j != mStyleIDs.italicId) && (j != mStyleIDs.underlineId) && (j != mStyleIDs.ttId) && (j != mStyleIDs.bigId) && (j != mStyleIDs.smallId) && (j != mStyleIDs.subId) && (j != mStyleIDs.supId) && (j != mStyleIDs.strikeId) && (j != mStyleIDs.listItemId) && (j != mStyleIDs.marqueeId))
          {
            localObject1 = nativeGetString(mNative, j);
            if (((String)localObject1).equals("b")) {
              StyleIDs.access$002(mStyleIDs, j);
            } else if (((String)localObject1).equals("i")) {
              StyleIDs.access$102(mStyleIDs, j);
            } else if (((String)localObject1).equals("u")) {
              StyleIDs.access$202(mStyleIDs, j);
            } else if (((String)localObject1).equals("tt")) {
              StyleIDs.access$302(mStyleIDs, j);
            } else if (((String)localObject1).equals("big")) {
              StyleIDs.access$402(mStyleIDs, j);
            } else if (((String)localObject1).equals("small")) {
              StyleIDs.access$502(mStyleIDs, j);
            } else if (((String)localObject1).equals("sup")) {
              StyleIDs.access$702(mStyleIDs, j);
            } else if (((String)localObject1).equals("sub")) {
              StyleIDs.access$602(mStyleIDs, j);
            } else if (((String)localObject1).equals("strike")) {
              StyleIDs.access$802(mStyleIDs, j);
            } else if (((String)localObject1).equals("li")) {
              StyleIDs.access$902(mStyleIDs, j);
            } else if (((String)localObject1).equals("marquee")) {
              StyleIDs.access$1002(mStyleIDs, j);
            }
          }
        }
        localObject1 = applyStyles(str, arrayOfInt, mStyleIDs);
      }
      if (mStrings != null) {
        mStrings[paramInt] = localObject1;
      } else {
        mSparseStrings.put(paramInt, localObject1);
      }
      return localObject1;
    }
    finally {}
  }
  
  private static class Height
    implements LineHeightSpan.WithDensity
  {
    private static float sProportion = 0.0F;
    private int mSize;
    
    public Height(int paramInt)
    {
      mSize = paramInt;
    }
    
    public void chooseHeight(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Paint.FontMetricsInt paramFontMetricsInt)
    {
      chooseHeight(paramCharSequence, paramInt1, paramInt2, paramInt3, paramInt4, paramFontMetricsInt, null);
    }
    
    public void chooseHeight(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Paint.FontMetricsInt paramFontMetricsInt, TextPaint paramTextPaint)
    {
      paramInt2 = mSize;
      paramInt1 = paramInt2;
      if (paramTextPaint != null) {
        paramInt1 = (int)(paramInt2 * density);
      }
      if (bottom - top < paramInt1)
      {
        top = (bottom - paramInt1);
        ascent -= paramInt1;
      }
      else
      {
        if (sProportion == 0.0F)
        {
          paramCharSequence = new Paint();
          paramCharSequence.setTextSize(100.0F);
          paramTextPaint = new Rect();
          paramCharSequence.getTextBounds("ABCDEFG", 0, 7, paramTextPaint);
          sProportion = top / paramCharSequence.ascent();
        }
        paramInt2 = (int)Math.ceil(-top * sProportion);
        if (paramInt1 - descent >= paramInt2)
        {
          top = (bottom - paramInt1);
          ascent = (descent - paramInt1);
        }
        else if (paramInt1 >= paramInt2)
        {
          paramInt2 = -paramInt2;
          ascent = paramInt2;
          top = paramInt2;
          paramInt1 = top + paramInt1;
          descent = paramInt1;
          bottom = paramInt1;
        }
        else
        {
          paramInt1 = -paramInt1;
          ascent = paramInt1;
          top = paramInt1;
          descent = 0;
          bottom = 0;
        }
      }
    }
  }
  
  static final class StyleIDs
  {
    private int bigId = -1;
    private int boldId = -1;
    private int italicId = -1;
    private int listItemId = -1;
    private int marqueeId = -1;
    private int smallId = -1;
    private int strikeId = -1;
    private int subId = -1;
    private int supId = -1;
    private int ttId = -1;
    private int underlineId = -1;
    
    StyleIDs() {}
  }
}
