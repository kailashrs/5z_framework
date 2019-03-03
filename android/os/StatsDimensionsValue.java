package android.os;

import android.annotation.SystemApi;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;

@SystemApi
public final class StatsDimensionsValue
  implements Parcelable
{
  public static final int BOOLEAN_VALUE_TYPE = 5;
  public static final Parcelable.Creator<StatsDimensionsValue> CREATOR = new Parcelable.Creator()
  {
    public StatsDimensionsValue createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StatsDimensionsValue(paramAnonymousParcel);
    }
    
    public StatsDimensionsValue[] newArray(int paramAnonymousInt)
    {
      return new StatsDimensionsValue[paramAnonymousInt];
    }
  };
  public static final int FLOAT_VALUE_TYPE = 6;
  public static final int INT_VALUE_TYPE = 3;
  public static final int LONG_VALUE_TYPE = 4;
  public static final int STRING_VALUE_TYPE = 2;
  private static final String TAG = "StatsDimensionsValue";
  public static final int TUPLE_VALUE_TYPE = 7;
  private final int mField;
  private final Object mValue;
  private final int mValueType;
  
  public StatsDimensionsValue(Parcel paramParcel)
  {
    mField = paramParcel.readInt();
    mValueType = paramParcel.readInt();
    mValue = readValueFromParcel(mValueType, paramParcel);
  }
  
  private static Object readValueFromParcel(int paramInt, Parcel paramParcel)
  {
    switch (paramInt)
    {
    default: 
      paramParcel = new StringBuilder();
      paramParcel.append("readValue of an impossible type ");
      paramParcel.append(paramInt);
      Slog.w("StatsDimensionsValue", paramParcel.toString());
      return null;
    case 7: 
      int i = paramParcel.readInt();
      StatsDimensionsValue[] arrayOfStatsDimensionsValue = new StatsDimensionsValue[i];
      for (paramInt = 0; paramInt < i; paramInt++) {
        arrayOfStatsDimensionsValue[paramInt] = new StatsDimensionsValue(paramParcel);
      }
      return arrayOfStatsDimensionsValue;
    case 6: 
      return Float.valueOf(paramParcel.readFloat());
    case 5: 
      return Boolean.valueOf(paramParcel.readBoolean());
    case 4: 
      return Long.valueOf(paramParcel.readLong());
    case 3: 
      return Integer.valueOf(paramParcel.readInt());
    }
    return paramParcel.readString();
  }
  
  private static boolean writeValueToParcel(int paramInt1, Object paramObject, Parcel paramParcel, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      break;
    }
    try
    {
      paramObject = (StatsDimensionsValue[])paramObject;
      paramParcel.writeInt(paramObject.length);
      for (paramInt1 = 0; paramInt1 < paramObject.length; paramInt1++) {
        paramObject[paramInt1].writeToParcel(paramParcel, paramInt2);
      }
      return true;
    }
    catch (ClassCastException paramObject)
    {
      Slog.w("StatsDimensionsValue", "writeValue cast failed", paramObject);
    }
    paramParcel.writeFloat(((Float)paramObject).floatValue());
    return true;
    paramParcel.writeBoolean(((Boolean)paramObject).booleanValue());
    return true;
    paramParcel.writeLong(((Long)paramObject).longValue());
    return true;
    paramParcel.writeInt(((Integer)paramObject).intValue());
    return true;
    paramParcel.writeString((String)paramObject);
    return true;
    paramObject = new java/lang/StringBuilder;
    paramObject.<init>();
    paramObject.append("readValue of an impossible type ");
    paramObject.append(paramInt1);
    Slog.w("StatsDimensionsValue", paramObject.toString());
    return false;
    return false;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean getBooleanValue()
  {
    try
    {
      if (mValueType == 5)
      {
        boolean bool = ((Boolean)mValue).booleanValue();
        return bool;
      }
    }
    catch (ClassCastException localClassCastException)
    {
      Slog.w("StatsDimensionsValue", "Failed to successfully get value", localClassCastException);
    }
    return false;
  }
  
  public int getField()
  {
    return mField;
  }
  
  public float getFloatValue()
  {
    try
    {
      if (mValueType == 6)
      {
        float f = ((Float)mValue).floatValue();
        return f;
      }
    }
    catch (ClassCastException localClassCastException)
    {
      Slog.w("StatsDimensionsValue", "Failed to successfully get value", localClassCastException);
    }
    return 0.0F;
  }
  
  public int getIntValue()
  {
    try
    {
      if (mValueType == 3)
      {
        int i = ((Integer)mValue).intValue();
        return i;
      }
    }
    catch (ClassCastException localClassCastException)
    {
      Slog.w("StatsDimensionsValue", "Failed to successfully get value", localClassCastException);
    }
    return 0;
  }
  
  public long getLongValue()
  {
    try
    {
      if (mValueType == 4)
      {
        long l = ((Long)mValue).longValue();
        return l;
      }
    }
    catch (ClassCastException localClassCastException)
    {
      Slog.w("StatsDimensionsValue", "Failed to successfully get value", localClassCastException);
    }
    return 0L;
  }
  
  public String getStringValue()
  {
    try
    {
      if (mValueType == 2)
      {
        String str = (String)mValue;
        return str;
      }
    }
    catch (ClassCastException localClassCastException)
    {
      Slog.w("StatsDimensionsValue", "Failed to successfully get value", localClassCastException);
    }
    return null;
  }
  
  public List<StatsDimensionsValue> getTupleValueList()
  {
    if (mValueType != 7) {
      return null;
    }
    try
    {
      StatsDimensionsValue[] arrayOfStatsDimensionsValue = (StatsDimensionsValue[])mValue;
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(arrayOfStatsDimensionsValue.length);
      for (int i = 0; i < arrayOfStatsDimensionsValue.length; i++) {
        localArrayList.add(arrayOfStatsDimensionsValue[i]);
      }
      return localArrayList;
    }
    catch (ClassCastException localClassCastException)
    {
      Slog.w("StatsDimensionsValue", "Failed to successfully get value", localClassCastException);
    }
    return null;
  }
  
  public int getValueType()
  {
    return mValueType;
  }
  
  public boolean isValueType(int paramInt)
  {
    boolean bool;
    if (mValueType == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append(mField);
      localStringBuilder.append(":");
      if (mValueType == 7)
      {
        localStringBuilder.append("{");
        localObject = (StatsDimensionsValue[])mValue;
        for (int i = 0; i < localObject.length; i++)
        {
          localStringBuilder.append(localObject[i].toString());
          localStringBuilder.append("|");
        }
        localStringBuilder.append("}");
      }
      else
      {
        localStringBuilder.append(mValue.toString());
      }
      Object localObject = localStringBuilder.toString();
      return localObject;
    }
    catch (ClassCastException localClassCastException)
    {
      Slog.w("StatsDimensionsValue", "Failed to successfully get value", localClassCastException);
    }
    return "";
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mField);
    paramParcel.writeInt(mValueType);
    writeValueToParcel(mValueType, mValue, paramParcel, paramInt);
  }
}
