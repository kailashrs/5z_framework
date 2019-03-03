package android.media;

abstract interface TtmlNodeListener
{
  public abstract void onRootNodeParsed(TtmlNode paramTtmlNode);
  
  public abstract void onTtmlNodeParsed(TtmlNode paramTtmlNode);
}
