package android.view.inputmethod;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class InputBinding
  implements Parcelable
{
  public static final Parcelable.Creator<InputBinding> CREATOR = new Parcelable.Creator()
  {
    public InputBinding createFromParcel(Parcel paramAnonymousParcel)
    {
      return new InputBinding(paramAnonymousParcel);
    }
    
    public InputBinding[] newArray(int paramAnonymousInt)
    {
      return new InputBinding[paramAnonymousInt];
    }
  };
  static final String TAG = "InputBinding";
  final InputConnection mConnection;
  final IBinder mConnectionToken;
  final int mPid;
  final int mUid;
  
  InputBinding(Parcel paramParcel)
  {
    mConnection = null;
    mConnectionToken = paramParcel.readStrongBinder();
    mUid = paramParcel.readInt();
    mPid = paramParcel.readInt();
  }
  
  public InputBinding(InputConnection paramInputConnection, IBinder paramIBinder, int paramInt1, int paramInt2)
  {
    mConnection = paramInputConnection;
    mConnectionToken = paramIBinder;
    mUid = paramInt1;
    mPid = paramInt2;
  }
  
  public InputBinding(InputConnection paramInputConnection, InputBinding paramInputBinding)
  {
    mConnection = paramInputConnection;
    mConnectionToken = paramInputBinding.getConnectionToken();
    mUid = paramInputBinding.getUid();
    mPid = paramInputBinding.getPid();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public InputConnection getConnection()
  {
    return mConnection;
  }
  
  public IBinder getConnectionToken()
  {
    return mConnectionToken;
  }
  
  public int getPid()
  {
    return mPid;
  }
  
  public int getUid()
  {
    return mUid;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("InputBinding{");
    localStringBuilder.append(mConnectionToken);
    localStringBuilder.append(" / uid ");
    localStringBuilder.append(mUid);
    localStringBuilder.append(" / pid ");
    localStringBuilder.append(mPid);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(mConnectionToken);
    paramParcel.writeInt(mUid);
    paramParcel.writeInt(mPid);
  }
}
