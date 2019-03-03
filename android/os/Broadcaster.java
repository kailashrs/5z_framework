package android.os;

import java.io.PrintStream;

public class Broadcaster
{
  private Registration mReg;
  
  public Broadcaster() {}
  
  public void broadcast(Message paramMessage)
  {
    try
    {
      if (mReg == null) {
        return;
      }
      int i = what;
      Object localObject1 = mReg;
      Object localObject2 = localObject1;
      Object localObject3;
      while (senderWhat < i)
      {
        localObject3 = next;
        localObject2 = localObject3;
        if (localObject3 == localObject1) {
          localObject2 = localObject3;
        }
      }
      if (senderWhat == i)
      {
        localObject3 = targets;
        localObject1 = targetWhats;
        int j = localObject3.length;
        for (i = 0; i < j; i++)
        {
          Object localObject4 = localObject3[i];
          localObject2 = Message.obtain();
          ((Message)localObject2).copyFrom(paramMessage);
          what = localObject1[i];
          localObject4.sendMessage((Message)localObject2);
        }
      }
      return;
    }
    finally {}
  }
  
  public void cancelRequest(int paramInt1, Handler paramHandler, int paramInt2)
  {
    try
    {
      Object localObject1 = mReg;
      Object localObject2 = localObject1;
      Object localObject3 = localObject2;
      if (localObject2 == null) {
        return;
      }
      while (senderWhat < paramInt1)
      {
        localObject2 = next;
        localObject3 = localObject2;
        if (localObject2 == localObject1) {
          localObject3 = localObject2;
        }
      }
      if (senderWhat == paramInt1)
      {
        localObject1 = targets;
        localObject2 = targetWhats;
        int i = localObject1.length;
        for (paramInt1 = 0; paramInt1 < i; paramInt1++) {
          if ((localObject1[paramInt1] == paramHandler) && (localObject2[paramInt1] == paramInt2))
          {
            targets = new Handler[i - 1];
            targetWhats = new int[i - 1];
            if (paramInt1 > 0)
            {
              System.arraycopy(localObject1, 0, targets, 0, paramInt1);
              System.arraycopy(localObject2, 0, targetWhats, 0, paramInt1);
            }
            paramInt2 = i - paramInt1 - 1;
            if (paramInt2 == 0) {
              break;
            }
            System.arraycopy(localObject1, paramInt1 + 1, targets, paramInt1, paramInt2);
            System.arraycopy(localObject2, paramInt1 + 1, targetWhats, paramInt1, paramInt2);
            break;
          }
        }
      }
      return;
    }
    finally {}
  }
  
  public void dumpRegistrations()
  {
    try
    {
      Registration localRegistration = mReg;
      Object localObject1 = System.out;
      Object localObject3 = new java/lang/StringBuilder;
      ((StringBuilder)localObject3).<init>();
      ((StringBuilder)localObject3).append("Broadcaster ");
      ((StringBuilder)localObject3).append(this);
      ((StringBuilder)localObject3).append(" {");
      ((PrintStream)localObject1).println(((StringBuilder)localObject3).toString());
      if (localRegistration != null)
      {
        localObject1 = localRegistration;
        do
        {
          Object localObject4 = System.out;
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("    senderWhat=");
          ((StringBuilder)localObject3).append(senderWhat);
          ((PrintStream)localObject4).println(((StringBuilder)localObject3).toString());
          int i = targets.length;
          for (int j = 0; j < i; j++)
          {
            localObject3 = System.out;
            localObject4 = new java/lang/StringBuilder;
            ((StringBuilder)localObject4).<init>();
            ((StringBuilder)localObject4).append("        [");
            ((StringBuilder)localObject4).append(targetWhats[j]);
            ((StringBuilder)localObject4).append("] ");
            ((StringBuilder)localObject4).append(targets[j]);
            ((PrintStream)localObject3).println(((StringBuilder)localObject4).toString());
          }
          localObject3 = next;
          localObject1 = localObject3;
        } while (localObject3 != localRegistration);
      }
      System.out.println("}");
      return;
    }
    finally {}
  }
  
  public void request(int paramInt1, Handler paramHandler, int paramInt2)
  {
    try
    {
      Object localObject1;
      if (mReg == null)
      {
        localObject1 = new android/os/Broadcaster$Registration;
        ((Registration)localObject1).<init>(this, null);
        senderWhat = paramInt1;
        targets = new Handler[1];
        targetWhats = new int[1];
        targets[0] = paramHandler;
        targetWhats[0] = paramInt2;
        mReg = ((Registration)localObject1);
        next = ((Registration)localObject1);
        prev = ((Registration)localObject1);
      }
      else
      {
        Object localObject2 = mReg;
        localObject1 = localObject2;
        Object localObject3;
        while (senderWhat < paramInt1)
        {
          localObject3 = next;
          localObject1 = localObject3;
          if (localObject3 == localObject2) {
            localObject1 = localObject3;
          }
        }
        if (senderWhat != paramInt1)
        {
          localObject3 = new android/os/Broadcaster$Registration;
          ((Registration)localObject3).<init>(this, null);
          senderWhat = paramInt1;
          targets = new Handler[1];
          targetWhats = new int[1];
          next = ((Registration)localObject1);
          prev = prev;
          prev.next = ((Registration)localObject3);
          prev = ((Registration)localObject3);
          if ((localObject1 == mReg) && (senderWhat > senderWhat)) {
            mReg = ((Registration)localObject3);
          }
          localObject1 = localObject3;
          paramInt1 = 0;
        }
        else
        {
          int i = targets.length;
          localObject3 = targets;
          localObject2 = targetWhats;
          for (paramInt1 = 0; paramInt1 < i; paramInt1++) {
            if ((localObject3[paramInt1] == paramHandler) && (localObject2[paramInt1] == paramInt2)) {
              return;
            }
          }
          targets = new Handler[i + 1];
          System.arraycopy(localObject3, 0, targets, 0, i);
          targetWhats = new int[i + 1];
          System.arraycopy(localObject2, 0, targetWhats, 0, i);
          paramInt1 = i;
        }
        targets[paramInt1] = paramHandler;
        targetWhats[paramInt1] = paramInt2;
      }
      return;
    }
    finally {}
  }
  
  private class Registration
  {
    Registration next;
    Registration prev;
    int senderWhat;
    int[] targetWhats;
    Handler[] targets;
    
    private Registration() {}
  }
}
