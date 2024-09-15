

/**
 * Helper JavaBean for JSPs, because JSTL 1.1/EL 2.0 is too dumb to
 * to what I need (call methods with parameters), or I am too dumb to use it correctly. :)
 * @author C&eacute;drik LIME
 */
public class JspHelper {



    /**
     * Try to get user locale from the session, if possible.
     * IMPLEMENTATION NOTE: this method has explicit support for Tapestry 3 and Struts 1.x
     * @param in_session
     * @return String
     */

    private static String localeToString(Locale locale) {
        if (locale != null) {
            return locale.toString();//locale.getDisplayName();
        } else {
            return "";
        }
    }

    
}
