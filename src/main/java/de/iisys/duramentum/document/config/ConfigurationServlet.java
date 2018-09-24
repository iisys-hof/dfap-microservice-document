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

package de.iisys.duramentum.document.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author Alexander Schmid <alexander.schmid@hof-university.de>
 */
@WebListener
public class ConfigurationServlet implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationServlet.class);

    /**
     *
     * @param servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("------------------------------------------------------------------");
        logger.info("------------START Microservice Duramentum-Dokument----------------");
        logger.info("------------------------------------------------------------------");
        ServletContext servletContext = servletContextEvent.getServletContext();
        Configuration.loadConfig(servletContext);
    }

    /**
     *
     * @param servletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("------------------------------------------------------------------");
        logger.info("------------STOP Microservice Duramentum-Mail---------------------");
        logger.info("------------------------------------------------------------------");
    }
}
