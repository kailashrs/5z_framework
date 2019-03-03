package android.webkit;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

@SystemApi
public class FindActionModeCallback
  implements ActionMode.Callback, TextWatcher, View.OnClickListener, WebView.FindListener
{
  private ActionMode mActionMode;
  private int mActiveMatchIndex;
  private View mCustomView;
  private EditText mEditText;
  private Point mGlobalVisibleOffset = new Point();
  private Rect mGlobalVisibleRect = new Rect();
  private InputMethodManager mInput;
  private TextView mMatches;
  private boolean mMatchesFound;
  private int mNumberOfMatches;
  private Resources mResources;
  private WebView mWebView;
  
  public FindActionModeCallback(Context paramContext)
  {
    mCustomView = LayoutInflater.from(paramContext).inflate(17367357, null);
    mEditText = ((EditText)mCustomView.findViewById(16908291));
    mEditText.setCustomSelectionActionModeCallback(new NoAction());
    mEditText.setOnClickListener(this);
    setText("");
    mMatches = ((TextView)mCustomView.findViewById(16909106));
    mInput = ((InputMethodManager)paramContext.getSystemService(InputMethodManager.class));
    mResources = paramContext.getResources();
  }
  
  private void findNext(boolean paramBoolean)
  {
    if (mWebView != null)
    {
      if (!mMatchesFound)
      {
        findAll();
        return;
      }
      if (mNumberOfMatches == 0) {
        return;
      }
      mWebView.findNext(paramBoolean);
      updateMatchesString();
      return;
    }
    throw new AssertionError("No WebView for FindActionModeCallback::findNext");
  }
  
  private void updateMatchesString()
  {
    if (mNumberOfMatches == 0) {
      mMatches.setText(17040475);
    } else {
      mMatches.setText(mResources.getQuantityString(18153492, mNumberOfMatches, new Object[] { Integer.valueOf(mActiveMatchIndex + 1), Integer.valueOf(mNumberOfMatches) }));
    }
    mMatches.setVisibility(0);
  }
  
  public void afterTextChanged(Editable paramEditable) {}
  
  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public void findAll()
  {
    if (mWebView != null)
    {
      Editable localEditable = mEditText.getText();
      if (localEditable.length() == 0)
      {
        mWebView.clearMatches();
        mMatches.setVisibility(8);
        mMatchesFound = false;
        mWebView.findAll(null);
      }
      else
      {
        mMatchesFound = true;
        mMatches.setVisibility(4);
        mNumberOfMatches = 0;
        mWebView.findAllAsync(localEditable.toString());
      }
      return;
    }
    throw new AssertionError("No WebView for FindActionModeCallback::findAll");
  }
  
  public void finish()
  {
    mActionMode.finish();
  }
  
  public int getActionModeGlobalBottom()
  {
    if (mActionMode == null) {
      return 0;
    }
    View localView1 = (View)mCustomView.getParent();
    View localView2 = localView1;
    if (localView1 == null) {
      localView2 = mCustomView;
    }
    localView2.getGlobalVisibleRect(mGlobalVisibleRect, mGlobalVisibleOffset);
    return mGlobalVisibleRect.bottom;
  }
  
  public boolean onActionItemClicked(ActionMode paramActionMode, MenuItem paramMenuItem)
  {
    if (mWebView != null)
    {
      mInput.hideSoftInputFromWindow(mWebView.getWindowToken(), 0);
      switch (paramMenuItem.getItemId())
      {
      default: 
        return false;
      case 16908943: 
        findNext(false);
        break;
      case 16908942: 
        findNext(true);
      }
      return true;
    }
    throw new AssertionError("No WebView for FindActionModeCallback::onActionItemClicked");
  }
  
  public void onClick(View paramView)
  {
    findNext(true);
  }
  
  public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu)
  {
    if (!paramActionMode.isUiFocusable()) {
      return false;
    }
    paramActionMode.setCustomView(mCustomView);
    paramActionMode.getMenuInflater().inflate(18087938, paramMenu);
    mActionMode = paramActionMode;
    paramActionMode = mEditText.getText();
    Selection.setSelection(paramActionMode, paramActionMode.length());
    mMatches.setVisibility(8);
    mMatchesFound = false;
    mMatches.setText("0");
    mEditText.requestFocus();
    return true;
  }
  
  public void onDestroyActionMode(ActionMode paramActionMode)
  {
    mActionMode = null;
    mWebView.notifyFindDialogDismissed();
    mWebView.setFindDialogFindListener(null);
    mInput.hideSoftInputFromWindow(mWebView.getWindowToken(), 0);
  }
  
  public void onFindResultReceived(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (paramInt2 == 0) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      updateMatchCount(paramInt1, paramInt2, paramBoolean);
    }
  }
  
  public boolean onPrepareActionMode(ActionMode paramActionMode, Menu paramMenu)
  {
    return false;
  }
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    findAll();
  }
  
  public void setText(String paramString)
  {
    mEditText.setText(paramString);
    paramString = mEditText.getText();
    int i = paramString.length();
    Selection.setSelection(paramString, i, i);
    paramString.setSpan(this, 0, i, 18);
    mMatchesFound = false;
  }
  
  public void setWebView(WebView paramWebView)
  {
    if (paramWebView != null)
    {
      mWebView = paramWebView;
      mWebView.setFindDialogFindListener(this);
      return;
    }
    throw new AssertionError("WebView supplied to FindActionModeCallback cannot be null");
  }
  
  public void showSoftInput()
  {
    if (mEditText.requestFocus()) {
      mInput.showSoftInput(mEditText, 0);
    }
  }
  
  public void updateMatchCount(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      mNumberOfMatches = paramInt2;
      mActiveMatchIndex = paramInt1;
      updateMatchesString();
    }
    else
    {
      mMatches.setVisibility(8);
      mNumberOfMatches = 0;
    }
  }
  
  public static class NoAction
    implements ActionMode.Callback
  {
    public NoAction() {}
    
    public boolean onActionItemClicked(ActionMode paramActionMode, MenuItem paramMenuItem)
    {
      return false;
    }
    
    public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      return false;
    }
    
    public void onDestroyActionMode(ActionMode paramActionMode) {}
    
    public boolean onPrepareActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      return false;
    }
  }
}
