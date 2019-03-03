package com.android.internal.telephony.cat;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.uicc.IccFileHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

class CommandParamsFactory
  extends Handler
{
  static final int DTTZ_SETTING = 3;
  static final int LANGUAGE_SETTING = 4;
  static final int LOAD_MULTI_ICONS = 2;
  static final int LOAD_NO_ICON = 0;
  static final int LOAD_SINGLE_ICON = 1;
  private static final int MAX_GSM7_DEFAULT_CHARS = 239;
  private static final int MAX_UCS2_CHARS = 118;
  static final int MSG_ID_LOAD_ICON_DONE = 1;
  static final int NON_SPECIFIC_LANGUAGE = 0;
  static final int SPECIFIC_LANGUAGE = 1;
  private static CommandParamsFactory sInstance = null;
  private RilMessageDecoder mCaller = null;
  private CommandParams mCmdParams = null;
  private int mIconLoadState = 0;
  private IconLoader mIconLoader;
  private String mRequestedLanguage;
  private String mSavedLanguage;
  private boolean mloadIcon = false;
  
  private CommandParamsFactory(RilMessageDecoder paramRilMessageDecoder, IccFileHandler paramIccFileHandler)
  {
    mCaller = paramRilMessageDecoder;
    mIconLoader = IconLoader.getInstance(this, paramIccFileHandler);
  }
  
  static CommandParamsFactory getInstance(RilMessageDecoder paramRilMessageDecoder, IccFileHandler paramIccFileHandler)
  {
    try
    {
      if (sInstance != null)
      {
        paramRilMessageDecoder = sInstance;
        return paramRilMessageDecoder;
      }
      if (paramIccFileHandler != null)
      {
        paramRilMessageDecoder = new CommandParamsFactory(paramRilMessageDecoder, paramIccFileHandler);
        return paramRilMessageDecoder;
      }
      return null;
    }
    finally {}
  }
  
  private boolean processBIPClient(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    AppInterface.CommandType localCommandType = AppInterface.CommandType.fromInt(typeOfCommand);
    if (localCommandType != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("process ");
      ((StringBuilder)localObject1).append(localCommandType.name());
      CatLog.d(this, ((StringBuilder)localObject1).toString());
    }
    Object localObject1 = new TextMessage();
    localCommandType = null;
    boolean bool = false;
    Object localObject2 = searchForTag(ComprehensionTlvTag.ALPHA_ID, paramList);
    if (localObject2 != null)
    {
      text = ValueParser.retrieveAlphaId((ComprehensionTlv)localObject2);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("alpha TLV text=");
      ((StringBuilder)localObject2).append(text);
      CatLog.d(this, ((StringBuilder)localObject2).toString());
      bool = true;
    }
    localObject2 = searchForTag(ComprehensionTlvTag.ICON_ID, paramList);
    paramList = localCommandType;
    if (localObject2 != null)
    {
      paramList = ValueParser.retrieveIconId((ComprehensionTlv)localObject2);
      iconSelfExplanatory = selfExplanatory;
    }
    responseNeeded = false;
    mCmdParams = new BIPClientParams(paramCommandDetails, (TextMessage)localObject1, bool);
    if (paramList != null)
    {
      mIconLoadState = 1;
      mIconLoader.loadIcon(recordNumber, obtainMessage(1));
      return true;
    }
    return false;
  }
  
  private CommandDetails processCommandDetails(List<ComprehensionTlv> paramList)
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    Object localObject3;
    if (paramList != null)
    {
      paramList = searchForTag(ComprehensionTlvTag.COMMAND_DETAILS, paramList);
      localObject2 = localObject1;
      if (paramList != null) {
        try
        {
          localObject2 = ValueParser.retrieveCommandDetails(paramList);
        }
        catch (ResultException localResultException)
        {
          paramList = new StringBuilder();
          paramList.append("processCommandDetails: Failed to procees command details e=");
          paramList.append(localResultException);
          CatLog.d(this, paramList.toString());
          localObject3 = localObject1;
        }
      }
    }
    return localObject3;
  }
  
  private boolean processDisplayText(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    CatLog.d(this, "process DisplayText");
    TextMessage localTextMessage = new TextMessage();
    IconId localIconId = null;
    ComprehensionTlv localComprehensionTlv = searchForTag(ComprehensionTlvTag.TEXT_STRING, paramList);
    if (localComprehensionTlv != null) {
      text = ValueParser.retrieveTextString(localComprehensionTlv);
    }
    if (text != null)
    {
      if (searchForTag(ComprehensionTlvTag.IMMEDIATE_RESPONSE, paramList) != null) {
        responseNeeded = false;
      }
      localComprehensionTlv = searchForTag(ComprehensionTlvTag.ICON_ID, paramList);
      if (localComprehensionTlv != null)
      {
        localIconId = ValueParser.retrieveIconId(localComprehensionTlv);
        iconSelfExplanatory = selfExplanatory;
      }
      paramList = searchForTag(ComprehensionTlvTag.DURATION, paramList);
      if (paramList != null) {
        duration = ValueParser.retrieveDuration(paramList);
      }
      boolean bool;
      if ((commandQualifier & 0x1) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      isHighPriority = bool;
      if ((commandQualifier & 0x80) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      userClear = bool;
      mCmdParams = new DisplayTextParams(paramCommandDetails, localTextMessage);
      if (localIconId != null)
      {
        mloadIcon = true;
        mIconLoadState = 1;
        mIconLoader.loadIcon(recordNumber, obtainMessage(1));
        return true;
      }
      return false;
    }
    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
  }
  
  private boolean processEventNotify(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    CatLog.d(this, "process EventNotify");
    TextMessage localTextMessage = new TextMessage();
    Object localObject = null;
    text = ValueParser.retrieveAlphaId(searchForTag(ComprehensionTlvTag.ALPHA_ID, paramList));
    ComprehensionTlv localComprehensionTlv = searchForTag(ComprehensionTlvTag.ICON_ID, paramList);
    paramList = localObject;
    if (localComprehensionTlv != null)
    {
      paramList = ValueParser.retrieveIconId(localComprehensionTlv);
      iconSelfExplanatory = selfExplanatory;
    }
    responseNeeded = false;
    mCmdParams = new DisplayTextParams(paramCommandDetails, localTextMessage);
    if (paramList != null)
    {
      mloadIcon = true;
      mIconLoadState = 1;
      mIconLoader.loadIcon(recordNumber, obtainMessage(1));
      return true;
    }
    return false;
  }
  
  private boolean processGetInkey(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    CatLog.d(this, "process GetInkey");
    Input localInput = new Input();
    IconId localIconId = null;
    ComprehensionTlv localComprehensionTlv = searchForTag(ComprehensionTlvTag.TEXT_STRING, paramList);
    if (localComprehensionTlv != null)
    {
      text = ValueParser.retrieveTextString(localComprehensionTlv);
      localComprehensionTlv = searchForTag(ComprehensionTlvTag.ICON_ID, paramList);
      if (localComprehensionTlv != null)
      {
        localIconId = ValueParser.retrieveIconId(localComprehensionTlv);
        iconSelfExplanatory = selfExplanatory;
      }
      paramList = searchForTag(ComprehensionTlvTag.DURATION, paramList);
      if (paramList != null) {
        duration = ValueParser.retrieveDuration(paramList);
      }
      minLen = 1;
      maxLen = 1;
      boolean bool;
      if ((commandQualifier & 0x1) == 0) {
        bool = true;
      } else {
        bool = false;
      }
      digitOnly = bool;
      if ((commandQualifier & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      ucs2 = bool;
      if ((commandQualifier & 0x4) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      yesNo = bool;
      if ((commandQualifier & 0x80) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      helpAvailable = bool;
      echo = true;
      mCmdParams = new GetInputParams(paramCommandDetails, localInput);
      if (localIconId != null)
      {
        mloadIcon = true;
        mIconLoadState = 1;
        mIconLoader.loadIcon(recordNumber, obtainMessage(1));
        return true;
      }
      return false;
    }
    throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
  }
  
  private boolean processGetInput(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    CatLog.d(this, "process GetInput");
    Input localInput = new Input();
    IconId localIconId = null;
    Object localObject = searchForTag(ComprehensionTlvTag.TEXT_STRING, paramList);
    if (localObject != null)
    {
      text = ValueParser.retrieveTextString((ComprehensionTlv)localObject);
      ComprehensionTlv localComprehensionTlv = searchForTag(ComprehensionTlvTag.RESPONSE_LENGTH, paramList);
      if (localComprehensionTlv != null) {
        try
        {
          localObject = localComprehensionTlv.getRawValue();
          int i = localComprehensionTlv.getValueIndex();
          minLen = (localObject[i] & 0xFF);
          maxLen = (localObject[(i + 1)] & 0xFF);
          localObject = searchForTag(ComprehensionTlvTag.DEFAULT_TEXT, paramList);
          if (localObject != null) {
            defaultText = ValueParser.retrieveTextString((ComprehensionTlv)localObject);
          }
          localObject = searchForTag(ComprehensionTlvTag.ICON_ID, paramList);
          if (localObject != null)
          {
            localIconId = ValueParser.retrieveIconId((ComprehensionTlv)localObject);
            iconSelfExplanatory = selfExplanatory;
          }
          paramList = searchForTag(ComprehensionTlvTag.DURATION, paramList);
          if (paramList != null) {
            duration = ValueParser.retrieveDuration(paramList);
          }
          boolean bool;
          if ((commandQualifier & 0x1) == 0) {
            bool = true;
          } else {
            bool = false;
          }
          digitOnly = bool;
          if ((commandQualifier & 0x2) != 0) {
            bool = true;
          } else {
            bool = false;
          }
          ucs2 = bool;
          if ((commandQualifier & 0x4) == 0) {
            bool = true;
          } else {
            bool = false;
          }
          echo = bool;
          if ((commandQualifier & 0x8) != 0) {
            bool = true;
          } else {
            bool = false;
          }
          packed = bool;
          if ((commandQualifier & 0x80) != 0) {
            bool = true;
          } else {
            bool = false;
          }
          helpAvailable = bool;
          if ((ucs2) && (maxLen > 118))
          {
            paramList = new StringBuilder();
            paramList.append("UCS2: received maxLen = ");
            paramList.append(maxLen);
            paramList.append(", truncating to ");
            paramList.append(118);
            CatLog.d(this, paramList.toString());
            maxLen = 118;
          }
          else if ((!packed) && (maxLen > 239))
          {
            paramList = new StringBuilder();
            paramList.append("GSM 7Bit Default: received maxLen = ");
            paramList.append(maxLen);
            paramList.append(", truncating to ");
            paramList.append(239);
            CatLog.d(this, paramList.toString());
            maxLen = 239;
          }
          mCmdParams = new GetInputParams(paramCommandDetails, localInput);
          if (localIconId != null)
          {
            mloadIcon = true;
            mIconLoadState = 1;
            mIconLoader.loadIcon(recordNumber, obtainMessage(1));
            return true;
          }
          return false;
        }
        catch (IndexOutOfBoundsException paramCommandDetails)
        {
          throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
      }
      throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
    }
    throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
  }
  
  private boolean processLanguageNotification(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    CatLog.d(this, "process Language Notification");
    StringBuilder localStringBuilder = null;
    ComprehensionTlv localComprehensionTlv = null;
    String str = Locale.getDefault().getLanguage();
    switch (commandQualifier)
    {
    default: 
      paramList = new StringBuilder();
      paramList.append("LN[");
      paramList.append(commandQualifier);
      paramList.append("] Command Not Supported");
      CatLog.d(this, paramList.toString());
      paramList = localStringBuilder;
      break;
    case 1: 
      localComprehensionTlv = searchForTag(ComprehensionTlvTag.LANGUAGE, paramList);
      paramList = localStringBuilder;
      if (localComprehensionTlv != null) {
        if (localComprehensionTlv.getLength() == 2)
        {
          paramList = GsmAlphabet.gsm8BitUnpackedToString(localComprehensionTlv.getRawValue(), localComprehensionTlv.getValueIndex(), 2);
          if ((TextUtils.isEmpty(mSavedLanguage)) || ((!TextUtils.isEmpty(mRequestedLanguage)) && (!mRequestedLanguage.equals(str)))) {
            mSavedLanguage = str;
          }
          mRequestedLanguage = paramList;
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Specific language notification changes the language setting to ");
          localStringBuilder.append(mRequestedLanguage);
          CatLog.d(this, localStringBuilder.toString());
        }
        else
        {
          throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
      }
      break;
    case 0: 
      paramList = localComprehensionTlv;
      if (!TextUtils.isEmpty(mSavedLanguage))
      {
        paramList = localComprehensionTlv;
        if (!TextUtils.isEmpty(mRequestedLanguage))
        {
          paramList = localComprehensionTlv;
          if (mRequestedLanguage.equals(str))
          {
            paramList = new StringBuilder();
            paramList.append("Non-specific language notification changes the language setting back to ");
            paramList.append(mSavedLanguage);
            CatLog.d(this, paramList.toString());
            paramList = mSavedLanguage;
          }
        }
      }
      mSavedLanguage = null;
      mRequestedLanguage = null;
    }
    mCmdParams = new LanguageParams(paramCommandDetails, paramList);
    return false;
  }
  
  private boolean processLaunchBrowser(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    CatLog.d(this, "process LaunchBrowser");
    TextMessage localTextMessage = new TextMessage();
    IconId localIconId = null;
    Object localObject = null;
    ComprehensionTlv localComprehensionTlv = searchForTag(ComprehensionTlvTag.URL, paramList);
    if (localComprehensionTlv != null) {
      try
      {
        localObject = localComprehensionTlv.getRawValue();
        int i = localComprehensionTlv.getValueIndex();
        int j = localComprehensionTlv.getLength();
        if (j > 0) {
          localObject = GsmAlphabet.gsm8BitUnpackedToString((byte[])localObject, i, j);
        } else {
          localObject = null;
        }
      }
      catch (IndexOutOfBoundsException paramCommandDetails)
      {
        throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
      }
    }
    text = ValueParser.retrieveAlphaId(searchForTag(ComprehensionTlvTag.ALPHA_ID, paramList));
    paramList = searchForTag(ComprehensionTlvTag.ICON_ID, paramList);
    if (paramList != null)
    {
      localIconId = ValueParser.retrieveIconId(paramList);
      iconSelfExplanatory = selfExplanatory;
    }
    switch (commandQualifier)
    {
    default: 
      paramList = LaunchBrowserMode.LAUNCH_IF_NOT_ALREADY_LAUNCHED;
      break;
    case 3: 
      paramList = LaunchBrowserMode.LAUNCH_NEW_BROWSER;
      break;
    case 2: 
      paramList = LaunchBrowserMode.USE_EXISTING_BROWSER;
    }
    mCmdParams = new LaunchBrowserParams(paramCommandDetails, localTextMessage, (String)localObject, paramList);
    if (localIconId != null)
    {
      mIconLoadState = 1;
      mIconLoader.loadIcon(recordNumber, obtainMessage(1));
      return true;
    }
    return false;
  }
  
  private boolean processPlayTone(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    CatLog.d(this, "process PlayTone");
    ComprehensionTlv localComprehensionTlv1 = null;
    TextMessage localTextMessage = new TextMessage();
    Duration localDuration = null;
    Object localObject1 = null;
    ComprehensionTlv localComprehensionTlv2 = searchForTag(ComprehensionTlvTag.TONE, paramList);
    Object localObject2 = localComprehensionTlv1;
    if (localComprehensionTlv2 != null)
    {
      localObject2 = localComprehensionTlv1;
      if (localComprehensionTlv2.getLength() > 0) {
        try
        {
          localObject2 = Tone.fromInt(localComprehensionTlv2.getRawValue()[localComprehensionTlv2.getValueIndex()]);
        }
        catch (IndexOutOfBoundsException paramCommandDetails)
        {
          throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
      }
    }
    localComprehensionTlv1 = searchForTag(ComprehensionTlvTag.ALPHA_ID, paramList);
    if (localComprehensionTlv1 != null)
    {
      text = ValueParser.retrieveAlphaId(localComprehensionTlv1);
      if (text == null) {
        text = "";
      }
    }
    localComprehensionTlv1 = searchForTag(ComprehensionTlvTag.DURATION, paramList);
    if (localComprehensionTlv1 != null) {
      localDuration = ValueParser.retrieveDuration(localComprehensionTlv1);
    }
    localComprehensionTlv1 = searchForTag(ComprehensionTlvTag.ICON_ID, paramList);
    paramList = localObject1;
    if (localComprehensionTlv1 != null)
    {
      paramList = ValueParser.retrieveIconId(localComprehensionTlv1);
      iconSelfExplanatory = selfExplanatory;
    }
    boolean bool;
    if ((commandQualifier & 0x1) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    responseNeeded = false;
    mCmdParams = new PlayToneParams(paramCommandDetails, localTextMessage, (Tone)localObject2, localDuration, bool);
    if (paramList != null)
    {
      mIconLoadState = 1;
      mIconLoader.loadIcon(recordNumber, obtainMessage(1));
      return true;
    }
    return false;
  }
  
  private boolean processProvideLocalInfo(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    CatLog.d(this, "process ProvideLocalInfo");
    switch (commandQualifier)
    {
    default: 
      paramList = new StringBuilder();
      paramList.append("PLI[");
      paramList.append(commandQualifier);
      paramList.append("] Command Not Supported");
      CatLog.d(this, paramList.toString());
      mCmdParams = new CommandParams(paramCommandDetails);
      throw new ResultException(ResultCode.BEYOND_TERMINAL_CAPABILITY);
    case 4: 
      CatLog.d(this, "PLI [LANGUAGE_SETTING]");
      mCmdParams = new CommandParams(paramCommandDetails);
      break;
    case 3: 
      CatLog.d(this, "PLI [DTTZ_SETTING]");
      mCmdParams = new CommandParams(paramCommandDetails);
    }
    return false;
  }
  
  private boolean processSelectItem(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    CatLog.d(this, "process SelectItem");
    Menu localMenu = new Menu();
    IconId localIconId = null;
    Object localObject1 = null;
    Object localObject2 = paramList.iterator();
    AppInterface.CommandType localCommandType = AppInterface.CommandType.fromInt(typeOfCommand);
    ComprehensionTlv localComprehensionTlv = searchForTag(ComprehensionTlvTag.ALPHA_ID, paramList);
    if (localComprehensionTlv != null) {
      title = ValueParser.retrieveAlphaId(localComprehensionTlv);
    } else {
      if (localCommandType == AppInterface.CommandType.SET_UP_MENU) {
        break label483;
      }
    }
    for (;;)
    {
      localComprehensionTlv = searchForNextTag(ComprehensionTlvTag.ITEM, (Iterator)localObject2);
      if (localComprehensionTlv == null) {
        break;
      }
      items.add(ValueParser.retrieveItem(localComprehensionTlv));
    }
    if (items.size() != 0)
    {
      localObject2 = searchForTag(ComprehensionTlvTag.ITEM_ID, paramList);
      if (localObject2 != null) {
        defaultItem = (ValueParser.retrieveItemId((ComprehensionTlv)localObject2) - 1);
      }
      localObject2 = searchForTag(ComprehensionTlvTag.ICON_ID, paramList);
      if (localObject2 != null)
      {
        mIconLoadState = 1;
        localIconId = ValueParser.retrieveIconId((ComprehensionTlv)localObject2);
        titleIconSelfExplanatory = selfExplanatory;
      }
      localObject2 = searchForTag(ComprehensionTlvTag.ITEM_ICON_ID_LIST, paramList);
      paramList = localObject1;
      if (localObject2 != null)
      {
        mIconLoadState = 2;
        paramList = ValueParser.retrieveItemsIconId((ComprehensionTlv)localObject2);
        itemsIconSelfExplanatory = selfExplanatory;
      }
      int i;
      if ((commandQualifier & 0x1) != 0) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0) {
        if ((0x2 & commandQualifier) == 0) {
          presentationType = PresentationType.DATA_VALUES;
        } else {
          presentationType = PresentationType.NAVIGATION_OPTIONS;
        }
      }
      boolean bool;
      if ((commandQualifier & 0x4) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      softKeyPreferred = bool;
      if ((commandQualifier & 0x80) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      helpAvailable = bool;
      if (localIconId != null) {
        bool = true;
      } else {
        bool = false;
      }
      mCmdParams = new SelectItemParams(paramCommandDetails, localMenu, bool);
      switch (mIconLoadState)
      {
      default: 
        break;
      case 2: 
        paramCommandDetails = recordNumbers;
        if (localIconId != null)
        {
          paramCommandDetails = new int[recordNumbers.length + 1];
          paramCommandDetails[0] = recordNumber;
          System.arraycopy(recordNumbers, 0, paramCommandDetails, 1, recordNumbers.length);
        }
        mloadIcon = true;
        mIconLoader.loadIcons(paramCommandDetails, obtainMessage(1));
        break;
      case 1: 
        mloadIcon = true;
        mIconLoader.loadIcon(recordNumber, obtainMessage(1));
        break;
      case 0: 
        return false;
      }
      return true;
    }
    throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
    label483:
    throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
  }
  
  private boolean processSetUpEventList(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
  {
    CatLog.d(this, "process SetUpEventList");
    Object localObject = searchForTag(ComprehensionTlvTag.EVENT_LIST, paramList);
    if (localObject != null) {
      try
      {
        paramList = ((ComprehensionTlv)localObject).getRawValue();
        int i = ((ComprehensionTlv)localObject).getValueIndex();
        int j = ((ComprehensionTlv)localObject).getLength();
        localObject = new int[j];
        int k = 0;
        while (j > 0)
        {
          int m = paramList[i] & 0xFF;
          i++;
          j--;
          switch (m)
          {
          default: 
            break;
          case 4: 
          case 5: 
          case 7: 
          case 8: 
          case 15: 
            localObject[k] = m;
            k++;
          }
        }
        paramList = new com/android/internal/telephony/cat/SetEventListParams;
        paramList.<init>(paramCommandDetails, (int[])localObject);
        mCmdParams = paramList;
      }
      catch (IndexOutOfBoundsException paramCommandDetails)
      {
        CatLog.e(this, " IndexOutofBoundException in processSetUpEventList");
      }
    }
    return false;
  }
  
  private boolean processSetUpIdleModeText(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    CatLog.d(this, "process SetUpIdleModeText");
    TextMessage localTextMessage = new TextMessage();
    Object localObject = null;
    ComprehensionTlv localComprehensionTlv = searchForTag(ComprehensionTlvTag.TEXT_STRING, paramList);
    if (localComprehensionTlv != null) {
      text = ValueParser.retrieveTextString(localComprehensionTlv);
    }
    localComprehensionTlv = searchForTag(ComprehensionTlvTag.ICON_ID, paramList);
    paramList = localObject;
    if (localComprehensionTlv != null)
    {
      paramList = ValueParser.retrieveIconId(localComprehensionTlv);
      iconSelfExplanatory = selfExplanatory;
    }
    if ((text == null) && (paramList != null) && (!iconSelfExplanatory)) {
      throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
    }
    mCmdParams = new DisplayTextParams(paramCommandDetails, localTextMessage);
    if (paramList != null)
    {
      mloadIcon = true;
      mIconLoadState = 1;
      mIconLoader.loadIcon(recordNumber, obtainMessage(1));
      return true;
    }
    return false;
  }
  
  private boolean processSetupCall(CommandDetails paramCommandDetails, List<ComprehensionTlv> paramList)
    throws ResultException
  {
    CatLog.d(this, "process SetupCall");
    Object localObject1 = paramList.iterator();
    TextMessage localTextMessage1 = new TextMessage();
    TextMessage localTextMessage2 = new TextMessage();
    IconId localIconId = null;
    Object localObject2 = null;
    text = ValueParser.retrieveAlphaId(searchForNextTag(ComprehensionTlvTag.ALPHA_ID, (Iterator)localObject1));
    ComprehensionTlv localComprehensionTlv = searchForTag(ComprehensionTlvTag.ICON_ID, paramList);
    if (localComprehensionTlv != null)
    {
      localIconId = ValueParser.retrieveIconId(localComprehensionTlv);
      iconSelfExplanatory = selfExplanatory;
    }
    localObject1 = searchForNextTag(ComprehensionTlvTag.ALPHA_ID, (Iterator)localObject1);
    if (localObject1 != null) {
      text = ValueParser.retrieveAlphaId((ComprehensionTlv)localObject1);
    }
    localObject1 = searchForTag(ComprehensionTlvTag.ICON_ID, paramList);
    paramList = localObject2;
    if (localObject1 != null)
    {
      paramList = ValueParser.retrieveIconId((ComprehensionTlv)localObject1);
      iconSelfExplanatory = selfExplanatory;
    }
    mCmdParams = new CallSetupParams(paramCommandDetails, localTextMessage1, localTextMessage2);
    if ((localIconId == null) && (paramList == null)) {
      return false;
    }
    mIconLoadState = 2;
    int i = -1;
    int j;
    if (localIconId != null) {
      j = recordNumber;
    } else {
      j = -1;
    }
    if (paramList != null) {
      i = recordNumber;
    }
    paramCommandDetails = mIconLoader;
    paramList = obtainMessage(1);
    paramCommandDetails.loadIcons(new int[] { j, i }, paramList);
    return true;
  }
  
  private ComprehensionTlv searchForNextTag(ComprehensionTlvTag paramComprehensionTlvTag, Iterator<ComprehensionTlv> paramIterator)
  {
    int i = paramComprehensionTlvTag.value();
    while (paramIterator.hasNext())
    {
      paramComprehensionTlvTag = (ComprehensionTlv)paramIterator.next();
      if (paramComprehensionTlvTag.getTag() == i) {
        return paramComprehensionTlvTag;
      }
    }
    return null;
  }
  
  private ComprehensionTlv searchForTag(ComprehensionTlvTag paramComprehensionTlvTag, List<ComprehensionTlv> paramList)
  {
    return searchForNextTag(paramComprehensionTlvTag, paramList.iterator());
  }
  
  private void sendCmdParams(ResultCode paramResultCode)
  {
    mCaller.sendMsgParamsDecoded(paramResultCode, mCmdParams);
  }
  
  private ResultCode setIcons(Object paramObject)
  {
    int i = 0;
    if (paramObject == null)
    {
      CatLog.d(this, "Optional Icon data is NULL");
      mCmdParams.mLoadIconFailed = true;
      mloadIcon = false;
      return ResultCode.OK;
    }
    Bitmap[] arrayOfBitmap;
    int j;
    switch (mIconLoadState)
    {
    default: 
      break;
    case 2: 
      arrayOfBitmap = (Bitmap[])paramObject;
      j = arrayOfBitmap.length;
    case 1: 
      while (i < j)
      {
        paramObject = arrayOfBitmap[i];
        mCmdParams.setIcon(paramObject);
        if ((paramObject == null) && (mloadIcon))
        {
          CatLog.d(this, "Optional Icon data is NULL while loading multi icons");
          mCmdParams.mLoadIconFailed = true;
        }
        i++;
        continue;
        mCmdParams.setIcon((Bitmap)paramObject);
      }
    }
    return ResultCode.OK;
  }
  
  public void dispose()
  {
    mIconLoader.dispose();
    mIconLoader = null;
    mCmdParams = null;
    mCaller = null;
    sInstance = null;
  }
  
  public void handleMessage(Message paramMessage)
  {
    if ((what == 1) && (mIconLoader != null)) {
      sendCmdParams(setIcons(obj));
    }
  }
  
  void make(BerTlv paramBerTlv)
  {
    if (paramBerTlv == null) {
      return;
    }
    mCmdParams = null;
    mIconLoadState = 0;
    if (paramBerTlv.getTag() != 208)
    {
      sendCmdParams(ResultCode.CMD_TYPE_NOT_UNDERSTOOD);
      return;
    }
    Object localObject = paramBerTlv.getComprehensionTlvs();
    CommandDetails localCommandDetails = processCommandDetails((List)localObject);
    if (localCommandDetails == null)
    {
      sendCmdParams(ResultCode.CMD_TYPE_NOT_UNDERSTOOD);
      return;
    }
    AppInterface.CommandType localCommandType = AppInterface.CommandType.fromInt(typeOfCommand);
    if (localCommandType == null)
    {
      mCmdParams = new CommandParams(localCommandDetails);
      sendCmdParams(ResultCode.BEYOND_TERMINAL_CAPABILITY);
      return;
    }
    if (!paramBerTlv.isLengthValid())
    {
      mCmdParams = new CommandParams(localCommandDetails);
      sendCmdParams(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
      return;
    }
    try
    {
      boolean bool;
      switch (1.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[localCommandType.ordinal()])
      {
      default: 
        paramBerTlv = new com/android/internal/telephony/cat/CommandParams;
        break;
      case 20: 
      case 21: 
      case 22: 
      case 23: 
        bool = processBIPClient(localCommandDetails, (List)localObject);
        break;
      case 19: 
        bool = processLanguageNotification(localCommandDetails, (List)localObject);
        break;
      case 18: 
        bool = processProvideLocalInfo(localCommandDetails, (List)localObject);
        break;
      case 17: 
        bool = processSetUpEventList(localCommandDetails, (List)localObject);
        break;
      case 16: 
        bool = processPlayTone(localCommandDetails, (List)localObject);
        break;
      case 15: 
        bool = processLaunchBrowser(localCommandDetails, (List)localObject);
        break;
      case 14: 
        bool = processEventNotify(localCommandDetails, (List)localObject);
        break;
      case 12: 
      case 13: 
        bool = processSetupCall(localCommandDetails, (List)localObject);
        break;
      case 7: 
      case 8: 
      case 9: 
      case 10: 
      case 11: 
        bool = processEventNotify(localCommandDetails, (List)localObject);
        break;
      case 6: 
        bool = processGetInput(localCommandDetails, (List)localObject);
        break;
      case 5: 
        bool = processGetInkey(localCommandDetails, (List)localObject);
        break;
      case 4: 
        bool = processSetUpIdleModeText(localCommandDetails, (List)localObject);
        break;
      case 3: 
        bool = processDisplayText(localCommandDetails, (List)localObject);
        break;
      case 2: 
        bool = processSelectItem(localCommandDetails, (List)localObject);
        break;
      case 1: 
        bool = processSelectItem(localCommandDetails, (List)localObject);
      }
      if (!bool) {
        sendCmdParams(ResultCode.OK);
      }
      return;
      paramBerTlv.<init>(localCommandDetails);
      mCmdParams = paramBerTlv;
      sendCmdParams(ResultCode.BEYOND_TERMINAL_CAPABILITY);
      return;
    }
    catch (ResultException paramBerTlv)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("make: caught ResultException e=");
      ((StringBuilder)localObject).append(paramBerTlv);
      CatLog.d(this, ((StringBuilder)localObject).toString());
      mCmdParams = new CommandParams(localCommandDetails);
      sendCmdParams(paramBerTlv.result());
    }
  }
}
