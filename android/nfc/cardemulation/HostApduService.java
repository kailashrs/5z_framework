package android.nfc.cardemulation;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public abstract class HostApduService
  extends Service
{
  public static final int DEACTIVATION_DESELECTED = 1;
  public static final int DEACTIVATION_LINK_LOSS = 0;
  public static final String KEY_DATA = "data";
  public static final int MSG_COMMAND_APDU = 0;
  public static final int MSG_DEACTIVATED = 2;
  public static final int MSG_RESPONSE_APDU = 1;
  public static final int MSG_UNHANDLED = 3;
  public static final String SERVICE_INTERFACE = "android.nfc.cardemulation.action.HOST_APDU_SERVICE";
  public static final String SERVICE_META_DATA = "android.nfc.cardemulation.host_apdu_service";
  static final String TAG = "ApduService";
  final Messenger mMessenger = new Messenger(new MsgHandler());
  Messenger mNfcService = null;
  
  public HostApduService() {}
  
  public final void notifyUnhandled()
  {
    Message localMessage = Message.obtain(null, 3);
    try
    {
      mMessenger.send(localMessage);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TAG", "Local messenger has died.");
    }
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    return mMessenger.getBinder();
  }
  
  public abstract void onDeactivated(int paramInt);
  
  public abstract byte[] processCommandApdu(byte[] paramArrayOfByte, Bundle paramBundle);
  
  public final void sendResponseApdu(byte[] paramArrayOfByte)
  {
    Message localMessage = Message.obtain(null, 1);
    Bundle localBundle = new Bundle();
    localBundle.putByteArray("data", paramArrayOfByte);
    localMessage.setData(localBundle);
    try
    {
      mMessenger.send(localMessage);
    }
    catch (RemoteException paramArrayOfByte)
    {
      Log.e("TAG", "Local messenger has died.");
    }
  }
  
  final class MsgHandler
    extends Handler
  {
    MsgHandler() {}
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        super.handleMessage(paramMessage);
        break;
      case 3: 
        if (mNfcService == null)
        {
          Log.e("ApduService", "notifyUnhandled not sent; service was deactivated.");
          return;
        }
        try
        {
          replyTo = mMessenger;
          mNfcService.send(paramMessage);
        }
        catch (RemoteException paramMessage)
        {
          Log.e("ApduService", "RemoteException calling into NfcService.");
        }
      case 2: 
        mNfcService = null;
        onDeactivated(arg1);
        break;
      case 1: 
        if (mNfcService == null)
        {
          Log.e("ApduService", "Response not sent; service was deactivated.");
          return;
        }
        try
        {
          replyTo = mMessenger;
          mNfcService.send(paramMessage);
        }
        catch (RemoteException paramMessage)
        {
          Log.e("ApduService", "RemoteException calling into NfcService.");
        }
      case 0: 
        Object localObject = paramMessage.getData();
        if (localObject == null) {
          return;
        }
        if (mNfcService == null) {
          mNfcService = replyTo;
        }
        paramMessage = ((Bundle)localObject).getByteArray("data");
        if (paramMessage != null)
        {
          byte[] arrayOfByte = processCommandApdu(paramMessage, null);
          if (arrayOfByte != null)
          {
            if (mNfcService == null)
            {
              Log.e("ApduService", "Response not sent; service was deactivated.");
              return;
            }
            localObject = Message.obtain(null, 1);
            paramMessage = new Bundle();
            paramMessage.putByteArray("data", arrayOfByte);
            ((Message)localObject).setData(paramMessage);
            replyTo = mMessenger;
            try
            {
              mNfcService.send((Message)localObject);
            }
            catch (RemoteException paramMessage)
            {
              Log.e("TAG", "Response not sent; RemoteException calling into NfcService.");
            }
          }
        }
        else
        {
          Log.e("ApduService", "Received MSG_COMMAND_APDU without data.");
        }
        break;
      }
    }
  }
}
