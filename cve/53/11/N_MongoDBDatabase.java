

public class MongoDBDatabase extends Database<MongoDBConnection> {


    @Override
    public Pair<String, Object> getDeleteStatement(Table table, boolean version, String filter) {
        String deleteStatement =  "db.getSiblingDB('" + table.getSchema().getName() + "')." + table.getName() + ".deleteMany({ 'success': " + getBooleanFalse() + ", " +
                (version ?
                        "'version': ? " :
                        "'desciption': ? ") +
                "})";

        return Pair.of(deleteStatement, filter);
    }


}