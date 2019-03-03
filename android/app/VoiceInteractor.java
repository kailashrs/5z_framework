package android.app;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.DebugUtils;
import android.util.Log;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.IVoiceInteractorCallback;
import com.android.internal.app.IVoiceInteractorCallback.Stub;
import com.android.internal.app.IVoiceInteractorRequest;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.HandlerCaller.Callback;
import com.android.internal.os.SomeArgs;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class VoiceInteractor
{
  static final boolean DEBUG = false;
  static final int MSG_ABORT_VOICE_RESULT = 4;
  static final int MSG_CANCEL_RESULT = 6;
  static final int MSG_COMMAND_RESULT = 5;
  static final int MSG_COMPLETE_VOICE_RESULT = 3;
  static final int MSG_CONFIRMATION_RESULT = 1;
  static final int MSG_PICK_OPTION_RESULT = 2;
  static final Request[] NO_REQUESTS = new Request[0];
  static final String TAG = "VoiceInteractor";
  final ArrayMap<IBinder, Request> mActiveRequests = new ArrayMap();
  Activity mActivity;
  final IVoiceInteractorCallback.Stub mCallback = new IVoiceInteractorCallback.Stub()
  {
    public void deliverAbortVoiceResult(IVoiceInteractorRequest paramAnonymousIVoiceInteractorRequest, Bundle paramAnonymousBundle)
    {
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageOO(4, paramAnonymousIVoiceInteractorRequest, paramAnonymousBundle));
    }
    
    public void deliverCancel(IVoiceInteractorRequest paramAnonymousIVoiceInteractorRequest)
      throws RemoteException
    {
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageOO(6, paramAnonymousIVoiceInteractorRequest, null));
    }
    
    public void deliverCommandResult(IVoiceInteractorRequest paramAnonymousIVoiceInteractorRequest, boolean paramAnonymousBoolean, Bundle paramAnonymousBundle)
    {
      HandlerCaller localHandlerCaller1 = mHandlerCaller;
      HandlerCaller localHandlerCaller2 = mHandlerCaller;
      localHandlerCaller1.sendMessage(localHandlerCaller2.obtainMessageIOO(5, paramAnonymousBoolean, paramAnonymousIVoiceInteractorRequest, paramAnonymousBundle));
    }
    
    public void deliverCompleteVoiceResult(IVoiceInteractorRequest paramAnonymousIVoiceInteractorRequest, Bundle paramAnonymousBundle)
    {
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageOO(3, paramAnonymousIVoiceInteractorRequest, paramAnonymousBundle));
    }
    
    public void deliverConfirmationResult(IVoiceInteractorRequest paramAnonymousIVoiceInteractorRequest, boolean paramAnonymousBoolean, Bundle paramAnonymousBundle)
    {
      HandlerCaller localHandlerCaller1 = mHandlerCaller;
      HandlerCaller localHandlerCaller2 = mHandlerCaller;
      localHandlerCaller1.sendMessage(localHandlerCaller2.obtainMessageIOO(1, paramAnonymousBoolean, paramAnonymousIVoiceInteractorRequest, paramAnonymousBundle));
    }
    
    public void deliverPickOptionResult(IVoiceInteractorRequest paramAnonymousIVoiceInteractorRequest, boolean paramAnonymousBoolean, VoiceInteractor.PickOptionRequest.Option[] paramAnonymousArrayOfOption, Bundle paramAnonymousBundle)
    {
      HandlerCaller localHandlerCaller1 = mHandlerCaller;
      HandlerCaller localHandlerCaller2 = mHandlerCaller;
      localHandlerCaller1.sendMessage(localHandlerCaller2.obtainMessageIOOO(2, paramAnonymousBoolean, paramAnonymousIVoiceInteractorRequest, paramAnonymousArrayOfOption, paramAnonymousBundle));
    }
  };
  Context mContext;
  final HandlerCaller mHandlerCaller;
  final HandlerCaller.Callback mHandlerCallerCallback = new HandlerCaller.Callback()
  {
    public void executeMessage(Message paramAnonymousMessage)
    {
      SomeArgs localSomeArgs = (SomeArgs)obj;
      int i = what;
      boolean bool1 = false;
      boolean bool2 = false;
      boolean bool3 = false;
      Object localObject1;
      Object localObject2;
      switch (i)
      {
      default: 
        break;
      case 6: 
        paramAnonymousMessage = pullRequest((IVoiceInteractorRequest)arg1, true);
        if (paramAnonymousMessage != null)
        {
          paramAnonymousMessage.onCancel();
          paramAnonymousMessage.clear();
        }
        break;
      case 5: 
        if (arg1 != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        localObject1 = pullRequest((IVoiceInteractorRequest)arg1, bool1);
        if (localObject1 != null)
        {
          localObject2 = (VoiceInteractor.CommandRequest)localObject1;
          if (arg1 != 0) {
            bool3 = true;
          }
          ((VoiceInteractor.CommandRequest)localObject2).onCommandResult(bool3, (Bundle)arg2);
          if (bool1) {
            ((VoiceInteractor.Request)localObject1).clear();
          }
        }
        break;
      case 4: 
        paramAnonymousMessage = pullRequest((IVoiceInteractorRequest)arg1, true);
        if (paramAnonymousMessage != null)
        {
          ((VoiceInteractor.AbortVoiceRequest)paramAnonymousMessage).onAbortResult((Bundle)arg2);
          paramAnonymousMessage.clear();
        }
        break;
      case 3: 
        paramAnonymousMessage = pullRequest((IVoiceInteractorRequest)arg1, true);
        if (paramAnonymousMessage != null)
        {
          ((VoiceInteractor.CompleteVoiceRequest)paramAnonymousMessage).onCompleteResult((Bundle)arg2);
          paramAnonymousMessage.clear();
        }
        break;
      case 2: 
        if (arg1 != 0) {
          bool1 = true;
        }
        paramAnonymousMessage = pullRequest((IVoiceInteractorRequest)arg1, bool1);
        if (paramAnonymousMessage != null)
        {
          ((VoiceInteractor.PickOptionRequest)paramAnonymousMessage).onPickOptionResult(bool1, (VoiceInteractor.PickOptionRequest.Option[])arg2, (Bundle)arg3);
          if (bool1) {
            paramAnonymousMessage.clear();
          }
        }
        break;
      case 1: 
        localObject2 = pullRequest((IVoiceInteractorRequest)arg1, true);
        if (localObject2 != null)
        {
          localObject1 = (VoiceInteractor.ConfirmationRequest)localObject2;
          bool1 = bool2;
          if (arg1 != 0) {
            bool1 = true;
          }
          ((VoiceInteractor.ConfirmationRequest)localObject1).onConfirmationResult(bool1, (Bundle)arg2);
          ((VoiceInteractor.Request)localObject2).clear();
        }
        break;
      }
    }
  };
  final IVoiceInteractor mInteractor;
  boolean mRetaining;
  
  VoiceInteractor(IVoiceInteractor paramIVoiceInteractor, Context paramContext, Activity paramActivity, Looper paramLooper)
  {
    mInteractor = paramIVoiceInteractor;
    mContext = paramContext;
    mActivity = paramActivity;
    mHandlerCaller = new HandlerCaller(paramContext, paramLooper, mHandlerCallerCallback, true);
  }
  
  private ArrayList<Request> makeRequestList()
  {
    int i = mActiveRequests.size();
    if (i < 1) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(i);
    for (int j = 0; j < i; j++) {
      localArrayList.add((Request)mActiveRequests.valueAt(j));
    }
    return localArrayList;
  }
  
  void attachActivity(Activity paramActivity)
  {
    int i = 0;
    mRetaining = false;
    if (mActivity == paramActivity) {
      return;
    }
    mContext = paramActivity;
    mActivity = paramActivity;
    ArrayList localArrayList = makeRequestList();
    if (localArrayList != null) {
      while (i < localArrayList.size())
      {
        Request localRequest = (Request)localArrayList.get(i);
        mContext = paramActivity;
        mActivity = paramActivity;
        localRequest.onAttached(paramActivity);
        i++;
      }
    }
  }
  
  void detachActivity()
  {
    ArrayList localArrayList = makeRequestList();
    int i = 0;
    int j;
    Object localObject;
    if (localArrayList != null) {
      for (j = 0; j < localArrayList.size(); j++)
      {
        localObject = (Request)localArrayList.get(j);
        ((Request)localObject).onDetached();
        mActivity = null;
        mContext = null;
      }
    }
    if (!mRetaining)
    {
      localObject = makeRequestList();
      if (localObject != null) {
        for (j = i; j < ((ArrayList)localObject).size(); j++) {
          ((Request)((ArrayList)localObject).get(j)).cancel();
        }
      }
      mActiveRequests.clear();
    }
    mContext = null;
    mActivity = null;
  }
  
  void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("    ");
    String str = ((StringBuilder)localObject).toString();
    if (mActiveRequests.size() > 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Active voice requests:");
      for (int i = 0; i < mActiveRequests.size(); i++)
      {
        localObject = (Request)mActiveRequests.valueAt(i);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  #");
        paramPrintWriter.print(i);
        paramPrintWriter.print(": ");
        paramPrintWriter.println(localObject);
        ((Request)localObject).dump(str, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
    }
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("VoiceInteractor misc state:");
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("  mInteractor=");
    paramPrintWriter.println(mInteractor.asBinder());
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("  mActivity=");
    paramPrintWriter.println(mActivity);
  }
  
  public Request getActiveRequest(String paramString)
  {
    synchronized (mActiveRequests)
    {
      int i = mActiveRequests.size();
      int j = 0;
      while (j < i)
      {
        Request localRequest = (Request)mActiveRequests.valueAt(j);
        if ((paramString != localRequest.getName()) && ((paramString == null) || (!paramString.equals(localRequest.getName())))) {
          j++;
        } else {
          return localRequest;
        }
      }
      return null;
    }
  }
  
  public Request[] getActiveRequests()
  {
    synchronized (mActiveRequests)
    {
      int i = mActiveRequests.size();
      if (i <= 0)
      {
        arrayOfRequest = NO_REQUESTS;
        return arrayOfRequest;
      }
      Request[] arrayOfRequest = new Request[i];
      for (int j = 0; j < i; j++) {
        arrayOfRequest[j] = ((Request)mActiveRequests.valueAt(j));
      }
      return arrayOfRequest;
    }
  }
  
  Request pullRequest(IVoiceInteractorRequest paramIVoiceInteractorRequest, boolean paramBoolean)
  {
    synchronized (mActiveRequests)
    {
      Request localRequest = (Request)mActiveRequests.get(paramIVoiceInteractorRequest.asBinder());
      if ((localRequest != null) && (paramBoolean)) {
        mActiveRequests.remove(paramIVoiceInteractorRequest.asBinder());
      }
      return localRequest;
    }
  }
  
  void retainInstance()
  {
    mRetaining = true;
  }
  
  public boolean submitRequest(Request paramRequest)
  {
    return submitRequest(paramRequest, null);
  }
  
  public boolean submitRequest(Request paramRequest, String arg2)
  {
    try
    {
      if (mRequestInterface == null)
      {
        localObject = paramRequest.submit(mInteractor, mContext.getOpPackageName(), mCallback);
        mRequestInterface = ((IVoiceInteractorRequest)localObject);
        mContext = mContext;
        mActivity = mActivity;
        mName = ???;
        synchronized (mActiveRequests)
        {
          mActiveRequests.put(((IVoiceInteractorRequest)localObject).asBinder(), paramRequest);
          return true;
        }
      }
      ??? = new java/lang/IllegalStateException;
      Object localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("Given ");
      ((StringBuilder)localObject).append(paramRequest);
      ((StringBuilder)localObject).append(" is already active");
      ???.<init>(((StringBuilder)localObject).toString());
      throw ???;
    }
    catch (RemoteException paramRequest)
    {
      Log.w("VoiceInteractor", "Remove voice interactor service died", paramRequest);
    }
    return false;
  }
  
  public boolean[] supportsCommands(String[] paramArrayOfString)
  {
    try
    {
      paramArrayOfString = mInteractor.supportsCommands(mContext.getOpPackageName(), paramArrayOfString);
      return paramArrayOfString;
    }
    catch (RemoteException paramArrayOfString)
    {
      throw new RuntimeException("Voice interactor has died", paramArrayOfString);
    }
  }
  
  public static class AbortVoiceRequest
    extends VoiceInteractor.Request
  {
    final Bundle mExtras;
    final VoiceInteractor.Prompt mPrompt;
    
    public AbortVoiceRequest(VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
    {
      mPrompt = paramPrompt;
      mExtras = paramBundle;
    }
    
    public AbortVoiceRequest(CharSequence paramCharSequence, Bundle paramBundle)
    {
      if (paramCharSequence != null) {
        paramCharSequence = new VoiceInteractor.Prompt(paramCharSequence);
      } else {
        paramCharSequence = null;
      }
      mPrompt = paramCharSequence;
      mExtras = paramBundle;
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mPrompt=");
      paramPrintWriter.println(mPrompt);
      if (mExtras != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mExtras=");
        paramPrintWriter.println(mExtras);
      }
    }
    
    String getRequestTypeName()
    {
      return "AbortVoice";
    }
    
    public void onAbortResult(Bundle paramBundle) {}
    
    IVoiceInteractorRequest submit(IVoiceInteractor paramIVoiceInteractor, String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback)
      throws RemoteException
    {
      return paramIVoiceInteractor.startAbortVoice(paramString, paramIVoiceInteractorCallback, mPrompt, mExtras);
    }
  }
  
  public static class CommandRequest
    extends VoiceInteractor.Request
  {
    final Bundle mArgs;
    final String mCommand;
    
    public CommandRequest(String paramString, Bundle paramBundle)
    {
      mCommand = paramString;
      mArgs = paramBundle;
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mCommand=");
      paramPrintWriter.println(mCommand);
      if (mArgs != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mArgs=");
        paramPrintWriter.println(mArgs);
      }
    }
    
    String getRequestTypeName()
    {
      return "Command";
    }
    
    public void onCommandResult(boolean paramBoolean, Bundle paramBundle) {}
    
    IVoiceInteractorRequest submit(IVoiceInteractor paramIVoiceInteractor, String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback)
      throws RemoteException
    {
      return paramIVoiceInteractor.startCommand(paramString, paramIVoiceInteractorCallback, mCommand, mArgs);
    }
  }
  
  public static class CompleteVoiceRequest
    extends VoiceInteractor.Request
  {
    final Bundle mExtras;
    final VoiceInteractor.Prompt mPrompt;
    
    public CompleteVoiceRequest(VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
    {
      mPrompt = paramPrompt;
      mExtras = paramBundle;
    }
    
    public CompleteVoiceRequest(CharSequence paramCharSequence, Bundle paramBundle)
    {
      if (paramCharSequence != null) {
        paramCharSequence = new VoiceInteractor.Prompt(paramCharSequence);
      } else {
        paramCharSequence = null;
      }
      mPrompt = paramCharSequence;
      mExtras = paramBundle;
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mPrompt=");
      paramPrintWriter.println(mPrompt);
      if (mExtras != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mExtras=");
        paramPrintWriter.println(mExtras);
      }
    }
    
    String getRequestTypeName()
    {
      return "CompleteVoice";
    }
    
    public void onCompleteResult(Bundle paramBundle) {}
    
    IVoiceInteractorRequest submit(IVoiceInteractor paramIVoiceInteractor, String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback)
      throws RemoteException
    {
      return paramIVoiceInteractor.startCompleteVoice(paramString, paramIVoiceInteractorCallback, mPrompt, mExtras);
    }
  }
  
  public static class ConfirmationRequest
    extends VoiceInteractor.Request
  {
    final Bundle mExtras;
    final VoiceInteractor.Prompt mPrompt;
    
    public ConfirmationRequest(VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
    {
      mPrompt = paramPrompt;
      mExtras = paramBundle;
    }
    
    public ConfirmationRequest(CharSequence paramCharSequence, Bundle paramBundle)
    {
      if (paramCharSequence != null) {
        paramCharSequence = new VoiceInteractor.Prompt(paramCharSequence);
      } else {
        paramCharSequence = null;
      }
      mPrompt = paramCharSequence;
      mExtras = paramBundle;
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mPrompt=");
      paramPrintWriter.println(mPrompt);
      if (mExtras != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mExtras=");
        paramPrintWriter.println(mExtras);
      }
    }
    
    String getRequestTypeName()
    {
      return "Confirmation";
    }
    
    public void onConfirmationResult(boolean paramBoolean, Bundle paramBundle) {}
    
    IVoiceInteractorRequest submit(IVoiceInteractor paramIVoiceInteractor, String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback)
      throws RemoteException
    {
      return paramIVoiceInteractor.startConfirmation(paramString, paramIVoiceInteractorCallback, mPrompt, mExtras);
    }
  }
  
  public static class PickOptionRequest
    extends VoiceInteractor.Request
  {
    final Bundle mExtras;
    final Option[] mOptions;
    final VoiceInteractor.Prompt mPrompt;
    
    public PickOptionRequest(VoiceInteractor.Prompt paramPrompt, Option[] paramArrayOfOption, Bundle paramBundle)
    {
      mPrompt = paramPrompt;
      mOptions = paramArrayOfOption;
      mExtras = paramBundle;
    }
    
    public PickOptionRequest(CharSequence paramCharSequence, Option[] paramArrayOfOption, Bundle paramBundle)
    {
      if (paramCharSequence != null) {
        paramCharSequence = new VoiceInteractor.Prompt(paramCharSequence);
      } else {
        paramCharSequence = null;
      }
      mPrompt = paramCharSequence;
      mOptions = paramArrayOfOption;
      mExtras = paramBundle;
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mPrompt=");
      paramPrintWriter.println(mPrompt);
      if (mOptions != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Options:");
        for (int i = 0; i < mOptions.length; i++)
        {
          paramFileDescriptor = mOptions[i];
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(i);
          paramPrintWriter.println(":");
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("    mLabel=");
          paramPrintWriter.println(mLabel);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("    mIndex=");
          paramPrintWriter.println(mIndex);
          if ((mSynonyms != null) && (mSynonyms.size() > 0))
          {
            paramPrintWriter.print(paramString);
            paramPrintWriter.println("    Synonyms:");
            for (int j = 0; j < mSynonyms.size(); j++)
            {
              paramPrintWriter.print(paramString);
              paramPrintWriter.print("      #");
              paramPrintWriter.print(j);
              paramPrintWriter.print(": ");
              paramPrintWriter.println(mSynonyms.get(j));
            }
          }
          if (mExtras != null)
          {
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("    mExtras=");
            paramPrintWriter.println(mExtras);
          }
        }
      }
      if (mExtras != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mExtras=");
        paramPrintWriter.println(mExtras);
      }
    }
    
    String getRequestTypeName()
    {
      return "PickOption";
    }
    
    public void onPickOptionResult(boolean paramBoolean, Option[] paramArrayOfOption, Bundle paramBundle) {}
    
    IVoiceInteractorRequest submit(IVoiceInteractor paramIVoiceInteractor, String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback)
      throws RemoteException
    {
      return paramIVoiceInteractor.startPickOption(paramString, paramIVoiceInteractorCallback, mPrompt, mOptions, mExtras);
    }
    
    public static final class Option
      implements Parcelable
    {
      public static final Parcelable.Creator<Option> CREATOR = new Parcelable.Creator()
      {
        public VoiceInteractor.PickOptionRequest.Option createFromParcel(Parcel paramAnonymousParcel)
        {
          return new VoiceInteractor.PickOptionRequest.Option(paramAnonymousParcel);
        }
        
        public VoiceInteractor.PickOptionRequest.Option[] newArray(int paramAnonymousInt)
        {
          return new VoiceInteractor.PickOptionRequest.Option[paramAnonymousInt];
        }
      };
      Bundle mExtras;
      final int mIndex;
      final CharSequence mLabel;
      ArrayList<CharSequence> mSynonyms;
      
      Option(Parcel paramParcel)
      {
        mLabel = paramParcel.readCharSequence();
        mIndex = paramParcel.readInt();
        mSynonyms = paramParcel.readCharSequenceList();
        mExtras = paramParcel.readBundle();
      }
      
      public Option(CharSequence paramCharSequence)
      {
        mLabel = paramCharSequence;
        mIndex = -1;
      }
      
      public Option(CharSequence paramCharSequence, int paramInt)
      {
        mLabel = paramCharSequence;
        mIndex = paramInt;
      }
      
      public Option addSynonym(CharSequence paramCharSequence)
      {
        if (mSynonyms == null) {
          mSynonyms = new ArrayList();
        }
        mSynonyms.add(paramCharSequence);
        return this;
      }
      
      public int countSynonyms()
      {
        int i;
        if (mSynonyms != null) {
          i = mSynonyms.size();
        } else {
          i = 0;
        }
        return i;
      }
      
      public int describeContents()
      {
        return 0;
      }
      
      public Bundle getExtras()
      {
        return mExtras;
      }
      
      public int getIndex()
      {
        return mIndex;
      }
      
      public CharSequence getLabel()
      {
        return mLabel;
      }
      
      public CharSequence getSynonymAt(int paramInt)
      {
        CharSequence localCharSequence;
        if (mSynonyms != null) {
          localCharSequence = (CharSequence)mSynonyms.get(paramInt);
        } else {
          localCharSequence = null;
        }
        return localCharSequence;
      }
      
      public void setExtras(Bundle paramBundle)
      {
        mExtras = paramBundle;
      }
      
      public void writeToParcel(Parcel paramParcel, int paramInt)
      {
        paramParcel.writeCharSequence(mLabel);
        paramParcel.writeInt(mIndex);
        paramParcel.writeCharSequenceList(mSynonyms);
        paramParcel.writeBundle(mExtras);
      }
    }
  }
  
  public static class Prompt
    implements Parcelable
  {
    public static final Parcelable.Creator<Prompt> CREATOR = new Parcelable.Creator()
    {
      public VoiceInteractor.Prompt createFromParcel(Parcel paramAnonymousParcel)
      {
        return new VoiceInteractor.Prompt(paramAnonymousParcel);
      }
      
      public VoiceInteractor.Prompt[] newArray(int paramAnonymousInt)
      {
        return new VoiceInteractor.Prompt[paramAnonymousInt];
      }
    };
    private final CharSequence mVisualPrompt;
    private final CharSequence[] mVoicePrompts;
    
    Prompt(Parcel paramParcel)
    {
      mVoicePrompts = paramParcel.readCharSequenceArray();
      mVisualPrompt = paramParcel.readCharSequence();
    }
    
    public Prompt(CharSequence paramCharSequence)
    {
      mVoicePrompts = new CharSequence[] { paramCharSequence };
      mVisualPrompt = paramCharSequence;
    }
    
    public Prompt(CharSequence[] paramArrayOfCharSequence, CharSequence paramCharSequence)
    {
      if (paramArrayOfCharSequence != null)
      {
        if (paramArrayOfCharSequence.length != 0)
        {
          if (paramCharSequence != null)
          {
            mVoicePrompts = paramArrayOfCharSequence;
            mVisualPrompt = paramCharSequence;
            return;
          }
          throw new NullPointerException("visualPrompt must not be null");
        }
        throw new IllegalArgumentException("voicePrompts must not be empty");
      }
      throw new NullPointerException("voicePrompts must not be null");
    }
    
    public int countVoicePrompts()
    {
      return mVoicePrompts.length;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public CharSequence getVisualPrompt()
    {
      return mVisualPrompt;
    }
    
    public CharSequence getVoicePromptAt(int paramInt)
    {
      return mVoicePrompts[paramInt];
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      DebugUtils.buildShortClassTag(this, localStringBuilder);
      CharSequence localCharSequence = mVisualPrompt;
      int i = 0;
      if ((localCharSequence != null) && (mVoicePrompts != null) && (mVoicePrompts.length == 1) && (mVisualPrompt.equals(mVoicePrompts[0])))
      {
        localStringBuilder.append(" ");
        localStringBuilder.append(mVisualPrompt);
      }
      else
      {
        if (mVisualPrompt != null)
        {
          localStringBuilder.append(" visual=");
          localStringBuilder.append(mVisualPrompt);
        }
        if (mVoicePrompts != null)
        {
          localStringBuilder.append(", voice=");
          while (i < mVoicePrompts.length)
          {
            if (i > 0) {
              localStringBuilder.append(" | ");
            }
            localStringBuilder.append(mVoicePrompts[i]);
            i++;
          }
        }
      }
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeCharSequenceArray(mVoicePrompts);
      paramParcel.writeCharSequence(mVisualPrompt);
    }
  }
  
  public static abstract class Request
  {
    Activity mActivity;
    Context mContext;
    String mName;
    IVoiceInteractorRequest mRequestInterface;
    
    Request() {}
    
    public void cancel()
    {
      if (mRequestInterface != null)
      {
        try
        {
          mRequestInterface.cancel();
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("VoiceInteractor", "Voice interactor has died", localRemoteException);
        }
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Request ");
      localStringBuilder.append(this);
      localStringBuilder.append(" is no longer active");
      throw new IllegalStateException(localStringBuilder.toString());
    }
    
    void clear()
    {
      mRequestInterface = null;
      mContext = null;
      mActivity = null;
      mName = null;
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mRequestInterface=");
      paramPrintWriter.println(mRequestInterface.asBinder());
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mActivity=");
      paramPrintWriter.println(mActivity);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mName=");
      paramPrintWriter.println(mName);
    }
    
    public Activity getActivity()
    {
      return mActivity;
    }
    
    public Context getContext()
    {
      return mContext;
    }
    
    public String getName()
    {
      return mName;
    }
    
    String getRequestTypeName()
    {
      return "Request";
    }
    
    public void onAttached(Activity paramActivity) {}
    
    public void onCancel() {}
    
    public void onDetached() {}
    
    abstract IVoiceInteractorRequest submit(IVoiceInteractor paramIVoiceInteractor, String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback)
      throws RemoteException;
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      DebugUtils.buildShortClassTag(this, localStringBuilder);
      localStringBuilder.append(" ");
      localStringBuilder.append(getRequestTypeName());
      localStringBuilder.append(" name=");
      localStringBuilder.append(mName);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
}
