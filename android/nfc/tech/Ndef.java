package android.nfc.tech;

import android.nfc.FormatException;
import android.nfc.INfcTag;
import android.nfc.NdefMessage;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;

public final class Ndef
  extends BasicTagTechnology
{
  public static final String EXTRA_NDEF_CARDSTATE = "ndefcardstate";
  public static final String EXTRA_NDEF_MAXLENGTH = "ndefmaxlength";
  public static final String EXTRA_NDEF_MSG = "ndefmsg";
  public static final String EXTRA_NDEF_TYPE = "ndeftype";
  public static final String ICODE_SLI = "com.nxp.ndef.icodesli";
  public static final String MIFARE_CLASSIC = "com.nxp.ndef.mifareclassic";
  public static final int NDEF_MODE_READ_ONLY = 1;
  public static final int NDEF_MODE_READ_WRITE = 2;
  public static final int NDEF_MODE_UNKNOWN = 3;
  public static final String NFC_FORUM_TYPE_1 = "org.nfcforum.ndef.type1";
  public static final String NFC_FORUM_TYPE_2 = "org.nfcforum.ndef.type2";
  public static final String NFC_FORUM_TYPE_3 = "org.nfcforum.ndef.type3";
  public static final String NFC_FORUM_TYPE_4 = "org.nfcforum.ndef.type4";
  private static final String TAG = "NFC";
  public static final int TYPE_1 = 1;
  public static final int TYPE_2 = 2;
  public static final int TYPE_3 = 3;
  public static final int TYPE_4 = 4;
  public static final int TYPE_ICODE_SLI = 102;
  public static final int TYPE_MIFARE_CLASSIC = 101;
  public static final int TYPE_OTHER = -1;
  public static final String UNKNOWN = "android.ndef.unknown";
  private final int mCardState;
  private final int mMaxNdefSize;
  private final NdefMessage mNdefMsg;
  private final int mNdefType;
  
  public Ndef(Tag paramTag)
    throws RemoteException
  {
    super(paramTag, 6);
    paramTag = paramTag.getTechExtras(6);
    if (paramTag != null)
    {
      mMaxNdefSize = paramTag.getInt("ndefmaxlength");
      mCardState = paramTag.getInt("ndefcardstate");
      mNdefMsg = ((NdefMessage)paramTag.getParcelable("ndefmsg"));
      mNdefType = paramTag.getInt("ndeftype");
      return;
    }
    throw new NullPointerException("NDEF tech extras are null.");
  }
  
  public static Ndef get(Tag paramTag)
  {
    if (!paramTag.hasTech(6)) {
      return null;
    }
    try
    {
      paramTag = new Ndef(paramTag);
      return paramTag;
    }
    catch (RemoteException paramTag) {}
    return null;
  }
  
  public boolean canMakeReadOnly()
  {
    INfcTag localINfcTag = mTag.getTagService();
    if (localINfcTag == null) {
      return false;
    }
    try
    {
      boolean bool = localINfcTag.canMakeReadOnly(mNdefType);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
    }
    return false;
  }
  
  public NdefMessage getCachedNdefMessage()
  {
    return mNdefMsg;
  }
  
  public int getMaxSize()
  {
    return mMaxNdefSize;
  }
  
  public NdefMessage getNdefMessage()
    throws IOException, FormatException
  {
    checkConnected();
    try
    {
      INfcTag localINfcTag = mTag.getTagService();
      if (localINfcTag != null)
      {
        int i = mTag.getServiceHandle();
        if (localINfcTag.isNdef(i))
        {
          localObject = localINfcTag.ndefRead(i);
          if ((localObject == null) && (!localINfcTag.isPresent(i)))
          {
            localObject = new android/nfc/TagLostException;
            ((TagLostException)localObject).<init>();
            throw ((Throwable)localObject);
          }
          return localObject;
        }
        if (localINfcTag.isPresent(i)) {
          return null;
        }
        localObject = new android/nfc/TagLostException;
        ((TagLostException)localObject).<init>();
        throw ((Throwable)localObject);
      }
      Object localObject = new java/io/IOException;
      ((IOException)localObject).<init>("Mock tags don't support this operation.");
      throw ((Throwable)localObject);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
    }
    return null;
  }
  
  public String getType()
  {
    int i = mNdefType;
    switch (i)
    {
    default: 
      switch (i)
      {
      default: 
        return "android.ndef.unknown";
      case 102: 
        return "com.nxp.ndef.icodesli";
      }
      return "com.nxp.ndef.mifareclassic";
    case 4: 
      return "org.nfcforum.ndef.type4";
    case 3: 
      return "org.nfcforum.ndef.type3";
    case 2: 
      return "org.nfcforum.ndef.type2";
    }
    return "org.nfcforum.ndef.type1";
  }
  
  public boolean isWritable()
  {
    boolean bool;
    if (mCardState == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean makeReadOnly()
    throws IOException
  {
    checkConnected();
    try
    {
      Object localObject = mTag.getTagService();
      if (localObject == null) {
        return false;
      }
      if (((INfcTag)localObject).isNdef(mTag.getServiceHandle()))
      {
        int i = ((INfcTag)localObject).ndefMakeReadOnly(mTag.getServiceHandle());
        if (i != -8)
        {
          switch (i)
          {
          default: 
            localObject = new java/io/IOException;
            ((IOException)localObject).<init>();
            throw ((Throwable)localObject);
          case 0: 
            return true;
          }
          localObject = new java/io/IOException;
          ((IOException)localObject).<init>();
          throw ((Throwable)localObject);
        }
        return false;
      }
      localObject = new java/io/IOException;
      ((IOException)localObject).<init>("Tag is not ndef");
      throw ((Throwable)localObject);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
    }
    return false;
  }
  
  public void writeNdefMessage(NdefMessage paramNdefMessage)
    throws IOException, FormatException
  {
    checkConnected();
    try
    {
      INfcTag localINfcTag = mTag.getTagService();
      if (localINfcTag != null)
      {
        int i = mTag.getServiceHandle();
        if (localINfcTag.isNdef(i))
        {
          i = localINfcTag.ndefWrite(i, paramNdefMessage);
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
            paramNdefMessage = new android/nfc/FormatException;
            paramNdefMessage.<init>();
            throw paramNdefMessage;
          }
        }
        else
        {
          paramNdefMessage = new java/io/IOException;
          paramNdefMessage.<init>("Tag is not ndef");
          throw paramNdefMessage;
        }
      }
      else
      {
        paramNdefMessage = new java/io/IOException;
        paramNdefMessage.<init>("Mock tags don't support this operation.");
        throw paramNdefMessage;
      }
    }
    catch (RemoteException paramNdefMessage)
    {
      Log.e("NFC", "NFC service dead", paramNdefMessage);
    }
  }
}
