package android.service.autofill;

import android.app.ActivityThread;
import android.app.Application;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class UserData
  implements Parcelable
{
  public static final Parcelable.Creator<UserData> CREATOR = new Parcelable.Creator()
  {
    public UserData createFromParcel(Parcel paramAnonymousParcel)
    {
      String str = paramAnonymousParcel.readString();
      String[] arrayOfString1 = paramAnonymousParcel.readStringArray();
      String[] arrayOfString2 = paramAnonymousParcel.readStringArray();
      paramAnonymousParcel = new UserData.Builder(str, arrayOfString2[0], arrayOfString1[0]).setFieldClassificationAlgorithm(paramAnonymousParcel.readString(), paramAnonymousParcel.readBundle());
      for (int i = 1; i < arrayOfString1.length; i++) {
        paramAnonymousParcel.add(arrayOfString2[i], arrayOfString1[i]);
      }
      return paramAnonymousParcel.build();
    }
    
    public UserData[] newArray(int paramAnonymousInt)
    {
      return new UserData[paramAnonymousInt];
    }
  };
  private static final int DEFAULT_MAX_CATEGORY_COUNT = 10;
  private static final int DEFAULT_MAX_FIELD_CLASSIFICATION_IDS_SIZE = 10;
  private static final int DEFAULT_MAX_USER_DATA_SIZE = 50;
  private static final int DEFAULT_MAX_VALUE_LENGTH = 100;
  private static final int DEFAULT_MIN_VALUE_LENGTH = 3;
  private static final String TAG = "UserData";
  private final String mAlgorithm;
  private final Bundle mAlgorithmArgs;
  private final String[] mCategoryIds;
  private final String mId;
  private final String[] mValues;
  
  private UserData(Builder paramBuilder)
  {
    mId = mId;
    mAlgorithm = mAlgorithm;
    mAlgorithmArgs = mAlgorithmArgs;
    mCategoryIds = new String[mCategoryIds.size()];
    mCategoryIds.toArray(mCategoryIds);
    mValues = new String[mValues.size()];
    mValues.toArray(mValues);
  }
  
  public static void dumpConstraints(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("maxUserDataSize: ");
    paramPrintWriter.println(getMaxUserDataSize());
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("maxFieldClassificationIdsSize: ");
    paramPrintWriter.println(getMaxFieldClassificationIdsSize());
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("maxCategoryCount: ");
    paramPrintWriter.println(getMaxCategoryCount());
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("minValueLength: ");
    paramPrintWriter.println(getMinValueLength());
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("maxValueLength: ");
    paramPrintWriter.println(getMaxValueLength());
  }
  
  private static int getInt(String paramString, int paramInt)
  {
    Object localObject = null;
    ActivityThread localActivityThread = ActivityThread.currentActivityThread();
    if (localActivityThread != null) {
      localObject = localActivityThread.getApplication().getContentResolver();
    }
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Could not read from ");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("; hardcoding ");
      ((StringBuilder)localObject).append(paramInt);
      Log.w("UserData", ((StringBuilder)localObject).toString());
      return paramInt;
    }
    return Settings.Secure.getInt((ContentResolver)localObject, paramString, paramInt);
  }
  
  public static int getMaxCategoryCount()
  {
    return getInt("autofill_user_data_max_category_count", 10);
  }
  
  public static int getMaxFieldClassificationIdsSize()
  {
    return getInt("autofill_user_data_max_field_classification_size", 10);
  }
  
  public static int getMaxUserDataSize()
  {
    return getInt("autofill_user_data_max_user_data_size", 50);
  }
  
  public static int getMaxValueLength()
  {
    return getInt("autofill_user_data_max_value_length", 100);
  }
  
  public static int getMinValueLength()
  {
    return getInt("autofill_user_data_min_value_length", 3);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("id: ");
    paramPrintWriter.print(mId);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Algorithm: ");
    paramPrintWriter.print(mAlgorithm);
    paramPrintWriter.print(" Args: ");
    paramPrintWriter.println(mAlgorithmArgs);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Field ids size: ");
    paramPrintWriter.println(mCategoryIds.length);
    int i = 0;
    for (int j = 0; j < mCategoryIds.length; j++)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print(j);
      paramPrintWriter.print(": ");
      paramPrintWriter.println(Helper.getRedacted(mCategoryIds[j]));
    }
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Values size: ");
    paramPrintWriter.println(mValues.length);
    for (j = i; j < mValues.length; j++)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print(j);
      paramPrintWriter.print(": ");
      paramPrintWriter.println(Helper.getRedacted(mValues[j]));
    }
  }
  
  public Bundle getAlgorithmArgs()
  {
    return mAlgorithmArgs;
  }
  
  public String[] getCategoryIds()
  {
    return mCategoryIds;
  }
  
  public String getFieldClassificationAlgorithm()
  {
    return mAlgorithm;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public String[] getValues()
  {
    return mValues;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder("UserData: [id=");
    localStringBuilder.append(mId);
    localStringBuilder.append(", algorithm=");
    localStringBuilder = localStringBuilder.append(mAlgorithm);
    localStringBuilder.append(", categoryIds=");
    Helper.appendRedacted(localStringBuilder, mCategoryIds);
    localStringBuilder.append(", values=");
    Helper.appendRedacted(localStringBuilder, mValues);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mId);
    paramParcel.writeStringArray(mCategoryIds);
    paramParcel.writeStringArray(mValues);
    paramParcel.writeString(mAlgorithm);
    paramParcel.writeBundle(mAlgorithmArgs);
  }
  
  public static final class Builder
  {
    private String mAlgorithm;
    private Bundle mAlgorithmArgs;
    private final ArrayList<String> mCategoryIds;
    private boolean mDestroyed;
    private final String mId = checkNotEmpty("id", paramString1);
    private final ArraySet<String> mUniqueCategoryIds;
    private final ArrayList<String> mValues;
    
    public Builder(String paramString1, String paramString2, String paramString3)
    {
      checkNotEmpty("categoryId", paramString3);
      checkValidValue(paramString2);
      int i = UserData.getMaxUserDataSize();
      mCategoryIds = new ArrayList(i);
      mValues = new ArrayList(i);
      mUniqueCategoryIds = new ArraySet(UserData.getMaxCategoryCount());
      addMapping(paramString2, paramString3);
    }
    
    private void addMapping(String paramString1, String paramString2)
    {
      mCategoryIds.add(paramString2);
      mValues.add(paramString1);
      mUniqueCategoryIds.add(paramString2);
    }
    
    private String checkNotEmpty(String paramString1, String paramString2)
    {
      Preconditions.checkNotNull(paramString2);
      Preconditions.checkArgument(TextUtils.isEmpty(paramString2) ^ true, "%s cannot be empty", new Object[] { paramString1 });
      return paramString2;
    }
    
    private void checkValidValue(String paramString)
    {
      Preconditions.checkNotNull(paramString);
      int i = paramString.length();
      int j = UserData.getMinValueLength();
      int k = UserData.getMaxValueLength();
      paramString = new StringBuilder();
      paramString.append("value length (");
      paramString.append(i);
      paramString.append(")");
      Preconditions.checkArgumentInRange(i, j, k, paramString.toString());
    }
    
    private void throwIfDestroyed()
    {
      if (!mDestroyed) {
        return;
      }
      throw new IllegalStateException("Already called #build()");
    }
    
    public Builder add(String paramString1, String paramString2)
    {
      throwIfDestroyed();
      checkNotEmpty("categoryId", paramString2);
      checkValidValue(paramString1);
      boolean bool1 = mUniqueCategoryIds.contains(paramString2);
      boolean bool2 = false;
      if (!bool1)
      {
        if (mUniqueCategoryIds.size() < UserData.getMaxCategoryCount()) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("already added ");
        localStringBuilder.append(mUniqueCategoryIds.size());
        localStringBuilder.append(" unique category ids");
        Preconditions.checkState(bool1, localStringBuilder.toString());
      }
      Preconditions.checkState(mValues.contains(paramString1) ^ true, "already has entry with same value");
      bool1 = bool2;
      if (mValues.size() < UserData.getMaxUserDataSize()) {
        bool1 = true;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("already added ");
      localStringBuilder.append(mValues.size());
      localStringBuilder.append(" elements");
      Preconditions.checkState(bool1, localStringBuilder.toString());
      addMapping(paramString1, paramString2);
      return this;
    }
    
    public UserData build()
    {
      throwIfDestroyed();
      mDestroyed = true;
      return new UserData(this, null);
    }
    
    public Builder setFieldClassificationAlgorithm(String paramString, Bundle paramBundle)
    {
      throwIfDestroyed();
      mAlgorithm = paramString;
      mAlgorithmArgs = paramBundle;
      return this;
    }
  }
}
