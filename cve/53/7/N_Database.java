

/**
 * Abstraction for database-specific functionality.
 */
@CustomLog
public abstract class Database<C extends Connection> implements Closeable {


    public Pair<String, Object> getDeleteStatement(Table table, boolean version, String filter) {
        String deleteStatement = "DELETE FROM " + table +
            " WHERE " + quote("success") + " = " + getBooleanFalse() + " AND " +
            (version ?
                quote("version") + " = ?" :
                quote("description") + " = ?");

        return Pair.of(deleteStatement, filter);
    }


}