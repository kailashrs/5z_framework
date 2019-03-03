package android.hardware.camera2.impl;

public abstract interface CameraCaptureSessionCore
{
  public abstract CameraDeviceImpl.StateCallbackKK getDeviceStateCallback();
  
  public abstract boolean isAborting();
  
  public abstract void replaceSessionClose();
}
