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

package org.dbrain.yaw.system.http.webapp;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.servlet.ServletProperties;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This class configure the parameters for linking Jersey's servlet with the application's
 * service locator.
 */
public class WebAppConfigServletContextListener implements ServletContextListener {


    private final ServiceLocator serviceLocator;

    @Inject
    public WebAppConfigServletContextListener( ServiceLocator serviceLocator ) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public void contextInitialized( ServletContextEvent sce ) {
        sce.getServletContext().setAttribute( ServletProperties.SERVICE_LOCATOR, serviceLocator );
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce ) {
    }

}
