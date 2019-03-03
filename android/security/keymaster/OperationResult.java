package android.security.keymaster;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class OperationResult
  implements Parcelable
{
  public static final Parcelable.Creator<OperationResult> CREATOR = new Parcelable.Creator()
  {
    public OperationResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new OperationResult(paramAnonymousParcel);
    }
    
    public OperationResult[] newArray(int paramAnonymousInt)
    {
      return new OperationResult[paramAnonymousInt];
    }
  };
  public final int inputConsumed;
  public final long operationHandle;
  public final KeymasterArguments outParams;
  public final byte[] output;
  public final int resultCode;
  public final IBinder token;
  
  public OperationResult(int paramInt1, IBinder paramIBinder, long paramLong, int paramInt2, byte[] paramArrayOfByte, KeymasterArguments paramKeymasterArguments)
  {
    resultCode = paramInt1;
    token = paramIBinder;
    operationHandle = paramLong;
    inputConsumed = paramInt2;
    output = paramArrayOfByte;
    outParams = paramKeymasterArguments;
  }
  
  protected OperationResult(Parcel paramParcel)
  {
    resultCode = paramParcel.readInt();
    token = paramParcel.readStrongBinder();
    operationHandle = paramParcel.readLong();
    inputConsumed = paramParcel.readInt();
    output = paramParcel.createByteArray();
    outParams = ((KeymasterArguments)KeymasterArguments.CREATOR.createFromParcel(paramParcel));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(resultCode);
    paramParcel.writeStrongBinder(token);
    paramParcel.writeLong(operationHandle);
    paramParcel.writeInt(inputConsumed);
    paramParcel.writeByteArray(output);
    outParams.writeToParcel(paramParcel, paramInt);
  }
}
