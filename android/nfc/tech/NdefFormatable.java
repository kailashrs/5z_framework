package android.nfc.tech;

import android.nfc.FormatException;
import android.nfc.INfcTag;
import android.nfc.NdefMessage;
import android.nfc.Tag;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;

public final class NdefFormatable
  extends BasicTagTechnology
{
  private static final String TAG = "NFC";
  
  public NdefFormatable(Tag paramTag)
    throws RemoteException
  {
    super(paramTag, 7);
  }
  
  public static NdefFormatable get(Tag paramTag)
  {
    if (!paramTag.hasTech(7)) {
      return null;
    }
    try
    {
      paramTag = new NdefFormatable(paramTag);
      return paramTag;
    }
    catch (RemoteException paramTag) {}
    return null;
  }
  
  public void format(NdefMessage paramNdefMessage)
    throws IOException, FormatException
  {
    format(paramNdefMessage, false);
  }
  
  void format(NdefMessage paramNdefMessage, boolean paramBoolean)
    throws IOException, FormatException
  {
    checkConnected();
    try
    {
      int i = mTag.getServiceHandle();
      INfcTag localINfcTag = mTag.getTagService();
      int j = localINfcTag.formatNdef(i, MifareClassic.KEY_DEFAULT);
      if (j != -8)
      {
        switch (j)
        {
        default: 
          paramNdefMessage = new java/io/IOException;
          paramNdefMessage.<init>();
          throw paramNdefMessage;
        case 0: 
          if (localINfcTag.isNdef(i))
          {
            if (paramNdefMessage != null)
            {
              j = localINfcTag.ndefWrite(i, paramNdefMessage);
              if (j != -8)
              {
                switch (j)
                {
                default: 
                  paramNdefMessage = new java/io/IOException;
                  paramNdefMessage.<init>();
                  throw paramNdefMessage;
                case 0: 
                  break;
                case -1: 
                  paramNdefMessage = new java/io/IOException;
                  paramNdefMessage.<init>();
                  throw paramNdefMessage;
                }
              }
              else
              {
                paramNdefMessage = new android/nfc/FormatException;
                paramNdefMessage.<init>();
                throw paramNdefMessage;
              }
            }
            if (paramBoolean)
            {
              i = localINfcTag.ndefMakeReadOnly(i);
              if (i != -8)
              {
                switch (i)
                {
                default: 
                  paramNdefMessage = new java/io/IOException;
                  paramNdefMessage.<init>();
                  throw paramNdefMessage;
                case 0: 
                  break;
                case -1: 
                  paramNdefMessage = new java/io/IOException;
                  paramNdefMessage.<init>();
                  throw paramNdefMessage;
                }
              }
              else
              {
                paramNdefMessage = new java/io/IOException;
                paramNdefMessage.<init>();
                throw paramNdefMessage;
              }
            }
            break;
          }
          paramNdefMessage = new java/io/IOException;
          paramNdefMessage.<init>();
          throw paramNdefMessage;
        case -1: 
          paramNdefMessage = new java/io/IOException;
          paramNdefMessage.<init>();
          throw paramNdefMessage;
        }
      }
      else
      {
        paramNdefMessage = new android/nfc/FormatException;
        paramNdefMessage.<init>();
        throw paramNdefMessage;
      }
    }
    catch (RemoteException paramNdefMessage)
    {
      Log.e("NFC", "NFC service dead", paramNdefMessage);
    }
  }
  
  public void formatReadOnly(NdefMessage paramNdefMessage)
    throws IOException, FormatException
  {
    format(paramNdefMessage, true);
  }
}
