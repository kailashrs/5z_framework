package android.media.midi;

import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.midi.MidiDispatcher;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

public final class MidiOutputPort
  extends MidiSender
  implements Closeable
{
  private static final String TAG = "MidiOutputPort";
  private IMidiDeviceServer mDeviceServer;
  private final MidiDispatcher mDispatcher = new MidiDispatcher();
  private final CloseGuard mGuard = CloseGuard.get();
  private final FileInputStream mInputStream;
  private boolean mIsClosed;
  private final int mPortNumber;
  private final Thread mThread = new Thread()
  {
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: sipush 1024
      //   3: newarray byte
      //   5: astore_1
      //   6: aload_0
      //   7: getfield 12	android/media/midi/MidiOutputPort$1:this$0	Landroid/media/midi/MidiOutputPort;
      //   10: invokestatic 23	android/media/midi/MidiOutputPort:access$000	(Landroid/media/midi/MidiOutputPort;)Ljava/io/FileInputStream;
      //   13: aload_1
      //   14: invokevirtual 29	java/io/FileInputStream:read	([B)I
      //   17: istore_2
      //   18: iload_2
      //   19: ifge +6 -> 25
      //   22: goto +145 -> 167
      //   25: aload_1
      //   26: iload_2
      //   27: invokestatic 35	android/media/midi/MidiPortImpl:getPacketType	([BI)I
      //   30: istore_3
      //   31: iload_3
      //   32: tableswitch	default:+24->56, 1:+40->72, 2:+27->59
      //   56: goto +55 -> 111
      //   59: aload_0
      //   60: getfield 12	android/media/midi/MidiOutputPort$1:this$0	Landroid/media/midi/MidiOutputPort;
      //   63: invokestatic 39	android/media/midi/MidiOutputPort:access$100	(Landroid/media/midi/MidiOutputPort;)Lcom/android/internal/midi/MidiDispatcher;
      //   66: invokevirtual 44	com/android/internal/midi/MidiDispatcher:flush	()V
      //   69: goto +78 -> 147
      //   72: aload_1
      //   73: iload_2
      //   74: invokestatic 47	android/media/midi/MidiPortImpl:getDataOffset	([BI)I
      //   77: istore_3
      //   78: aload_1
      //   79: iload_2
      //   80: invokestatic 50	android/media/midi/MidiPortImpl:getDataSize	([BI)I
      //   83: istore 4
      //   85: aload_1
      //   86: iload_2
      //   87: invokestatic 54	android/media/midi/MidiPortImpl:getPacketTimestamp	([BI)J
      //   90: lstore 5
      //   92: aload_0
      //   93: getfield 12	android/media/midi/MidiOutputPort$1:this$0	Landroid/media/midi/MidiOutputPort;
      //   96: invokestatic 39	android/media/midi/MidiOutputPort:access$100	(Landroid/media/midi/MidiOutputPort;)Lcom/android/internal/midi/MidiDispatcher;
      //   99: aload_1
      //   100: iload_3
      //   101: iload 4
      //   103: lload 5
      //   105: invokevirtual 58	com/android/internal/midi/MidiDispatcher:send	([BIIJ)V
      //   108: goto +39 -> 147
      //   111: new 60	java/lang/StringBuilder
      //   114: astore 7
      //   116: aload 7
      //   118: invokespecial 61	java/lang/StringBuilder:<init>	()V
      //   121: aload 7
      //   123: ldc 63
      //   125: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   128: pop
      //   129: aload 7
      //   131: iload_3
      //   132: invokevirtual 70	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   135: pop
      //   136: ldc 72
      //   138: aload 7
      //   140: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   143: invokestatic 82	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   146: pop
      //   147: goto -141 -> 6
      //   150: astore 7
      //   152: goto +26 -> 178
      //   155: astore 7
      //   157: ldc 72
      //   159: ldc 84
      //   161: aload 7
      //   163: invokestatic 87	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   166: pop
      //   167: aload_0
      //   168: getfield 12	android/media/midi/MidiOutputPort$1:this$0	Landroid/media/midi/MidiOutputPort;
      //   171: invokestatic 23	android/media/midi/MidiOutputPort:access$000	(Landroid/media/midi/MidiOutputPort;)Ljava/io/FileInputStream;
      //   174: invokestatic 93	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   177: return
      //   178: aload_0
      //   179: getfield 12	android/media/midi/MidiOutputPort$1:this$0	Landroid/media/midi/MidiOutputPort;
      //   182: invokestatic 23	android/media/midi/MidiOutputPort:access$000	(Landroid/media/midi/MidiOutputPort;)Ljava/io/FileInputStream;
      //   185: invokestatic 93	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   188: aload 7
      //   190: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	191	0	this	1
      //   5	95	1	arrayOfByte	byte[]
      //   17	70	2	i	int
      //   30	102	3	j	int
      //   83	19	4	k	int
      //   90	14	5	l	long
      //   114	25	7	localStringBuilder	StringBuilder
      //   150	1	7	localObject	Object
      //   155	34	7	localIOException	IOException
      // Exception table:
      //   from	to	target	type
      //   6	18	150	finally
      //   25	31	150	finally
      //   59	69	150	finally
      //   72	108	150	finally
      //   111	147	150	finally
      //   157	167	150	finally
      //   6	18	155	java/io/IOException
      //   25	31	155	java/io/IOException
      //   59	69	155	java/io/IOException
      //   72	108	155	java/io/IOException
      //   111	147	155	java/io/IOException
    }
  };
  private final IBinder mToken;
  
  MidiOutputPort(IMidiDeviceServer paramIMidiDeviceServer, IBinder paramIBinder, FileDescriptor paramFileDescriptor, int paramInt)
  {
    mDeviceServer = paramIMidiDeviceServer;
    mToken = paramIBinder;
    mPortNumber = paramInt;
    mInputStream = new ParcelFileDescriptor.AutoCloseInputStream(new ParcelFileDescriptor(paramFileDescriptor));
    mThread.start();
    mGuard.open("close");
  }
  
  MidiOutputPort(FileDescriptor paramFileDescriptor, int paramInt)
  {
    this(null, null, paramFileDescriptor, paramInt);
  }
  
  public void close()
    throws IOException
  {
    synchronized (mGuard)
    {
      if (mIsClosed) {
        return;
      }
      mGuard.close();
      mInputStream.close();
      IMidiDeviceServer localIMidiDeviceServer = mDeviceServer;
      if (localIMidiDeviceServer != null) {
        try
        {
          mDeviceServer.closePort(mToken);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MidiOutputPort", "RemoteException in MidiOutputPort.close()");
        }
      }
      mIsClosed = true;
      return;
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mGuard != null) {
        mGuard.warnIfOpen();
      }
      mDeviceServer = null;
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public final int getPortNumber()
  {
    return mPortNumber;
  }
  
  public void onConnect(MidiReceiver paramMidiReceiver)
  {
    mDispatcher.getSender().connect(paramMidiReceiver);
  }
  
  public void onDisconnect(MidiReceiver paramMidiReceiver)
  {
    mDispatcher.getSender().disconnect(paramMidiReceiver);
  }
}
