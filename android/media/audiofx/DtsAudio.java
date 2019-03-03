package android.media.audiofx;

import android.util.Log;
import java.util.UUID;

public class DtsAudio
  extends AudioEffect
{
  private static final String TAG = DtsAudio.class.getSimpleName();
  
  public DtsAudio(UUID paramUUID1, UUID paramUUID2, int paramInt1, int paramInt2)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    super(paramUUID1, paramUUID2, paramInt1, paramInt2);
    Log.d(TAG, "constructor");
  }
  
  public boolean getEnabled()
    throws IllegalStateException
  {
    Log.d(TAG, "getEnabled");
    return super.getEnabled();
  }
  
  public int getParameter(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IllegalStateException
  {
    Log.d(TAG, "getParameter");
    return super.getParameter(paramArrayOfByte1, paramArrayOfByte2);
  }
  
  public int setEnabled(boolean paramBoolean)
    throws IllegalStateException
  {
    String str = TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setEnabled ");
    localStringBuilder.append(paramBoolean);
    Log.d(str, localStringBuilder.toString());
    return super.setEnabled(paramBoolean);
  }
  
  public int setParameter(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IllegalStateException
  {
    Log.d(TAG, "setParameter");
    return super.setParameter(paramArrayOfByte1, paramArrayOfByte2);
  }
}
