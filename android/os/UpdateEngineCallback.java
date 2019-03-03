package android.os;

import android.annotation.SystemApi;

@SystemApi
public abstract class UpdateEngineCallback
{
  public UpdateEngineCallback() {}
  
  @SystemApi
  public abstract void onPayloadApplicationComplete(int paramInt);
  
  @SystemApi
  public abstract void onStatusUpdate(int paramInt, float paramFloat);
}
