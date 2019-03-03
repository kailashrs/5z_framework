package android.service.autofill;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.util.Log;
import android.view.autofill.AutofillValue;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.Arrays;
import java.util.List;

@SystemApi
public abstract class AutofillFieldClassificationService
  extends Service
{
  public static final String EXTRA_SCORES = "scores";
  public static final String SERVICE_INTERFACE = "android.service.autofill.AutofillFieldClassificationService";
  public static final String SERVICE_META_DATA_KEY_AVAILABLE_ALGORITHMS = "android.autofill.field_classification.available_algorithms";
  public static final String SERVICE_META_DATA_KEY_DEFAULT_ALGORITHM = "android.autofill.field_classification.default_algorithm";
  private static final String TAG = "AutofillFieldClassificationService";
  private final Handler mHandler = new Handler(Looper.getMainLooper(), null, true);
  private AutofillFieldClassificationServiceWrapper mWrapper;
  
  public AutofillFieldClassificationService() {}
  
  private void getScores(RemoteCallback paramRemoteCallback, String paramString, Bundle paramBundle, List<AutofillValue> paramList, String[] paramArrayOfString)
  {
    Bundle localBundle = new Bundle();
    paramString = onGetScores(paramString, paramBundle, paramList, Arrays.asList(paramArrayOfString));
    if (paramString != null) {
      localBundle.putParcelable("scores", new Scores(paramString, null));
    }
    paramRemoteCallback.sendResult(localBundle);
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return mWrapper;
  }
  
  public void onCreate()
  {
    super.onCreate();
    mWrapper = new AutofillFieldClassificationServiceWrapper(null);
  }
  
  @SystemApi
  public float[][] onGetScores(String paramString, Bundle paramBundle, List<AutofillValue> paramList, List<String> paramList1)
  {
    paramString = new StringBuilder();
    paramString.append("service implementation (");
    paramString.append(getClass());
    paramString.append(" does not implement onGetScore()");
    Log.e("AutofillFieldClassificationService", paramString.toString());
    return null;
  }
  
  private final class AutofillFieldClassificationServiceWrapper
    extends IAutofillFieldClassificationService.Stub
  {
    private AutofillFieldClassificationServiceWrapper() {}
    
    public void getScores(RemoteCallback paramRemoteCallback, String paramString, Bundle paramBundle, List<AutofillValue> paramList, String[] paramArrayOfString)
      throws RemoteException
    {
      mHandler.sendMessage(PooledLambda.obtainMessage(_..Lambda.AutofillFieldClassificationService.AutofillFieldClassificationServiceWrapper.LVFO8nQdiSarBMY_Qsf1G30GEZQ.INSTANCE, AutofillFieldClassificationService.this, paramRemoteCallback, paramString, paramBundle, paramList, paramArrayOfString));
    }
  }
  
  public static final class Scores
    implements Parcelable
  {
    public static final Parcelable.Creator<Scores> CREATOR = new Parcelable.Creator()
    {
      public AutofillFieldClassificationService.Scores createFromParcel(Parcel paramAnonymousParcel)
      {
        return new AutofillFieldClassificationService.Scores(paramAnonymousParcel, null);
      }
      
      public AutofillFieldClassificationService.Scores[] newArray(int paramAnonymousInt)
      {
        return new AutofillFieldClassificationService.Scores[paramAnonymousInt];
      }
    };
    public final float[][] scores;
    
    private Scores(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      int j = paramParcel.readInt();
      scores = new float[i][j];
      for (int k = 0; k < i; k++) {
        for (int m = 0; m < j; m++) {
          scores[k][m] = paramParcel.readFloat();
        }
      }
    }
    
    private Scores(float[][] paramArrayOfFloat)
    {
      scores = paramArrayOfFloat;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      int i = scores.length;
      int j = 0;
      if (i > 0) {
        k = scores[0].length;
      } else {
        k = 0;
      }
      StringBuilder localStringBuilder = new StringBuilder("Scores [");
      localStringBuilder.append(i);
      localStringBuilder.append("x");
      localStringBuilder.append(k);
      localStringBuilder = localStringBuilder.append("] ");
      for (int k = j; k < i; k++)
      {
        localStringBuilder.append(k);
        localStringBuilder.append(": ");
        localStringBuilder.append(Arrays.toString(scores[k]));
        localStringBuilder.append(' ');
      }
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      int i = scores.length;
      int j = scores[0].length;
      paramParcel.writeInt(i);
      paramParcel.writeInt(j);
      for (paramInt = 0; paramInt < i; paramInt++) {
        for (int k = 0; k < j; k++) {
          paramParcel.writeFloat(scores[paramInt][k]);
        }
      }
    }
  }
}
