package com.android.internal.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.InputChannel;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class InputBindResult
  implements Parcelable
{
  public static final Parcelable.Creator<InputBindResult> CREATOR = new Parcelable.Creator()
  {
    public InputBindResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new InputBindResult(paramAnonymousParcel);
    }
    
    public InputBindResult[] newArray(int paramAnonymousInt)
    {
      return new InputBindResult[paramAnonymousInt];
    }
  };
  public static final InputBindResult IME_NOT_CONNECTED = error(8);
  public static final InputBindResult INVALID_PACKAGE_NAME;
  public static final InputBindResult INVALID_USER = error(9);
  public static final InputBindResult NOT_IME_TARGET_WINDOW;
  public static final InputBindResult NO_EDITOR;
  public static final InputBindResult NO_IME;
  public static final InputBindResult NULL = error(4);
  public static final InputBindResult NULL_EDITOR_INFO;
  public final InputChannel channel;
  public final String id;
  public final IInputMethodSession method;
  public final int result;
  public final int sequence;
  public final int userActionNotificationSequenceNumber;
  
  static
  {
    NO_IME = error(5);
    NO_EDITOR = error(12);
    INVALID_PACKAGE_NAME = error(6);
    NULL_EDITOR_INFO = error(10);
    NOT_IME_TARGET_WINDOW = error(11);
  }
  
  public InputBindResult(int paramInt1, IInputMethodSession paramIInputMethodSession, InputChannel paramInputChannel, String paramString, int paramInt2, int paramInt3)
  {
    result = paramInt1;
    method = paramIInputMethodSession;
    channel = paramInputChannel;
    id = paramString;
    sequence = paramInt2;
    userActionNotificationSequenceNumber = paramInt3;
  }
  
  InputBindResult(Parcel paramParcel)
  {
    result = paramParcel.readInt();
    method = IInputMethodSession.Stub.asInterface(paramParcel.readStrongBinder());
    if (paramParcel.readInt() != 0) {
      channel = ((InputChannel)InputChannel.CREATOR.createFromParcel(paramParcel));
    } else {
      channel = null;
    }
    id = paramParcel.readString();
    sequence = paramParcel.readInt();
    userActionNotificationSequenceNumber = paramParcel.readInt();
  }
  
  private static InputBindResult error(int paramInt)
  {
    return new InputBindResult(paramInt, null, null, null, -1, -1);
  }
  
  public int describeContents()
  {
    int i;
    if (channel != null) {
      i = channel.describeContents();
    } else {
      i = 0;
    }
    return i;
  }
  
  public String getResultString()
  {
    switch (result)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown(");
      localStringBuilder.append(result);
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    case 12: 
      return "ERROR_NO_EDITOR";
    case 11: 
      return "ERROR_NOT_IME_TARGET_WINDOW";
    case 10: 
      return "ERROR_NULL_EDITOR_INFO";
    case 9: 
      return "ERROR_INVALID_USER";
    case 8: 
      return "ERROR_IME_NOT_CONNECTED";
    case 7: 
      return "ERROR_SYSTEM_NOT_READY";
    case 6: 
      return "ERROR_INVALID_PACKAGE_NAME";
    case 5: 
      return "ERROR_NO_IME";
    case 4: 
      return "ERROR_NULL";
    case 3: 
      return "SUCCESS_REPORT_WINDOW_FOCUS_ONLY";
    case 2: 
      return "SUCCESS_WAITING_IME_BINDING";
    case 1: 
      return "SUCCESS_WAITING_IME_SESSION";
    }
    return "SUCCESS_WITH_IME_SESSION";
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("InputBindResult{result=");
    localStringBuilder.append(getResultString());
    localStringBuilder.append(" method=");
    localStringBuilder.append(method);
    localStringBuilder.append(" id=");
    localStringBuilder.append(id);
    localStringBuilder.append(" sequence=");
    localStringBuilder.append(sequence);
    localStringBuilder.append(" userActionNotificationSequenceNumber=");
    localStringBuilder.append(userActionNotificationSequenceNumber);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(result);
    paramParcel.writeStrongInterface(method);
    if (channel != null)
    {
      paramParcel.writeInt(1);
      channel.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeString(id);
    paramParcel.writeInt(sequence);
    paramParcel.writeInt(userActionNotificationSequenceNumber);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ResultCode
  {
    public static final int ERROR_IME_NOT_CONNECTED = 8;
    public static final int ERROR_INVALID_PACKAGE_NAME = 6;
    public static final int ERROR_INVALID_USER = 9;
    public static final int ERROR_NOT_IME_TARGET_WINDOW = 11;
    public static final int ERROR_NO_EDITOR = 12;
    public static final int ERROR_NO_IME = 5;
    public static final int ERROR_NULL = 4;
    public static final int ERROR_NULL_EDITOR_INFO = 10;
    public static final int ERROR_SYSTEM_NOT_READY = 7;
    public static final int SUCCESS_REPORT_WINDOW_FOCUS_ONLY = 3;
    public static final int SUCCESS_WAITING_IME_BINDING = 2;
    public static final int SUCCESS_WAITING_IME_SESSION = 1;
    public static final int SUCCESS_WITH_IME_SESSION = 0;
  }
}
