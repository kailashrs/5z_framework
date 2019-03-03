package com.android.internal.telephony.uicc.euicc.apdu;

import android.os.AsyncResult;
import android.os.Message;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandException.Error;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.uicc.euicc.async.AsyncMessageInvocation;

class OpenLogicalChannelInvocation
  extends AsyncMessageInvocation<String, IccOpenLogicalChannelResponse>
{
  private static final String LOG_TAG = "OpenChan";
  private final CommandsInterface mCi;
  
  OpenLogicalChannelInvocation(CommandsInterface paramCommandsInterface)
  {
    mCi = paramCommandsInterface;
  }
  
  protected IccOpenLogicalChannelResponse parseResult(AsyncResult paramAsyncResult)
  {
    int i;
    int j;
    if ((exception == null) && (result != null))
    {
      int[] arrayOfInt = (int[])result;
      i = arrayOfInt[0];
      paramAsyncResult = null;
      if (arrayOfInt.length > 1)
      {
        localObject = new byte[arrayOfInt.length - 1];
        for (j = 1;; j++)
        {
          paramAsyncResult = (AsyncResult)localObject;
          if (j >= arrayOfInt.length) {
            break;
          }
          localObject[(j - 1)] = ((byte)(byte)arrayOfInt[j]);
        }
      }
      paramAsyncResult = new IccOpenLogicalChannelResponse(i, 1, paramAsyncResult);
    }
    else
    {
      if (result == null) {
        Rlog.e("OpenChan", "Empty response");
      }
      if (exception != null) {
        Rlog.e("OpenChan", "Exception", exception);
      }
      i = 4;
      j = i;
      if ((exception instanceof CommandException))
      {
        paramAsyncResult = ((CommandException)exception).getCommandError();
        if (paramAsyncResult == CommandException.Error.MISSING_RESOURCE)
        {
          j = 2;
        }
        else
        {
          j = i;
          if (paramAsyncResult == CommandException.Error.NO_SUCH_ELEMENT) {
            j = 3;
          }
        }
      }
      paramAsyncResult = new IccOpenLogicalChannelResponse(-1, j, null);
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Response: ");
    ((StringBuilder)localObject).append(paramAsyncResult);
    Rlog.v("OpenChan", ((StringBuilder)localObject).toString());
    return paramAsyncResult;
  }
  
  protected void sendRequestMessage(String paramString, Message paramMessage)
  {
    mCi.iccOpenLogicalChannel(paramString, 0, paramMessage);
  }
}
