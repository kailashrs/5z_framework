package com.android.server;

import android.os.StrictMode;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Slog;
import dalvik.system.SocketTagger;
import java.io.FileDescriptor;
import java.net.SocketException;

public final class NetworkManagementSocketTagger
  extends SocketTagger
{
  private static final boolean LOGD = false;
  public static final String PROP_QTAGUID_ENABLED = "net.qtaguid_enabled";
  private static final String TAG = "NetworkManagementSocketTagger";
  private static ThreadLocal<SocketTags> threadSocketTags = new ThreadLocal()
  {
    protected NetworkManagementSocketTagger.SocketTags initialValue()
    {
      return new NetworkManagementSocketTagger.SocketTags();
    }
  };
  
  public NetworkManagementSocketTagger() {}
  
  public static int getThreadSocketStatsTag()
  {
    return threadSocketTagsgetstatsTag;
  }
  
  public static int getThreadSocketStatsUid()
  {
    return threadSocketTagsgetstatsUid;
  }
  
  public static void install()
  {
    SocketTagger.set(new NetworkManagementSocketTagger());
  }
  
  public static int kernelToTag(String paramString)
  {
    int i = paramString.length();
    if (i > 10) {
      return Long.decode(paramString.substring(0, i - 8)).intValue();
    }
    return 0;
  }
  
  private static native int native_deleteTagData(int paramInt1, int paramInt2);
  
  private static native int native_setCounterSet(int paramInt1, int paramInt2);
  
  private static native int native_tagSocketFd(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2);
  
  private static native int native_untagSocketFd(FileDescriptor paramFileDescriptor);
  
  public static void resetKernelUidStats(int paramInt)
  {
    if (SystemProperties.getBoolean("net.qtaguid_enabled", false))
    {
      int i = native_deleteTagData(0, paramInt);
      if (i < 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("problem clearing counters for uid ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(" : errno ");
        localStringBuilder.append(i);
        Slog.w("NetworkManagementSocketTagger", localStringBuilder.toString());
      }
    }
  }
  
  public static void setKernelCounterSet(int paramInt1, int paramInt2)
  {
    if (SystemProperties.getBoolean("net.qtaguid_enabled", false))
    {
      int i = native_setCounterSet(paramInt2, paramInt1);
      if (i < 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("setKernelCountSet(");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(", ");
        localStringBuilder.append(paramInt2);
        localStringBuilder.append(") failed with errno ");
        localStringBuilder.append(i);
        Log.w("NetworkManagementSocketTagger", localStringBuilder.toString());
      }
    }
  }
  
  public static int setThreadSocketStatsTag(int paramInt)
  {
    int i = threadSocketTagsgetstatsTag;
    threadSocketTagsgetstatsTag = paramInt;
    return i;
  }
  
  public static int setThreadSocketStatsUid(int paramInt)
  {
    int i = threadSocketTagsgetstatsUid;
    threadSocketTagsgetstatsUid = paramInt;
    return i;
  }
  
  private void tagSocketFd(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2)
  {
    if ((paramInt1 == -1) && (paramInt2 == -1)) {
      return;
    }
    if (SystemProperties.getBoolean("net.qtaguid_enabled", false))
    {
      int i = native_tagSocketFd(paramFileDescriptor, paramInt1, paramInt2);
      if (i < 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("tagSocketFd(");
        localStringBuilder.append(paramFileDescriptor.getInt$());
        localStringBuilder.append(", ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(", ");
        localStringBuilder.append(paramInt2);
        localStringBuilder.append(") failed with errno");
        localStringBuilder.append(i);
        Log.i("NetworkManagementSocketTagger", localStringBuilder.toString());
      }
    }
  }
  
  private void unTagSocketFd(FileDescriptor paramFileDescriptor)
  {
    Object localObject = (SocketTags)threadSocketTags.get();
    if ((statsTag == -1) && (statsUid == -1)) {
      return;
    }
    if (SystemProperties.getBoolean("net.qtaguid_enabled", false))
    {
      int i = native_untagSocketFd(paramFileDescriptor);
      if (i < 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("untagSocket(");
        ((StringBuilder)localObject).append(paramFileDescriptor.getInt$());
        ((StringBuilder)localObject).append(") failed with errno ");
        ((StringBuilder)localObject).append(i);
        Log.w("NetworkManagementSocketTagger", ((StringBuilder)localObject).toString());
      }
    }
  }
  
  public void tag(FileDescriptor paramFileDescriptor)
    throws SocketException
  {
    SocketTags localSocketTags = (SocketTags)threadSocketTags.get();
    if ((statsTag == -1) && (StrictMode.vmUntaggedSocketEnabled())) {
      StrictMode.onUntaggedSocket();
    }
    tagSocketFd(paramFileDescriptor, statsTag, statsUid);
  }
  
  public void untag(FileDescriptor paramFileDescriptor)
    throws SocketException
  {
    unTagSocketFd(paramFileDescriptor);
  }
  
  public static class SocketTags
  {
    public int statsTag = -1;
    public int statsUid = -1;
    
    public SocketTags() {}
  }
}
