package android.app.servertransaction;

import android.os.Parcelable;

public abstract class ClientTransactionItem
  implements BaseClientRequest, Parcelable
{
  public ClientTransactionItem() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getPostExecutionState()
  {
    return -1;
  }
}
