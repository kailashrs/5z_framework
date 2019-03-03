package com.android.internal.telephony.cat;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CatCmdMessage
  implements Parcelable
{
  public static final Parcelable.Creator<CatCmdMessage> CREATOR = new Parcelable.Creator()
  {
    public CatCmdMessage createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CatCmdMessage(paramAnonymousParcel);
    }
    
    public CatCmdMessage[] newArray(int paramAnonymousInt)
    {
      return new CatCmdMessage[paramAnonymousInt];
    }
  };
  static final int REFRESH_NAA_INIT = 3;
  static final int REFRESH_NAA_INIT_AND_FILE_CHANGE = 2;
  static final int REFRESH_NAA_INIT_AND_FULL_FILE_CHANGE = 0;
  static final int REFRESH_UICC_RESET = 4;
  private BrowserSettings mBrowserSettings = null;
  private CallSettings mCallSettings = null;
  CommandDetails mCmdDet;
  private Input mInput;
  private boolean mLoadIconFailed;
  private Menu mMenu;
  private SetupEventListSettings mSetupEventListSettings = null;
  private TextMessage mTextMsg;
  private ToneSettings mToneSettings = null;
  
  public CatCmdMessage(Parcel paramParcel)
  {
    int i = 0;
    mLoadIconFailed = false;
    mCmdDet = ((CommandDetails)paramParcel.readParcelable(null));
    mTextMsg = ((TextMessage)paramParcel.readParcelable(null));
    mMenu = ((Menu)paramParcel.readParcelable(null));
    mInput = ((Input)paramParcel.readParcelable(null));
    int j = paramParcel.readByte();
    boolean bool = true;
    if (j != 1) {
      bool = false;
    }
    mLoadIconFailed = bool;
    j = 2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[getCmdType().ordinal()];
    if (j != 16)
    {
      if (j != 21)
      {
        switch (j)
        {
        default: 
          break;
        case 14: 
          mToneSettings = ((ToneSettings)paramParcel.readParcelable(null));
          break;
        case 13: 
          mBrowserSettings = new BrowserSettings();
          mBrowserSettings.url = paramParcel.readString();
          mBrowserSettings.mode = LaunchBrowserMode.values()[paramParcel.readInt()];
          break;
        }
      }
      else
      {
        mSetupEventListSettings = new SetupEventListSettings();
        j = paramParcel.readInt();
        mSetupEventListSettings.eventList = new int[j];
        while (i < j)
        {
          mSetupEventListSettings.eventList[i] = paramParcel.readInt();
          i++;
        }
      }
    }
    else
    {
      mCallSettings = new CallSettings();
      mCallSettings.confirmMsg = ((TextMessage)paramParcel.readParcelable(null));
      mCallSettings.callMsg = ((TextMessage)paramParcel.readParcelable(null));
    }
  }
  
  CatCmdMessage(CommandParams paramCommandParams)
  {
    mLoadIconFailed = false;
    mCmdDet = mCmdDet;
    mLoadIconFailed = mLoadIconFailed;
    switch (2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[getCmdType().ordinal()])
    {
    default: 
      break;
    case 21: 
      mSetupEventListSettings = new SetupEventListSettings();
      mSetupEventListSettings.eventList = mEventInfo;
      break;
    case 17: 
    case 18: 
    case 19: 
    case 20: 
      mTextMsg = mTextMsg;
      break;
    case 16: 
      mCallSettings = new CallSettings();
      mCallSettings.confirmMsg = mConfirmMsg;
      mCallSettings.callMsg = mCallMsg;
      break;
    case 15: 
      mTextMsg = mConfirmMsg;
      break;
    case 14: 
      paramCommandParams = (PlayToneParams)paramCommandParams;
      mToneSettings = mSettings;
      mTextMsg = mTextMsg;
      break;
    case 13: 
      mTextMsg = mConfirmMsg;
      mBrowserSettings = new BrowserSettings();
      mBrowserSettings.url = mUrl;
      mBrowserSettings.mode = mMode;
      break;
    case 11: 
    case 12: 
      mInput = mInput;
      break;
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
      mTextMsg = mTextMsg;
      break;
    case 1: 
    case 2: 
      mMenu = mMenu;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Input geInput()
  {
    return mInput;
  }
  
  public TextMessage geTextMessage()
  {
    return mTextMsg;
  }
  
  public BrowserSettings getBrowserSettings()
  {
    return mBrowserSettings;
  }
  
  public CallSettings getCallSettings()
  {
    return mCallSettings;
  }
  
  public AppInterface.CommandType getCmdType()
  {
    return AppInterface.CommandType.fromInt(mCmdDet.typeOfCommand);
  }
  
  public Menu getMenu()
  {
    return mMenu;
  }
  
  public SetupEventListSettings getSetEventList()
  {
    return mSetupEventListSettings;
  }
  
  public ToneSettings getToneSettings()
  {
    return mToneSettings;
  }
  
  public boolean hasIconLoadFailed()
  {
    return mLoadIconFailed;
  }
  
  public boolean isRefreshResetOrInit()
  {
    return (mCmdDet.commandQualifier == 0) || (mCmdDet.commandQualifier == 2) || (mCmdDet.commandQualifier == 3) || (mCmdDet.commandQualifier == 4);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mCmdDet, 0);
    paramParcel.writeParcelable(mTextMsg, 0);
    paramParcel.writeParcelable(mMenu, 0);
    paramParcel.writeParcelable(mInput, 0);
    paramParcel.writeByte((byte)mLoadIconFailed);
    paramInt = 2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[getCmdType().ordinal()];
    if (paramInt != 16)
    {
      if (paramInt != 21) {
        switch (paramInt)
        {
        default: 
          break;
        case 14: 
          paramParcel.writeParcelable(mToneSettings, 0);
          break;
        case 13: 
          paramParcel.writeString(mBrowserSettings.url);
          paramParcel.writeInt(mBrowserSettings.mode.ordinal());
          break;
        }
      } else {
        paramParcel.writeIntArray(mSetupEventListSettings.eventList);
      }
    }
    else
    {
      paramParcel.writeParcelable(mCallSettings.confirmMsg, 0);
      paramParcel.writeParcelable(mCallSettings.callMsg, 0);
    }
  }
  
  public class BrowserSettings
  {
    public LaunchBrowserMode mode;
    public String url;
    
    public BrowserSettings() {}
  }
  
  public final class BrowserTerminationCauses
  {
    public static final int ERROR_TERMINATION = 1;
    public static final int USER_TERMINATION = 0;
    
    public BrowserTerminationCauses() {}
  }
  
  public class CallSettings
  {
    public TextMessage callMsg;
    public TextMessage confirmMsg;
    
    public CallSettings() {}
  }
  
  public final class SetupEventListConstants
  {
    public static final int BROWSER_TERMINATION_EVENT = 8;
    public static final int BROWSING_STATUS_EVENT = 15;
    public static final int IDLE_SCREEN_AVAILABLE_EVENT = 5;
    public static final int LANGUAGE_SELECTION_EVENT = 7;
    public static final int USER_ACTIVITY_EVENT = 4;
    
    public SetupEventListConstants() {}
  }
  
  public class SetupEventListSettings
  {
    public int[] eventList;
    
    public SetupEventListSettings() {}
  }
}
