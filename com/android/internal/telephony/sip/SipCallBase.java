package com.android.internal.telephony.sip;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.Connection;
import java.util.ArrayList;
import java.util.List;

abstract class SipCallBase
  extends Call
{
  SipCallBase() {}
  
  public List<Connection> getConnections()
  {
    return mConnections;
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
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mState.toString());
    localStringBuilder.append(":");
    localStringBuilder.append(super.toString());
    return localStringBuilder.toString();
  }
}
