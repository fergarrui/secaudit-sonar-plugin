/*
 * Copyright (c) 2016, Fernando Garcia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fg.sonar.plugins.secaudit.rules;

import fg.sonar.plugins.secaudit.rules.checks.FilePathTraversalCheck;
import fg.sonar.plugins.secaudit.rules.checks.HttpResponseRedirectCheck;
import fg.sonar.plugins.secaudit.rules.checks.HttpServletRequestCheck;
import fg.sonar.plugins.secaudit.rules.checks.InsecureCookieCheck;
import fg.sonar.plugins.secaudit.rules.checks.JaxRSRequestCheck;
import fg.sonar.plugins.secaudit.rules.checks.JaxWSRequestCheck;
import fg.sonar.plugins.secaudit.rules.checks.MessageDigestCustomCheck;
import fg.sonar.plugins.secaudit.rules.checks.OScommandCheck;
import fg.sonar.plugins.secaudit.rules.checks.RequestHeaderCheck;
import fg.sonar.plugins.secaudit.rules.checks.RequestReceivedStrutsCheck;
import fg.sonar.plugins.secaudit.rules.checks.SpringControllerCheck;
import fg.sonar.plugins.secaudit.rules.checks.TrustManagerImplementationCheck;
import fg.sonar.plugins.secaudit.rules.checks.UnsafeReflectionCheck;
import fg.sonar.plugins.secaudit.rules.checks.XSSCheck;
import java.util.Arrays;
import java.util.Collection;
import org.sonar.plugins.java.api.JavaCheck;

public class SecAuditRules {

  private static final Collection<Class<? extends JavaCheck>> checks =
      Arrays.asList(OScommandCheck.class, RequestHeaderCheck.class, MessageDigestCustomCheck.class,
              RequestReceivedStrutsCheck.class, JaxRSRequestCheck.class, JaxWSRequestCheck.class,
              HttpServletRequestCheck.class, SpringControllerCheck.class, InsecureCookieCheck.class,
              HttpResponseRedirectCheck.class, FilePathTraversalCheck.class, UnsafeReflectionCheck.class,
              TrustManagerImplementationCheck.class, XSSCheck.class);

  private SecAuditRules() {
    throw new IllegalAccessError("Do not instantiate this class.");
  }

  public static Class<? extends JavaCheck>[] getChecks() {
    return checks.toArray(new Class[checks.size()]);
  }
}
