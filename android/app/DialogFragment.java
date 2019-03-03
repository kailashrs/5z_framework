package android.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import java.io.FileDescriptor;
import java.io.PrintWriter;

@Deprecated
public class DialogFragment
  extends Fragment
  implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener
{
  private static final String SAVED_BACK_STACK_ID = "android:backStackId";
  private static final String SAVED_CANCELABLE = "android:cancelable";
  private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
  private static final String SAVED_SHOWS_DIALOG = "android:showsDialog";
  private static final String SAVED_STYLE = "android:style";
  private static final String SAVED_THEME = "android:theme";
  public static final int STYLE_NORMAL = 0;
  public static final int STYLE_NO_FRAME = 2;
  public static final int STYLE_NO_INPUT = 3;
  public static final int STYLE_NO_TITLE = 1;
  int mBackStackId = -1;
  boolean mCancelable = true;
  Dialog mDialog;
  boolean mDismissed;
  boolean mShownByMe;
  boolean mShowsDialog = true;
  int mStyle = 0;
  int mTheme = 0;
  boolean mViewDestroyed;
  
  public DialogFragment() {}
  
  public void dismiss()
  {
    dismissInternal(false);
  }
  
  public void dismissAllowingStateLoss()
  {
    dismissInternal(true);
  }
  
  void dismissInternal(boolean paramBoolean)
  {
    if (mDismissed) {
      return;
    }
    mDismissed = true;
    mShownByMe = false;
    if (mDialog != null)
    {
      mDialog.dismiss();
      mDialog = null;
    }
    mViewDestroyed = true;
    if (mBackStackId >= 0)
    {
      getFragmentManager().popBackStack(mBackStackId, 1);
      mBackStackId = -1;
    }
    else
    {
      FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
      localFragmentTransaction.remove(this);
      if (paramBoolean) {
        localFragmentTransaction.commitAllowingStateLoss();
      } else {
        localFragmentTransaction.commit();
      }
    }
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("DialogFragment:");
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("  mStyle=");
    paramPrintWriter.print(mStyle);
    paramPrintWriter.print(" mTheme=0x");
    paramPrintWriter.println(Integer.toHexString(mTheme));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("  mCancelable=");
    paramPrintWriter.print(mCancelable);
    paramPrintWriter.print(" mShowsDialog=");
    paramPrintWriter.print(mShowsDialog);
    paramPrintWriter.print(" mBackStackId=");
    paramPrintWriter.println(mBackStackId);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("  mDialog=");
    paramPrintWriter.println(mDialog);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("  mViewDestroyed=");
    paramPrintWriter.print(mViewDestroyed);
    paramPrintWriter.print(" mDismissed=");
    paramPrintWriter.print(mDismissed);
    paramPrintWriter.print(" mShownByMe=");
    paramPrintWriter.println(mShownByMe);
  }
  
  public Dialog getDialog()
  {
    return mDialog;
  }
  
  public boolean getShowsDialog()
  {
    return mShowsDialog;
  }
  
  public int getTheme()
  {
    return mTheme;
  }
  
  public boolean isCancelable()
  {
    return mCancelable;
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (!mShowsDialog) {
      return;
    }
    Object localObject = getView();
    if (localObject != null) {
      if (((View)localObject).getParent() == null) {
        mDialog.setContentView((View)localObject);
      } else {
        throw new IllegalStateException("DialogFragment can not be attached to a container view");
      }
    }
    localObject = getActivity();
    if (localObject != null) {
      mDialog.setOwnerActivity((Activity)localObject);
    }
    mDialog.setCancelable(mCancelable);
    if (mDialog.takeCancelAndDismissListeners("DialogFragment", this, this))
    {
      if (paramBundle != null)
      {
        paramBundle = paramBundle.getBundle("android:savedDialogState");
        if (paramBundle != null) {
          mDialog.onRestoreInstanceState(paramBundle);
        }
      }
      return;
    }
    throw new IllegalStateException("You can not set Dialog's OnCancelListener or OnDismissListener");
  }
  
  public void onAttach(Context paramContext)
  {
    super.onAttach(paramContext);
    if (!mShownByMe) {
      mDismissed = false;
    }
  }
  
