package android.hardware.radio;

import android.annotation.SystemApi;
import android.graphics.Bitmap;
import java.util.List;
import java.util.Map;

@SystemApi
public abstract class RadioTuner
{
  public static final int DIRECTION_DOWN = 1;
  public static final int DIRECTION_UP = 0;
  @Deprecated
  public static final int ERROR_BACKGROUND_SCAN_FAILED = 6;
  @Deprecated
  public static final int ERROR_BACKGROUND_SCAN_UNAVAILABLE = 5;
  @Deprecated
  public static final int ERROR_CANCELLED = 2;
  @Deprecated
  public static final int ERROR_CONFIG = 4;
  @Deprecated
  public static final int ERROR_HARDWARE_FAILURE = 0;
  @Deprecated
  public static final int ERROR_SCAN_TIMEOUT = 3;
  @Deprecated
  public static final int ERROR_SERVER_DIED = 1;
  
  public RadioTuner() {}
  
  public abstract int cancel();
  
  public abstract void cancelAnnouncement();
  
  public abstract void close();
  
  @Deprecated
  public abstract int getConfiguration(RadioManager.BandConfig[] paramArrayOfBandConfig);
  
  public ProgramList getDynamicProgramList(ProgramList.Filter paramFilter)
  {
    return null;
  }
  
  public abstract Bitmap getMetadataImage(int paramInt);
  
  public abstract boolean getMute();
  
  public Map<String, String> getParameters(List<String> paramList)
  {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public abstract int getProgramInformation(RadioManager.ProgramInfo[] paramArrayOfProgramInfo);
  
  @Deprecated
  public abstract List<RadioManager.ProgramInfo> getProgramList(Map<String, String> paramMap);
  
  public abstract boolean hasControl();
  
  @Deprecated
  public abstract boolean isAnalogForced();
  
  @Deprecated
  public abstract boolean isAntennaConnected();
  
  public boolean isConfigFlagSet(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public boolean isConfigFlagSupported(int paramInt)
  {
    return false;
  }
  
  public abstract int scan(int paramInt, boolean paramBoolean);
  
  @Deprecated
  public abstract void setAnalogForced(boolean paramBoolean);
  
  public void setConfigFlag(int paramInt, boolean paramBoolean)
  {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public abstract int setConfiguration(RadioManager.BandConfig paramBandConfig);
  
  public abstract int setMute(boolean paramBoolean);
  
  public Map<String, String> setParameters(Map<String, String> paramMap)
  {
    throw new UnsupportedOperationException();
  }
  
  public abstract boolean startBackgroundScan();
  
  public abstract int step(int paramInt, boolean paramBoolean);
  
  @Deprecated
  public abstract int tune(int paramInt1, int paramInt2);
  
  public abstract void tune(ProgramSelector paramProgramSelector);
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public void onAntennaState(boolean paramBoolean) {}
    
    public void onBackgroundScanAvailabilityChange(boolean paramBoolean) {}
    
    public void onBackgroundScanComplete() {}
    
    @Deprecated
    public void onConfigurationChanged(RadioManager.BandConfig paramBandConfig) {}
    
    public void onControlChanged(boolean paramBoolean) {}
    
    public void onEmergencyAnnouncement(boolean paramBoolean) {}
    
    public void onError(int paramInt) {}
    
    @Deprecated
    public void onMetadataChanged(RadioMetadata paramRadioMetadata) {}
    
    public void onParametersUpdated(Map<String, String> paramMap) {}
    
    public void onProgramInfoChanged(RadioManager.ProgramInfo paramProgramInfo) {}
    
    public void onProgramListChanged() {}
    
    public void onTrafficAnnouncement(boolean paramBoolean) {}
    
    public void onTuneFailed(int paramInt, ProgramSelector paramProgramSelector) {}
  }
}
