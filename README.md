# SecAudit SonarQube plugin

[![Build Status](https://travis-ci.org/fergarrui/secaudit-sonar-plugin.svg?branch=master)](https://travis-ci.org/fergarrui/secaudit-sonar-plugin)


A plugin to highlight dangerous/suspicious parts of the code to help security auditors. It is marking useful security audit points that may not necessarily be vulnerabilities or bugs. The aim of this plugin is to detect areas of the code where a manual or visual review should be done.

## Instalation
### Building snapshot from source
```
# Clone repository
git clone https://github.com/fergarrui/secaudit-sonar-plugin.git
# Build it
cd secaudit-sonar-plugin
mvn clean package
# Install into SonarQube
cp target/<plugin-version>.jar <sonar-qube-server>/extensions/plugins
# Start SonarQube
<sonar-qube-server-root>/bin/<os_version>/sonar.sh start
# Analyze the target project
cd <target-project-to-analyze>
mvn clean package
mvn sonar:sonar
```

### Installing from release
[...]


## Audit points
* [Spring MVC backend](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/SpringControllerCheck_desc.html)
* [Struts1 backend](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/RequestReceivedStrutsCheck_desc.html)
* [Java Servlet backend](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/HttpServletRequestCheck_desc.html)
* [JaxRS backend](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/JaxRSRequestCheck_desc.html)
* [JaxWS backend](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/JaxWSRequestCheck_desc.html)
* [File path traversal](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/FilePathTraversalCheck_desc.html)
* [File upload](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/FileUploadCheck_desc.html)
* [Http Response redirect](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/HttpResponseRedirectCheck_desc.html)
* [Insecure Cookie](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/InsecureCookieCheck_desc.html)
* [Insufficient Session expiration](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/InsufficientSessionExpirationCheck_desc.html)
* [Custom MessageDigest](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/MessageDigestCustomCheck_desc.html)
* [Object deserialization](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/DeserializationCheck_desc.html)
* [OS Command Exec](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/OSCommandCheck_desc.html)
* [Request Header use](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/RequestHeaderCheck_desc.html)
* [System.exit()](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/SystemExitCheck_desc.html)
* [TrustManager implementation](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/TrustManagerImplementationCheck_desc.html)
* [Unsafe Reflection](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/UnsafeReflectionCheck_desc.html)
* [XEE - XML External Entity](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/XEECheck_desc.html)
* [XPath injection](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/XPathInjectionCheck_desc.html)
* [XSS on Servlet](https://cdn.rawgit.com/fergarrui/secaudit-sonar-plugin/master/src/main/resources/fg.sonar.plugins.secaudit.rules.description/XSSCheck_desc.html)


