package android.speech.tts;

import android.media.AudioFormat;
import android.util.Log;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

class FileSynthesisCallback
  extends AbstractSynthesisCallback
{
  private static final boolean DBG = false;
  private static final int MAX_AUDIO_BUFFER_SIZE = 8192;
  private static final String TAG = "FileSynthesisRequest";
  private static final short WAV_FORMAT_PCM = 1;
  private static final int WAV_HEADER_LENGTH = 44;
  private int mAudioFormat;
  private int mChannelCount;
  private final TextToSpeechService.UtteranceProgressDispatcher mDispatcher;
  private boolean mDone = false;
  private FileChannel mFileChannel;
  private int mSampleRateInHz;
  private boolean mStarted = false;
  private final Object mStateLock = new Object();
  protected int mStatusCode;
  
  FileSynthesisCallback(FileChannel paramFileChannel, TextToSpeechService.UtteranceProgressDispatcher paramUtteranceProgressDispatcher, boolean paramBoolean)
  {
    super(paramBoolean);
    mFileChannel = paramFileChannel;
    mDispatcher = paramUtteranceProgressDispatcher;
    mStatusCode = 0;
  }
  
  private void cleanUp()
  {
    closeFile();
  }
  
  private void closeFile()
  {
    mFileChannel = null;
  }
  
  private ByteBuffer makeWavHeader(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramInt2 = AudioFormat.getBytesPerSample(paramInt2);
    short s1 = (short)(paramInt2 * paramInt3);
    short s2 = (short)(paramInt2 * 8);
    ByteBuffer localByteBuffer = ByteBuffer.wrap(new byte[44]);
    localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    localByteBuffer.put(new byte[] { 82, 73, 70, 70 });
    localByteBuffer.putInt(paramInt4 + 44 - 8);
    localByteBuffer.put(new byte[] { 87, 65, 86, 69 });
    localByteBuffer.put(new byte[] { 102, 109, 116, 32 });
    localByteBuffer.putInt(16);
    localByteBuffer.putShort((short)1);
    localByteBuffer.putShort((short)paramInt3);
    localByteBuffer.putInt(paramInt1);
    localByteBuffer.putInt(paramInt1 * paramInt2 * paramInt3);
    localByteBuffer.putShort(s1);
    localByteBuffer.putShort(s2);
    localByteBuffer.put(new byte[] { 100, 97, 116, 97 });
    localByteBuffer.putInt(paramInt4);
    localByteBuffer.flip();
    return localByteBuffer;
  }
  
  public int audioAvailable(byte[] arg1, int paramInt1, int paramInt2)
  {
    synchronized (mStateLock)
    {
      if (mStatusCode == -2)
      {
        paramInt1 = errorCodeOnStop();
        return paramInt1;
      }
      if (mStatusCode != 0) {
        return -1;
      }
      if (mFileChannel == null)
      {
        Log.e("FileSynthesisRequest", "File not open");
        mStatusCode = -5;
        return -1;
      }
      if (!mStarted)
      {
        Log.e("FileSynthesisRequest", "Start method was not called");
        return -1;
      }
      FileChannel localFileChannel = mFileChannel;
      ??? = new byte[paramInt2];
      System.arraycopy(???, paramInt1, (byte[])???, 0, paramInt2);
      mDispatcher.dispatchOnAudioAvailable((byte[])???);
      try
      {
        localFileChannel.write(ByteBuffer.wrap(???, paramInt1, paramInt2));
        return 0;
      }
      catch (IOException ???)
      {
        Log.e("FileSynthesisRequest", "Failed to write to output file descriptor", ???);
        synchronized (mStateLock)
        {
          cleanUp();
          mStatusCode = -5;
          return -1;
        }
      }
    }
  }
  
  public int done()
  {
    synchronized (mStateLock)
    {
      if (mDone)
      {
        Log.w("FileSynthesisRequest", "Duplicate call to done()");
        return -1;
      }
      if (mStatusCode == -2)
      {
        i = errorCodeOnStop();
        return i;
      }
      if ((mStatusCode != 0) && (mStatusCode != -2))
      {
        mDispatcher.dispatchOnError(mStatusCode);
        return -1;
      }
      if (mFileChannel == null)
      {
        Log.e("FileSynthesisRequest", "File not open");
        return -1;
      }
      mDone = true;
      FileChannel localFileChannel = mFileChannel;
      int i = mSampleRateInHz;
      int j = mAudioFormat;
      int k = mChannelCount;
      try
      {
        localFileChannel.position(0L);
        int m = (int)(localFileChannel.size() - 44L);
        localFileChannel.write(makeWavHeader(i, j, k, m));
        synchronized (mStateLock)
        {
          closeFile();
          mDispatcher.dispatchOnSuccess();
          return 0;
        }
        localObject5 = finally;
      }
      catch (IOException localIOException)
      {
        Log.e("FileSynthesisRequest", "Failed to write to output file descriptor", localIOException);
        synchronized (mStateLock)
        {
          cleanUp();
          return -1;
        }
      }
    }
  }
  
  public void error()
  {
    error(-3);
  }
  
  public void error(int paramInt)
  {
    synchronized (mStateLock)
    {
      if (mDone) {
        return;
      }
      cleanUp();
      mStatusCode = paramInt;
      return;
    }
  }
  
  public int getMaxBufferSize()
  {
    return 8192;
  }
  
  public boolean hasFinished()
  {
    synchronized (mStateLock)
    {
      boolean bool = mDone;
      return bool;
    }
  }
  
  public boolean hasStarted()
  {
    synchronized (mStateLock)
    {
      boolean bool = mStarted;
      return bool;
    }
  }
  
  public void rangeStart(int paramInt1, int paramInt2, int paramInt3)
  {
    mDispatcher.dispatchOnRangeStart(paramInt1, paramInt2, paramInt3);
  }
  
  public int start(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt2 != 3) && (paramInt2 != 2) && (paramInt2 != 4))
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Audio format encoding ");
      ((StringBuilder)???).append(paramInt2);
      ((StringBuilder)???).append(" not supported. Please use one of AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT or AudioFormat.ENCODING_PCM_FLOAT");
      Log.e("FileSynthesisRequest", ((StringBuilder)???).toString());
    }
    mDispatcher.dispatchOnBeginSynthesis(paramInt1, paramInt2, paramInt3);
    synchronized (mStateLock)
    {
      if (mStatusCode == -2)
      {
        paramInt1 = errorCodeOnStop();
        return paramInt1;
      }
      if (mStatusCode != 0) {
        return -1;
      }
      if (mStarted)
      {
        Log.e("FileSynthesisRequest", "Start called twice");
        return -1;
      }
      mStarted = true;
      mSampleRateInHz = paramInt1;
      mAudioFormat = paramInt2;
      mChannelCount = paramInt3;
      mDispatcher.dispatchOnStart();
      ??? = mFileChannel;
      try
      {
        ((FileChannel)???).write(ByteBuffer.allocate(44));
        return 0;
      }
      catch (IOException localIOException)
      {
        Log.e("FileSynthesisRequest", "Failed to write wav header to output file descriptor", localIOException);
        synchronized (mStateLock)
        {
          cleanUp();
          mStatusCode = -5;
          return -1;
        }
      }
    }
  }
  
  void stop()
  {
    synchronized (mStateLock)
    {
      if (mDone) {
        return;
      }
      if (mStatusCode == -2) {
        return;
      }
      mStatusCode = -2;
      cleanUp();
      mDispatcher.dispatchOnStop();
      return;
    }
  }
}
