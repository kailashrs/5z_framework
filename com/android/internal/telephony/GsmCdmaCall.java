package com.android.internal.telephony;

import java.util.ArrayList;
import java.util.List;

public class GsmCdmaCall
  extends Call
{
  GsmCdmaCallTracker mOwner;
  
  public GsmCdmaCall(GsmCdmaCallTracker paramGsmCdmaCallTracker)
  {
    mOwner = paramGsmCdmaCallTracker;
  }
  
  public void attach(Connection paramConnection, DriverCall paramDriverCall)
  {
    mConnections.add(paramConnection);
    mState = stateFromDCState(state);
  }
  
  public void attachFake(Connection paramConnection, Call.State paramState)
  {
    mConnections.add(paramConnection);
    mState = paramState;
  }
  
  public boolean connectionDisconnected(GsmCdmaConnection paramGsmCdmaConnection)
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
        return true;
      }
    }
    return false;
  }
  
  public void detach(GsmCdmaConnection paramGsmCdmaConnection)
  {
    mConnections.remove(paramGsmCdmaConnection);
    if (mConnections.size() == 0) {
      mState = Call.State.IDLE;
    }
  }
  
  public List<Connection> getConnections()
  {
    return mConnections;
  }
  
  public Phone getPhone()
  {
    return mOwner.getPhone();
  }
  
  public void hangup()
    throws CallStateException
  {
    mOwner.hangup(this);
  }
  
  boolean isFull()
  {
    boolean bool;
    if (mConnections.size() == mOwner.getMaxConnectionsPerCall()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isMultiparty()
  {
    int i = mConnections.size();
    boolean bool = true;
    if (i <= 1) {
      bool = false;
    }
    return bool;
  }
  
  void onHangupLocal()
  {
    int i = 0;
    int j = mConnections.size();
    while (i < j)
    {
      ((GsmCdmaConnection)mConnections.get(i)).onHangupLocal();
      i++;
    }
    mState = Call.State.DISCONNECTING;
  }
  
  public String toString()
  {
    return mState.toString();
  }
  
  boolean update(GsmCdmaConnection paramGsmCdmaConnection, DriverCall paramDriverCall)
  {
    boolean bool = false;
    paramGsmCdmaConnection = stateFromDCState(state);
    if (paramGsmCdmaConnection != mState)
    {
      mState = paramGsmCdmaConnection;
      bool = true;
    }
    return bool;
  }
}
