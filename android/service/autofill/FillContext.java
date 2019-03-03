package android.service.autofill;

import android.app.assist.AssistStructure;
import android.app.assist.AssistStructure.ViewNode;
import android.app.assist.AssistStructure.WindowNode;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.util.SparseIntArray;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import java.util.LinkedList;

public final class FillContext
  implements Parcelable
{
  public static final Parcelable.Creator<FillContext> CREATOR = new Parcelable.Creator()
  {
    public FillContext createFromParcel(Parcel paramAnonymousParcel)
    {
      return new FillContext(paramAnonymousParcel, null);
    }
    
    public FillContext[] newArray(int paramAnonymousInt)
    {
      return new FillContext[paramAnonymousInt];
    }
  };
  private final int mRequestId;
  private final AssistStructure mStructure;
  private ArrayMap<AutofillId, AssistStructure.ViewNode> mViewNodeLookupTable;
  
  public FillContext(int paramInt, AssistStructure paramAssistStructure)
  {
    mRequestId = paramInt;
    mStructure = paramAssistStructure;
  }
  
  private FillContext(Parcel paramParcel)
  {
    this(paramParcel.readInt(), (AssistStructure)paramParcel.readParcelable(null));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public AssistStructure.ViewNode[] findViewNodesByAutofillIds(AutofillId[] paramArrayOfAutofillId)
  {
    LinkedList localLinkedList = new LinkedList();
    AssistStructure.ViewNode[] arrayOfViewNode = new AssistStructure.ViewNode[paramArrayOfAutofillId.length];
    SparseIntArray localSparseIntArray = new SparseIntArray(paramArrayOfAutofillId.length);
    int i = 0;
    for (int j = 0; j < paramArrayOfAutofillId.length; j++) {
      if (mViewNodeLookupTable != null)
      {
        k = mViewNodeLookupTable.indexOfKey(paramArrayOfAutofillId[j]);
        if (k >= 0) {
          arrayOfViewNode[j] = ((AssistStructure.ViewNode)mViewNodeLookupTable.valueAt(k));
        } else {
          localSparseIntArray.put(j, 0);
        }
      }
      else
      {
        localSparseIntArray.put(j, 0);
      }
    }
    int k = mStructure.getWindowNodeCount();
    for (j = 0; j < k; j++) {
      localLinkedList.add(mStructure.getWindowNodeAt(j).getRootViewNode());
    }
    while ((localSparseIntArray.size() > 0) && (!localLinkedList.isEmpty()))
    {
      AssistStructure.ViewNode localViewNode = (AssistStructure.ViewNode)localLinkedList.removeFirst();
      for (j = 0; j < localSparseIntArray.size(); j++)
      {
        k = localSparseIntArray.keyAt(j);
        AutofillId localAutofillId = paramArrayOfAutofillId[k];
        if (localAutofillId.equals(localViewNode.getAutofillId()))
        {
          arrayOfViewNode[k] = localViewNode;
          if (mViewNodeLookupTable == null) {
            mViewNodeLookupTable = new ArrayMap(paramArrayOfAutofillId.length);
          }
          mViewNodeLookupTable.put(localAutofillId, localViewNode);
          localSparseIntArray.removeAt(j);
          break;
        }
      }
      for (j = 0; j < localViewNode.getChildCount(); j++) {
        localLinkedList.addLast(localViewNode.getChildAt(j));
      }
    }
    for (j = i; j < localSparseIntArray.size(); j++)
    {
      if (mViewNodeLookupTable == null) {
        mViewNodeLookupTable = new ArrayMap(localSparseIntArray.size());
      }
      mViewNodeLookupTable.put(paramArrayOfAutofillId[localSparseIntArray.keyAt(j)], null);
    }
    return arrayOfViewNode;
  }
  
  public int getRequestId()
  {
    return mRequestId;
  }
  
  public AssistStructure getStructure()
  {
    return mStructure;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("FillContext [reqId=");
    localStringBuilder.append(mRequestId);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRequestId);
    paramParcel.writeParcelable(mStructure, paramInt);
  }
}
