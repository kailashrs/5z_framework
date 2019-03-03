package com.android.internal.telephony.uicc.euicc.apdu;

import android.os.AsyncResult;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.uicc.IccIoResult;
import com.android.internal.telephony.uicc.euicc.async.AsyncMessageInvocation;

public class TransmitApduLogicalChannelInvocation
  extends AsyncMessageInvocation<ApduCommand, IccIoResult>
{
  private static final String LOG_TAG = "TransApdu";
  private static final int SW1_ERROR = 111;
  private final CommandsInterface mCi;
  
  TransmitApduLogicalChannelInvocation(CommandsInterface paramCommandsInterface)
  {
    mCi = paramCommandsInterface;
  }
  
  protected IccIoResult parseResult(AsyncResult paramAsyncResult)
  {
    if ((exception == null) && (result != null))
    {
      paramAsyncResult = (IccIoResult)result;
    }
    else
    {
      if (result == null) {
        Rlog.e("TransApdu", "Empty response");
      } else if ((exception instanceof CommandException)) {
        Rlog.e("TransApdu", "CommandException", exception);
      } else {
        Rlog.e("TransApdu", "CommandException", exception);
      }
      paramAsyncResult = new IccIoResult(111, 0, (byte[])null);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Response: ");
    localStringBuilder.append(paramAsyncResult);
    Rlog.v("TransApdu", localStringBuilder.toString());
    return paramAsyncResult;
  }
  
  protected void sendRequestMessage(ApduCommand paramApduCommand, Message paramMessage)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Send: ");
    localStringBuilder.append(paramApduCommand);
    Rlog.v("TransApdu", localStringBuilder.toString());
    mCi.iccTransmitApduLogicalChannel(channel, cla | channel, ins, p1, p2, p3, cmdHex, paramMessage);
  }
}
