@@ -13,14 +13,15 @@
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
-package org.flywaydb.telemetry.otel.exceptions;
+package org.flywaydb.core.api.output;
 
-import org.flywaydb.core.api.FlywayException;
+import lombok.Getter;
+import lombok.Setter;
 
-public class FlywaySanitizedException extends FlywayException {
+import java.util.List;
 
-    public FlywaySanitizedException(Throwable cause) {
-        super(cause.getClass().getName());
-        this.setStackTrace(cause.getStackTrace());
-    }
+@Getter
+@Setter
+public class DashboardResult extends HtmlResult {
+    private List<HtmlResult> results;
 }
\ No newline at end of file
