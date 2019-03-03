package android.webkit;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@Deprecated
public class WebViewFragment
  extends Fragment
{
  private boolean mIsWebViewAvailable;
  private WebView mWebView;
  
  public WebViewFragment() {}
  
  public WebView getWebView()
  {
    WebView localWebView;
    if (mIsWebViewAvailable) {
      localWebView = mWebView;
    } else {
      localWebView = null;
    }
    return localWebView;
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    if (mWebView != null) {
      mWebView.destroy();
    }
    mWebView = new WebView(getContext());
    mIsWebViewAvailable = true;
    return mWebView;
  }
  
  public void onDestroy()
  {
    if (mWebView != null)
    {
      mWebView.destroy();
      mWebView = null;
    }
    super.onDestroy();
  }
  
  public void onDestroyView()
  {
    mIsWebViewAvailable = false;
    super.onDestroyView();
  }
  
  public void onPause()
  {
    super.onPause();
    mWebView.onPause();
  }
  
  public void onResume()
  {
    mWebView.onResume();
    super.onResume();
  }
}
