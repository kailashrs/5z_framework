package android.hardware.camera2.impl;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PhysicalCaptureResultInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PhysicalCaptureResultInfo> CREATOR = new Parcelable.Creator()
  {
    public PhysicalCaptureResultInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PhysicalCaptureResultInfo(paramAnonymousParcel, null);
    }
    
    public PhysicalCaptureResultInfo[] newArray(int paramAnonymousInt)
    {
      return new PhysicalCaptureResultInfo[paramAnonymousInt];
    }
  };
  private String cameraId;
  private CameraMetadataNative cameraMetadata;
  
  private PhysicalCaptureResultInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public PhysicalCaptureResultInfo(String paramString, CameraMetadataNative paramCameraMetadataNative)
  {
    cameraId = paramString;
    cameraMetadata = paramCameraMetadataNative;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getCameraId()
  {
    return cameraId;
  }
  
  public CameraMetadataNative getCameraMetadata()
  {
    return cameraMetadata;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    cameraId = paramParcel.readString();
    cameraMetadata = new CameraMetadataNative();
    cameraMetadata.readFromParcel(paramParcel);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(cameraId);
    cameraMetadata.writeToParcel(paramParcel, paramInt);
  }
}
