package android.webkit;

public class WebMessage
{
  private String mData;
  private WebMessagePort[] mPorts;
  
  public WebMessage(String paramString)
  {
    mData = paramString;
  }
  
  public WebMessage(String paramString, WebMessagePort[] paramArrayOfWebMessagePort)
  {
    mData = paramString;
    mPorts = paramArrayOfWebMessagePort;
  }
  
  public String getData()
  {
    return mData;
  }
  
  public WebMessagePort[] getPorts()
  {
    return mPorts;
  }
}
