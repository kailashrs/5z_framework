package android.hardware.camera2.impl;

public abstract interface SetCommand
{
  public abstract <T> void setValue(CameraMetadataNative paramCameraMetadataNative, T paramT);
}
