/*
 * Copyright 2018 Thomas Winkler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iisys.duramentum.document.extern;


import de.iisys.duramentum.document.config.Configuration;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Schmid on 25.11.16.
 */
@Named
@RequestScoped
public class DocumentConnector {

    private static final Logger logger = LoggerFactory.getLogger(DocumentConnector.class);

    private Session session;


    /**
     * Create a session with the cmis-dms-server with the given {@link Configuration}.
     */
    private void createSession(){
        logger.debug("Start to connect to cmis-server.");
        logger.debug("Loading configuration.");
        SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<>();

        parameter.put(SessionParameter.BINDING_TYPE, BindingType.BROWSER.value());
        //from config
        parameter.put(SessionParameter.USER, Configuration.getUsername());
        parameter.put(SessionParameter.PASSWORD, Configuration.getPassword());
        parameter.put(SessionParameter.BROWSER_URL, Configuration.getCmisurl());

        parameter.put(SessionParameter.COOKIES, "true");
        //parameter.put(SessionParameter.AUTH_SOAP_USERNAMETOKEN, "true");
        //parameter.put(SessionParameter.AUTH_HTTP_BASIC, "true");
        //parameter.put(SessionParameter.AUTH_OAUTH_BEARER, "false");
        parameter.put(SessionParameter.COMPRESSION, "true");
        parameter.put(SessionParameter.CLIENT_COMPRESSION, "false");
        parameter.put(SessionParameter.REPOSITORY_ID, "default");

        //create session
        session = factory.createSession(parameter);
        logger.debug("End of connecting process.");
    }

    /**
     * The session object is a singleton.
     * @return Return the singleton session. If session is null, a new one is created.
     */
    public Session getSession() {
        logger.debug("Get singleton session.");
        if(session == null) {
            createSession();
        }
        return session;
    }
}
