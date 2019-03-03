package com.android.internal.view;

import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.MergedConfiguration;
import android.view.DisplayCutout.ParcelableWrapper;
import android.view.DragEvent;
import android.view.IWindow.Stub;
import android.view.IWindowSession;
import com.android.internal.os.IResultReceiver;

public class BaseIWindow
  extends IWindow.Stub
{
  public int mSeq;
  private IWindowSession mSession;
  
  public BaseIWindow() {}
  
  public void closeSystemDialogs(String paramString) {}
  
  public void dispatchAppVisibility(boolean paramBoolean) {}
  
  public void dispatchDragEvent(DragEvent paramDragEvent)
  {
    if (paramDragEvent.getAction() == 3) {
      try
      {
        mSession.reportDropResult(this, false);
      }
      catch (RemoteException paramDragEvent) {}
    }
  }
  
  public void dispatchGetNewSurface() {}
  
  public void dispatchPointerCaptureChanged(boolean paramBoolean) {}
  
  public void dispatchSystemUiVisibilityChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mSeq = paramInt1;
  }
  
  public void dispatchWallpaperCommand(String paramString, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle, boolean paramBoolean)
  {
    if (paramBoolean) {
      try
      {
        mSession.wallpaperCommandComplete(asBinder(), null);
      }
      catch (RemoteException paramString) {}
    }
  }
  
  public void dispatchWallpaperOffsets(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, boolean paramBoolean)
  {
    if (paramBoolean) {
      try
      {
        mSession.wallpaperOffsetsComplete(asBinder());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  public void dispatchWindowShown() {}
  
  public void executeCommand(String paramString1, String paramString2, ParcelFileDescriptor paramParcelFileDescriptor) {}
  
  public void moved(int paramInt1, int paramInt2) {}
  
  public void requestAppKeyboardShortcuts(IResultReceiver paramIResultReceiver, int paramInt) {}
  
  public void resized(Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, Rect paramRect5, Rect paramRect6, boolean paramBoolean1, MergedConfiguration paramMergedConfiguration, Rect paramRect7, boolean paramBoolean2, boolean paramBoolean3, int paramInt, DisplayCutout.ParcelableWrapper paramParcelableWrapper)
  {
    if (paramBoolean1) {
      try
      {
        mSession.finishDrawing(this);
      }
      catch (RemoteException paramRect1) {}
    }
  }
  
  public void setSession(IWindowSession paramIWindowSession)
  {
    mSession = paramIWindowSession;
  }
  
  public void updatePointerIcon(float paramFloat1, float paramFloat2)
  {
    InputManager.getInstance().setPointerIconType(1);
  }
  
  public void windowFocusChanged(boolean paramBoolean1, boolean paramBoolean2) {}
}
