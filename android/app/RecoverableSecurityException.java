package android.app;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public final class RecoverableSecurityException
  extends SecurityException
  implements Parcelable
{
  public static final Parcelable.Creator<RecoverableSecurityException> CREATOR = new Parcelable.Creator()
  {
    public RecoverableSecurityException createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RecoverableSecurityException(paramAnonymousParcel);
    }
    
    public RecoverableSecurityException[] newArray(int paramAnonymousInt)
    {
      return new RecoverableSecurityException[paramAnonymousInt];
    }
  };
  private static final String TAG = "RecoverableSecurityException";
  private final RemoteAction mUserAction;
  private final CharSequence mUserMessage;
  
  public RecoverableSecurityException(Parcel paramParcel)
  {
    this(new SecurityException(paramParcel.readString()), paramParcel.readCharSequence(), (RemoteAction)RemoteAction.CREATOR.createFromParcel(paramParcel));
  }
  
  public RecoverableSecurityException(Throwable paramThrowable, CharSequence paramCharSequence, RemoteAction paramRemoteAction)
  {
    super(paramThrowable.getMessage());
    mUserMessage = ((CharSequence)Preconditions.checkNotNull(paramCharSequence));
    mUserAction = ((RemoteAction)Preconditions.checkNotNull(paramRemoteAction));
  }
  
  @Deprecated
  public RecoverableSecurityException(Throwable paramThrowable, CharSequence paramCharSequence1, CharSequence paramCharSequence2, PendingIntent paramPendingIntent)
  {
    this(paramThrowable, paramCharSequence1, new RemoteAction(Icon.createWithResource("android", 17303003), paramCharSequence2, paramCharSequence2, paramPendingIntent));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public RemoteAction getUserAction()
  {
    return mUserAction;
  }
  
  public CharSequence getUserMessage()
  {
    return mUserMessage;
  }
  
  public void showAsDialog(Activity paramActivity)
  {
    LocalDialog localLocalDialog = new LocalDialog();
    Object localObject1 = new Bundle();
    ((Bundle)localObject1).putParcelable("RecoverableSecurityException", this);
    localLocalDialog.setArguments((Bundle)localObject1);
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("RecoverableSecurityException_");
    ((StringBuilder)localObject1).append(mUserAction.getActionIntent().getCreatorUid());
    localObject1 = ((StringBuilder)localObject1).toString();
    Object localObject2 = paramActivity.getFragmentManager();
    paramActivity = ((FragmentManager)localObject2).beginTransaction();
    localObject2 = ((FragmentManager)localObject2).findFragmentByTag((String)localObject1);
    if (localObject2 != null) {
      paramActivity.remove((Fragment)localObject2);
    }
    paramActivity.add(localLocalDialog, (String)localObject1);
    paramActivity.commitAllowingStateLoss();
  }
  
  @Deprecated
  public void showAsNotification(Context paramContext)
  {
    NotificationManager localNotificationManager = (NotificationManager)paramContext.getSystemService(NotificationManager.class);
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("RecoverableSecurityException_");
    ((StringBuilder)localObject).append(mUserAction.getActionIntent().getCreatorUid());
    localObject = ((StringBuilder)localObject).toString();
    localNotificationManager.createNotificationChannel(new NotificationChannel((String)localObject, "RecoverableSecurityException", 3));
    showAsNotification(paramContext, (String)localObject);
  }
  
  public void showAsNotification(Context paramContext, String paramString)
  {
    NotificationManager localNotificationManager = (NotificationManager)paramContext.getSystemService(NotificationManager.class);
    paramContext = new Notification.Builder(paramContext, paramString).setSmallIcon(17302999).setContentTitle(mUserAction.getTitle()).setContentText(mUserMessage).setContentIntent(mUserAction.getActionIntent()).setCategory("err");
    localNotificationManager.notify("RecoverableSecurityException", mUserAction.getActionIntent().getCreatorUid(), paramContext.build());
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(getMessage());
    paramParcel.writeCharSequence(mUserMessage);
    mUserAction.writeToParcel(paramParcel, paramInt);
  }
  
  public static class LocalDialog
    extends DialogFragment
  {
    public LocalDialog() {}
    
    public Dialog onCreateDialog(Bundle paramBundle)
    {
      paramBundle = (RecoverableSecurityException)getArguments().getParcelable("RecoverableSecurityException");
      return new AlertDialog.Builder(getActivity()).setMessage(mUserMessage).setPositiveButton(mUserAction.getTitle(), new _..Lambda.RecoverableSecurityException.LocalDialog.r8YNkpjWIZllJsQ_8eA0q51FU5Q(paramBundle)).setNegativeButton(17039360, null).create();
    }
  }
}
