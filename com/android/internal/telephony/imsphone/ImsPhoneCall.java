package com.android.internal.telephony.imsphone;

import android.telecom.ConferenceParticipant;
import android.telephony.Rlog;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsStreamMediaProfile;
import com.android.ims.ImsCall;
import com.android.ims.ImsException;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImsPhoneCall
  extends Call
{
  public static final String CONTEXT_BACKGROUND = "BG";
  public static final String CONTEXT_FOREGROUND = "FG";
  public static final String CONTEXT_HANDOVER = "HO";
  public static final String CONTEXT_RINGING = "RG";
  public static final String CONTEXT_UNKNOWN = "UK";
  private static final boolean DBG = Rlog.isLoggable("ImsPhoneCall", 3);
  private static final boolean FORCE_DEBUG = false;
  private static final String LOG_TAG = "ImsPhoneCall";
  private static final boolean VDBG = Rlog.isLoggable("ImsPhoneCall", 2);
  private final String mCallContext;
  ImsPhoneCallTracker mOwner;
  private boolean mRingbackTonePlayed = false;
  
  ImsPhoneCall()
  {
    mCallContext = "UK";
  }
  
  public ImsPhoneCall(ImsPhoneCallTracker paramImsPhoneCallTracker, String paramString)
  {
    mOwner = paramImsPhoneCallTracker;
    mCallContext = paramString;
  }
  
  static boolean isLocalTone(ImsCall paramImsCall)
  {
    boolean bool = false;
    if ((paramImsCall != null) && (paramImsCall.getCallProfile() != null) && (getCallProfilemMediaProfile != null))
    {
      if (getCallProfilemMediaProfile.mAudioDirection == 0) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  private void takeOver(ImsPhoneCall paramImsPhoneCall)
  {
    mConnections = mConnections;
    mState = mState;
    paramImsPhoneCall = mConnections.iterator();
    while (paramImsPhoneCall.hasNext()) {
      ((ImsPhoneConnection)paramImsPhoneCall.next()).changeParent(this);
    }
  }
  
  public void attach(Connection paramConnection)
  {
    if (VDBG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("attach : ");
      localStringBuilder.append(mCallContext);
      localStringBuilder.append(" conn = ");
      localStringBuilder.append(paramConnection);
      Rlog.v("ImsPhoneCall", localStringBuilder.toString());
    }
    clearDisconnected();
    mConnections.add(paramConnection);
    mOwner.logState();
  }
  
  public void attach(Connection paramConnection, Call.State paramState)
  {
    if (VDBG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("attach : ");
      localStringBuilder.append(mCallContext);
      localStringBuilder.append(" state = ");
      localStringBuilder.append(paramState.toString());
      Rlog.v("ImsPhoneCall", localStringBuilder.toString());
    }
    attach(paramConnection);
    mState = paramState;
  }
  
  public void attachFake(Connection paramConnection, Call.State paramState)
  {
    attach(paramConnection, paramState);
  }
  
  public boolean connectionDisconnected(ImsPhoneConnection paramImsPhoneConnection)
  {
    if (mState != Call.State.DISCONNECTED)
    {
      int i = 1;
      int j = 0;
      int k = mConnections.size();
      int m;
      for (;;)
      {
        m = i;
        if (j >= k) {
          break;
        }
        if (((Connection)mConnections.get(j)).getState() != Call.State.DISCONNECTED)
        {
          m = 0;
          break;
        }
        j++;
      }
      if (m != 0)
      {
        mState = Call.State.DISCONNECTED;
        if (VDBG)
        {
          paramImsPhoneConnection = new StringBuilder();
          paramImsPhoneConnection.append("connectionDisconnected : ");
          paramImsPhoneConnection.append(mCallContext);
          paramImsPhoneConnection.append(" state = ");
          paramImsPhoneConnection.append(mState);
          Rlog.v("ImsPhoneCall", paramImsPhoneConnection.toString());
        }
        return true;
      }
    }
    return false;
  }
  
  public void detach(ImsPhoneConnection paramImsPhoneConnection)
  {
    if (VDBG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("detach : ");
      localStringBuilder.append(mCallContext);
      localStringBuilder.append(" conn = ");
      localStringBuilder.append(paramImsPhoneConnection);
      Rlog.v("ImsPhoneCall", localStringBuilder.toString());
    }
    mConnections.remove(paramImsPhoneConnection);
    clearDisconnected();
    mOwner.logState();
  }
  
  /* Error */
  public void dispose()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 55	com/android/internal/telephony/imsphone/ImsPhoneCall:mOwner	Lcom/android/internal/telephony/imsphone/ImsPhoneCallTracker;
    //   4: aload_0
    //   5: invokevirtual 187	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:hangup	(Lcom/android/internal/telephony/imsphone/ImsPhoneCall;)V
    //   8: iconst_0
    //   9: istore_1
    //   10: aload_0
    //   11: getfield 81	com/android/internal/telephony/imsphone/ImsPhoneCall:mConnections	Ljava/util/ArrayList;
    //   14: invokevirtual 164	java/util/ArrayList:size	()I
    //   17: istore_2
    //   18: iload_1
    //   19: iload_2
    //   20: if_icmpge +106 -> 126
    //   23: aload_0
    //   24: getfield 81	com/android/internal/telephony/imsphone/ImsPhoneCall:mConnections	Ljava/util/ArrayList;
    //   27: iload_1
    //   28: invokevirtual 168	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   31: checkcast 105	com/android/internal/telephony/imsphone/ImsPhoneConnection
    //   34: bipush 14
    //   36: invokevirtual 191	com/android/internal/telephony/imsphone/ImsPhoneConnection:onDisconnect	(I)Z
    //   39: pop
    //   40: iinc 1 1
    //   43: goto -25 -> 18
    //   46: astore_3
    //   47: iconst_0
    //   48: istore_1
    //   49: aload_0
    //   50: getfield 81	com/android/internal/telephony/imsphone/ImsPhoneCall:mConnections	Ljava/util/ArrayList;
    //   53: invokevirtual 164	java/util/ArrayList:size	()I
    //   56: istore_2
    //   57: iload_1
    //   58: iload_2
    //   59: if_icmpge +26 -> 85
    //   62: aload_0
    //   63: getfield 81	com/android/internal/telephony/imsphone/ImsPhoneCall:mConnections	Ljava/util/ArrayList;
    //   66: iload_1
    //   67: invokevirtual 168	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   70: checkcast 105	com/android/internal/telephony/imsphone/ImsPhoneConnection
    //   73: bipush 14
    //   75: invokevirtual 191	com/android/internal/telephony/imsphone/ImsPhoneConnection:onDisconnect	(I)Z
    //   78: pop
    //   79: iinc 1 1
    //   82: goto -25 -> 57
    //   85: aload_3
    //   86: athrow
    //   87: astore_3
    //   88: iconst_0
    //   89: istore_1
    //   90: aload_0
    //   91: getfield 81	com/android/internal/telephony/imsphone/ImsPhoneCall:mConnections	Ljava/util/ArrayList;
    //   94: invokevirtual 164	java/util/ArrayList:size	()I
    //   97: istore_2
    //   98: iload_1
    //   99: iload_2
    //   100: if_icmpge +26 -> 126
    //   103: aload_0
    //   104: getfield 81	com/android/internal/telephony/imsphone/ImsPhoneCall:mConnections	Ljava/util/ArrayList;
    //   107: iload_1
    //   108: invokevirtual 168	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   111: checkcast 105	com/android/internal/telephony/imsphone/ImsPhoneConnection
    //   114: bipush 14
    //   116: invokevirtual 191	com/android/internal/telephony/imsphone/ImsPhoneConnection:onDisconnect	(I)Z
    //   119: pop
    //   120: iinc 1 1
    //   123: goto -25 -> 98
    //   126: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	127	0	this	ImsPhoneCall
    //   9	112	1	i	int
    //   17	84	2	j	int
    //   46	40	3	localObject	Object
    //   87	1	3	localCallStateException	CallStateException
    // Exception table:
    //   from	to	target	type
    //   0	8	46	finally
    //   0	8	87	com/android/internal/telephony/CallStateException
  }
  
  public List<ConferenceParticipant> getConferenceParticipants()
  {
    ImsCall localImsCall = getImsCall();
    if (localImsCall == null) {
      return null;
    }
    return localImsCall.getConferenceParticipants();
  }
  
  public List<Connection> getConnections()
  {
    return mConnections;
  }
  
  @VisibleForTesting
  public ImsPhoneConnection getFirstConnection()
  {
    if (mConnections.size() == 0) {
      return null;
    }
    return (ImsPhoneConnection)mConnections.get(0);
  }
  
  ImsPhoneConnection getHandoverConnection()
  {
    return (ImsPhoneConnection)getEarliestConnection();
  }
  
  @VisibleForTesting
  public ImsCall getImsCall()
  {
    ImsCall localImsCall;
    if (getFirstConnection() == null) {
      localImsCall = null;
    } else {
      localImsCall = getFirstConnection().getImsCall();
    }
    return localImsCall;
  }
  
  public Phone getPhone()
  {
    return mOwner.mPhone;
  }
  
  public void hangup()
    throws CallStateException
  {
    mOwner.hangup(this);
  }
  
  boolean isFull()
  {
    boolean bool;
    if (mConnections.size() == 5) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isMultiparty()
  {
    ImsCall localImsCall = getImsCall();
    if (localImsCall == null) {
      return false;
    }
    return localImsCall.isMultiparty();
  }
  
  void merge(ImsPhoneCall paramImsPhoneCall, Call.State paramState)
  {
    Object localObject = getFirstConnection();
    if (localObject != null)
    {
      long l = ((ImsPhoneConnection)localObject).getConferenceConnectTime();
      if (l > 0L)
      {
        ((ImsPhoneConnection)localObject).setConnectTime(l);
        ((ImsPhoneConnection)localObject).setConnectTimeReal(((ImsPhoneConnection)localObject).getConnectTimeReal());
      }
      else if (DBG)
      {
        Rlog.d("ImsPhoneCall", "merge: conference connect time is 0");
      }
    }
    if (DBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("merge(");
      ((StringBuilder)localObject).append(mCallContext);
      ((StringBuilder)localObject).append("): ");
      ((StringBuilder)localObject).append(paramImsPhoneCall);
      ((StringBuilder)localObject).append("state = ");
      ((StringBuilder)localObject).append(paramState);
      Rlog.d("ImsPhoneCall", ((StringBuilder)localObject).toString());
    }
  }
  
  void onHangupLocal()
  {
    int i = 0;
    int j = mConnections.size();
    while (i < j)
    {
      ((ImsPhoneConnection)mConnections.get(i)).onHangupLocal();
      i++;
    }
    mState = Call.State.DISCONNECTING;
    if (VDBG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onHangupLocal : ");
      localStringBuilder.append(mCallContext);
      localStringBuilder.append(" state = ");
      localStringBuilder.append(mState);
      Rlog.v("ImsPhoneCall", localStringBuilder.toString());
    }
  }
  
  void setMute(boolean paramBoolean)
  {
    Object localObject;
    if (getFirstConnection() == null) {
      localObject = null;
    } else {
      localObject = getFirstConnection().getImsCall();
    }
    if (localObject != null) {
      try
      {
        ((ImsCall)localObject).setMute(paramBoolean);
      }
      catch (ImsException localImsException)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("setMute failed : ");
        ((StringBuilder)localObject).append(localImsException.getMessage());
        Rlog.e("ImsPhoneCall", ((StringBuilder)localObject).toString());
      }
    }
  }
  
  public void switchWith(ImsPhoneCall paramImsPhoneCall)
  {
    Object localObject;
    if (VDBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("switchWith : switchCall = ");
      ((StringBuilder)localObject).append(this);
      ((StringBuilder)localObject).append(" withCall = ");
      ((StringBuilder)localObject).append(paramImsPhoneCall);
      Rlog.v("ImsPhoneCall", ((StringBuilder)localObject).toString());
    }
    try
    {
      localObject = new com/android/internal/telephony/imsphone/ImsPhoneCall;
      ((ImsPhoneCall)localObject).<init>();
      ((ImsPhoneCall)localObject).takeOver(this);
      takeOver(paramImsPhoneCall);
      paramImsPhoneCall.takeOver((ImsPhoneCall)localObject);
      mOwner.logState();
      return;
    }
    finally {}
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ImsPhoneCall ");
    localStringBuilder.append(mCallContext);
    localStringBuilder.append(" state: ");
    localStringBuilder.append(mState.toString());
    localStringBuilder.append(" ");
    if (mConnections.size() > 1) {
      localStringBuilder.append(" ERROR_MULTIPLE ");
    }
    Iterator localIterator = mConnections.iterator();
    while (localIterator.hasNext())
    {
      localStringBuilder.append((Connection)localIterator.next());
      localStringBuilder.append(" ");
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public boolean update(ImsPhoneConnection paramImsPhoneConnection, ImsCall paramImsCall, Call.State paramState)
  {
    boolean bool = false;
    paramImsPhoneConnection = mState;
    if (paramState == Call.State.ALERTING)
    {
      if ((mRingbackTonePlayed) && (!isLocalTone(paramImsCall)))
      {
        mOwner.mPhone.stopRingbackTone();
        mRingbackTonePlayed = false;
      }
      else if ((!mRingbackTonePlayed) && (isLocalTone(paramImsCall)))
      {
        mOwner.mPhone.startRingbackTone();
        mRingbackTonePlayed = true;
      }
    }
    else if (mRingbackTonePlayed)
    {
      mOwner.mPhone.stopRingbackTone();
      mRingbackTonePlayed = false;
    }
    if ((paramState != mState) && (paramState != Call.State.DISCONNECTED))
    {
      mState = paramState;
      bool = true;
    }
    else if (paramState == Call.State.DISCONNECTED)
    {
      bool = true;
    }
    if (VDBG)
    {
      paramImsCall = new StringBuilder();
      paramImsCall.append("update : ");
      paramImsCall.append(mCallContext);
      paramImsCall.append(" state: ");
      paramImsCall.append(paramImsPhoneConnection);
      paramImsCall.append(" --> ");
      paramImsCall.append(mState);
      Rlog.v("ImsPhoneCall", paramImsCall.toString());
    }
    return bool;
  }
}
