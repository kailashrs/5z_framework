package com.android.internal.telephony.uicc;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class UiccPkcs15
  extends Handler
{
  private static final String CARRIER_RULE_AID = "FFFFFFFFFFFF";
  private static final boolean DBG = true;
  private static final int EVENT_CLOSE_LOGICAL_CHANNEL_DONE = 7;
  private static final int EVENT_LOAD_ACCF_DONE = 6;
  private static final int EVENT_LOAD_ACMF_DONE = 4;
  private static final int EVENT_LOAD_ACRF_DONE = 5;
  private static final int EVENT_LOAD_DODF_DONE = 3;
  private static final int EVENT_LOAD_ODF_DONE = 2;
  private static final int EVENT_SELECT_PKCS15_DONE = 1;
  private static final String ID_ACRF = "4300";
  private static final String LOG_TAG = "UiccPkcs15";
  private static final String TAG_ASN_OCTET_STRING = "04";
  private static final String TAG_ASN_SEQUENCE = "30";
  private static final String TAG_TARGET_AID = "A0";
  private int mChannelId = -1;
  private FileHandler mFh;
  private Message mLoadedCallback;
  private Pkcs15Selector mPkcs15Selector;
  private List<String> mRules = new ArrayList();
  private UiccProfile mUiccProfile;
  
  public UiccPkcs15(UiccProfile paramUiccProfile, Message paramMessage)
  {
    log("Creating UiccPkcs15");
    mUiccProfile = paramUiccProfile;
    mLoadedCallback = paramMessage;
    mPkcs15Selector = new Pkcs15Selector(obtainMessage(1));
  }
  
  private void cleanUp()
  {
    log("cleanUp");
    if (mChannelId >= 0)
    {
      mUiccProfile.iccCloseLogicalChannel(mChannelId, obtainMessage(7));
      mChannelId = -1;
    }
    mLoadedCallback.sendToTarget();
  }
  
  private static void log(String paramString)
  {
    Rlog.d("UiccPkcs15", paramString);
  }
  
  private void parseAccf(String paramString)
  {
    for (;;)
    {
      if (!paramString.isEmpty())
      {
        UiccCarrierPrivilegeRules.TLV localTLV1 = new UiccCarrierPrivilegeRules.TLV("30");
        UiccCarrierPrivilegeRules.TLV localTLV2 = new UiccCarrierPrivilegeRules.TLV("04");
        try
        {
          paramString = localTLV1.parse(paramString, false);
          localTLV2.parse(localTLV1.getValue(), true);
          if (!localTLV2.getValue().isEmpty()) {
            mRules.add(localTLV2.getValue());
          }
        }
        catch (IllegalArgumentException|IndexOutOfBoundsException localIllegalArgumentException)
        {
          paramString = new StringBuilder();
          paramString.append("Error: ");
          paramString.append(localIllegalArgumentException);
          log(paramString.toString());
        }
      }
    }
  }
  
  private String parseAcrf(String paramString)
  {
    Object localObject = null;
    String str1 = paramString;
    paramString = (String)localObject;
    for (;;)
    {
      if (!str1.isEmpty())
      {
        localObject = new UiccCarrierPrivilegeRules.TLV("30");
        try
        {
          str1 = ((UiccCarrierPrivilegeRules.TLV)localObject).parse(str1, false);
          String str2 = ((UiccCarrierPrivilegeRules.TLV)localObject).getValue();
          localObject = paramString;
          if (str2.startsWith("A0"))
          {
            localObject = new com/android/internal/telephony/uicc/UiccCarrierPrivilegeRules$TLV;
            ((UiccCarrierPrivilegeRules.TLV)localObject).<init>("A0");
            UiccCarrierPrivilegeRules.TLV localTLV1 = new com/android/internal/telephony/uicc/UiccCarrierPrivilegeRules$TLV;
            localTLV1.<init>("04");
            UiccCarrierPrivilegeRules.TLV localTLV2 = new com/android/internal/telephony/uicc/UiccCarrierPrivilegeRules$TLV;
            localTLV2.<init>("30");
            UiccCarrierPrivilegeRules.TLV localTLV3 = new com/android/internal/telephony/uicc/UiccCarrierPrivilegeRules$TLV;
            localTLV3.<init>("04");
            str2 = ((UiccCarrierPrivilegeRules.TLV)localObject).parse(str2, false);
            localTLV1.parse(((UiccCarrierPrivilegeRules.TLV)localObject).getValue(), true);
            localObject = paramString;
            if ("FFFFFFFFFFFF".equals(localTLV1.getValue()))
            {
              localTLV2.parse(str2, true);
              localTLV3.parse(localTLV2.getValue(), true);
              localObject = localTLV3.getValue();
            }
          }
          paramString = (String)localObject;
        }
        catch (IllegalArgumentException|IndexOutOfBoundsException localIllegalArgumentException)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Error: ");
          ((StringBuilder)localObject).append(localIllegalArgumentException);
          log(((StringBuilder)localObject).toString());
        }
      }
    }
    return paramString;
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    if (mRules != null)
    {
      paramPrintWriter.println(" mRules:");
      Iterator localIterator = mRules.iterator();
      while (localIterator.hasNext())
      {
        paramFileDescriptor = (String)localIterator.next();
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("  ");
        paramArrayOfString.append(paramFileDescriptor);
        paramPrintWriter.println(paramArrayOfString.toString());
      }
    }
  }
  
  public List<String> getRules()
  {
    return mRules;
  }
  
  public void handleMessage(Message paramMessage)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("handleMessage: ");
    ((StringBuilder)localObject).append(what);
    log(((StringBuilder)localObject).toString());
    localObject = (AsyncResult)obj;
    int i = what;
    if (i != 1)
    {
      switch (i)
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unknown event ");
        ((StringBuilder)localObject).append(what);
        Rlog.e("UiccPkcs15", ((StringBuilder)localObject).toString());
        break;
      case 7: 
        break;
      case 6: 
        if ((exception == null) && (result != null)) {
          parseAccf((String)result);
        }
        cleanUp();
        break;
      case 5: 
        if ((exception == null) && (result != null))
        {
          paramMessage = parseAcrf((String)result);
          if (!mFh.loadFile(paramMessage, obtainMessage(6))) {
            cleanUp();
          }
        }
        else
        {
          cleanUp();
        }
        break;
      }
    }
    else if (exception == null)
    {
      mFh = new FileHandler((String)result);
      if (!mFh.loadFile("4300", obtainMessage(5))) {
        cleanUp();
      }
    }
    else
    {
      paramMessage = new StringBuilder();
      paramMessage.append("select pkcs15 failed: ");
      paramMessage.append(exception);
      log(paramMessage.toString());
      mLoadedCallback.sendToTarget();
    }
  }
  
  private class FileHandler
    extends Handler
  {
    protected static final int EVENT_READ_BINARY_DONE = 102;
    protected static final int EVENT_SELECT_FILE_DONE = 101;
    private Message mCallback;
    private String mFileId;
    private final String mPkcs15Path;
    
    public FileHandler(String paramString)
    {
      this$1 = new StringBuilder();
      append("Creating FileHandler, pkcs15Path: ");
      append(paramString);
      UiccPkcs15.log(toString());
      mPkcs15Path = paramString;
    }
    
    private void readBinary()
    {
      if (mChannelId >= 0) {
        mUiccProfile.iccTransmitApduLogicalChannel(mChannelId, 0, 176, 0, 0, 0, "", obtainMessage(102));
      } else {
        UiccPkcs15.log("EF based");
      }
    }
    
    private void selectFile()
    {
      if (mChannelId >= 0) {
        mUiccProfile.iccTransmitApduLogicalChannel(mChannelId, 0, 164, 0, 4, 2, mFileId, obtainMessage(101));
      } else {
        UiccPkcs15.log("EF based");
      }
    }
    
    public void handleMessage(Message paramMessage)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleMessage: ");
      localStringBuilder.append(what);
      UiccPkcs15.log(localStringBuilder.toString());
      Object localObject1 = (AsyncResult)obj;
      Object localObject2 = exception;
      localStringBuilder = null;
      if ((localObject2 == null) && (result != null))
      {
        switch (what)
        {
        default: 
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown event");
          localStringBuilder.append(what);
          UiccPkcs15.log(localStringBuilder.toString());
          break;
        case 102: 
          localObject2 = (IccIoResult)result;
          localObject1 = IccUtils.bytesToHexString(payload).toUpperCase(Locale.US);
          paramMessage = new StringBuilder();
          paramMessage.append("IccIoResult: ");
          paramMessage.append(localObject2);
          paramMessage.append(" payload: ");
          paramMessage.append((String)localObject1);
          UiccPkcs15.log(paramMessage.toString());
          localObject2 = mCallback;
          paramMessage = localStringBuilder;
          if (localObject1 == null)
          {
            paramMessage = new StringBuilder();
            paramMessage.append("Error: null response for ");
            paramMessage.append(mFileId);
            paramMessage = new IccException(paramMessage.toString());
          }
          AsyncResult.forMessage((Message)localObject2, localObject1, paramMessage);
          mCallback.sendToTarget();
          break;
        case 101: 
          readBinary();
        }
        return;
      }
      paramMessage = new StringBuilder();
      paramMessage.append("Error: ");
      paramMessage.append(exception);
      UiccPkcs15.log(paramMessage.toString());
      AsyncResult.forMessage(mCallback, null, exception);
      mCallback.sendToTarget();
    }
    
    public boolean loadFile(String paramString, Message paramMessage)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("loadFile: ");
      localStringBuilder.append(paramString);
      UiccPkcs15.log(localStringBuilder.toString());
      if ((paramString != null) && (paramMessage != null))
      {
        mFileId = paramString;
        mCallback = paramMessage;
        selectFile();
        return true;
      }
      return false;
    }
  }
  
  private class Pkcs15Selector
    extends Handler
  {
    private static final int EVENT_OPEN_LOGICAL_CHANNEL_DONE = 201;
    private static final String PKCS15_AID = "A000000063504B43532D3135";
    private Message mCallback;
    
    public Pkcs15Selector(Message paramMessage)
    {
      mCallback = paramMessage;
      mUiccProfile.iccOpenLogicalChannel("A000000063504B43532D3135", 4, obtainMessage(201));
    }
    
    public void handleMessage(Message paramMessage)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("handleMessage: ");
      ((StringBuilder)localObject).append(what);
      UiccPkcs15.log(((StringBuilder)localObject).toString());
      if (what != 201)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unknown event");
        ((StringBuilder)localObject).append(what);
        UiccPkcs15.log(((StringBuilder)localObject).toString());
      }
      else
      {
        localObject = (AsyncResult)obj;
        if ((exception == null) && (result != null))
        {
          UiccPkcs15.access$102(UiccPkcs15.this, ((int[])result)[0]);
          paramMessage = new StringBuilder();
          paramMessage.append("mChannelId: ");
          paramMessage.append(mChannelId);
          UiccPkcs15.log(paramMessage.toString());
          AsyncResult.forMessage(mCallback, null, null);
        }
        else
        {
          paramMessage = new StringBuilder();
          paramMessage.append("error: ");
          paramMessage.append(exception);
          UiccPkcs15.log(paramMessage.toString());
          AsyncResult.forMessage(mCallback, null, exception);
        }
        mCallback.sendToTarget();
      }
    }
  }
}
