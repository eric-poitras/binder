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

package org.dbrain.yaw.system.scope;

import org.dbrain.yaw.app.Configuration;
import org.dbrain.yaw.app.Feature;
import org.dbrain.yaw.scope.RequestScoped;
import org.dbrain.yaw.scope.SessionScoped;
import org.glassfish.hk2.api.Context;
import org.glassfish.hk2.api.TypeLiteral;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by epoitras on 3/11/15.
 */
public class StandardScopeFeature implements Feature {

    private final Configuration config;

    @Inject
    public StandardScopeFeature( Configuration config ) {
        this.config = config;
    }

    @Override
    public void complete() {

        // Define the request scope
        config.defineService( RequestScopeContext.class ) //
                .servicing( new TypeLiteral<Context<RequestScoped>>() {}.getType() ) //
                .servicing( RequestScopeContext.class ) //
                .in( Singleton.class ) //
                .complete();

        // Define the session scope
        config.defineService( SessionScopeContext.class ) //
                .servicing( new TypeLiteral<Context<SessionScoped>>() {}.getType() ) //
                .servicing( SessionScopeContext.class ) //
                .in( Singleton.class ) //
                .complete();

    }
}
