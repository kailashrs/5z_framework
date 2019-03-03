package com.android.internal.telephony.cat;

public class CatResponseMessage
{
  byte[] mAddedInfo = null;
  int mAdditionalInfo = 0;
  CommandDetails mCmdDet = null;
  int mEventValue = -1;
  boolean mIncludeAdditionalInfo = false;
  ResultCode mResCode = ResultCode.OK;
  boolean mUsersConfirm = false;
  String mUsersInput = null;
  int mUsersMenuSelection = 0;
  boolean mUsersYesNoSelection = false;
  
  public CatResponseMessage(CatCmdMessage paramCatCmdMessage)
  {
    mCmdDet = mCmdDet;
  }
  
  CommandDetails getCmdDetails()
  {
    return mCmdDet;
  }
  
  public void setAdditionalInfo(int paramInt)
  {
    mIncludeAdditionalInfo = true;
    mAdditionalInfo = paramInt;
  }
  
  public void setConfirmation(boolean paramBoolean)
  {
    mUsersConfirm = paramBoolean;
  }
  
  public void setEventDownload(int paramInt, byte[] paramArrayOfByte)
  {
    mEventValue = paramInt;
    mAddedInfo = paramArrayOfByte;
  }
  
  public void setInput(String paramString)
  {
    mUsersInput = paramString;
  }
  
  public void setMenuSelection(int paramInt)
  {
    mUsersMenuSelection = paramInt;
  }
  
  public void setResultCode(ResultCode paramResultCode)
  {
    mResCode = paramResultCode;
  }
  
  public void setYesNo(boolean paramBoolean)
  {
    mUsersYesNoSelection = paramBoolean;
  }
}
