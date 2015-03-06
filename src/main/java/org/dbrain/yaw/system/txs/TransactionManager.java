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

package org.dbrain.yaw.system.txs;

import com.google.inject.Provider;
import org.dbrain.yaw.txs.Transaction;
import org.dbrain.yaw.txs.TransactionControl;
import org.dbrain.yaw.txs.exceptions.NoTransactionException;
import org.dbrain.yaw.txs.exceptions.TransactionAlreadyStartedException;
import org.glassfish.hk2.api.IterableProvider;

import javax.inject.Inject;

/**
 * Implementation of the transaction control interface as well as provide a registry for the transaction
 * scope.
 */
public class TransactionManager implements TransactionControl {

    private final IterableProvider<TransactionMember.Wrapper> memberFactories;
    private final ThreadLocal<TransactionScope> transaction = new ThreadLocal<>();


    @Inject
    public TransactionManager( IterableProvider<TransactionMember.Wrapper> memberFactory ) {
        this.memberFactories = memberFactory;
    }

    /**
     * Get or provision an instance of the service identified by the instance of Key.
     */
    public <T> T get( Object key, Provider<T> unscopedProvider ) {
        TransactionScope tx = transaction.get();
        if ( tx == null ) {
            throw new NoTransactionException();
        }
        return tx.get( key, unscopedProvider );
    }

    public boolean contains( Object key ) {
        TransactionScope tx = transaction.get();
        if ( tx == null ) {
            return false;
        }
        return tx.contains( key );
    }


    @Override
    public Transaction current() {
        return transaction.get();
    }

    @Override
    public Transaction start() {
        TransactionScope tx = transaction.get();
        if ( tx == null ) {
            tx = new TransactionScope( memberFactories, () -> transaction.set( null ) );
            transaction.set( tx );
        } else {
            throw new TransactionAlreadyStartedException();
        }
        return tx;
    }


}