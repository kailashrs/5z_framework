package android.hardware.camera2.impl;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CaptureResultExtras
  implements Parcelable
{
  public static final Parcelable.Creator<CaptureResultExtras> CREATOR = new Parcelable.Creator()
  {
    public CaptureResultExtras createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CaptureResultExtras(paramAnonymousParcel, null);
    }
    
    public CaptureResultExtras[] newArray(int paramAnonymousInt)
    {
      return new CaptureResultExtras[paramAnonymousInt];
    }
  };
  private int afTriggerId;
  private int errorStreamId;
  private long frameNumber;
  private int partialResultCount;
  private int precaptureTriggerId;
  private int requestId;
  private int subsequenceId;
  
  public CaptureResultExtras(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong, int paramInt5, int paramInt6)
  {
    requestId = paramInt1;
    subsequenceId = paramInt2;
    afTriggerId = paramInt3;
    precaptureTriggerId = paramInt4;
    frameNumber = paramLong;
    partialResultCount = paramInt5;
    errorStreamId = paramInt6;
  }
  
  private CaptureResultExtras(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAfTriggerId()
  {
    return afTriggerId;
  }
  
  public int getErrorStreamId()
  {
    return errorStreamId;
  }
  
  public long getFrameNumber()
  {
    return frameNumber;
  }
  
  public int getPartialResultCount()
  {
    return partialResultCount;
  }
  
  public int getPrecaptureTriggerId()
  {
    return precaptureTriggerId;
  }
  
  public int getRequestId()
  {
    return requestId;
  }
  
  public int getSubsequenceId()
  {
    return subsequenceId;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    requestId = paramParcel.readInt();
    subsequenceId = paramParcel.readInt();
    afTriggerId = paramParcel.readInt();
    precaptureTriggerId = paramParcel.readInt();
    frameNumber = paramParcel.readLong();
    partialResultCount = paramParcel.readInt();
    errorStreamId = paramParcel.readInt();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(requestId);
    paramParcel.writeInt(subsequenceId);
    paramParcel.writeInt(afTriggerId);
    paramParcel.writeInt(precaptureTriggerId);
    paramParcel.writeLong(frameNumber);
    paramParcel.writeInt(partialResultCount);
    paramParcel.writeInt(errorStreamId);
  }
}
