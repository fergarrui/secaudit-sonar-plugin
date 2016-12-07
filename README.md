# SecAudit SonarQube plugin

[![Build Status](https://travis-ci.org/fergarrui/secaudit-sonar-plugin.svg?branch=master)](https://travis-ci.org/fergarrui/secaudit-sonar-plugin)

[Plugin under construction]

A plugin to highlight dangerous/suspicious parts of the code to help security auditors. It is marking useful security audit points that may not necessarily be vulnerabilities or bugs. the aim of this plugin is to detect areas of the code where a manual or visual review should be done.

### Instalation

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