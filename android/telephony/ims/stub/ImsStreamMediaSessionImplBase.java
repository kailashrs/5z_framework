package android.telephony.ims.stub;

import android.os.RemoteException;
import com.android.ims.internal.IImsStreamMediaSession.Stub;

public class ImsStreamMediaSessionImplBase
  extends IImsStreamMediaSession.Stub
{
  public ImsStreamMediaSessionImplBase() {}
  
  public void close()
    throws RemoteException
  {}
}
