package android.view.textclassifier;

import android.content.res.AssetFileDescriptor;

final class TextClassifierImplNative
{
  private final long mModelPtr;
  
  static
  {
    System.loadLibrary("textclassifier");
  }
  
  TextClassifierImplNative(int paramInt)
  {
    mModelPtr = nativeNew(paramInt);
    if (mModelPtr != 0L) {
      return;
    }
    throw new IllegalArgumentException("Couldn't initialize TC from file descriptor.");
  }
  
  TextClassifierImplNative(AssetFileDescriptor paramAssetFileDescriptor)
  {
    mModelPtr = nativeNewFromAssetFileDescriptor(paramAssetFileDescriptor, paramAssetFileDescriptor.getStartOffset(), paramAssetFileDescriptor.getLength());
    if (mModelPtr != 0L) {
      return;
    }
    throw new IllegalArgumentException("Couldn't initialize TC from given AssetFileDescriptor");
  }
  
  TextClassifierImplNative(String paramString)
  {
    mModelPtr = nativeNewFromPath(paramString);
    if (mModelPtr != 0L) {
      return;
    }
    throw new IllegalArgumentException("Couldn't initialize TC from given file.");
  }
  
  public static String getLocales(int paramInt)
  {
    return nativeGetLocales(paramInt);
  }
  
  public static int getVersion(int paramInt)
  {
    return nativeGetVersion(paramInt);
  }
  
  private static native AnnotatedSpan[] nativeAnnotate(long paramLong, String paramString, AnnotationOptions paramAnnotationOptions);
  
  private static native ClassificationResult[] nativeClassifyText(long paramLong, String paramString, int paramInt1, int paramInt2, ClassificationOptions paramClassificationOptions);
  
  private static native void nativeClose(long paramLong);
  
  private static native String nativeGetLocales(int paramInt);
  
  private static native int nativeGetVersion(int paramInt);
  
  private static native long nativeNew(int paramInt);
  
  private static native long nativeNewFromAssetFileDescriptor(AssetFileDescriptor paramAssetFileDescriptor, long paramLong1, long paramLong2);
  
  private static native long nativeNewFromPath(String paramString);
  
  private static native int[] nativeSuggestSelection(long paramLong, String paramString, int paramInt1, int paramInt2, SelectionOptions paramSelectionOptions);
  
  public AnnotatedSpan[] annotate(String paramString, AnnotationOptions paramAnnotationOptions)
  {
    return nativeAnnotate(mModelPtr, paramString, paramAnnotationOptions);
  }
  
  public ClassificationResult[] classifyText(String paramString, int paramInt1, int paramInt2, ClassificationOptions paramClassificationOptions)
  {
    return nativeClassifyText(mModelPtr, paramString, paramInt1, paramInt2, paramClassificationOptions);
  }
  
  public void close()
  {
    nativeClose(mModelPtr);
  }
  
  public int[] suggestSelection(String paramString, int paramInt1, int paramInt2, SelectionOptions paramSelectionOptions)
  {
    return nativeSuggestSelection(mModelPtr, paramString, paramInt1, paramInt2, paramSelectionOptions);
  }
  
  public static final class AnnotatedSpan
  {
    private final TextClassifierImplNative.ClassificationResult[] mClassification;
    private final int mEndIndex;
    private final int mStartIndex;
    
    AnnotatedSpan(int paramInt1, int paramInt2, TextClassifierImplNative.ClassificationResult[] paramArrayOfClassificationResult)
    {
      mStartIndex = paramInt1;
      mEndIndex = paramInt2;
      mClassification = paramArrayOfClassificationResult;
    }
    
    public TextClassifierImplNative.ClassificationResult[] getClassification()
    {
      return mClassification;
    }
    
    public int getEndIndex()
    {
      return mEndIndex;
    }
    
    public int getStartIndex()
    {
      return mStartIndex;
    }
  }
  
  public static final class AnnotationOptions
  {
    private final String mLocales;
    private final long mReferenceTimeMsUtc;
    private final String mReferenceTimezone;
    
    AnnotationOptions(long paramLong, String paramString1, String paramString2)
    {
      mReferenceTimeMsUtc = paramLong;
      mReferenceTimezone = paramString1;
      mLocales = paramString2;
    }
    
    public String getLocale()
    {
      return mLocales;
    }
    
    public long getReferenceTimeMsUtc()
    {
      return mReferenceTimeMsUtc;
    }
    
    public String getReferenceTimezone()
    {
      return mReferenceTimezone;
    }
  }
  
  public static final class ClassificationOptions
  {
    private final String mLocales;
    private final long mReferenceTimeMsUtc;
    private final String mReferenceTimezone;
    
    ClassificationOptions(long paramLong, String paramString1, String paramString2)
    {
      mReferenceTimeMsUtc = paramLong;
      mReferenceTimezone = paramString1;
      mLocales = paramString2;
    }
    
    public String getLocale()
    {
      return mLocales;
    }
    
    public long getReferenceTimeMsUtc()
    {
      return mReferenceTimeMsUtc;
    }
    
    public String getReferenceTimezone()
    {
      return mReferenceTimezone;
    }
  }
  
  public static final class ClassificationResult
  {
    private final String mCollection;
    private final TextClassifierImplNative.DatetimeResult mDatetimeResult;
    private final float mScore;
    
    ClassificationResult(String paramString, float paramFloat, TextClassifierImplNative.DatetimeResult paramDatetimeResult)
    {
      mCollection = paramString;
      mScore = paramFloat;
      mDatetimeResult = paramDatetimeResult;
    }
    
    public String getCollection()
    {
      if ((mCollection.equals("date")) && (mDatetimeResult != null))
      {
        switch (mDatetimeResult.getGranularity())
        {
        default: 
          return "date";
        }
        return "datetime";
      }
      return mCollection;
    }
    
    public TextClassifierImplNative.DatetimeResult getDatetimeResult()
    {
      return mDatetimeResult;
    }
    
    public float getScore()
    {
      return mScore;
    }
  }
  
  public static final class DatetimeResult
  {
    static final int GRANULARITY_DAY = 3;
    static final int GRANULARITY_HOUR = 4;
    static final int GRANULARITY_MINUTE = 5;
    static final int GRANULARITY_MONTH = 1;
    static final int GRANULARITY_SECOND = 6;
    static final int GRANULARITY_WEEK = 2;
    static final int GRANULARITY_YEAR = 0;
    private final int mGranularity;
    private final long mTimeMsUtc;
    
    DatetimeResult(long paramLong, int paramInt)
    {
      mGranularity = paramInt;
      mTimeMsUtc = paramLong;
    }
    
    public int getGranularity()
    {
      return mGranularity;
    }
    
    public long getTimeMsUtc()
    {
      return mTimeMsUtc;
    }
  }
  
  public static final class SelectionOptions
  {
    private final String mLocales;
    
    SelectionOptions(String paramString)
    {
      mLocales = paramString;
    }
    
    public String getLocales()
    {
      return mLocales;
    }
  }
}
