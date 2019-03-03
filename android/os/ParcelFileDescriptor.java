package android.os;

import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.ByteOrder;
import libcore.io.IoUtils;
import libcore.io.Memory;

public class ParcelFileDescriptor
  implements Parcelable, Closeable
{
  public static final Parcelable.Creator<ParcelFileDescriptor> CREATOR = new Parcelable.Creator()
  {
    public ParcelFileDescriptor createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      FileDescriptor localFileDescriptor1 = paramAnonymousParcel.readRawFileDescriptor();
      FileDescriptor localFileDescriptor2 = null;
      if (i != 0) {
        localFileDescriptor2 = paramAnonymousParcel.readRawFileDescriptor();
      }
      return new ParcelFileDescriptor(localFileDescriptor1, localFileDescriptor2);
    }
    
    public ParcelFileDescriptor[] newArray(int paramAnonymousInt)
    {
      return new ParcelFileDescriptor[paramAnonymousInt];
    }
  };
  private static final int MAX_STATUS = 1024;
  public static final int MODE_APPEND = 33554432;
  public static final int MODE_CREATE = 134217728;
  public static final int MODE_READ_ONLY = 268435456;
  public static final int MODE_READ_WRITE = 805306368;
  public static final int MODE_TRUNCATE = 67108864;
  @Deprecated
  public static final int MODE_WORLD_READABLE = 1;
  @Deprecated
  public static final int MODE_WORLD_WRITEABLE = 2;
  public static final int MODE_WRITE_ONLY = 536870912;
  private static final String TAG = "ParcelFileDescriptor";
  private volatile boolean mClosed;
  private FileDescriptor mCommFd;
  private final FileDescriptor mFd;
  private final CloseGuard mGuard = CloseGuard.get();
  private Status mStatus;
  private byte[] mStatusBuf;
  private final ParcelFileDescriptor mWrapped;
  
  public ParcelFileDescriptor(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    mWrapped = paramParcelFileDescriptor;
    mFd = null;
    mCommFd = null;
    mClosed = true;
  }
  
  public ParcelFileDescriptor(FileDescriptor paramFileDescriptor)
  {
    this(paramFileDescriptor, null);
  }
  
  public ParcelFileDescriptor(FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2)
  {
    if (paramFileDescriptor1 != null)
    {
      mWrapped = null;
      mFd = paramFileDescriptor1;
      mCommFd = paramFileDescriptor2;
      mGuard.open("close");
      return;
    }
    throw new NullPointerException("FileDescriptor must not be null");
  }
  
  public static ParcelFileDescriptor adoptFd(int paramInt)
  {
    FileDescriptor localFileDescriptor = new FileDescriptor();
    localFileDescriptor.setInt$(paramInt);
    return new ParcelFileDescriptor(localFileDescriptor);
  }
  
  private void closeWithStatus(int paramInt, String paramString)
  {
    if (mClosed) {
      return;
    }
    mClosed = true;
    if (mGuard != null) {
      mGuard.close();
    }
    writeCommStatusAndClose(paramInt, paramString);
    IoUtils.closeQuietly(mFd);
    releaseResources();
  }
  
  private static FileDescriptor[] createCommSocketPair()
    throws IOException
  {
    try
    {
      FileDescriptor localFileDescriptor1 = new java/io/FileDescriptor;
      localFileDescriptor1.<init>();
      FileDescriptor localFileDescriptor2 = new java/io/FileDescriptor;
      localFileDescriptor2.<init>();
      Os.socketpair(OsConstants.AF_UNIX, OsConstants.SOCK_SEQPACKET, 0, localFileDescriptor1, localFileDescriptor2);
      IoUtils.setBlocking(localFileDescriptor1, false);
      IoUtils.setBlocking(localFileDescriptor2, false);
      return new FileDescriptor[] { localFileDescriptor1, localFileDescriptor2 };
    }
    catch (ErrnoException localErrnoException)
    {
      throw localErrnoException.rethrowAsIOException();
    }
  }
  
  public static ParcelFileDescriptor[] createPipe()
    throws IOException
  {
    try
    {
      Object localObject = Os.pipe();
      ParcelFileDescriptor localParcelFileDescriptor = new android/os/ParcelFileDescriptor;
      localParcelFileDescriptor.<init>(localObject[0]);
      localObject = new ParcelFileDescriptor(localObject[1]);
      return new ParcelFileDescriptor[] { localParcelFileDescriptor, localObject };
    }
    catch (ErrnoException localErrnoException)
    {
      throw localErrnoException.rethrowAsIOException();
    }
  }
  
  public static ParcelFileDescriptor[] createReliablePipe()
    throws IOException
  {
    try
    {
      Object localObject = createCommSocketPair();
      FileDescriptor[] arrayOfFileDescriptor = Os.pipe();
      ParcelFileDescriptor localParcelFileDescriptor = new android/os/ParcelFileDescriptor;
      localParcelFileDescriptor.<init>(arrayOfFileDescriptor[0], localObject[0]);
      localObject = new ParcelFileDescriptor(arrayOfFileDescriptor[1], localObject[1]);
      return new ParcelFileDescriptor[] { localParcelFileDescriptor, localObject };
    }
    catch (ErrnoException localErrnoException)
    {
      throw localErrnoException.rethrowAsIOException();
    }
  }
  
  public static ParcelFileDescriptor[] createReliableSocketPair()
    throws IOException
  {
    return createReliableSocketPair(OsConstants.SOCK_STREAM);
  }
  
  public static ParcelFileDescriptor[] createReliableSocketPair(int paramInt)
    throws IOException
  {
    try
    {
      FileDescriptor[] arrayOfFileDescriptor = createCommSocketPair();
      FileDescriptor localFileDescriptor = new java/io/FileDescriptor;
      localFileDescriptor.<init>();
      Object localObject = new java/io/FileDescriptor;
      ((FileDescriptor)localObject).<init>();
      Os.socketpair(OsConstants.AF_UNIX, paramInt, 0, localFileDescriptor, (FileDescriptor)localObject);
      ParcelFileDescriptor localParcelFileDescriptor = new android/os/ParcelFileDescriptor;
      localParcelFileDescriptor.<init>(localFileDescriptor, arrayOfFileDescriptor[0]);
      localObject = new ParcelFileDescriptor((FileDescriptor)localObject, arrayOfFileDescriptor[1]);
      return new ParcelFileDescriptor[] { localParcelFileDescriptor, localObject };
    }
    catch (ErrnoException localErrnoException)
    {
      throw localErrnoException.rethrowAsIOException();
    }
  }
  
  public static ParcelFileDescriptor[] createSocketPair()
    throws IOException
  {
    return createSocketPair(OsConstants.SOCK_STREAM);
  }
  
  public static ParcelFileDescriptor[] createSocketPair(int paramInt)
    throws IOException
  {
    try
    {
      Object localObject = new java/io/FileDescriptor;
      ((FileDescriptor)localObject).<init>();
      FileDescriptor localFileDescriptor = new java/io/FileDescriptor;
      localFileDescriptor.<init>();
      Os.socketpair(OsConstants.AF_UNIX, paramInt, 0, (FileDescriptor)localObject, localFileDescriptor);
      ParcelFileDescriptor localParcelFileDescriptor = new android/os/ParcelFileDescriptor;
      localParcelFileDescriptor.<init>((FileDescriptor)localObject);
      localObject = new ParcelFileDescriptor(localFileDescriptor);
      return new ParcelFileDescriptor[] { localParcelFileDescriptor, localObject };
    }
    catch (ErrnoException localErrnoException)
    {
      throw localErrnoException.rethrowAsIOException();
    }
  }
  
  public static ParcelFileDescriptor dup(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    try
    {
      paramFileDescriptor = new ParcelFileDescriptor(Os.dup(paramFileDescriptor));
      return paramFileDescriptor;
    }
    catch (ErrnoException paramFileDescriptor)
    {
      throw paramFileDescriptor.rethrowAsIOException();
    }
  }
  
  @Deprecated
  public static ParcelFileDescriptor fromData(byte[] paramArrayOfByte, String paramString)
    throws IOException
  {
    Object localObject = null;
    if (paramArrayOfByte == null) {
      return null;
    }
    paramString = new MemoryFile(paramString, paramArrayOfByte.length);
    if (paramArrayOfByte.length > 0) {
      paramString.writeBytes(paramArrayOfByte, 0, 0, paramArrayOfByte.length);
    }
    paramString.deactivate();
    paramString = paramString.getFileDescriptor();
    paramArrayOfByte = localObject;
    if (paramString != null) {
      paramArrayOfByte = new ParcelFileDescriptor(paramString);
    }
    return paramArrayOfByte;
  }
  
  public static ParcelFileDescriptor fromDatagramSocket(DatagramSocket paramDatagramSocket)
  {
    paramDatagramSocket = paramDatagramSocket.getFileDescriptor$();
    if (paramDatagramSocket != null) {
      paramDatagramSocket = new ParcelFileDescriptor(paramDatagramSocket);
    } else {
      paramDatagramSocket = null;
    }
    return paramDatagramSocket;
  }
  
  public static ParcelFileDescriptor fromFd(int paramInt)
    throws IOException
  {
    Object localObject = new FileDescriptor();
    ((FileDescriptor)localObject).setInt$(paramInt);
    try
    {
      localObject = new ParcelFileDescriptor(Os.dup((FileDescriptor)localObject));
      return localObject;
    }
    catch (ErrnoException localErrnoException)
    {
      throw localErrnoException.rethrowAsIOException();
    }
  }
  
  public static ParcelFileDescriptor fromFd(FileDescriptor paramFileDescriptor, Handler paramHandler, final OnCloseListener paramOnCloseListener)
    throws IOException
  {
    if (paramHandler != null)
    {
      if (paramOnCloseListener != null)
      {
        FileDescriptor[] arrayOfFileDescriptor = createCommSocketPair();
        paramFileDescriptor = new ParcelFileDescriptor(paramFileDescriptor, arrayOfFileDescriptor[0]);
        paramHandler = paramHandler.getLooper().getQueue();
        paramHandler.addOnFileDescriptorEventListener(arrayOfFileDescriptor[1], 1, new MessageQueue.OnFileDescriptorEventListener()
        {
          public int onFileDescriptorEvents(FileDescriptor paramAnonymousFileDescriptor, int paramAnonymousInt)
          {
            ParcelFileDescriptor.Status localStatus = null;
            if ((paramAnonymousInt & 0x1) != 0) {
              localStatus = ParcelFileDescriptor.readCommStatus(paramAnonymousFileDescriptor, new byte['Ѐ']);
            } else if ((paramAnonymousInt & 0x4) != 0) {
              localStatus = new ParcelFileDescriptor.Status(-2);
            }
            if (localStatus != null)
            {
              removeOnFileDescriptorEventListener(paramAnonymousFileDescriptor);
              IoUtils.closeQuietly(paramAnonymousFileDescriptor);
              paramOnCloseListener.onClose(localStatus.asIOException());
              return 0;
            }
            return 1;
          }
        });
        return paramFileDescriptor;
      }
      throw new IllegalArgumentException("Listener must not be null");
    }
    throw new IllegalArgumentException("Handler must not be null");
  }
  
  public static ParcelFileDescriptor fromSocket(Socket paramSocket)
  {
    paramSocket = paramSocket.getFileDescriptor$();
    if (paramSocket != null) {
      paramSocket = new ParcelFileDescriptor(paramSocket);
    } else {
      paramSocket = null;
    }
    return paramSocket;
  }
  
  public static File getFile(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("/proc/self/fd/");
      localStringBuilder.append(paramFileDescriptor.getInt$());
      String str = Os.readlink(localStringBuilder.toString());
      if (OsConstants.S_ISREG(statst_mode)) {
        return new File(str);
      }
      paramFileDescriptor = new java/io/IOException;
      localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Not a regular file: ");
      localStringBuilder.append(str);
      paramFileDescriptor.<init>(localStringBuilder.toString());
      throw paramFileDescriptor;
    }
    catch (ErrnoException paramFileDescriptor)
    {
      throw paramFileDescriptor.rethrowAsIOException();
    }
  }
  
  private byte[] getOrCreateStatusBuffer()
  {
    if (mStatusBuf == null) {
      mStatusBuf = new byte['Ѐ'];
    }
    return mStatusBuf;
  }
  
  public static ParcelFileDescriptor open(File paramFile, int paramInt)
    throws FileNotFoundException
  {
    paramFile = openInternal(paramFile, paramInt);
    if (paramFile == null) {
      return null;
    }
    return new ParcelFileDescriptor(paramFile);
  }
  
  public static ParcelFileDescriptor open(File paramFile, int paramInt, Handler paramHandler, OnCloseListener paramOnCloseListener)
    throws IOException
  {
    if (paramHandler != null)
    {
      if (paramOnCloseListener != null)
      {
        paramFile = openInternal(paramFile, paramInt);
        if (paramFile == null) {
          return null;
        }
        return fromFd(paramFile, paramHandler, paramOnCloseListener);
      }
      throw new IllegalArgumentException("Listener must not be null");
    }
    throw new IllegalArgumentException("Handler must not be null");
  }
  
  private static FileDescriptor openInternal(File paramFile, int paramInt)
    throws FileNotFoundException
  {
    if ((paramInt & 0x30000000) != 0)
    {
      int i = 0;
      int j = paramInt & 0x30000000;
      if ((j != 0) && (j != 268435456))
      {
        if (j != 536870912)
        {
          if (j == 805306368) {
            i = OsConstants.O_RDWR;
          }
        }
        else {
          i = OsConstants.O_WRONLY;
        }
      }
      else {
        i = OsConstants.O_RDONLY;
      }
      j = i;
      if ((0x8000000 & paramInt) != 0) {
        j = i | OsConstants.O_CREAT;
      }
      i = j;
      if ((0x4000000 & paramInt) != 0) {
        i = j | OsConstants.O_TRUNC;
      }
      j = i;
      if ((0x2000000 & paramInt) != 0) {
        j = i | OsConstants.O_APPEND;
      }
      int k = OsConstants.S_IRWXU | OsConstants.S_IRWXG;
      i = k;
      if ((paramInt & 0x1) != 0) {
        i = k | OsConstants.S_IROTH;
      }
      k = i;
      if ((paramInt & 0x2) != 0) {
        k = i | OsConstants.S_IWOTH;
      }
      paramFile = paramFile.getPath();
      try
      {
        paramFile = Os.open(paramFile, j, k);
        return paramFile;
      }
      catch (ErrnoException paramFile)
      {
        throw new FileNotFoundException(paramFile.getMessage());
      }
    }
    throw new IllegalArgumentException("Must specify MODE_READ_ONLY, MODE_WRITE_ONLY, or MODE_READ_WRITE");
  }
  
  public static int parseMode(String paramString)
  {
    int i;
    if ("r".equals(paramString)) {
      i = 268435456;
    }
    for (;;)
    {
      break;
      if ((!"w".equals(paramString)) && (!"wt".equals(paramString)))
      {
        if ("wa".equals(paramString))
        {
          i = 704643072;
        }
        else if ("rw".equals(paramString))
        {
          i = 939524096;
        }
        else if ("rwt".equals(paramString))
        {
          i = 1006632960;
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Bad mode '");
          localStringBuilder.append(paramString);
          localStringBuilder.append("'");
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
      }
      else {
        i = 738197504;
      }
    }
    return i;
  }
  
  private static Status readCommStatus(FileDescriptor paramFileDescriptor, byte[] paramArrayOfByte)
  {
    try
    {
      int i = Os.read(paramFileDescriptor, paramArrayOfByte, 0, paramArrayOfByte.length);
      if (i == 0) {
        return new Status(-2);
      }
      int j = Memory.peekInt(paramArrayOfByte, 0, ByteOrder.BIG_ENDIAN);
      if (j == 1)
      {
        paramFileDescriptor = new java/lang/String;
        paramFileDescriptor.<init>(paramArrayOfByte, 4, i - 4);
        return new Status(j, paramFileDescriptor);
      }
      paramFileDescriptor = new Status(j);
      return paramFileDescriptor;
    }
    catch (InterruptedIOException paramArrayOfByte)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("Failed to read status; assuming dead: ");
      paramFileDescriptor.append(paramArrayOfByte);
      Log.d("ParcelFileDescriptor", paramFileDescriptor.toString());
      return new Status(-2);
    }
    catch (ErrnoException paramFileDescriptor)
    {
      if (errno == OsConstants.EAGAIN) {
        return null;
      }
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Failed to read status; assuming dead: ");
      paramArrayOfByte.append(paramFileDescriptor);
      Log.d("ParcelFileDescriptor", paramArrayOfByte.toString());
    }
    return new Status(-2);
  }
  
  private void writeCommStatusAndClose(int paramInt, String paramString)
  {
    Object localObject;
    if (mCommFd == null)
    {
      if (paramString != null)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unable to inform peer: ");
        ((StringBuilder)localObject).append(paramString);
        Log.w("ParcelFileDescriptor", ((StringBuilder)localObject).toString());
      }
      return;
    }
    if (paramInt == 2) {
      Log.w("ParcelFileDescriptor", "Peer expected signal when closed; unable to deliver after detach");
    }
    if (paramInt == -1)
    {
      IoUtils.closeQuietly(mCommFd);
      mCommFd = null;
      return;
    }
    try
    {
      mStatus = readCommStatus(mCommFd, getOrCreateStatusBuffer());
      localObject = mStatus;
      if (localObject != null) {
        return;
      }
      try
      {
        localObject = getOrCreateStatusBuffer();
        Memory.pokeInt((byte[])localObject, 0, paramInt, ByteOrder.BIG_ENDIAN);
        int i = 0 + 4;
        paramInt = i;
        if (paramString != null)
        {
          paramString = paramString.getBytes();
          paramInt = Math.min(paramString.length, localObject.length - i);
          System.arraycopy(paramString, 0, (byte[])localObject, i, paramInt);
          paramInt = i + paramInt;
        }
        Os.write(mCommFd, (byte[])localObject, 0, paramInt);
      }
      catch (InterruptedIOException paramString)
      {
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Failed to report status: ");
        ((StringBuilder)localObject).append(paramString);
        Log.w("ParcelFileDescriptor", ((StringBuilder)localObject).toString());
      }
      catch (ErrnoException localErrnoException)
      {
        for (;;)
        {
          paramString = new java/lang/StringBuilder;
          paramString.<init>();
          paramString.append("Failed to report status: ");
          paramString.append(localErrnoException);
          Log.w("ParcelFileDescriptor", paramString.toString());
        }
      }
      return;
    }
    finally
    {
      IoUtils.closeQuietly(mCommFd);
      mCommFd = null;
    }
  }
  
  public boolean canDetectErrors()
  {
    if (mWrapped != null) {
      return mWrapped.canDetectErrors();
    }
    boolean bool;
    if (mCommFd != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void checkError()
    throws IOException
  {
    if (mWrapped != null)
    {
      mWrapped.checkError();
      return;
    }
    if (mStatus == null)
    {
      if (mCommFd == null)
      {
        Log.w("ParcelFileDescriptor", "Peer didn't provide a comm channel; unable to check for errors");
        return;
      }
      mStatus = readCommStatus(mCommFd, getOrCreateStatusBuffer());
    }
    if ((mStatus != null) && (mStatus.status != 0)) {
      throw mStatus.asIOException();
    }
  }
  
  public void close()
    throws IOException
  {
    if (mWrapped != null) {}
    try
    {
      mWrapped.close();
      releaseResources();
    }
    finally
    {
      releaseResources();
    }
  }
  
  public void closeWithError(String paramString)
    throws IOException
  {
    if (mWrapped != null) {}
    try
    {
      mWrapped.closeWithError(paramString);
      releaseResources();
    }
    finally
    {
      releaseResources();
    }
    closeWithStatus(1, paramString);
    return;
    throw new IllegalArgumentException("Message must not be null");
  }
  
  public int describeContents()
  {
    if (mWrapped != null) {
      return mWrapped.describeContents();
    }
    return 1;
  }
  
  public int detachFd()
  {
    if (mWrapped != null) {
      return mWrapped.detachFd();
    }
    if (!mClosed)
    {
      int i = getFd();
      mFd.setInt$(-1);
      writeCommStatusAndClose(2, null);
      mClosed = true;
      mGuard.close();
      releaseResources();
      return i;
    }
    throw new IllegalStateException("Already closed");
  }
  
  public ParcelFileDescriptor dup()
    throws IOException
  {
    if (mWrapped != null) {
      return mWrapped.dup();
    }
    return dup(getFileDescriptor());
  }
  
  protected void finalize()
    throws Throwable
  {
    if (mWrapped != null) {
      releaseResources();
    }
    if (mGuard != null) {
      mGuard.warnIfOpen();
    }
    try
    {
      if (!mClosed) {
        closeWithStatus(3, null);
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getFd()
  {
    if (mWrapped != null) {
      return mWrapped.getFd();
    }
    if (!mClosed) {
      return mFd.getInt$();
    }
    throw new IllegalStateException("Already closed");
  }
  
  public FileDescriptor getFileDescriptor()
  {
    if (mWrapped != null) {
      return mWrapped.getFileDescriptor();
    }
    return mFd;
  }
  
  public long getStatSize()
  {
    if (mWrapped != null) {
      return mWrapped.getStatSize();
    }
    try
    {
      localObject = Os.fstat(mFd);
      if ((!OsConstants.S_ISREG(st_mode)) && (!OsConstants.S_ISLNK(st_mode))) {
        return -1L;
      }
      long l = st_size;
      return l;
    }
    catch (ErrnoException localErrnoException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("fstat() failed: ");
      ((StringBuilder)localObject).append(localErrnoException);
      Log.w("ParcelFileDescriptor", ((StringBuilder)localObject).toString());
    }
    return -1L;
  }
  
  public void releaseResources() {}
  
  public long seekTo(long paramLong)
    throws IOException
  {
    if (mWrapped != null) {
      return mWrapped.seekTo(paramLong);
    }
    try
    {
      paramLong = Os.lseek(mFd, paramLong, OsConstants.SEEK_SET);
      return paramLong;
    }
    catch (ErrnoException localErrnoException)
    {
      throw localErrnoException.rethrowAsIOException();
    }
  }
  
  public String toString()
  {
    if (mWrapped != null) {
      return mWrapped.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{ParcelFileDescriptor: ");
    localStringBuilder.append(mFd);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mWrapped != null) {}
    try
    {
      mWrapped.writeToParcel(paramParcel, paramInt);
      releaseResources();
    }
    finally
    {
      releaseResources();
    }
    paramParcel.writeInt(1);
    paramParcel.writeFileDescriptor(mFd);
    paramParcel.writeFileDescriptor(mCommFd);
    break label74;
    paramParcel.writeInt(0);
    paramParcel.writeFileDescriptor(mFd);
    label74:
    if (((paramInt & 0x1) != 0) && (!mClosed)) {
      closeWithStatus(-1, null);
    }
  }
  
  public static class AutoCloseInputStream
    extends FileInputStream
  {
    private final ParcelFileDescriptor mPfd;
    
    public AutoCloseInputStream(ParcelFileDescriptor paramParcelFileDescriptor)
    {
      super();
      mPfd = paramParcelFileDescriptor;
    }
    
    public void close()
      throws IOException
    {
      try
      {
        mPfd.close();
        return;
      }
      finally
      {
        super.close();
      }
    }
    
    public int read()
      throws IOException
    {
      int i = super.read();
      if ((i == -1) && (mPfd.canDetectErrors())) {
        mPfd.checkError();
      }
      return i;
    }
    
    public int read(byte[] paramArrayOfByte)
      throws IOException
    {
      int i = super.read(paramArrayOfByte);
      if ((i == -1) && (mPfd.canDetectErrors())) {
        mPfd.checkError();
      }
      return i;
    }
    
    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      paramInt1 = super.read(paramArrayOfByte, paramInt1, paramInt2);
      if ((paramInt1 == -1) && (mPfd.canDetectErrors())) {
        mPfd.checkError();
      }
      return paramInt1;
    }
  }
  
  public static class AutoCloseOutputStream
    extends FileOutputStream
  {
    private final ParcelFileDescriptor mPfd;
    
    public AutoCloseOutputStream(ParcelFileDescriptor paramParcelFileDescriptor)
    {
      super();
      mPfd = paramParcelFileDescriptor;
    }
    
    public void close()
      throws IOException
    {
      try
      {
        mPfd.close();
        return;
      }
      finally
      {
        super.close();
      }
    }
  }
  
  public static class FileDescriptorDetachedException
    extends IOException
  {
    private static final long serialVersionUID = 955542466045L;
    
    public FileDescriptorDetachedException()
    {
      super();
    }
  }
  
  public static abstract interface OnCloseListener
  {
    public abstract void onClose(IOException paramIOException);
  }
  
  private static class Status
  {
    public static final int DEAD = -2;
    public static final int DETACHED = 2;
    public static final int ERROR = 1;
    public static final int LEAKED = 3;
    public static final int OK = 0;
    public static final int SILENCE = -1;
    public final String msg;
    public final int status;
    
    public Status(int paramInt)
    {
      this(paramInt, null);
    }
    
    public Status(int paramInt, String paramString)
    {
      status = paramInt;
      msg = paramString;
    }
    
    public IOException asIOException()
    {
      int i = status;
      if (i != -2)
      {
        StringBuilder localStringBuilder;
        switch (i)
        {
        default: 
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown status: ");
          localStringBuilder.append(status);
          return new IOException(localStringBuilder.toString());
        case 3: 
          return new IOException("Remote side was leaked");
        case 2: 
          return new ParcelFileDescriptor.FileDescriptorDetachedException();
        case 1: 
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Remote error: ");
          localStringBuilder.append(msg);
          return new IOException(localStringBuilder.toString());
        }
        return null;
      }
      return new IOException("Remote side is dead");
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{");
      localStringBuilder.append(status);
      localStringBuilder.append(": ");
      localStringBuilder.append(msg);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
}
