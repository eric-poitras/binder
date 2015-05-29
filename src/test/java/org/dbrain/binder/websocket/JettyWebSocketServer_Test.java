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

package org.dbrain.binder.websocket;

import org.dbrain.binder.App;
import org.dbrain.binder.http.JettyServerComponent;
import org.dbrain.binder.http.artifacts.resources.GuidService;
import org.dbrain.binder.http.server.ServletContextBuilder;
import org.dbrain.binder.http.server.defs.WebSocketDef;
import org.dbrain.binder.lifecycle.RequestScoped;
import org.dbrain.binder.lifecycle.SessionScoped;
import org.dbrain.binder.websocket.artifacts.WsPingClient;
import org.dbrain.binder.websocket.artifacts.WsPingServer;
import org.eclipse.jetty.server.Server;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

public class JettyWebSocketServer_Test {

    private App buildApp() throws Exception {
        App app = App.create();

        app.configure( ( binder ) -> {

            ServletContextBuilder servletContext = new ServletContextBuilder( "/" );
            servletContext.serve( WebSocketDef.of( WsPingServer.class ) );

            binder.bindComponent(JettyServerComponent.class) //
                    .listen( 40001 ) //
                    .serve( servletContext.build() ) //
                    .complete();

            binder.bind( GuidService.class )
                  .to( GuidService.class )
                  .in( RequestScoped.class )
                  .named( "request" )
                  .useProxy()
                  .complete();

            binder.bind( GuidService.class )
                  .to( GuidService.class )
                  .in( SessionScoped.class )
                  .named( "session" )
                  .useProxy()
                  .complete();

        } );

        return app;
    }


    @Test
    public void testWebSocket() throws Exception {

        try ( App app = buildApp() ) {

            CountDownLatch completeSignal = new CountDownLatch( 1 );

            StringBuilder sb = new StringBuilder();

            Server server = app.getInstance( Server.class );

            assertNotNull( server );
            server.start();

            WsPingClient client = new WsPingClient( URI.create( "ws://localhost:40001/ws" ) );

            client.addMessageHandler( ( s ) -> {
                sb.append( s );
                completeSignal.countDown();
            } );

            // Send message and await response
            client.sendMessage( "hello" );
            completeSignal.await( 10, TimeUnit.SECONDS );

            // close the client
            client.close();

            Assert.assertEquals( "hello", sb.toString() );
        }

    }

    @Test
    public void testRequestScope() throws Exception {

        try ( App app = buildApp() ) {

            CountDownLatch completeSignal = new CountDownLatch( 2 );

            List<String> sb = new ArrayList<>();

            Server server = app.getInstance( Server.class );

            assertNotNull( server );
            server.start();

            WsPingClient client = new WsPingClient( URI.create( "ws://localhost:40001/ws" ) );

            client.addMessageHandler( ( s ) -> {
                sb.add( s );
                completeSignal.countDown();
            } );

            // Send message and await response
            client.sendMessage( "request" );
            client.sendMessage( "request" );
            completeSignal.await( 10, TimeUnit.SECONDS );

            // close the client
            client.close();

            Assert.assertEquals( 2, sb.size() );
            Assert.assertNotEquals( sb.get( 0 ), sb.get( 1 ) );
        }

    }

    @Test
    public void testSessionScope() throws Exception {

        try ( App app = buildApp() ) {

            CountDownLatch completeSignal = new CountDownLatch( 2 );

            List<String> sb = new ArrayList<>();

            Server server = app.getInstance( Server.class );

            assertNotNull( server );
            server.start();

            WsPingClient client = new WsPingClient( URI.create( "ws://localhost:40001/ws" ) );

            client.addMessageHandler( ( s ) -> {
                sb.add( s );
                completeSignal.countDown();
            } );

            // Send message and await response
            client.sendMessage( "session" );
            client.sendMessage( "session" );
            completeSignal.await( 10, TimeUnit.SECONDS );

            // close the client
            client.close();

            Assert.assertEquals( 2, sb.size() );
            Assert.assertEquals( sb.get( 0 ), sb.get( 1 ) );
        }

    }

}