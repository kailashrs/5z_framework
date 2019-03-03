package android.webkit;

import android.annotation.SystemApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.net.MalformedURLException;
import java.net.URL;

@SystemApi
public class JsDialogHelper
{
  public static final int ALERT = 1;
  public static final int CONFIRM = 2;
  public static final int PROMPT = 3;
  private static final String TAG = "JsDialogHelper";
  public static final int UNLOAD = 4;
  private final String mDefaultValue;
  private final String mMessage;
  private final JsPromptResult mResult;
  private final int mType;
  private final String mUrl;
  
  public JsDialogHelper(JsPromptResult paramJsPromptResult, int paramInt, String paramString1, String paramString2, String paramString3)
  {
    mResult = paramJsPromptResult;
    mDefaultValue = paramString1;
    mMessage = paramString2;
    mType = paramInt;
    mUrl = paramString3;
  }
  
  public JsDialogHelper(JsPromptResult paramJsPromptResult, Message paramMessage)
  {
    mResult = paramJsPromptResult;
    mDefaultValue = paramMessage.getData().getString("default");
    mMessage = paramMessage.getData().getString("message");
    mType = paramMessage.getData().getInt("type");
    mUrl = paramMessage.getData().getString("url");
  }
  
  private static boolean canShowAlertDialog(Context paramContext)
  {
    return paramContext instanceof Activity;
  }
  
  private String getJsDialogTitle(Context paramContext)
  {
    String str = mUrl;
    if (URLUtil.isDataUrl(mUrl)) {
      paramContext = paramContext.getString(17040151);
    } else {
      try
      {
        URL localURL = new java/net/URL;
        localURL.<init>(mUrl);
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append(localURL.getProtocol());
        localStringBuilder.append("://");
        localStringBuilder.append(localURL.getHost());
        paramContext = paramContext.getString(17040150, new Object[] { localStringBuilder.toString() });
      }
      catch (MalformedURLException paramContext)
      {
        paramContext = str;
      }
    }
    return paramContext;
  }
  
  public boolean invokeCallback(WebChromeClient paramWebChromeClient, WebView paramWebView)
  {
    switch (mType)
    {
    default: 
      paramWebChromeClient = new StringBuilder();
      paramWebChromeClient.append("Unexpected type: ");
      paramWebChromeClient.append(mType);
      throw new IllegalArgumentException(paramWebChromeClient.toString());
    case 4: 
      return paramWebChromeClient.onJsBeforeUnload(paramWebView, mUrl, mMessage, mResult);
    case 3: 
      return paramWebChromeClient.onJsPrompt(paramWebView, mUrl, mMessage, mDefaultValue, mResult);
    case 2: 
      return paramWebChromeClient.onJsConfirm(paramWebView, mUrl, mMessage, mResult);
    }
    return paramWebChromeClient.onJsAlert(paramWebView, mUrl, mMessage, mResult);
  }
  
  public void showDialog(Context paramContext)
  {
    if (!canShowAlertDialog(paramContext))
    {
      Log.w("JsDialogHelper", "Cannot create a dialog, the WebView context is not an Activity");
      mResult.cancel();
      return;
    }
    String str;
    Object localObject;
    int i;
    int j;
    if (mType == 4)
    {
      str = paramContext.getString(17040149);
      localObject = paramContext.getString(17040146, new Object[] { mMessage });
      i = 17040148;
      j = 17040147;
    }
    else
    {
      str = getJsDialogTitle(paramContext);
      localObject = mMessage;
      i = 17039370;
      j = 17039360;
    }
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
    localBuilder.setTitle(str);
    localBuilder.setOnCancelListener(new CancelListener(null));
    if (mType != 3)
    {
      localBuilder.setMessage((CharSequence)localObject);
      localBuilder.setPositiveButton(i, new PositiveListener(null));
    }
    else
    {
      paramContext = LayoutInflater.from(paramContext).inflate(17367190, null);
      localObject = (EditText)paramContext.findViewById(16909547);
      ((EditText)localObject).setText(mDefaultValue);
      localBuilder.setPositiveButton(i, new PositiveListener((EditText)localObject));
      ((TextView)paramContext.findViewById(16908299)).setText(mMessage);
      localBuilder.setView(paramContext);
    }
    if (mType != 1) {
      localBuilder.setNegativeButton(j, new CancelListener(null));
    }
    localBuilder.show();
  }
  
  private class CancelListener
    implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener
  {
    private CancelListener() {}
    
    public void onCancel(DialogInterface paramDialogInterface)
    {
      mResult.cancel();
    }
    
    public void onClick(DialogInterface paramDialogInterface, int paramInt)
    {
      mResult.cancel();
    }
  }
  
  private class PositiveListener
    implements DialogInterface.OnClickListener
  {
    private final EditText mEdit;
    
    public PositiveListener(EditText paramEditText)
    {
      mEdit = paramEditText;
    }
    
    public void onClick(DialogInterface paramDialogInterface, int paramInt)
    {
      if (mEdit == null) {
        mResult.confirm();
      } else {
        mResult.confirm(mEdit.getText().toString());
      }
    }
  }
}
