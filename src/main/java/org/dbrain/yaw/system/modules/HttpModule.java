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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.dbrain.yaw.system.http.server.HttpContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: epoitras
 * Date: 29/06/13
 * Time: 8:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpModule extends AbstractModule {

    @Override
    protected void configure() {
    }


    @Provides
    private HttpServletRequest getHttpServletRequest() {
        return HttpContext.getCurrentRequest();
    }

    @Provides
    private HttpSession getHttpSession( HttpServletRequest request ) {
        if ( request != null ) {
            return request.getSession();
        } else {
            return null;
        }
    }
}