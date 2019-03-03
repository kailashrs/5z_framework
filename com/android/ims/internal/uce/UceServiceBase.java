package com.android.ims.internal.uce;

import com.android.ims.internal.uce.common.UceLong;
import com.android.ims.internal.uce.options.IOptionsListener;
import com.android.ims.internal.uce.options.IOptionsService;
import com.android.ims.internal.uce.presence.IPresenceListener;
import com.android.ims.internal.uce.presence.IPresenceService;
import com.android.ims.internal.uce.uceservice.IUceListener;
import com.android.ims.internal.uce.uceservice.IUceService.Stub;

public abstract class UceServiceBase
{
  private UceServiceBinder mBinder;
  
  public UceServiceBase() {}
  
  public UceServiceBinder getBinder()
  {
    if (mBinder == null) {
      mBinder = new UceServiceBinder(null);
    }
    return mBinder;
  }
  
  protected int onCreateOptionsService(IOptionsListener paramIOptionsListener, UceLong paramUceLong)
  {
    return 0;
  }
  
  protected int onCreateOptionsService(IOptionsListener paramIOptionsListener, UceLong paramUceLong, String paramString)
  {
    return 0;
  }
  
  protected int onCreatePresService(IPresenceListener paramIPresenceListener, UceLong paramUceLong)
  {
    return 0;
  }
  
  protected int onCreatePresService(IPresenceListener paramIPresenceListener, UceLong paramUceLong, String paramString)
  {
    return 0;
  }
  
  protected void onDestroyOptionsService(int paramInt) {}
  
  protected void onDestroyPresService(int paramInt) {}
  
  protected IOptionsService onGetOptionsService()
  {
    return null;
  }
  
  protected IOptionsService onGetOptionsService(String paramString)
  {
    return null;
  }
  
  protected IPresenceService onGetPresenceService()
  {
    return null;
  }
  
  protected IPresenceService onGetPresenceService(String paramString)
  {
    return null;
  }
  
  protected boolean onGetServiceStatus()
  {
    return false;
  }
  
  protected boolean onIsServiceStarted()
  {
    return false;
  }
  
  protected boolean onServiceStart(IUceListener paramIUceListener)
  {
    return false;
  }
  
  protected boolean onStopService()
  {
    return false;
  }
  
  private final class UceServiceBinder
    extends IUceService.Stub
  {
    private UceServiceBinder() {}
    
    public int createOptionsService(IOptionsListener paramIOptionsListener, UceLong paramUceLong)
    {
      return onCreateOptionsService(paramIOptionsListener, paramUceLong);
    }
    
    public int createOptionsServiceForSubscription(IOptionsListener paramIOptionsListener, UceLong paramUceLong, String paramString)
    {
      return onCreateOptionsService(paramIOptionsListener, paramUceLong, paramString);
    }
    
    public int createPresenceService(IPresenceListener paramIPresenceListener, UceLong paramUceLong)
    {
      return onCreatePresService(paramIPresenceListener, paramUceLong);
    }
    
    public int createPresenceServiceForSubscription(IPresenceListener paramIPresenceListener, UceLong paramUceLong, String paramString)
    {
      return onCreatePresService(paramIPresenceListener, paramUceLong, paramString);
    }
    
    public void destroyOptionsService(int paramInt)
    {
      onDestroyOptionsService(paramInt);
    }
    
    public void destroyPresenceService(int paramInt)
    {
      onDestroyPresService(paramInt);
    }
    
    public IOptionsService getOptionsService()
    {
      return onGetOptionsService();
    }
    
    public IOptionsService getOptionsServiceForSubscription(String paramString)
    {
      return onGetOptionsService(paramString);
    }
    
    public IPresenceService getPresenceService()
    {
      return onGetPresenceService();
    }
    
    public IPresenceService getPresenceServiceForSubscription(String paramString)
    {
      return onGetPresenceService(paramString);
    }
    
    public boolean getServiceStatus()
    {
      return onGetServiceStatus();
    }
    
    public boolean isServiceStarted()
    {
      return onIsServiceStarted();
    }
    
    public boolean startService(IUceListener paramIUceListener)
    {
      return onServiceStart(paramIUceListener);
    }
    
    public boolean stopService()
    {
      return onStopService();
    }
  }
}
