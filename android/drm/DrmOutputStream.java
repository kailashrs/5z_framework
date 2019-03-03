package android.drm;

import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownServiceException;
import java.util.Arrays;
import libcore.io.IoBridge;
import libcore.io.Streams;

public class DrmOutputStream
  extends OutputStream
{
  private static final String TAG = "DrmOutputStream";
  private final DrmManagerClient mClient;
  private final FileDescriptor mFd;
  private final ParcelFileDescriptor mPfd;
  private int mSessionId = -1;
  
  public DrmOutputStream(DrmManagerClient paramDrmManagerClient, ParcelFileDescriptor paramParcelFileDescriptor, String paramString)
    throws IOException
  {
    mClient = paramDrmManagerClient;
    mPfd = paramParcelFileDescriptor;
    mFd = paramParcelFileDescriptor.getFileDescriptor();
    mSessionId = mClient.openConvertSession(paramString);
    if (mSessionId != -1) {
      return;
    }
    paramDrmManagerClient = new StringBuilder();
    paramDrmManagerClient.append("Failed to open DRM session for ");
    paramDrmManagerClient.append(paramString);
    throw new UnknownServiceException(paramDrmManagerClient.toString());
  }
  
  public void close()
    throws IOException
  {
    if (mSessionId == -1) {
      Log.w("DrmOutputStream", "Closing stream without finishing");
    }
    mPfd.close();
  }
  
  public void finish()
    throws IOException
  {
    DrmConvertedStatus localDrmConvertedStatus = mClient.closeConvertSession(mSessionId);
    if (statusCode == 1)
    {
      try
      {
        Os.lseek(mFd, offset, OsConstants.SEEK_SET);
      }
      catch (ErrnoException localErrnoException)
      {
        localErrnoException.rethrowAsIOException();
      }
      IoBridge.write(mFd, convertedData, 0, convertedData.length);
      mSessionId = -1;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unexpected DRM status: ");
    localStringBuilder.append(statusCode);
    throw new IOException(localStringBuilder.toString());
  }
  
  public void write(int paramInt)
    throws IOException
  {
    Streams.writeSingleByte(this, paramInt);
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    Arrays.checkOffsetAndCount(paramArrayOfByte.length, paramInt1, paramInt2);
    if (paramInt2 != paramArrayOfByte.length)
    {
      localObject = new byte[paramInt2];
      System.arraycopy(paramArrayOfByte, paramInt1, (byte[])localObject, 0, paramInt2);
      paramArrayOfByte = (byte[])localObject;
    }
    paramArrayOfByte = mClient.convertData(mSessionId, paramArrayOfByte);
    if (statusCode == 1)
    {
      IoBridge.write(mFd, convertedData, 0, convertedData.length);
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unexpected DRM status: ");
    ((StringBuilder)localObject).append(statusCode);
    throw new IOException(((StringBuilder)localObject).toString());
  }
}
