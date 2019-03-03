package android.drm;

import java.util.ArrayList;
import java.util.Iterator;

public class DrmSupportInfo
{
  private String mDescription = "";
  private final ArrayList<String> mFileSuffixList = new ArrayList();
  private final ArrayList<String> mMimeTypeList = new ArrayList();
  
  public DrmSupportInfo() {}
  
  public void addFileSuffix(String paramString)
  {
    if (paramString != "")
    {
      mFileSuffixList.add(paramString);
      return;
    }
    throw new IllegalArgumentException("fileSuffix is an empty string");
  }
  
  public void addMimeType(String paramString)
  {
    if (paramString != null)
    {
      if (paramString != "")
      {
        mMimeTypeList.add(paramString);
        return;
      }
      throw new IllegalArgumentException("mimeType is an empty string");
    }
    throw new IllegalArgumentException("mimeType is null");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof DrmSupportInfo;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (DrmSupportInfo)paramObject;
      bool1 = bool2;
      if (mFileSuffixList.equals(mFileSuffixList))
      {
        bool1 = bool2;
        if (mMimeTypeList.equals(mMimeTypeList))
        {
          bool1 = bool2;
          if (mDescription.equals(mDescription)) {
            bool1 = true;
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public String getDescriprition()
  {
    return mDescription;
  }
  
  public String getDescription()
  {
    return mDescription;
  }
  
  public Iterator<String> getFileSuffixIterator()
  {
    return mFileSuffixList.iterator();
  }
  
  public Iterator<String> getMimeTypeIterator()
  {
    return mMimeTypeList.iterator();
  }
  
  public int hashCode()
  {
    return mFileSuffixList.hashCode() + mMimeTypeList.hashCode() + mDescription.hashCode();
  }
  
  boolean isSupportedFileSuffix(String paramString)
  {
    return mFileSuffixList.contains(paramString);
  }
  
  boolean isSupportedMimeType(String paramString)
  {
    if ((paramString != null) && (!paramString.equals(""))) {
      for (int i = 0; i < mMimeTypeList.size(); i++) {
        if (((String)mMimeTypeList.get(i)).startsWith(paramString)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public void setDescription(String paramString)
  {
    if (paramString != null)
    {
      if (paramString != "")
      {
        mDescription = paramString;
        return;
      }
      throw new IllegalArgumentException("description is an empty string");
    }
    throw new IllegalArgumentException("description is null");
  }
}
