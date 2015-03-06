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

package org.dbrain.yaw.system.modules;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import org.dbrain.yaw.scope.TransactionScoped;
import org.dbrain.yaw.system.txs.TransactionManager;
import org.dbrain.yaw.txs.TransactionControl;

/**
 * Created by epoitras on 2/27/15.
 */
public class TransactionModule implements Module {

    private TransactionManager manager = null;//new TransactionManager();

    @Override
    public void configure( Binder binder ) {
        binder.bind( TransactionManager.class ).toInstance( manager );
        binder.bind( TransactionControl.class ).toInstance( manager );

        binder.bindScope( TransactionScoped.class, new Scope() {
            @Override
            public <T> Provider<T> scope( final Key<T> key, final Provider<T> unscoped ) {
                return () -> manager.get( key, unscoped );
            }
        } );

    }

}