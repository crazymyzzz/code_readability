

/**
 * Abstraction for database-specific functionality.
 */
@CustomLog
public abstract class Database<C extends Connection> implements Closeable {


    public String getDeleteStatement(Table table, boolean version) {
        return "DELETE FROM " + table +
                " WHERE " + quote("success") + " = " + getBooleanFalse() + " AND " +
                (version ?
                        quote("version") + " = ?" :
                        quote("description") + " = ?");
    }


}