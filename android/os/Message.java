package android.os;

import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;

public final class Message
  implements Parcelable
{
  public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator()
  {
    public Message createFromParcel(Parcel paramAnonymousParcel)
    {
      Message localMessage = Message.obtain();
      localMessage.readFromParcel(paramAnonymousParcel);
      return localMessage;
    }
    
    public Message[] newArray(int paramAnonymousInt)
    {
      return new Message[paramAnonymousInt];
    }
  };
  static final int FLAGS_TO_CLEAR_ON_COPY_FROM = 1;
  static final int FLAG_ASYNCHRONOUS = 2;
  static final int FLAG_IN_USE = 1;
  private static final int MAX_POOL_SIZE = 50;
  private static boolean gCheckRecycle;
  private static Message sPool;
  private static int sPoolSize;
  public static final Object sPoolSync = new Object();
  public int arg1;
  public int arg2;
  Runnable callback;
  Bundle data;
  int flags;
  Message next;
  public Object obj;
  public Messenger replyTo;
  public int sendingUid = -1;
  Handler target;
  public int what;
  long when;
  
  static
  {
    sPoolSize = 0;
    gCheckRecycle = true;
  }
  
  public Message() {}
  
  public static Message obtain()
  {
    synchronized (sPoolSync)
    {
      if (sPool != null)
      {
        Message localMessage = sPool;
        sPool = next;
        next = null;
        flags = 0;
        sPoolSize -= 1;
        return localMessage;
      }
      return new Message();
    }
  }
  
  public static Message obtain(Handler paramHandler)
  {
    Message localMessage = obtain();
    target = paramHandler;
    return localMessage;
  }
  
  public static Message obtain(Handler paramHandler, int paramInt)
  {
    Message localMessage = obtain();
    target = paramHandler;
    what = paramInt;
    return localMessage;
  }
  
  public static Message obtain(Handler paramHandler, int paramInt1, int paramInt2, int paramInt3)
  {
    Message localMessage = obtain();
    target = paramHandler;
    what = paramInt1;
    arg1 = paramInt2;
    arg2 = paramInt3;
    return localMessage;
  }
  
  public static Message obtain(Handler paramHandler, int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    Message localMessage = obtain();
    target = paramHandler;
    what = paramInt1;
    arg1 = paramInt2;
    arg2 = paramInt3;
    obj = paramObject;
    return localMessage;
  }
  
  public static Message obtain(Handler paramHandler, int paramInt, Object paramObject)
  {
    Message localMessage = obtain();
    target = paramHandler;
    what = paramInt;
    obj = paramObject;
    return localMessage;
  }
  
  public static Message obtain(Handler paramHandler, Runnable paramRunnable)
  {
    Message localMessage = obtain();
    target = paramHandler;
    callback = paramRunnable;
    return localMessage;
  }
  
  public static Message obtain(Message paramMessage)
  {
    Message localMessage = obtain();
    what = what;
    arg1 = arg1;
    arg2 = arg2;
    obj = obj;
    replyTo = replyTo;
    sendingUid = sendingUid;
    if (data != null) {
      data = new Bundle(data);
    }
    target = target;
    callback = callback;
    return localMessage;
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    what = paramParcel.readInt();
    arg1 = paramParcel.readInt();
    arg2 = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      obj = paramParcel.readParcelable(getClass().getClassLoader());
    }
    when = paramParcel.readLong();
    data = paramParcel.readBundle();
    replyTo = Messenger.readMessengerOrNullFromParcel(paramParcel);
    sendingUid = paramParcel.readInt();
  }
  
  public static void updateCheckRecycle(int paramInt)
  {
    if (paramInt < 21) {
      gCheckRecycle = false;
    }
  }
  
  public void copyFrom(Message paramMessage)
  {
    flags &= 0xFFFFFFFE;
    what = what;
    arg1 = arg1;
    arg2 = arg2;
    obj = obj;
    replyTo = replyTo;
    sendingUid = sendingUid;
    if (data != null) {
      data = ((Bundle)data.clone());
    } else {
      data = null;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Runnable getCallback()
  {
    return callback;
  }
  
  public Bundle getData()
  {
    if (data == null) {
      data = new Bundle();
    }
    return data;
  }
  
  public Handler getTarget()
  {
    return target;
  }
  
  public long getWhen()
  {
    return when;
  }
  
  public boolean isAsynchronous()
  {
    boolean bool;
    if ((flags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isInUse()
  {
    int i = flags;
    boolean bool = true;
    if ((i & 0x1) != 1) {
      bool = false;
    }
    return bool;
  }
  
  void markInUse()
  {
    flags |= 0x1;
  }
  
  public Bundle peekData()
  {
    return data;
  }
  
  public void recycle()
  {
    if (isInUse())
    {
      if (!gCheckRecycle) {
        return;
      }
      throw new IllegalStateException("This message cannot be recycled because it is still in use.");
    }
    recycleUnchecked();
  }
  
  void recycleUnchecked()
  {
    flags = 1;
    what = 0;
    arg1 = 0;
    arg2 = 0;
    obj = null;
    replyTo = null;
    sendingUid = -1;
    when = 0L;
    target = null;
    callback = null;
    data = null;
    synchronized (sPoolSync)
    {
      if (sPoolSize < 50)
      {
        next = sPool;
        sPool = this;
        sPoolSize += 1;
      }
      return;
    }
  }
  
  public void sendToTarget()
  {
    target.sendMessage(this);
  }
  
  public void setAsynchronous(boolean paramBoolean)
  {
    if (paramBoolean) {
      flags |= 0x2;
    } else {
      flags &= 0xFFFFFFFD;
    }
  }
  
  public Message setCallback(Runnable paramRunnable)
  {
    callback = paramRunnable;
    return this;
  }
  
  public void setData(Bundle paramBundle)
  {
    data = paramBundle;
  }
  
  public void setTarget(Handler paramHandler)
  {
    target = paramHandler;
  }
  
  public Message setWhat(int paramInt)
  {
    what = paramInt;
    return this;
  }
  
  public String toString()
  {
    return toString(SystemClock.uptimeMillis());
  }
  
  String toString(long paramLong)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{ when=");
    TimeUtils.formatDuration(when - paramLong, localStringBuilder);
    if (target != null)
    {
      if (callback != null)
      {
        localStringBuilder.append(" callback=");
        localStringBuilder.append(callback.getClass().getName());
      }
      else
      {
        localStringBuilder.append(" what=");
        localStringBuilder.append(what);
      }
      if (arg1 != 0)
      {
        localStringBuilder.append(" arg1=");
        localStringBuilder.append(arg1);
      }
      if (arg2 != 0)
      {
        localStringBuilder.append(" arg2=");
        localStringBuilder.append(arg2);
      }
      if (obj != null)
      {
        localStringBuilder.append(" obj=");
        localStringBuilder.append(obj);
      }
      localStringBuilder.append(" target=");
      localStringBuilder.append(target.getClass().getName());
    }
    else
    {
      localStringBuilder.append(" barrier=");
      localStringBuilder.append(arg1);
    }
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (callback == null)
    {
      paramParcel.writeInt(what);
      paramParcel.writeInt(arg1);
      paramParcel.writeInt(arg2);
      if (obj != null) {
        try
        {
          Parcelable localParcelable = (Parcelable)obj;
          paramParcel.writeInt(1);
          paramParcel.writeParcelable(localParcelable, paramInt);
        }
        catch (ClassCastException paramParcel)
        {
          throw new RuntimeException("Can't marshal non-Parcelable objects across processes.");
        }
      } else {
        paramParcel.writeInt(0);
      }
      paramParcel.writeLong(when);
      paramParcel.writeBundle(data);
      Messenger.writeMessengerOrNullToParcel(replyTo, paramParcel);
      paramParcel.writeInt(sendingUid);
      return;
    }
    throw new RuntimeException("Can't marshal callbacks across processes.");
  }
  
  void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1112396529665L, when);
    if (target != null)
    {
      if (callback != null) {
        paramProtoOutputStream.write(1138166333442L, callback.getClass().getName());
      } else {
        paramProtoOutputStream.write(1120986464259L, what);
      }
      if (arg1 != 0) {
        paramProtoOutputStream.write(1120986464260L, arg1);
      }
      if (arg2 != 0) {
        paramProtoOutputStream.write(1120986464261L, arg2);
      }
      if (obj != null) {
        paramProtoOutputStream.write(1138166333446L, obj.toString());
      }
      paramProtoOutputStream.write(1138166333447L, target.getClass().getName());
    }
    else
    {
      paramProtoOutputStream.write(1120986464264L, arg1);
    }
    paramProtoOutputStream.end(paramLong);
  }
}
