package android.net;

public class Credentials
{
  private final int gid;
  private final int pid;
  private final int uid;
  
  public Credentials(int paramInt1, int paramInt2, int paramInt3)
  {
    pid = paramInt1;
    uid = paramInt2;
    gid = paramInt3;
  }
  
  public int getGid()
  {
    return gid;
  }
  
  public int getPid()
  {
    return pid;
  }
  
  public int getUid()
  {
    return uid;
  }
}
