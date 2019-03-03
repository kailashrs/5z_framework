package com.android.internal.telephony.imsphone;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import java.util.ArrayList;
import java.util.List;

public class ImsExternalCall
  extends Call
{
  private Phone mPhone;
  
  public ImsExternalCall(Phone paramPhone, ImsExternalConnection paramImsExternalConnection)
  {
    mPhone = paramPhone;
    mConnections.add(paramImsExternalConnection);
  }
  
  public List<Connection> getConnections()
  {
    return mConnections;
  }
  
  public Phone getPhone()
  {
    return mPhone;
  }
  
  public void hangup()
    throws CallStateException
  {}
  
  public boolean isMultiparty()
  {
    return false;
  }
  
  public void setActive()
  {
    setState(Call.State.ACTIVE);
  }
  
  public void setTerminated()
  {
    setState(Call.State.DISCONNECTED);
  }
}
