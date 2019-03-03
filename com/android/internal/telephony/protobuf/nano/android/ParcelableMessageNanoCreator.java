package com.android.internal.telephony.protobuf.nano.android;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ParcelableMessageNanoCreator<T extends MessageNano>
  implements Parcelable.Creator<T>
{
  private static final String TAG = "PMNCreator";
  private final Class<T> mClazz;
  
  public ParcelableMessageNanoCreator(Class<T> paramClass)
  {
    mClazz = paramClass;
  }
  
  static <T extends MessageNano> void writeToParcel(Class<T> paramClass, MessageNano paramMessageNano, Parcel paramParcel)
  {
    paramParcel.writeString(paramClass.getName());
    paramParcel.writeByteArray(MessageNano.toByteArray(paramMessageNano));
  }
  
  public T createFromParcel(Parcel paramParcel)
  {
    String str = paramParcel.readString();
    byte[] arrayOfByte = paramParcel.createByteArray();
    Parcel localParcel1 = null;
    Parcel localParcel2 = null;
    Parcel localParcel3 = null;
    Parcel localParcel4 = null;
    Parcel localParcel5 = null;
    Parcel localParcel6 = null;
    try
    {
      paramParcel = (MessageNano)Class.forName(str, false, getClass().getClassLoader()).asSubclass(MessageNano.class).getConstructor(new Class[0]).newInstance(new Object[0]);
      localParcel6 = paramParcel;
      localParcel1 = paramParcel;
      localParcel2 = paramParcel;
      localParcel3 = paramParcel;
      localParcel4 = paramParcel;
      localParcel5 = paramParcel;
      MessageNano.mergeFrom(paramParcel, arrayOfByte);
    }
    catch (InvalidProtocolBufferNanoException paramParcel)
    {
      Log.e("PMNCreator", "Exception trying to create proto from parcel", paramParcel);
      paramParcel = localParcel6;
    }
    catch (InstantiationException paramParcel)
    {
      Log.e("PMNCreator", "Exception trying to create proto from parcel", paramParcel);
      paramParcel = localParcel1;
    }
    catch (IllegalAccessException paramParcel)
    {
      Log.e("PMNCreator", "Exception trying to create proto from parcel", paramParcel);
      paramParcel = localParcel2;
    }
    catch (InvocationTargetException paramParcel)
    {
      Log.e("PMNCreator", "Exception trying to create proto from parcel", paramParcel);
      paramParcel = localParcel3;
    }
    catch (NoSuchMethodException paramParcel)
    {
      Log.e("PMNCreator", "Exception trying to create proto from parcel", paramParcel);
      paramParcel = localParcel4;
    }
    catch (ClassNotFoundException paramParcel)
    {
      Log.e("PMNCreator", "Exception trying to create proto from parcel", paramParcel);
      paramParcel = localParcel5;
    }
    return paramParcel;
  }
  
  public T[] newArray(int paramInt)
  {
    return (MessageNano[])Array.newInstance(mClazz, paramInt);
  }
}
