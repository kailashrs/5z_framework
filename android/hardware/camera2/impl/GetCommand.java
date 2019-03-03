package android.hardware.camera2.impl;

public abstract interface GetCommand
{
  public abstract <T> T getValue(CameraMetadataNative paramCameraMetadataNative, CameraMetadataNative.Key<T> paramKey);
}
