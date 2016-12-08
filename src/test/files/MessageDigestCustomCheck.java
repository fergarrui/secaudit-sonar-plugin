import java.security.MessageDigest;

class A extends MessageDigest { // Noncompliant [[sc=17;ec=30]]
  public A() {super("A");}
  @Override
  protected void engineUpdate(byte input) {
  }
  @Override
  protected void engineUpdate(byte[] input, int offset, int len) {
  }
  @Override
  protected byte[] engineDigest() {
    return new byte[0];
  }
  @Override
  protected void engineReset() {
  }
}

class B { // compliant
  public B() {}
  protected void engineUpdate(byte input) {
  }
  protected void engineUpdate(byte[] input, int offset, int len) {
  }
  protected byte[] engineDigest() {
    return new byte[0];
  }
  protected void engineReset() {
  }
}
