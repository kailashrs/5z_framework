package com.android.internal.telephony.imsphone;

import android.content.Context;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.UUSInfo;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ImsExternalConnection
  extends Connection
{
  private static final String CONFERENCE_PREFIX = "conf";
  private ImsExternalCall mCall;
  private int mCallId;
  private final Context mContext;
  private boolean mIsPullable;
  private final Set<Listener> mListeners = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9F, 1));
  private Uri mOriginalAddress;
  
  protected ImsExternalConnection(Phone paramPhone, int paramInt, Uri paramUri, boolean paramBoolean)
  {
    super(paramPhone.getPhoneType());
    mContext = paramPhone.getContext();
    mCall = new ImsExternalCall(paramPhone, this);
    mCallId = paramInt;
    setExternalConnectionAddress(paramUri);
    mNumberPresentation = 1;
    mIsPullable = paramBoolean;
    rebuildCapabilities();
    setActive();
  }
  
  private void rebuildCapabilities()
  {
    int i = 16;
    if (mIsPullable) {
      i = 0x10 | 0x20;
    }
    setConnectionCapabilities(i);
  }
  
  public void addListener(Listener paramListener)
  {
    mListeners.add(paramListener);
  }
  
  public void cancelPostDial() {}
  
  public void deflect(String paramString)
    throws CallStateException
  {
    throw new CallStateException("Deflect is not supported for external calls");
  }
  
  public Call getCall()
  {
    return mCall;
  }
  
  public int getCallId()
  {
    return mCallId;
  }
  
  public long getDisconnectTime()
  {
    return 0L;
  }
  
  public long getHoldDurationMillis()
  {
    return 0L;
  }
  
  public int getNumberPresentation()
  {
    return mNumberPresentation;
  }
  
  public int getPreciseDisconnectCause()
  {
    return 0;
  }
  
  public UUSInfo getUUSInfo()
  {
    return null;
  }
  
  public String getVendorDisconnectCause()
  {
    return null;
  }
  
  public void hangup()
    throws CallStateException
  {}
  
  public boolean isMultiparty()
  {
    return false;
  }
  
  public void proceedAfterWaitChar() {}
  
  public void proceedAfterWildChar(String paramString) {}
  
  public void pullExternalCall()
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onPullExternalCall(this);
    }
  }
  
  public void removeListener(Listener paramListener)
  {
    mListeners.remove(paramListener);
  }
  
  public void separate()
    throws CallStateException
  {}
  
  public void setActive()
  {
    if (mCall == null) {
      return;
    }
    mCall.setActive();
  }
  
  public void setExternalConnectionAddress(Uri paramUri)
  {
    mOriginalAddress = paramUri;
    if (("sip".equals(paramUri.getScheme())) && (paramUri.getSchemeSpecificPart().startsWith("conf")))
    {
      mCnapName = mContext.getString(17039665);
      mCnapNamePresentation = 1;
      mAddress = "";
      mNumberPresentation = 2;
      return;
    }
    mAddress = PhoneNumberUtils.convertSipUriToTelUri(paramUri).getSchemeSpecificPart();
  }
  
  public void setIsPullable(boolean paramBoolean)
  {
    mIsPullable = paramBoolean;
    rebuildCapabilities();
  }
  
  public void setTerminated()
  {
    if (mCall == null) {
      return;
    }
    mCall.setTerminated();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("[ImsExternalConnection dialogCallId:");
    localStringBuilder.append(mCallId);
    localStringBuilder.append(" state:");
    if (mCall.getState() == Call.State.ACTIVE) {
      localStringBuilder.append("Active");
    } else if (mCall.getState() == Call.State.DISCONNECTED) {
      localStringBuilder.append("Disconnected");
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public static abstract interface Listener
  {
    public abstract void onPullExternalCall(ImsExternalConnection paramImsExternalConnection);
  }
}
