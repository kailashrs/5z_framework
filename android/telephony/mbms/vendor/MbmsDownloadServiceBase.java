package android.telephony.mbms.vendor;

import android.annotation.SystemApi;
import android.os.Binder;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.telephony.mbms.DownloadProgressListener;
import android.telephony.mbms.DownloadRequest;
import android.telephony.mbms.DownloadStatusListener;
import android.telephony.mbms.FileInfo;
import android.telephony.mbms.FileServiceInfo;
import android.telephony.mbms.IDownloadProgressListener;
import android.telephony.mbms.IDownloadStatusListener;
import android.telephony.mbms.IMbmsDownloadSessionCallback;
import android.telephony.mbms.MbmsDownloadSessionCallback;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SystemApi
public class MbmsDownloadServiceBase
  extends IMbmsDownloadService.Stub
{
  private final Map<IBinder, IBinder.DeathRecipient> mDownloadCallbackDeathRecipients = new HashMap();
  private final Map<IBinder, DownloadProgressListener> mDownloadProgressListenerBinderMap = new HashMap();
  private final Map<IBinder, DownloadStatusListener> mDownloadStatusListenerBinderMap = new HashMap();
  
  public MbmsDownloadServiceBase() {}
  
  public int addProgressListener(DownloadRequest paramDownloadRequest, DownloadProgressListener paramDownloadProgressListener)
    throws RemoteException
  {
    return 0;
  }
  
  public final int addProgressListener(final DownloadRequest paramDownloadRequest, final IDownloadProgressListener paramIDownloadProgressListener)
    throws RemoteException
  {
    final int i = Binder.getCallingUid();
    if (paramDownloadRequest != null)
    {
      if (paramIDownloadProgressListener != null)
      {
        VendorDownloadProgressListener local5 = new VendorDownloadProgressListener(paramIDownloadProgressListener)
        {
          protected void onRemoteException(RemoteException paramAnonymousRemoteException)
          {
            onAppCallbackDied(i, paramDownloadRequest.getSubscriptionId());
          }
        };
        int j = addProgressListener(paramDownloadRequest, local5);
        if (j == 0)
        {
          paramDownloadRequest = new IBinder.DeathRecipient()
          {
            public void binderDied()
            {
              onAppCallbackDied(i, paramDownloadRequest.getSubscriptionId());
              mDownloadProgressListenerBinderMap.remove(paramIDownloadProgressListener.asBinder());
              mDownloadCallbackDeathRecipients.remove(paramIDownloadProgressListener.asBinder());
            }
          };
          mDownloadCallbackDeathRecipients.put(paramIDownloadProgressListener.asBinder(), paramDownloadRequest);
          paramIDownloadProgressListener.asBinder().linkToDeath(paramDownloadRequest, 0);
          mDownloadProgressListenerBinderMap.put(paramIDownloadProgressListener.asBinder(), local5);
        }
        return j;
      }
      throw new NullPointerException("Callback must not be null");
    }
    throw new NullPointerException("Download request must not be null");
  }
  
  public int addStatusListener(DownloadRequest paramDownloadRequest, DownloadStatusListener paramDownloadStatusListener)
    throws RemoteException
  {
    return 0;
  }
  
  public final int addStatusListener(final DownloadRequest paramDownloadRequest, final IDownloadStatusListener paramIDownloadStatusListener)
    throws RemoteException
  {
    final int i = Binder.getCallingUid();
    if (paramDownloadRequest != null)
    {
      if (paramIDownloadStatusListener != null)
      {
        VendorDownloadStatusListener local3 = new VendorDownloadStatusListener(paramIDownloadStatusListener)
        {
          protected void onRemoteException(RemoteException paramAnonymousRemoteException)
          {
            onAppCallbackDied(i, paramDownloadRequest.getSubscriptionId());
          }
        };
        int j = addStatusListener(paramDownloadRequest, local3);
        if (j == 0)
        {
          paramDownloadRequest = new IBinder.DeathRecipient()
          {
            public void binderDied()
            {
              onAppCallbackDied(i, paramDownloadRequest.getSubscriptionId());
              mDownloadStatusListenerBinderMap.remove(paramIDownloadStatusListener.asBinder());
              mDownloadCallbackDeathRecipients.remove(paramIDownloadStatusListener.asBinder());
            }
          };
          mDownloadCallbackDeathRecipients.put(paramIDownloadStatusListener.asBinder(), paramDownloadRequest);
          paramIDownloadStatusListener.asBinder().linkToDeath(paramDownloadRequest, 0);
          mDownloadStatusListenerBinderMap.put(paramIDownloadStatusListener.asBinder(), local3);
        }
        return j;
      }
      throw new NullPointerException("Callback must not be null");
    }
    throw new NullPointerException("Download request must not be null");
  }
  
  public int cancelDownload(DownloadRequest paramDownloadRequest)
    throws RemoteException
  {
    return 0;
  }
  
  public void dispose(int paramInt)
    throws RemoteException
  {}
  
  public int download(DownloadRequest paramDownloadRequest)
    throws RemoteException
  {
    return 0;
  }
  
  public final int initialize(final int paramInt, final IMbmsDownloadSessionCallback paramIMbmsDownloadSessionCallback)
    throws RemoteException
  {
    if (paramIMbmsDownloadSessionCallback != null)
    {
      final int i = Binder.getCallingUid();
      int j = initialize(paramInt, new MbmsDownloadSessionCallback()
      {
        public void onError(int paramAnonymousInt, String paramAnonymousString)
        {
          if (paramAnonymousInt != -1) {}
          try
          {
            paramIMbmsDownloadSessionCallback.onError(paramAnonymousInt, paramAnonymousString);
          }
          catch (RemoteException paramAnonymousString)
          {
            onAppCallbackDied(i, paramInt);
          }
          paramAnonymousString = new java/lang/IllegalArgumentException;
          paramAnonymousString.<init>("Middleware cannot send an unknown error.");
          throw paramAnonymousString;
        }
        
        public void onFileServicesUpdated(List<FileServiceInfo> paramAnonymousList)
        {
          try
          {
            paramIMbmsDownloadSessionCallback.onFileServicesUpdated(paramAnonymousList);
          }
          catch (RemoteException paramAnonymousList)
          {
            onAppCallbackDied(i, paramInt);
          }
        }
        
        public void onMiddlewareReady()
        {
          try
          {
            paramIMbmsDownloadSessionCallback.onMiddlewareReady();
          }
          catch (RemoteException localRemoteException)
          {
            onAppCallbackDied(i, paramInt);
          }
        }
      });
      if (j == 0) {
        paramIMbmsDownloadSessionCallback.asBinder().linkToDeath(new IBinder.DeathRecipient()
        {
          public void binderDied()
          {
            onAppCallbackDied(i, paramInt);
          }
        }, 0);
      }
      return j;
    }
    throw new NullPointerException("Callback must not be null");
  }
  
  public int initialize(int paramInt, MbmsDownloadSessionCallback paramMbmsDownloadSessionCallback)
    throws RemoteException
  {
    return 0;
  }
  
  public List<DownloadRequest> listPendingDownloads(int paramInt)
    throws RemoteException
  {
    return null;
  }
  
  public void onAppCallbackDied(int paramInt1, int paramInt2) {}
  
  public int removeProgressListener(DownloadRequest paramDownloadRequest, DownloadProgressListener paramDownloadProgressListener)
    throws RemoteException
  {
    return 0;
  }
  
  public final int removeProgressListener(DownloadRequest paramDownloadRequest, IDownloadProgressListener paramIDownloadProgressListener)
    throws RemoteException
  {
    if (paramDownloadRequest != null)
    {
      if (paramIDownloadProgressListener != null)
      {
        IBinder.DeathRecipient localDeathRecipient = (IBinder.DeathRecipient)mDownloadCallbackDeathRecipients.remove(paramIDownloadProgressListener.asBinder());
        if (localDeathRecipient != null)
        {
          paramIDownloadProgressListener.asBinder().unlinkToDeath(localDeathRecipient, 0);
          paramIDownloadProgressListener = (DownloadProgressListener)mDownloadProgressListenerBinderMap.remove(paramIDownloadProgressListener.asBinder());
          if (paramIDownloadProgressListener != null) {
            return removeProgressListener(paramDownloadRequest, paramIDownloadProgressListener);
          }
          throw new IllegalArgumentException("Unknown listener");
        }
        throw new IllegalArgumentException("Unknown listener");
      }
      throw new NullPointerException("Callback must not be null");
    }
    throw new NullPointerException("Download request must not be null");
  }
  
  public int removeStatusListener(DownloadRequest paramDownloadRequest, DownloadStatusListener paramDownloadStatusListener)
    throws RemoteException
  {
    return 0;
  }
  
  public final int removeStatusListener(DownloadRequest paramDownloadRequest, IDownloadStatusListener paramIDownloadStatusListener)
    throws RemoteException
  {
    if (paramDownloadRequest != null)
    {
      if (paramIDownloadStatusListener != null)
      {
        IBinder.DeathRecipient localDeathRecipient = (IBinder.DeathRecipient)mDownloadCallbackDeathRecipients.remove(paramIDownloadStatusListener.asBinder());
        if (localDeathRecipient != null)
        {
          paramIDownloadStatusListener.asBinder().unlinkToDeath(localDeathRecipient, 0);
          paramIDownloadStatusListener = (DownloadStatusListener)mDownloadStatusListenerBinderMap.remove(paramIDownloadStatusListener.asBinder());
          if (paramIDownloadStatusListener != null) {
            return removeStatusListener(paramDownloadRequest, paramIDownloadStatusListener);
          }
          throw new IllegalArgumentException("Unknown listener");
        }
        throw new IllegalArgumentException("Unknown listener");
      }
      throw new NullPointerException("Callback must not be null");
    }
    throw new NullPointerException("Download request must not be null");
  }
  
  public int requestDownloadState(DownloadRequest paramDownloadRequest, FileInfo paramFileInfo)
    throws RemoteException
  {
    return 0;
  }
  
  public int requestUpdateFileServices(int paramInt, List<String> paramList)
    throws RemoteException
  {
    return 0;
  }
  
  public int resetDownloadKnowledge(DownloadRequest paramDownloadRequest)
    throws RemoteException
  {
    return 0;
  }
  
  public int setTempFileRootDirectory(int paramInt, String paramString)
    throws RemoteException
  {
    return 0;
  }
  
  private static abstract class VendorDownloadProgressListener
    extends DownloadProgressListener
  {
    private final IDownloadProgressListener mListener;
    
    public VendorDownloadProgressListener(IDownloadProgressListener paramIDownloadProgressListener)
    {
      mListener = paramIDownloadProgressListener;
    }
    
    public void onProgressUpdated(DownloadRequest paramDownloadRequest, FileInfo paramFileInfo, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      try
      {
        mListener.onProgressUpdated(paramDownloadRequest, paramFileInfo, paramInt1, paramInt2, paramInt3, paramInt4);
      }
      catch (RemoteException paramDownloadRequest)
      {
        onRemoteException(paramDownloadRequest);
      }
    }
    
    protected abstract void onRemoteException(RemoteException paramRemoteException);
  }
  
  private static abstract class VendorDownloadStatusListener
    extends DownloadStatusListener
  {
    private final IDownloadStatusListener mListener;
    
    public VendorDownloadStatusListener(IDownloadStatusListener paramIDownloadStatusListener)
    {
      mListener = paramIDownloadStatusListener;
    }
    
    protected abstract void onRemoteException(RemoteException paramRemoteException);
    
    public void onStatusUpdated(DownloadRequest paramDownloadRequest, FileInfo paramFileInfo, int paramInt)
    {
      try
      {
        mListener.onStatusUpdated(paramDownloadRequest, paramFileInfo, paramInt);
      }
      catch (RemoteException paramDownloadRequest)
      {
        onRemoteException(paramDownloadRequest);
      }
    }
  }
}
