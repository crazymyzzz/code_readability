/*
 * Copyright (C) Red Gate Software Ltd 2010-2024
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.database.mongodb;

import org.flywaydb.core.internal.database.base.Connection;
import org.flywaydb.core.internal.database.base.Schema;

import java.sql.SQLException;

import static org.flywaydb.core.internal.logging.PreviewFeatureWarning.logPreviewFeature;

public class MongoDBConnection extends Connection<MongoDBDatabase> {
    protected MongoDBConnection(MongoDBDatabase database, java.sql.Connection connection) {
        super(database, connection);
        this.jdbcTemplate = new MongoDBJdbcTemplate(connection, database.getDatabaseType());
        logPreviewFeature("MongoDB support");
    }

    @Override
    protected String getCurrentSchemaNameOrSearchPath() throws SQLException {
        return jdbcTemplate.queryForString("db.getName()");
    }

    @Override
    public void doChangeCurrentSchemaOrSearchPathTo(String schema) throws SQLException {

    }

    @Override
    public Schema getSchema(String name) {
        return new MongoDBSchema(jdbcTemplate, database, name);
    }
}