package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public final class ContentValues
  implements Parcelable
{
  public static final Parcelable.Creator<ContentValues> CREATOR = new Parcelable.Creator()
  {
    public ContentValues createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ContentValues(paramAnonymousParcel.readHashMap(null), null);
    }
    
    public ContentValues[] newArray(int paramAnonymousInt)
    {
      return new ContentValues[paramAnonymousInt];
    }
  };
  public static final String TAG = "ContentValues";
  private HashMap<String, Object> mValues;
  
  public ContentValues()
  {
    mValues = new HashMap(8);
  }
  
  public ContentValues(int paramInt)
  {
    mValues = new HashMap(paramInt, 1.0F);
  }
  
  public ContentValues(ContentValues paramContentValues)
  {
    mValues = new HashMap(mValues);
  }
  
  private ContentValues(HashMap<String, Object> paramHashMap)
  {
    mValues = paramHashMap;
  }
  
  public void clear()
  {
    mValues.clear();
  }
  
  public boolean containsKey(String paramString)
  {
    return mValues.containsKey(paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof ContentValues)) {
      return false;
    }
    return mValues.equals(mValues);
  }
  
  public Object get(String paramString)
  {
    return mValues.get(paramString);
  }
  
  public Boolean getAsBoolean(String paramString)
  {
    Object localObject1 = mValues.get(paramString);
    try
    {
      localObject2 = (Boolean)localObject1;
      return localObject2;
    }
    catch (ClassCastException localClassCastException)
    {
      boolean bool1 = localObject1 instanceof CharSequence;
      boolean bool2 = true;
      boolean bool3 = true;
      if (bool1)
      {
        bool2 = bool3;
        if (!Boolean.valueOf(localObject1.toString()).booleanValue()) {
          if ("1".equals(localObject1)) {
            bool2 = bool3;
          } else {
            bool2 = false;
          }
        }
        return Boolean.valueOf(bool2);
      }
      if ((localObject1 instanceof Number))
      {
        if (((Number)localObject1).intValue() == 0) {
          bool2 = false;
        }
        return Boolean.valueOf(bool2);
      }
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Cannot cast value for ");
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append(" to a Boolean: ");
      ((StringBuilder)localObject2).append(localObject1);
      Log.e("ContentValues", ((StringBuilder)localObject2).toString(), localClassCastException);
    }
    return null;
  }
  
  public Byte getAsByte(String paramString)
  {
    Object localObject = mValues.get(paramString);
    Byte localByte = null;
    StringBuilder localStringBuilder;
    if (localObject != null) {
      try
      {
        byte b = ((Number)localObject).byteValue();
        localByte = Byte.valueOf(b);
      }
      catch (ClassCastException localClassCastException)
      {
        if ((localObject instanceof CharSequence)) {
          try
          {
            localByte = Byte.valueOf(localObject.toString());
            return localByte;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Cannot parse Byte value for ");
            localStringBuilder.append(localObject);
            localStringBuilder.append(" at key ");
            localStringBuilder.append(paramString);
            Log.e("ContentValues", localStringBuilder.toString());
            return null;
          }
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Cannot cast value for ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" to a Byte: ");
        localStringBuilder.append(localObject);
        Log.e("ContentValues", localStringBuilder.toString(), localClassCastException);
        return null;
      }
    }
    return localStringBuilder;
  }
  
  public byte[] getAsByteArray(String paramString)
  {
    paramString = mValues.get(paramString);
    if ((paramString instanceof byte[])) {
      return (byte[])paramString;
    }
    return null;
  }
  
  public Double getAsDouble(String paramString)
  {
    Object localObject = mValues.get(paramString);
    Double localDouble = null;
    StringBuilder localStringBuilder;
    if (localObject != null) {
      try
      {
        double d = ((Number)localObject).doubleValue();
        localDouble = Double.valueOf(d);
      }
      catch (ClassCastException localClassCastException)
      {
        if ((localObject instanceof CharSequence)) {
          try
          {
            localDouble = Double.valueOf(localObject.toString());
            return localDouble;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Cannot parse Double value for ");
            localStringBuilder.append(localObject);
            localStringBuilder.append(" at key ");
            localStringBuilder.append(paramString);
            Log.e("ContentValues", localStringBuilder.toString());
            return null;
          }
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Cannot cast value for ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" to a Double: ");
        localStringBuilder.append(localObject);
        Log.e("ContentValues", localStringBuilder.toString(), localClassCastException);
        return null;
      }
    }
    return localStringBuilder;
  }
  
  public Float getAsFloat(String paramString)
  {
    Object localObject = mValues.get(paramString);
    Float localFloat = null;
    StringBuilder localStringBuilder;
    if (localObject != null) {
      try
      {
        float f = ((Number)localObject).floatValue();
        localFloat = Float.valueOf(f);
      }
      catch (ClassCastException localClassCastException)
      {
        if ((localObject instanceof CharSequence)) {
          try
          {
            localFloat = Float.valueOf(localObject.toString());
            return localFloat;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Cannot parse Float value for ");
            localStringBuilder.append(localObject);
            localStringBuilder.append(" at key ");
            localStringBuilder.append(paramString);
            Log.e("ContentValues", localStringBuilder.toString());
            return null;
          }
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Cannot cast value for ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" to a Float: ");
        localStringBuilder.append(localObject);
        Log.e("ContentValues", localStringBuilder.toString(), localClassCastException);
        return null;
      }
    }
    return localStringBuilder;
  }
  
  public Integer getAsInteger(String paramString)
  {
    Object localObject = mValues.get(paramString);
    Integer localInteger1 = null;
    StringBuilder localStringBuilder1;
    if (localObject != null) {
      try
      {
        int i = ((Number)localObject).intValue();
        localInteger1 = Integer.valueOf(i);
      }
      catch (ClassCastException localClassCastException)
      {
        if ((localObject instanceof CharSequence)) {
          try
          {
            Integer localInteger2 = Integer.valueOf(localObject.toString());
            return localInteger2;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            localStringBuilder1 = new StringBuilder();
            localStringBuilder1.append("Cannot parse Integer value for ");
            localStringBuilder1.append(localObject);
            localStringBuilder1.append(" at key ");
            localStringBuilder1.append(paramString);
            Log.e("ContentValues", localStringBuilder1.toString());
            return null;
          }
        }
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("Cannot cast value for ");
        localStringBuilder2.append(paramString);
        localStringBuilder2.append(" to a Integer: ");
        localStringBuilder2.append(localObject);
        Log.e("ContentValues", localStringBuilder2.toString(), localStringBuilder1);
        return null;
      }
    }
    return localStringBuilder1;
  }
  
  public Long getAsLong(String paramString)
  {
    Object localObject = mValues.get(paramString);
    Long localLong = null;
    StringBuilder localStringBuilder;
    if (localObject != null) {
      try
      {
        long l = ((Number)localObject).longValue();
        localLong = Long.valueOf(l);
      }
      catch (ClassCastException localClassCastException)
      {
        if ((localObject instanceof CharSequence)) {
          try
          {
            localLong = Long.valueOf(localObject.toString());
            return localLong;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Cannot parse Long value for ");
            localStringBuilder.append(localObject);
            localStringBuilder.append(" at key ");
            localStringBuilder.append(paramString);
            Log.e("ContentValues", localStringBuilder.toString());
            return null;
          }
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Cannot cast value for ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" to a Long: ");
        localStringBuilder.append(localObject);
        Log.e("ContentValues", localStringBuilder.toString(), localClassCastException);
        return null;
      }
    }
    return localStringBuilder;
  }
  
  public Short getAsShort(String paramString)
  {
    Object localObject = mValues.get(paramString);
    Short localShort1 = null;
    StringBuilder localStringBuilder1;
    if (localObject != null) {
      try
      {
        short s = ((Number)localObject).shortValue();
        localShort1 = Short.valueOf(s);
      }
      catch (ClassCastException localClassCastException)
      {
        if ((localObject instanceof CharSequence)) {
          try
          {
            Short localShort2 = Short.valueOf(localObject.toString());
            return localShort2;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            localStringBuilder1 = new StringBuilder();
            localStringBuilder1.append("Cannot parse Short value for ");
            localStringBuilder1.append(localObject);
            localStringBuilder1.append(" at key ");
            localStringBuilder1.append(paramString);
            Log.e("ContentValues", localStringBuilder1.toString());
            return null;
          }
        }
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("Cannot cast value for ");
        localStringBuilder2.append(paramString);
        localStringBuilder2.append(" to a Short: ");
        localStringBuilder2.append(localObject);
        Log.e("ContentValues", localStringBuilder2.toString(), localStringBuilder1);
        return null;
      }
    }
    return localStringBuilder1;
  }
  
  public String getAsString(String paramString)
  {
    paramString = mValues.get(paramString);
    if (paramString != null) {
      paramString = paramString.toString();
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  @Deprecated
  public ArrayList<String> getStringArrayList(String paramString)
  {
    return (ArrayList)mValues.get(paramString);
  }
  
  public int hashCode()
  {
    return mValues.hashCode();
  }
  
  public boolean isEmpty()
  {
    return mValues.isEmpty();
  }
  
  public Set<String> keySet()
  {
    return mValues.keySet();
  }
  
  public void put(String paramString, Boolean paramBoolean)
  {
    mValues.put(paramString, paramBoolean);
  }
  
  public void put(String paramString, Byte paramByte)
  {
    mValues.put(paramString, paramByte);
  }
  
  public void put(String paramString, Double paramDouble)
  {
    mValues.put(paramString, paramDouble);
  }
  
  public void put(String paramString, Float paramFloat)
  {
    mValues.put(paramString, paramFloat);
  }
  
  public void put(String paramString, Integer paramInteger)
  {
    mValues.put(paramString, paramInteger);
  }
  
  public void put(String paramString, Long paramLong)
  {
    mValues.put(paramString, paramLong);
  }
  
  public void put(String paramString, Short paramShort)
  {
    mValues.put(paramString, paramShort);
  }
  
  public void put(String paramString1, String paramString2)
  {
    mValues.put(paramString1, paramString2);
  }
  
  public void put(String paramString, byte[] paramArrayOfByte)
  {
    mValues.put(paramString, paramArrayOfByte);
  }
  
  public void putAll(ContentValues paramContentValues)
  {
    mValues.putAll(mValues);
  }
  
  public void putNull(String paramString)
  {
    mValues.put(paramString, null);
  }
  
  @Deprecated
  public void putStringArrayList(String paramString, ArrayList<String> paramArrayList)
  {
    mValues.put(paramString, paramArrayList);
  }
  
  public void remove(String paramString)
  {
    mValues.remove(paramString);
  }
  
  public int size()
  {
    return mValues.size();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    Iterator localIterator = mValues.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      String str2 = getAsString(str1);
      if (localStringBuilder1.length() > 0) {
        localStringBuilder1.append(" ");
      }
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(str1);
      localStringBuilder2.append("=");
      localStringBuilder2.append(str2);
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    return localStringBuilder1.toString();
  }
  
  public Set<Map.Entry<String, Object>> valueSet()
  {
    return mValues.entrySet();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeMap(mValues);
  }
}
