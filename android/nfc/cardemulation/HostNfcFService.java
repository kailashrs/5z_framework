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

public abstract class HostNfcFService
  extends Service
{
  public static final int DEACTIVATION_LINK_LOSS = 0;
  public static final String KEY_DATA = "data";
  public static final String KEY_MESSENGER = "messenger";
  public static final int MSG_COMMAND_PACKET = 0;
  public static final int MSG_DEACTIVATED = 2;
  public static final int MSG_RESPONSE_PACKET = 1;
  public static final String SERVICE_INTERFACE = "android.nfc.cardemulation.action.HOST_NFCF_SERVICE";
  public static final String SERVICE_META_DATA = "android.nfc.cardemulation.host_nfcf_service";
  static final String TAG = "NfcFService";
  final Messenger mMessenger = new Messenger(new MsgHandler());
  Messenger mNfcService = null;
  
  public HostNfcFService() {}
  
  public final IBinder onBind(Intent paramIntent)
  {
    return mMessenger.getBinder();
  }
  
  public abstract void onDeactivated(int paramInt);
  
  public abstract byte[] processNfcFPacket(byte[] paramArrayOfByte, Bundle paramBundle);
  
  public final void sendResponsePacket(byte[] paramArrayOfByte)
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
      case 2: 
        mNfcService = null;
        onDeactivated(arg1);
        break;
      case 1: 
        if (mNfcService == null)
        {
          Log.e("NfcFService", "Response not sent; service was deactivated.");
          return;
        }
        try
        {
          replyTo = mMessenger;
          mNfcService.send(paramMessage);
        }
        catch (RemoteException paramMessage)
        {
          Log.e("NfcFService", "RemoteException calling into NfcService.");
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
          localObject = processNfcFPacket(paramMessage, null);
          if (localObject != null)
          {
            if (mNfcService == null)
            {
              Log.e("NfcFService", "Response not sent; service was deactivated.");
              return;
            }
            Message localMessage = Message.obtain(null, 1);
            paramMessage = new Bundle();
            paramMessage.putByteArray("data", (byte[])localObject);
            localMessage.setData(paramMessage);
            replyTo = mMessenger;
            try
            {
              mNfcService.send(localMessage);
            }
            catch (RemoteException paramMessage)
            {
              Log.e("TAG", "Response not sent; RemoteException calling into NfcService.");
            }
          }
        }
        else
        {
          Log.e("NfcFService", "Received MSG_COMMAND_PACKET without data.");
        }
        break;
      }
    }
  }
}
