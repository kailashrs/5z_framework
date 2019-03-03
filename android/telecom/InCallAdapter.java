package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import com.android.internal.telecom.IInCallAdapter;
import java.util.List;

public final class InCallAdapter
{
  private final IInCallAdapter mAdapter;
  
  public InCallAdapter(IInCallAdapter paramIInCallAdapter)
  {
    mAdapter = paramIInCallAdapter;
  }
  
  public void answerCall(String paramString, int paramInt)
  {
    try
    {
      mAdapter.answerCall(paramString, paramInt);
    }
    catch (RemoteException paramString) {}
  }
  
  public void conference(String paramString1, String paramString2)
  {
    try
    {
      mAdapter.conference(paramString1, paramString2);
    }
    catch (RemoteException paramString1) {}
  }
  
  public void deflectCall(String paramString, Uri paramUri)
  {
    try
    {
      mAdapter.deflectCall(paramString, paramUri);
    }
    catch (RemoteException paramString) {}
  }
  
  public void disconnectCall(String paramString)
  {
    try
    {
      mAdapter.disconnectCall(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void handoverTo(String paramString, PhoneAccountHandle paramPhoneAccountHandle, int paramInt, Bundle paramBundle)
  {
    try
    {
      mAdapter.handoverTo(paramString, paramPhoneAccountHandle, paramInt, paramBundle);
    }
    catch (RemoteException paramString) {}
  }
  
  public void holdCall(String paramString)
  {
    try
    {
      mAdapter.holdCall(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void mergeConference(String paramString)
  {
    try
    {
      mAdapter.mergeConference(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void mute(boolean paramBoolean)
  {
    try
    {
      mAdapter.mute(paramBoolean);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void phoneAccountSelected(String paramString, PhoneAccountHandle paramPhoneAccountHandle, boolean paramBoolean)
  {
    try
    {
      mAdapter.phoneAccountSelected(paramString, paramPhoneAccountHandle, paramBoolean);
    }
    catch (RemoteException paramString) {}
  }
  
  public void playDtmfTone(String paramString, char paramChar)
  {
    try
    {
      mAdapter.playDtmfTone(paramString, paramChar);
    }
    catch (RemoteException paramString) {}
  }
  
  public void postDialContinue(String paramString, boolean paramBoolean)
  {
    try
    {
      mAdapter.postDialContinue(paramString, paramBoolean);
    }
    catch (RemoteException paramString) {}
  }
  
  public void pullExternalCall(String paramString)
  {
    try
    {
      mAdapter.pullExternalCall(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void putExtra(String paramString1, String paramString2, int paramInt)
  {
    try
    {
      Bundle localBundle = new android/os/Bundle;
      localBundle.<init>();
      localBundle.putInt(paramString2, paramInt);
      mAdapter.putExtras(paramString1, localBundle);
    }
    catch (RemoteException paramString1) {}
  }
  
  public void putExtra(String paramString1, String paramString2, String paramString3)
  {
    try
    {
      Bundle localBundle = new android/os/Bundle;
      localBundle.<init>();
      localBundle.putString(paramString2, paramString3);
      mAdapter.putExtras(paramString1, localBundle);
    }
    catch (RemoteException paramString1) {}
  }
  
  public void putExtra(String paramString1, String paramString2, boolean paramBoolean)
  {
    try
    {
      Bundle localBundle = new android/os/Bundle;
      localBundle.<init>();
      localBundle.putBoolean(paramString2, paramBoolean);
      mAdapter.putExtras(paramString1, localBundle);
    }
    catch (RemoteException paramString1) {}
  }
  
  public void putExtras(String paramString, Bundle paramBundle)
  {
    try
    {
      mAdapter.putExtras(paramString, paramBundle);
    }
    catch (RemoteException paramString) {}
  }
  
  public void rejectCall(String paramString1, boolean paramBoolean, String paramString2)
  {
    try
    {
      mAdapter.rejectCall(paramString1, paramBoolean, paramString2);
    }
    catch (RemoteException paramString1) {}
  }
  
  public void removeExtras(String paramString, List<String> paramList)
  {
    try
    {
      mAdapter.removeExtras(paramString, paramList);
    }
    catch (RemoteException paramString) {}
  }
  
  public void requestBluetoothAudio(String paramString)
  {
    try
    {
      mAdapter.setAudioRoute(2, paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void respondToRttRequest(String paramString, int paramInt, boolean paramBoolean)
  {
    try
    {
      mAdapter.respondToRttRequest(paramString, paramInt, paramBoolean);
    }
    catch (RemoteException paramString) {}
  }
  
  public void sendCallEvent(String paramString1, String paramString2, int paramInt, Bundle paramBundle)
  {
    try
    {
      mAdapter.sendCallEvent(paramString1, paramString2, paramInt, paramBundle);
    }
    catch (RemoteException paramString1) {}
  }
  
  public void sendRttRequest(String paramString)
  {
    try
    {
      mAdapter.sendRttRequest(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void setAudioRoute(int paramInt)
  {
    try
    {
      mAdapter.setAudioRoute(paramInt, null);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void setRttMode(String paramString, int paramInt)
  {
    try
    {
      mAdapter.setRttMode(paramString, paramInt);
    }
    catch (RemoteException paramString) {}
  }
  
  public void splitFromConference(String paramString)
  {
    try
    {
      mAdapter.splitFromConference(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void stopDtmfTone(String paramString)
  {
    try
    {
      mAdapter.stopDtmfTone(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void stopRtt(String paramString)
  {
    try
    {
      mAdapter.stopRtt(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void swapConference(String paramString)
  {
    try
    {
      mAdapter.swapConference(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void turnProximitySensorOff(boolean paramBoolean)
  {
    try
    {
      mAdapter.turnOffProximitySensor(paramBoolean);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void turnProximitySensorOn()
  {
    try
    {
      mAdapter.turnOnProximitySensor();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void unholdCall(String paramString)
  {
    try
    {
      mAdapter.unholdCall(paramString);
    }
    catch (RemoteException paramString) {}
  }
}
