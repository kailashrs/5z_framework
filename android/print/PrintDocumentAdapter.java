package android.print;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;

public abstract class PrintDocumentAdapter
{
  public static final String EXTRA_PRINT_PREVIEW = "EXTRA_PRINT_PREVIEW";
  
  public PrintDocumentAdapter() {}
  
  public void onFinish() {}
  
  public abstract void onLayout(PrintAttributes paramPrintAttributes1, PrintAttributes paramPrintAttributes2, CancellationSignal paramCancellationSignal, LayoutResultCallback paramLayoutResultCallback, Bundle paramBundle);
  
  public void onStart() {}
  
  public abstract void onWrite(PageRange[] paramArrayOfPageRange, ParcelFileDescriptor paramParcelFileDescriptor, CancellationSignal paramCancellationSignal, WriteResultCallback paramWriteResultCallback);
  
  public static abstract class LayoutResultCallback
  {
    public LayoutResultCallback() {}
    
    public void onLayoutCancelled() {}
    
    public void onLayoutFailed(CharSequence paramCharSequence) {}
    
    public void onLayoutFinished(PrintDocumentInfo paramPrintDocumentInfo, boolean paramBoolean) {}
  }
  
  public static abstract class WriteResultCallback
  {
    public WriteResultCallback() {}
    
    public void onWriteCancelled() {}
    
    public void onWriteFailed(CharSequence paramCharSequence) {}
    
    public void onWriteFinished(PageRange[] paramArrayOfPageRange) {}
  }
}
