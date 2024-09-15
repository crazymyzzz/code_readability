

public class MongoDBDatabase extends Database<MongoDBConnection> {



    public String getDeleteStatement(Table table, boolean version) {
        return "db.getSiblingDB('" + table.getSchema().getName() + "')." + table.getName() + ".deleteMany({ 'success': " + getBooleanFalse() + ", " +
                (version ?
                        "'version': ? " :
                        "'desciption': ? ") +
                "})";
    }

}