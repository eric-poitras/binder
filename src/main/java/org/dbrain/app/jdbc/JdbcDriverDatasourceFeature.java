/*
 * Copyright [2015] [Eric Poitras]
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.dbrain.app.jdbc;

import org.dbrain.app.conf.Configuration;
import org.dbrain.app.lifecycle.TransactionScoped;
import org.dbrain.app.system.app.QualifiedFeature;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;


/**
 * Created by epoitras on 3/5/15.
 */
public class JdbcDriverDatasourceFeature extends QualifiedFeature<JdbcDriverDatasourceFeature> {

    private final Configuration config;

    private Provider<Connection> connectionProvider;

    @Inject
    public JdbcDriverDatasourceFeature( Configuration config ) {
        this.config = config;
    }

    @Override
    protected JdbcDriverDatasourceFeature self() {
        return this;
    }

    public JdbcDriverDatasourceFeature withProvider( Provider<Connection> connectionProvider ) {
        this.connectionProvider = connectionProvider;
        return this;
    }

    @Override
    public void complete() {

        config.bind( Connection.class ) //
                .to( Connection.class ) //
                .providedBy( connectionProvider::get ) //
                .qualifiedBy( getQualifiers() ) //
                .in( TransactionScoped.class ) //
                .complete();

    }

}