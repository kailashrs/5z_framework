package android.sax;

class Children
{
  Child[] children = new Child[16];
  
  Children() {}
  
  Element get(String paramString1, String paramString2)
  {
    int i = paramString1.hashCode() * 31 + paramString2.hashCode();
    Child localChild1 = children[(i & 0xF)];
    Child localChild2 = localChild1;
    if (localChild1 == null) {
      return null;
    }
    do
    {
      if ((hash == i) && (uri.compareTo(paramString1) == 0) && (localName.compareTo(paramString2) == 0)) {
        return localChild2;
      }
      localChild1 = next;
      localChild2 = localChild1;
    } while (localChild1 != null);
    return null;
  }
  
  Element getOrCreate(Element paramElement, String paramString1, String paramString2)
  {
    int i = paramString1.hashCode() * 31 + paramString2.hashCode();
    int j = i & 0xF;
    Object localObject1 = children[j];
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      paramElement = new Child(paramElement, paramString1, paramString2, depth + 1, i);
      children[j] = paramElement;
      return paramElement;
    }
    Child localChild;
    do
    {
      localObject1 = localObject2;
      if ((hash == i) && (uri.compareTo(paramString1) == 0) && (localName.compareTo(paramString2) == 0)) {
        return localObject1;
      }
      localChild = next;
      localObject2 = localChild;
    } while (localChild != null);
    paramElement = new Child(paramElement, paramString1, paramString2, depth + 1, i);
    next = paramElement;
    return paramElement;
  }
  
  static class Child
    extends Element
  {
    final int hash;
    Child next;
    
    Child(Element paramElement, String paramString1, String paramString2, int paramInt1, int paramInt2)
    {
      super(paramString1, paramString2, paramInt1);
      hash = paramInt2;
    }
  }
}
