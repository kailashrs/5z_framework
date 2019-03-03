package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.ims.ImsUtListener;
import com.android.ims.internal.IImsUt;
import com.android.ims.internal.IImsUt.Stub;
import com.android.ims.internal.IImsUtListener;

@SystemApi
public class ImsUtImplBase
{
  private IImsUt.Stub mServiceImpl = new IImsUt.Stub()
  {
    public void close()
      throws RemoteException
    {
      ImsUtImplBase.this.close();
    }
    
    public int queryCFForServiceClass(int paramAnonymousInt1, String paramAnonymousString, int paramAnonymousInt2)
      throws RemoteException
    {
      return ImsUtImplBase.this.queryCFForServiceClass(paramAnonymousInt1, paramAnonymousString, paramAnonymousInt2);
    }
    
    public int queryCLIP()
      throws RemoteException
    {
      return ImsUtImplBase.this.queryCLIP();
    }
    
    public int queryCLIR()
      throws RemoteException
    {
      return ImsUtImplBase.this.queryCLIR();
    }
    
    public int queryCOLP()
      throws RemoteException
    {
      return ImsUtImplBase.this.queryCOLP();
    }
    
    public int queryCOLR()
      throws RemoteException
    {
      return ImsUtImplBase.this.queryCOLR();
    }
    
    public int queryCallBarring(int paramAnonymousInt)
      throws RemoteException
    {
      return ImsUtImplBase.this.queryCallBarring(paramAnonymousInt);
    }
    
    public int queryCallBarringForServiceClass(int paramAnonymousInt1, int paramAnonymousInt2)
      throws RemoteException
    {
      return ImsUtImplBase.this.queryCallBarringForServiceClass(paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public int queryCallForward(int paramAnonymousInt, String paramAnonymousString)
      throws RemoteException
    {
      return ImsUtImplBase.this.queryCallForward(paramAnonymousInt, paramAnonymousString);
    }
    
    public int queryCallWaiting()
      throws RemoteException
    {
      return ImsUtImplBase.this.queryCallWaiting();
    }
    
    public void setListener(IImsUtListener paramAnonymousIImsUtListener)
      throws RemoteException
    {
      setListener(new ImsUtListener(paramAnonymousIImsUtListener));
    }
    
    public int transact(Bundle paramAnonymousBundle)
      throws RemoteException
    {
      return ImsUtImplBase.this.transact(paramAnonymousBundle);
    }
    
    public int updateCLIP(boolean paramAnonymousBoolean)
      throws RemoteException
    {
      return ImsUtImplBase.this.updateCLIP(paramAnonymousBoolean);
    }
    
    public int updateCLIR(int paramAnonymousInt)
      throws RemoteException
    {
      return ImsUtImplBase.this.updateCLIR(paramAnonymousInt);
    }
    
    public int updateCOLP(boolean paramAnonymousBoolean)
      throws RemoteException
    {
      return ImsUtImplBase.this.updateCOLP(paramAnonymousBoolean);
    }
    
    public int updateCOLR(int paramAnonymousInt)
      throws RemoteException
    {
      return ImsUtImplBase.this.updateCOLR(paramAnonymousInt);
    }
    
    public int updateCallBarring(int paramAnonymousInt1, int paramAnonymousInt2, String[] paramAnonymousArrayOfString)
      throws RemoteException
    {
      return ImsUtImplBase.this.updateCallBarring(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousArrayOfString);
    }
    
    public int updateCallBarringForServiceClass(int paramAnonymousInt1, int paramAnonymousInt2, String[] paramAnonymousArrayOfString, int paramAnonymousInt3)
      throws RemoteException
    {
      return ImsUtImplBase.this.updateCallBarringForServiceClass(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousArrayOfString, paramAnonymousInt3);
    }
    
    public int updateCallForward(int paramAnonymousInt1, int paramAnonymousInt2, String paramAnonymousString, int paramAnonymousInt3, int paramAnonymousInt4)
      throws RemoteException
    {
      return ImsUtImplBase.this.updateCallForward(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousString, paramAnonymousInt3, paramAnonymousInt4);
    }
    
    public int updateCallWaiting(boolean paramAnonymousBoolean, int paramAnonymousInt)
      throws RemoteException
    {
      return ImsUtImplBase.this.updateCallWaiting(paramAnonymousBoolean, paramAnonymousInt);
    }
  };
  
  public ImsUtImplBase() {}
  
  public void close() {}
  
  public IImsUt getInterface()
  {
    return mServiceImpl;
  }
  
  public int queryCFForServiceClass(int paramInt1, String paramString, int paramInt2)
  {
    return -1;
  }
  
  public int queryCLIP()
  {
    return queryClip();
  }
  
  public int queryCLIR()
  {
    return queryClir();
  }
  
  public int queryCOLP()
  {
    return queryColp();
  }
  
  public int queryCOLR()
  {
    return queryColr();
  }
  
  public int queryCallBarring(int paramInt)
  {
    return -1;
  }
  
  public int queryCallBarringForServiceClass(int paramInt1, int paramInt2)
  {
    return -1;
  }
  
  public int queryCallForward(int paramInt, String paramString)
  {
    return -1;
  }
  
  public int queryCallWaiting()
  {
    return -1;
  }
  
  public int queryClip()
  {
    return -1;
  }
  
  public int queryClir()
  {
    return -1;
  }
  
  public int queryColp()
  {
    return -1;
  }
  
  public int queryColr()
  {
    return -1;
  }
  
  public void setListener(ImsUtListener paramImsUtListener) {}
  
  public int transact(Bundle paramBundle)
  {
    return -1;
  }
  
  public int updateCLIP(boolean paramBoolean)
  {
    return updateClip(paramBoolean);
  }
  
  public int updateCLIR(int paramInt)
  {
    return updateClir(paramInt);
  }
  
  public int updateCOLP(boolean paramBoolean)
  {
    return updateColp(paramBoolean);
  }
  
  public int updateCOLR(int paramInt)
  {
    return updateColr(paramInt);
  }
  
  public int updateCallBarring(int paramInt1, int paramInt2, String[] paramArrayOfString)
  {
    return -1;
  }
  
  public int updateCallBarringForServiceClass(int paramInt1, int paramInt2, String[] paramArrayOfString, int paramInt3)
  {
    return -1;
  }
  
  public int updateCallForward(int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4)
  {
    return 0;
  }
  
  public int updateCallWaiting(boolean paramBoolean, int paramInt)
  {
    return -1;
  }
  
  public int updateClip(boolean paramBoolean)
  {
    return -1;
  }
  
  public int updateClir(int paramInt)
  {
    return -1;
  }
  
  public int updateColp(boolean paramBoolean)
  {
    return -1;
  }
  
  public int updateColr(int paramInt)
  {
    return -1;
  }
}
