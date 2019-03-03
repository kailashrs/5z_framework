package android.view.textclassifier;

import android.content.Context;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.service.textclassifier.ITextClassificationCallback;
import android.service.textclassifier.ITextClassificationCallback.Stub;
import android.service.textclassifier.ITextClassifierService;
import android.service.textclassifier.ITextClassifierService.Stub;
import android.service.textclassifier.ITextLinksCallback;
import android.service.textclassifier.ITextLinksCallback.Stub;
import android.service.textclassifier.ITextSelectionCallback;
import android.service.textclassifier.ITextSelectionCallback.Stub;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.Preconditions;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
public final class SystemTextClassifier
  implements TextClassifier
{
  private static final String LOG_TAG = "SystemTextClassifier";
  private final TextClassifier mFallback;
  private final ITextClassifierService mManagerService = ITextClassifierService.Stub.asInterface(ServiceManager.getServiceOrThrow("textclassification"));
  private final String mPackageName;
  private TextClassificationSessionId mSessionId;
  private final TextClassificationConstants mSettings;
  
  public SystemTextClassifier(Context paramContext, TextClassificationConstants paramTextClassificationConstants)
    throws ServiceManager.ServiceNotFoundException
  {
    mSettings = ((TextClassificationConstants)Preconditions.checkNotNull(paramTextClassificationConstants));
    mFallback = ((TextClassificationManager)paramContext.getSystemService(TextClassificationManager.class)).getTextClassifier(0);
    mPackageName = ((String)Preconditions.checkNotNull(paramContext.getPackageName()));
  }
  
  public TextClassification classifyText(TextClassification.Request paramRequest)
  {
    Preconditions.checkNotNull(paramRequest);
    TextClassifier.Utils.checkMainThread();
    try
    {
      Object localObject = new android/view/textclassifier/SystemTextClassifier$TextClassificationCallback;
      ((TextClassificationCallback)localObject).<init>(null);
      mManagerService.onClassifyText(mSessionId, paramRequest, (ITextClassificationCallback)localObject);
      localObject = (TextClassification)mReceiver.get();
      if (localObject != null) {
        return localObject;
      }
    }
    catch (RemoteException|InterruptedException localRemoteException)
    {
      Log.e("SystemTextClassifier", "Error classifying text. Using fallback.", localRemoteException);
    }
    return mFallback.classifyText(paramRequest);
  }
  
  public void destroy()
  {
    try
    {
      if (mSessionId != null) {
        mManagerService.onDestroyTextClassificationSession(mSessionId);
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SystemTextClassifier", "Error destroying classification session.", localRemoteException);
    }
  }
  
  public TextLinks generateLinks(TextLinks.Request paramRequest)
  {
    Preconditions.checkNotNull(paramRequest);
    TextClassifier.Utils.checkMainThread();
    if ((!mSettings.isSmartLinkifyEnabled()) && (paramRequest.isLegacyFallback())) {
      return TextClassifier.Utils.generateLegacyLinks(paramRequest);
    }
    try
    {
      paramRequest.setCallingPackageName(mPackageName);
      Object localObject = new android/view/textclassifier/SystemTextClassifier$TextLinksCallback;
      ((TextLinksCallback)localObject).<init>(null);
      mManagerService.onGenerateLinks(mSessionId, paramRequest, (ITextLinksCallback)localObject);
      localObject = (TextLinks)mReceiver.get();
      if (localObject != null) {
        return localObject;
      }
    }
    catch (RemoteException|InterruptedException localRemoteException)
    {
      Log.e("SystemTextClassifier", "Error generating links. Using fallback.", localRemoteException);
    }
    return mFallback.generateLinks(paramRequest);
  }
  
  public int getMaxGenerateLinksTextLength()
  {
    return mFallback.getMaxGenerateLinksTextLength();
  }
  
  void initializeRemoteSession(TextClassificationContext paramTextClassificationContext, TextClassificationSessionId paramTextClassificationSessionId)
  {
    mSessionId = ((TextClassificationSessionId)Preconditions.checkNotNull(paramTextClassificationSessionId));
    try
    {
      mManagerService.onCreateTextClassificationSession(paramTextClassificationContext, mSessionId);
    }
    catch (RemoteException paramTextClassificationContext)
    {
      Log.e("SystemTextClassifier", "Error starting a new classification session.", paramTextClassificationContext);
    }
  }
  
  public void onSelectionEvent(SelectionEvent paramSelectionEvent)
  {
    Preconditions.checkNotNull(paramSelectionEvent);
    TextClassifier.Utils.checkMainThread();
    try
    {
      mManagerService.onSelectionEvent(mSessionId, paramSelectionEvent);
    }
    catch (RemoteException paramSelectionEvent)
    {
      Log.e("SystemTextClassifier", "Error reporting selection event.", paramSelectionEvent);
    }
  }
  
  public TextSelection suggestSelection(TextSelection.Request paramRequest)
  {
    Preconditions.checkNotNull(paramRequest);
    TextClassifier.Utils.checkMainThread();
    try
    {
      Object localObject = new android/view/textclassifier/SystemTextClassifier$TextSelectionCallback;
      ((TextSelectionCallback)localObject).<init>(null);
      mManagerService.onSuggestSelection(mSessionId, paramRequest, (ITextSelectionCallback)localObject);
      localObject = (TextSelection)mReceiver.get();
      if (localObject != null) {
        return localObject;
      }
    }
    catch (RemoteException|InterruptedException localRemoteException)
    {
      Log.e("SystemTextClassifier", "Error suggesting selection for text. Using fallback.", localRemoteException);
    }
    return mFallback.suggestSelection(paramRequest);
  }
  
  private static final class ResponseReceiver<T>
  {
    private final CountDownLatch mLatch = new CountDownLatch(1);
    private T mResponse;
    
    private ResponseReceiver() {}
    
    public T get()
      throws InterruptedException
    {
      if (Looper.myLooper() != Looper.getMainLooper()) {
        mLatch.await(2L, TimeUnit.SECONDS);
      }
      return mResponse;
    }
    
    public void onFailure()
    {
      Log.e("SystemTextClassifier", "Request failed.", null);
      mLatch.countDown();
    }
    
    public void onSuccess(T paramT)
    {
      mResponse = paramT;
      mLatch.countDown();
    }
  }
  
  private static final class TextClassificationCallback
    extends ITextClassificationCallback.Stub
  {
    final SystemTextClassifier.ResponseReceiver<TextClassification> mReceiver = new SystemTextClassifier.ResponseReceiver(null);
    
    private TextClassificationCallback() {}
    
    public void onFailure()
    {
      mReceiver.onFailure();
    }
    
    public void onSuccess(TextClassification paramTextClassification)
    {
      mReceiver.onSuccess(paramTextClassification);
    }
  }
  
  private static final class TextLinksCallback
    extends ITextLinksCallback.Stub
  {
    final SystemTextClassifier.ResponseReceiver<TextLinks> mReceiver = new SystemTextClassifier.ResponseReceiver(null);
    
    private TextLinksCallback() {}
    
    public void onFailure()
    {
      mReceiver.onFailure();
    }
    
    public void onSuccess(TextLinks paramTextLinks)
    {
      mReceiver.onSuccess(paramTextLinks);
    }
  }
  
  private static final class TextSelectionCallback
    extends ITextSelectionCallback.Stub
  {
    final SystemTextClassifier.ResponseReceiver<TextSelection> mReceiver = new SystemTextClassifier.ResponseReceiver(null);
    
    private TextSelectionCallback() {}
    
    public void onFailure()
    {
      mReceiver.onFailure();
    }
    
    public void onSuccess(TextSelection paramTextSelection)
    {
      mReceiver.onSuccess(paramTextSelection);
    }
  }
}
