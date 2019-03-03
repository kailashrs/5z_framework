package com.android.internal.telephony.uicc.euicc.apdu;

import android.os.Handler;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.uicc.IccIoResult;
import com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback;
import com.android.internal.telephony.uicc.euicc.async.AsyncResultHelper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ApduSender
{
  private static final int INS_GET_MORE_RESPONSE = 192;
  private static final String LOG_TAG = "ApduSender";
  private static final int STATUS_NO_ERROR = 36864;
  private static final int SW1_MORE_RESPONSE = 97;
  private final String mAid;
  private final Object mChannelLock = new Object();
  private boolean mChannelOpened;
  private final CloseLogicalChannelInvocation mCloseChannel;
  private final OpenLogicalChannelInvocation mOpenChannel;
  private final boolean mSupportExtendedApdu;
  private final TransmitApduLogicalChannelInvocation mTransmitApdu;
  
  public ApduSender(CommandsInterface paramCommandsInterface, String paramString, boolean paramBoolean)
  {
    mAid = paramString;
    mSupportExtendedApdu = paramBoolean;
    mOpenChannel = new OpenLogicalChannelInvocation(paramCommandsInterface);
    mCloseChannel = new CloseLogicalChannelInvocation(paramCommandsInterface);
    mTransmitApdu = new TransmitApduLogicalChannelInvocation(paramCommandsInterface);
  }
  
  private void closeAndReturn(int paramInt, final byte[] paramArrayOfByte, final Throwable paramThrowable, final AsyncResultCallback<byte[]> paramAsyncResultCallback, Handler paramHandler)
  {
    mCloseChannel.invoke(Integer.valueOf(paramInt), new AsyncResultCallback()
    {
      public void onResult(Boolean paramAnonymousBoolean)
      {
        synchronized (mChannelLock)
        {
          ApduSender.access$102(ApduSender.this, false);
          if (paramThrowable == null) {
            paramAsyncResultCallback.onResult(paramArrayOfByte);
          } else {
            paramAsyncResultCallback.onException(paramThrowable);
          }
          return;
        }
      }
    }, paramHandler);
  }
  
  private void getCompleteResponse(final int paramInt, IccIoResult paramIccIoResult, final ByteArrayOutputStream paramByteArrayOutputStream, final AsyncResultCallback<IccIoResult> paramAsyncResultCallback, final Handler paramHandler)
  {
    if (paramByteArrayOutputStream == null) {
      paramByteArrayOutputStream = new ByteArrayOutputStream();
    }
    if (payload != null) {
      try
      {
        paramByteArrayOutputStream.write(payload);
      }
      catch (IOException localIOException) {}
    }
    if (sw1 != 97)
    {
      payload = paramByteArrayOutputStream.toByteArray();
      paramAsyncResultCallback.onResult(paramIccIoResult);
      return;
    }
    mTransmitApdu.invoke(new ApduCommand(paramInt, 0, 192, 0, 0, sw2, ""), new AsyncResultCallback()
    {
      public void onResult(IccIoResult paramAnonymousIccIoResult)
      {
        ApduSender.this.getCompleteResponse(paramInt, paramAnonymousIccIoResult, paramByteArrayOutputStream, paramAsyncResultCallback, paramHandler);
      }
    }, paramHandler);
  }
  
  private static void logv(String paramString)
  {
    Rlog.v("ApduSender", paramString);
  }
  
  private void sendCommand(final List<ApduCommand> paramList, final int paramInt, final AsyncResultCallback<byte[]> paramAsyncResultCallback, final Handler paramHandler)
  {
    final ApduCommand localApduCommand = (ApduCommand)paramList.get(paramInt);
    mTransmitApdu.invoke(localApduCommand, new AsyncResultCallback()
    {
      public void onResult(IccIoResult paramAnonymousIccIoResult)
      {
        ApduSender.this.getCompleteResponse(localApduCommandchannel, paramAnonymousIccIoResult, null, new AsyncResultCallback()
        {
          public void onResult(IccIoResult paramAnonymous2IccIoResult)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Full APDU response: ");
            localStringBuilder.append(paramAnonymous2IccIoResult);
            ApduSender.logv(localStringBuilder.toString());
            int i = sw1 << 8 | sw2;
            if (i != 36864)
            {
              ApduSender.this.closeAndReturn(val$command.channel, null, new ApduException(i), val$resultCallback, val$handler);
              return;
            }
            if (val$index == val$commands.size() - 1)
            {
              ApduSender.this.closeAndReturn(val$command.channel, payload, null, val$resultCallback, val$handler);
              return;
            }
            ApduSender.this.sendCommand(val$commands, val$index + 1, val$resultCallback, val$handler);
          }
        }, paramHandler);
      }
    }, paramHandler);
  }
  
  public void send(final RequestProvider paramRequestProvider, final AsyncResultCallback<byte[]> paramAsyncResultCallback, final Handler paramHandler)
  {
    synchronized (mChannelLock)
    {
      if (mChannelOpened)
      {
        paramRequestProvider = new com/android/internal/telephony/uicc/euicc/apdu/ApduException;
        paramRequestProvider.<init>("Logical channel has already been opened.");
        AsyncResultHelper.throwException(paramRequestProvider, paramAsyncResultCallback, paramHandler);
        return;
      }
      mChannelOpened = true;
      mOpenChannel.invoke(mAid, new AsyncResultCallback()
      {
        public void onResult(IccOpenLogicalChannelResponse arg1)
        {
          int i = ???.getChannel();
          int j = ???.getStatus();
          AsyncResultCallback localAsyncResultCallback;
          if ((i != -1) && (j == 1))
          {
            RequestBuilder localRequestBuilder = new RequestBuilder(i, mSupportExtendedApdu);
            localAsyncResultCallback = null;
            try
            {
              paramRequestProvider.buildRequest(???.getSelectResponse(), localRequestBuilder);
              ??? = localAsyncResultCallback;
            }
            catch (Throwable ???) {}
            if ((!localRequestBuilder.getCommands().isEmpty()) && (??? == null))
            {
              ApduSender.this.sendCommand(localRequestBuilder.getCommands(), 0, paramAsyncResultCallback, paramHandler);
              return;
            }
            ApduSender.this.closeAndReturn(i, null, ???, paramAsyncResultCallback, paramHandler);
            return;
          }
          synchronized (mChannelLock)
          {
            ApduSender.access$102(ApduSender.this, false);
            localAsyncResultCallback = paramAsyncResultCallback;
            ??? = new StringBuilder();
            ???.append("Failed to open logical channel opened for AID: ");
            ???.append(mAid);
            ???.append(", with status: ");
            ???.append(j);
            localAsyncResultCallback.onException(new ApduException(???.toString()));
            return;
          }
        }
      }, paramHandler);
      return;
    }
  }
}
