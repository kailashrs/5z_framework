package android.hardware;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CameraStatus
  implements Parcelable
{
  public static final Parcelable.Creator<CameraStatus> CREATOR = new Parcelable.Creator()
  {
    public CameraStatus createFromParcel(Parcel paramAnonymousParcel)
    {
      CameraStatus localCameraStatus = new CameraStatus();
      localCameraStatus.readFromParcel(paramAnonymousParcel);
      return localCameraStatus;
    }
    
    public CameraStatus[] newArray(int paramAnonymousInt)
    {
      return new CameraStatus[paramAnonymousInt];
    }
  };
  public String cameraId;
  public int status;
  
  public CameraStatus() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    cameraId = paramParcel.readString();
    status = paramParcel.readInt();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(cameraId);
    paramParcel.writeInt(status);
  }
}
