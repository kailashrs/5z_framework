package android.os;

public final class CancellationSignal
{
  private boolean mCancelInProgress;
  private boolean mIsCanceled;
  private OnCancelListener mOnCancelListener;
  private ICancellationSignal mRemote;
  
  public CancellationSignal() {}
  
  public static ICancellationSignal createTransport()
  {
    return new Transport(null);
  }
  
  public static CancellationSignal fromTransport(ICancellationSignal paramICancellationSignal)
  {
    if ((paramICancellationSignal instanceof Transport)) {
      return mCancellationSignal;
    }
    return null;
  }
  
  private void waitForCancelFinishedLocked()
  {
    for (;;)
    {
      if (mCancelInProgress) {
        try
        {
          wait();
        }
        catch (InterruptedException localInterruptedException)
        {
          for (;;) {}
        }
      }
    }
  }
  
  /* Error */
  public void cancel()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 48	android/os/CancellationSignal:mIsCanceled	Z
    //   6: ifeq +6 -> 12
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: aload_0
    //   13: iconst_1
    //   14: putfield 48	android/os/CancellationSignal:mIsCanceled	Z
    //   17: aload_0
    //   18: iconst_1
    //   19: putfield 40	android/os/CancellationSignal:mCancelInProgress	Z
    //   22: aload_0
    //   23: getfield 50	android/os/CancellationSignal:mOnCancelListener	Landroid/os/CancellationSignal$OnCancelListener;
    //   26: astore_1
    //   27: aload_0
    //   28: getfield 52	android/os/CancellationSignal:mRemote	Landroid/os/ICancellationSignal;
    //   31: astore_2
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: ifnull +16 -> 51
    //   38: aload_1
    //   39: invokeinterface 55 1 0
    //   44: goto +7 -> 51
    //   47: astore_2
    //   48: goto +16 -> 64
    //   51: aload_2
    //   52: ifnull +33 -> 85
    //   55: aload_2
    //   56: invokeinterface 59 1 0
    //   61: goto +24 -> 85
    //   64: aload_0
    //   65: monitorenter
    //   66: aload_0
    //   67: iconst_0
    //   68: putfield 40	android/os/CancellationSignal:mCancelInProgress	Z
    //   71: aload_0
    //   72: invokevirtual 62	java/lang/Object:notifyAll	()V
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_2
    //   78: athrow
    //   79: astore_2
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_2
    //   83: athrow
    //   84: astore_2
    //   85: aload_0
    //   86: monitorenter
    //   87: aload_0
    //   88: iconst_0
    //   89: putfield 40	android/os/CancellationSignal:mCancelInProgress	Z
    //   92: aload_0
    //   93: invokevirtual 62	java/lang/Object:notifyAll	()V
    //   96: aload_0
    //   97: monitorexit
    //   98: return
    //   99: astore_2
    //   100: aload_0
    //   101: monitorexit
    //   102: aload_2
    //   103: athrow
    //   104: astore_2
    //   105: aload_0
    //   106: monitorexit
    //   107: aload_2
    //   108: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	109	0	this	CancellationSignal
    //   26	13	1	localOnCancelListener	OnCancelListener
    //   31	1	2	localICancellationSignal	ICancellationSignal
    //   47	31	2	localObject1	Object
    //   79	4	2	localObject2	Object
    //   84	1	2	localRemoteException	RemoteException
    //   99	4	2	localObject3	Object
    //   104	4	2	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   38	44	47	finally
    //   55	61	47	finally
    //   66	77	79	finally
    //   80	82	79	finally
    //   55	61	84	android/os/RemoteException
    //   87	98	99	finally
    //   100	102	99	finally
    //   2	11	104	finally
    //   12	34	104	finally
    //   105	107	104	finally
  }
  
  public boolean isCanceled()
  {
    try
    {
      boolean bool = mIsCanceled;
      return bool;
    }
    finally {}
  }
  
  public void setOnCancelListener(OnCancelListener paramOnCancelListener)
  {
    try
    {
      waitForCancelFinishedLocked();
      if (mOnCancelListener == paramOnCancelListener) {
        return;
      }
      mOnCancelListener = paramOnCancelListener;
      if ((mIsCanceled) && (paramOnCancelListener != null))
      {
        paramOnCancelListener.onCancel();
        return;
      }
      return;
    }
    finally {}
  }
  
  public void setRemote(ICancellationSignal paramICancellationSignal)
  {
    try
    {
      waitForCancelFinishedLocked();
      if (mRemote == paramICancellationSignal) {
        return;
      }
      mRemote = paramICancellationSignal;
      if ((mIsCanceled) && (paramICancellationSignal != null))
      {
        try
        {
          paramICancellationSignal.cancel();
        }
        catch (RemoteException paramICancellationSignal) {}
        return;
      }
      return;
    }
    finally {}
  }
  
  public void throwIfCanceled()
  {
    if (!isCanceled()) {
      return;
    }
    throw new OperationCanceledException();
  }
  
  public static abstract interface OnCancelListener
  {
    public abstract void onCancel();
  }
  
  private static final class Transport
    extends ICancellationSignal.Stub
  {
    final CancellationSignal mCancellationSignal = new CancellationSignal();
    
    private Transport() {}
    
    public void cancel()
      throws RemoteException
    {
      mCancellationSignal.cancel();
    }
  }
}
