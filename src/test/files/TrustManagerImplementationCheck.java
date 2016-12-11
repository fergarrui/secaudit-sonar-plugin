import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

class A implements X509TrustManager { // Noncompliant {{TrustManager implementation. A wrong implementation can lead to MITM attacks.}} [[sc=7;ec=8]]

  @Override
  public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
  }

  @Override
  public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
  }

  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return null;
  }
}
class B implements Comparable { // Compliant
  @Override
  public int compareTo(Object o) {
    return 0;
  }
}

class C implements HostnameVerifier { // Noncompliant {{HostnameVerifier implementation. A wrong implementation can lead to MITM attacks.}} [[sc=7;ec=8]]
  public boolean verify(final String hostname, final SSLSession session) {
    return true;
  }
}