package android.nfc;

public final class NfcEvent
{
  public final NfcAdapter nfcAdapter;
  public final int peerLlcpMajorVersion;
  public final int peerLlcpMinorVersion;
  
  NfcEvent(NfcAdapter paramNfcAdapter, byte paramByte)
  {
    nfcAdapter = paramNfcAdapter;
    peerLlcpMajorVersion = ((paramByte & 0xF0) >> 4);
    peerLlcpMinorVersion = (paramByte & 0xF);
  }
}
