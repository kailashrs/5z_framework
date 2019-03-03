package com.google.android.mms.pdu;

import java.util.Map;
import java.util.Vector;

public class PduBody
{
  private Map<String, PduPart> mPartMapByContentId = null;
  private Map<String, PduPart> mPartMapByContentLocation = null;
  private Map<String, PduPart> mPartMapByFileName = null;
  private Map<String, PduPart> mPartMapByName = null;
  private Vector<PduPart> mParts = null;
  
  public PduBody() {}
  
  private void putPartToMaps(PduPart paramPduPart)
  {
    Object localObject = paramPduPart.getContentId();
    if (localObject != null) {
      mPartMapByContentId.put(new String((byte[])localObject), paramPduPart);
    }
    localObject = paramPduPart.getContentLocation();
    if (localObject != null)
    {
      localObject = new String((byte[])localObject);
      mPartMapByContentLocation.put(localObject, paramPduPart);
    }
    localObject = paramPduPart.getName();
    if (localObject != null)
    {
      localObject = new String((byte[])localObject);
      mPartMapByName.put(localObject, paramPduPart);
    }
    localObject = paramPduPart.getFilename();
    if (localObject != null)
    {
      localObject = new String((byte[])localObject);
      mPartMapByFileName.put(localObject, paramPduPart);
    }
  }
  
  public void addPart(int paramInt, PduPart paramPduPart)
  {
    if (paramPduPart != null)
    {
      putPartToMaps(paramPduPart);
      mParts.add(paramInt, paramPduPart);
      return;
    }
    throw new NullPointerException();
  }
  
  public boolean addPart(PduPart paramPduPart)
  {
    if (paramPduPart != null)
    {
      putPartToMaps(paramPduPart);
      return mParts.add(paramPduPart);
    }
    throw new NullPointerException();
  }
  
  public PduPart getPart(int paramInt)
  {
    return (PduPart)mParts.get(paramInt);
  }
  
  public PduPart getPartByContentId(String paramString)
  {
    return (PduPart)mPartMapByContentId.get(paramString);
  }
  
  public PduPart getPartByContentLocation(String paramString)
  {
    return (PduPart)mPartMapByContentLocation.get(paramString);
  }
  
  public PduPart getPartByFileName(String paramString)
  {
    return (PduPart)mPartMapByFileName.get(paramString);
  }
  
  public PduPart getPartByName(String paramString)
  {
    return (PduPart)mPartMapByName.get(paramString);
  }
  
  public int getPartIndex(PduPart paramPduPart)
  {
    return mParts.indexOf(paramPduPart);
  }
  
  public int getPartsNum()
  {
    return mParts.size();
  }
  
  public void removeAll()
  {
    mParts.clear();
  }
  
  public PduPart removePart(int paramInt)
  {
    return (PduPart)mParts.remove(paramInt);
  }
}