  public void onCancel(DialogInterface paramDialogInterface) {}
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    boolean bool;
    if (mContainerId == 0) {
      bool = true;
    } else {
      bool = false;
    }
    mShowsDialog = bool;
    if (paramBundle != null)
    {
      mStyle = paramBundle.getInt("android:style", 0);
      mTheme = paramBundle.getInt("android:theme", 0);
      mCancelable = paramBundle.getBoolean("android:cancelable", true);
      mShowsDialog = paramBundle.getBoolean("android:showsDialog", mShowsDialog);
      mBackStackId = paramBundle.getInt("android:backStackId", -1);
    }
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    return new Dialog(getActivity(), getTheme());
  }
  
  public void onDestroyView()
  {
    super.onDestroyView();
    if (mDialog != null)
    {
      mViewDestroyed = true;
      mDialog.dismiss();
      mDialog = null;
    }
  }
  
  public void onDetach()
  {
    super.onDetach();
    if ((!mShownByMe) && (!mDismissed)) {
      mDismissed = true;
    }
  }
  
  public void onDismiss(DialogInterface paramDialogInterface)
  {
    if (!mViewDestroyed) {
      dismissInternal(true);
    }
  }
  
  public LayoutInflater onGetLayoutInflater(Bundle paramBundle)
  {
    if (!mShowsDialog) {
      return super.onGetLayoutInflater(paramBundle);
    }
    mDialog = onCreateDialog(paramBundle);
    switch (mStyle)
    {
    default: 
      break;
    case 3: 
      mDialog.getWindow().addFlags(24);
    case 1: 
    case 2: 
      mDialog.requestWindowFeature(1);
    }
    if (mDialog != null) {
      return (LayoutInflater)mDialog.getContext().getSystemService("layout_inflater");
    }
    return (LayoutInflater)mHost.getContext().getSystemService("layout_inflater");
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (mDialog != null)
    {
      Bundle localBundle = mDialog.onSaveInstanceState();
      if (localBundle != null) {
        paramBundle.putBundle("android:savedDialogState", localBundle);
      }
    }
    if (mStyle != 0) {
      paramBundle.putInt("android:style", mStyle);
    }
    if (mTheme != 0) {
      paramBundle.putInt("android:theme", mTheme);
    }
    if (!mCancelable) {
      paramBundle.putBoolean("android:cancelable", mCancelable);
    }
    if (!mShowsDialog) {
      paramBundle.putBoolean("android:showsDialog", mShowsDialog);
    }
    if (mBackStackId != -1) {
      paramBundle.putInt("android:backStackId", mBackStackId);
    }
  }
  
  public void onStart()
  {
    super.onStart();
    if (mDialog != null)
    {
      mViewDestroyed = false;
      mDialog.show();
    }
  }
  
  public void onStop()
  {
    super.onStop();
    if (mDialog != null) {
      mDialog.hide();
    }
  }
  
  public void setCancelable(boolean paramBoolean)
  {
    mCancelable = paramBoolean;
    if (mDialog != null) {
      mDialog.setCancelable(paramBoolean);
    }
  }
  
  public void setShowsDialog(boolean paramBoolean)
  {
    mShowsDialog = paramBoolean;
  }
  
  public void setStyle(int paramInt1, int paramInt2)
  {
    mStyle = paramInt1;
    if ((mStyle == 2) || (mStyle == 3)) {
      mTheme = 16975018;
    }
    if (paramInt2 != 0) {
      mTheme = paramInt2;
    }
  }
  
  public int show(FragmentTransaction paramFragmentTransaction, String paramString)
  {
    mDismissed = false;
    mShownByMe = true;
    paramFragmentTransaction.add(this, paramString);
    mViewDestroyed = false;
    mBackStackId = paramFragmentTransaction.commit();
    return mBackStackId;
  }
  
  public void show(FragmentManager paramFragmentManager, String paramString)
  {
    mDismissed = false;
    mShownByMe = true;
    paramFragmentManager = paramFragmentManager.beginTransaction();
    paramFragmentManager.add(this, paramString);
    paramFragmentManager.commit();
  }
  
  public void showAllowingStateLoss(FragmentManager paramFragmentManager, String paramString)
  {
    mDismissed = false;
    mShownByMe = true;
    paramFragmentManager = paramFragmentManager.beginTransaction();
    paramFragmentManager.add(this, paramString);
    paramFragmentManager.commitAllowingStateLoss();
  }
}
