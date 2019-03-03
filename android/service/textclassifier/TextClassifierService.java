package android.service.textclassifier;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Slog;
import android.view.textclassifier.SelectionEvent;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassification.Options;
import android.view.textclassifier.TextClassification.Request;
import android.view.textclassifier.TextClassification.Request.Builder;
import android.view.textclassifier.TextClassificationContext;
import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassificationSessionId;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextLinks.Options;
import android.view.textclassifier.TextLinks.Request;
import android.view.textclassifier.TextLinks.Request.Builder;
import android.view.textclassifier.TextSelection;
import android.view.textclassifier.TextSelection.Options;
import android.view.textclassifier.TextSelection.Request;
import android.view.textclassifier.TextSelection.Request.Builder;
import com.android.internal.util.Preconditions;

@SystemApi
public abstract class TextClassifierService
  extends Service
{
  private static final String LOG_TAG = "TextClassifierService";
  @SystemApi
  public static final String SERVICE_INTERFACE = "android.service.textclassifier.TextClassifierService";
  private final ITextClassifierService.Stub mBinder = new ITextClassifierService.Stub()
  {
    private final CancellationSignal mCancellationSignal = new CancellationSignal();
    
    public void onClassifyText(TextClassificationSessionId paramAnonymousTextClassificationSessionId, TextClassification.Request paramAnonymousRequest, final ITextClassificationCallback paramAnonymousITextClassificationCallback)
      throws RemoteException
    {
      Preconditions.checkNotNull(paramAnonymousRequest);
      Preconditions.checkNotNull(paramAnonymousITextClassificationCallback);
      onClassifyText(paramAnonymousRequest.getText(), paramAnonymousRequest.getStartIndex(), paramAnonymousRequest.getEndIndex(), TextClassification.Options.from(paramAnonymousTextClassificationSessionId, paramAnonymousRequest), mCancellationSignal, new TextClassifierService.Callback()
      {
        public void onFailure(CharSequence paramAnonymous2CharSequence)
        {
          try
          {
            paramAnonymousITextClassificationCallback.onFailure();
          }
          catch (RemoteException paramAnonymous2CharSequence)
          {
            Slog.d("TextClassifierService", "Error calling callback");
          }
        }
        
        public void onSuccess(TextClassification paramAnonymous2TextClassification)
        {
          try
          {
            paramAnonymousITextClassificationCallback.onSuccess(paramAnonymous2TextClassification);
          }
          catch (RemoteException paramAnonymous2TextClassification)
          {
            Slog.d("TextClassifierService", "Error calling callback");
          }
        }
      });
    }
    
    public void onCreateTextClassificationSession(TextClassificationContext paramAnonymousTextClassificationContext, TextClassificationSessionId paramAnonymousTextClassificationSessionId)
      throws RemoteException
    {
      Preconditions.checkNotNull(paramAnonymousTextClassificationContext);
      Preconditions.checkNotNull(paramAnonymousTextClassificationSessionId);
      TextClassifierService.this.onCreateTextClassificationSession(paramAnonymousTextClassificationContext, paramAnonymousTextClassificationSessionId);
    }
    
    public void onDestroyTextClassificationSession(TextClassificationSessionId paramAnonymousTextClassificationSessionId)
      throws RemoteException
    {
      TextClassifierService.this.onDestroyTextClassificationSession(paramAnonymousTextClassificationSessionId);
    }
    
    public void onGenerateLinks(TextClassificationSessionId paramAnonymousTextClassificationSessionId, TextLinks.Request paramAnonymousRequest, final ITextLinksCallback paramAnonymousITextLinksCallback)
      throws RemoteException
    {
      Preconditions.checkNotNull(paramAnonymousRequest);
      Preconditions.checkNotNull(paramAnonymousITextLinksCallback);
      onGenerateLinks(paramAnonymousRequest.getText(), TextLinks.Options.from(paramAnonymousTextClassificationSessionId, paramAnonymousRequest), mCancellationSignal, new TextClassifierService.Callback()
      {
        public void onFailure(CharSequence paramAnonymous2CharSequence)
        {
          try
          {
            paramAnonymousITextLinksCallback.onFailure();
          }
          catch (RemoteException paramAnonymous2CharSequence)
          {
            Slog.d("TextClassifierService", "Error calling callback");
          }
        }
        
        public void onSuccess(TextLinks paramAnonymous2TextLinks)
        {
          try
          {
            paramAnonymousITextLinksCallback.onSuccess(paramAnonymous2TextLinks);
          }
          catch (RemoteException paramAnonymous2TextLinks)
          {
            Slog.d("TextClassifierService", "Error calling callback");
          }
        }
      });
    }
    
    public void onSelectionEvent(TextClassificationSessionId paramAnonymousTextClassificationSessionId, SelectionEvent paramAnonymousSelectionEvent)
      throws RemoteException
    {
      Preconditions.checkNotNull(paramAnonymousSelectionEvent);
      TextClassifierService.this.onSelectionEvent(paramAnonymousTextClassificationSessionId, paramAnonymousSelectionEvent);
    }
    
    public void onSuggestSelection(TextClassificationSessionId paramAnonymousTextClassificationSessionId, TextSelection.Request paramAnonymousRequest, final ITextSelectionCallback paramAnonymousITextSelectionCallback)
      throws RemoteException
    {
      Preconditions.checkNotNull(paramAnonymousRequest);
      Preconditions.checkNotNull(paramAnonymousITextSelectionCallback);
      onSuggestSelection(paramAnonymousRequest.getText(), paramAnonymousRequest.getStartIndex(), paramAnonymousRequest.getEndIndex(), TextSelection.Options.from(paramAnonymousTextClassificationSessionId, paramAnonymousRequest), mCancellationSignal, new TextClassifierService.Callback()
      {
        public void onFailure(CharSequence paramAnonymous2CharSequence)
        {
          try
          {
            if (paramAnonymousITextSelectionCallback.asBinder().isBinderAlive()) {
              paramAnonymousITextSelectionCallback.onFailure();
            }
          }
          catch (RemoteException paramAnonymous2CharSequence)
          {
            Slog.d("TextClassifierService", "Error calling callback");
          }
        }
        
        public void onSuccess(TextSelection paramAnonymous2TextSelection)
        {
          try
          {
            paramAnonymousITextSelectionCallback.onSuccess(paramAnonymous2TextSelection);
          }
          catch (RemoteException paramAnonymous2TextSelection)
          {
            Slog.d("TextClassifierService", "Error calling callback");
          }
        }
      });
    }
  };
  
  public TextClassifierService() {}
  
  public static ComponentName getServiceComponentName(Context paramContext)
  {
    String str = paramContext.getPackageManager().getSystemTextClassifierPackageName();
    if (TextUtils.isEmpty(str))
    {
      Slog.d("TextClassifierService", "No configured system TextClassifierService");
      return null;
    }
    Intent localIntent = new Intent("android.service.textclassifier.TextClassifierService").setPackage(str);
    paramContext = paramContext.getPackageManager().resolveService(localIntent, 1048576);
    if ((paramContext != null) && (serviceInfo != null))
    {
      paramContext = serviceInfo;
      if ("android.permission.BIND_TEXTCLASSIFIER_SERVICE".equals(permission)) {
        return paramContext.getComponentName();
      }
      Slog.w("TextClassifierService", String.format("Service %s should require %s permission. Found %s permission", new Object[] { paramContext.getComponentName(), "android.permission.BIND_TEXTCLASSIFIER_SERVICE", permission }));
      return null;
    }
    Slog.w("TextClassifierService", String.format("Package or service not found in package %s", new Object[] { str }));
    return null;
  }
  
  public final TextClassifier getLocalTextClassifier()
  {
    TextClassificationManager localTextClassificationManager = (TextClassificationManager)getSystemService(TextClassificationManager.class);
    if (localTextClassificationManager != null) {
      return localTextClassificationManager.getTextClassifier(0);
    }
    return TextClassifier.NO_OP;
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    if ("android.service.textclassifier.TextClassifierService".equals(paramIntent.getAction())) {
      return mBinder;
    }
    return null;
  }
  
  public abstract void onClassifyText(TextClassificationSessionId paramTextClassificationSessionId, TextClassification.Request paramRequest, CancellationSignal paramCancellationSignal, Callback<TextClassification> paramCallback);
  
  public void onClassifyText(CharSequence paramCharSequence, int paramInt1, int paramInt2, TextClassification.Options paramOptions, CancellationSignal paramCancellationSignal, Callback<TextClassification> paramCallback)
  {
    TextClassificationSessionId localTextClassificationSessionId = paramOptions.getSessionId();
    if (paramOptions.getRequest() != null) {
      paramCharSequence = paramOptions.getRequest();
    } else {
      paramCharSequence = new TextClassification.Request.Builder(paramCharSequence, paramInt1, paramInt2).setDefaultLocales(paramOptions.getDefaultLocales()).setReferenceTime(paramOptions.getReferenceTime()).build();
    }
    onClassifyText(localTextClassificationSessionId, paramCharSequence, paramCancellationSignal, paramCallback);
  }
  
  public void onCreateTextClassificationSession(TextClassificationContext paramTextClassificationContext, TextClassificationSessionId paramTextClassificationSessionId) {}
  
  public void onDestroyTextClassificationSession(TextClassificationSessionId paramTextClassificationSessionId) {}
  
  public abstract void onGenerateLinks(TextClassificationSessionId paramTextClassificationSessionId, TextLinks.Request paramRequest, CancellationSignal paramCancellationSignal, Callback<TextLinks> paramCallback);
  
  public void onGenerateLinks(CharSequence paramCharSequence, TextLinks.Options paramOptions, CancellationSignal paramCancellationSignal, Callback<TextLinks> paramCallback)
  {
    TextClassificationSessionId localTextClassificationSessionId = paramOptions.getSessionId();
    if (paramOptions.getRequest() != null) {
      paramCharSequence = paramOptions.getRequest();
    } else {
      paramCharSequence = new TextLinks.Request.Builder(paramCharSequence).setDefaultLocales(paramOptions.getDefaultLocales()).setEntityConfig(paramOptions.getEntityConfig()).build();
    }
    onGenerateLinks(localTextClassificationSessionId, paramCharSequence, paramCancellationSignal, paramCallback);
  }
  
  public void onSelectionEvent(TextClassificationSessionId paramTextClassificationSessionId, SelectionEvent paramSelectionEvent) {}
  
  public abstract void onSuggestSelection(TextClassificationSessionId paramTextClassificationSessionId, TextSelection.Request paramRequest, CancellationSignal paramCancellationSignal, Callback<TextSelection> paramCallback);
  
  public void onSuggestSelection(CharSequence paramCharSequence, int paramInt1, int paramInt2, TextSelection.Options paramOptions, CancellationSignal paramCancellationSignal, Callback<TextSelection> paramCallback)
  {
    TextClassificationSessionId localTextClassificationSessionId = paramOptions.getSessionId();
    if (paramOptions.getRequest() != null) {
      paramCharSequence = paramOptions.getRequest();
    } else {
      paramCharSequence = new TextSelection.Request.Builder(paramCharSequence, paramInt1, paramInt2).setDefaultLocales(paramOptions.getDefaultLocales()).build();
    }
    onSuggestSelection(localTextClassificationSessionId, paramCharSequence, paramCancellationSignal, paramCallback);
  }
  
  @SystemApi
  public static abstract interface Callback<T>
  {
    public abstract void onFailure(CharSequence paramCharSequence);
    
    public abstract void onSuccess(T paramT);
  }
}
