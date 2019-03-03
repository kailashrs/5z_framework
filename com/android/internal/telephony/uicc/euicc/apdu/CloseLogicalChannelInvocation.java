package com.android.internal.telephony.uicc.euicc.apdu;

import android.os.AsyncResult;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.uicc.euicc.async.AsyncMessageInvocation;

class CloseLogicalChannelInvocation
  extends AsyncMessageInvocation<Integer, Boolean>
{
  private static final String LOG_TAG = "CloseChan";
  private final CommandsInterface mCi;
  
  CloseLogicalChannelInvocation(CommandsInterface paramCommandsInterface)
  {
    mCi = paramCommandsInterface;
  }
  
  protected Boolean parseResult(AsyncResult paramAsyncResult)
  {
    if (exception == null) {
      return Boolean.valueOf(true);
    }
    if ((exception instanceof CommandException)) {
      Rlog.e("CloseChan", "CommandException", exception);
    } else {
      Rlog.e("CloseChan", "Unknown exception", exception);
    }
    return Boolean.valueOf(false);
  }
  
  protected void sendRequestMessage(Integer paramInteger, Message paramMessage)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Channel: ");
    localStringBuilder.append(paramInteger);
    Rlog.v("CloseChan", localStringBuilder.toString());
    mCi.iccCloseLogicalChannel(paramInteger.intValue(), paramMessage);
  }
}
