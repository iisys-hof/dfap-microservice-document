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
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

/**
 * @author Alexander Schmid <alexander.schmid@hof-university.de>
 * <p>
 * Read and provides the configuration, e.g. username, password, cmisurl, for the connection. The config.xml is essential for the configuration. Without this file, the microservice won't work.
 */
public class Configuration {
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    private static String CONFIG_PATH = "/WEB-INF/config.xml";
    private static String username;
    private static String password;
    private static String cmisurl;
    private static Boolean usesmb;
    private static String smbuser;
    private static String smbpassword;
    private static String smbhost;
    private static String smbSharename;
    private static String smbSystem;
    private static String smbDomain;
    private static String[] ignoredResources;
    private static SearchForMachineAndOrderStrategy searchStrategy;
    private static String[] searchPattern;


    /**
     * Load the config.xml file and arse the content. The config.xml is located in /WEB-INF/config.xml
     *
     * @param servletContext Is used for the correct filepath.
     */
    public static void loadConfig(ServletContext servletContext) {
        logger.info("Loading configfile");

        URL filepath = null;
        try {
            logger.info("Loading filepath URI");
            filepath = servletContext.getResource(CONFIG_PATH);
        } catch (MalformedURLException e) {
            //System.err.println("no config file.");
            //e.printStackTrace();
            logger.error("Error on filepath URI.");
            logger.debug("", e);
        }
        File file = null;

        try {
            logger.info("Loading configfile.");
            file = new File(filepath.toURI());
        } catch (URISyntaxException e) {
            //System.err.println("file failure");
            //e.printStackTrace();
            logger.error("Wrong fileuri. Cannot load/find configfile.");
            logger.debug("", e);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            logger.info("Parse configfile.");
            builder = factory.newDocumentBuilder();
            doc = builder.parse(file);

            doc.getDocumentElement().normalize();

            logger.info("Read document nodes.");

            username = doc.getElementsByTagName("username").item(0).getTextContent();
            password = doc.getElementsByTagName("password").item(0).getTextContent();
            cmisurl = doc.getElementsByTagName("cmis-url").item(0).getTextContent();

            usesmb = Boolean.parseBoolean(doc.getElementsByTagName("use-smb").item(0).getTextContent());
            smbuser = doc.getElementsByTagName("smb-user").item(0).getTextContent();
            smbpassword = doc.getElementsByTagName("smb-password").item(0).getTextContent();
            smbhost = doc.getElementsByTagName("smb-host").item(0).getTextContent();
            smbSharename = doc.getElementsByTagName("smb-sharename").item(0).getTextContent();
            smbDomain = doc.getElementsByTagName("smb-domain").item(0).getTextContent();
            smbSystem = doc.getElementsByTagName("smb-system").item(0).getTextContent();
            ignoredResources = doc.getElementsByTagName("ignored-resources").item(0).getTextContent().split(" ");

            searchStrategy = SearchForMachineAndOrderStrategy.valueOf(doc.getElementsByTagName("search-strategy").item(0).getTextContent());
            String searchString = doc.getElementsByTagName("search-pattern").item(0).getTextContent();
            searchPattern = searchString.split("\n");
            searchPattern[1] = searchPattern[1].replaceAll("\\s+","");
            searchPattern[2] = searchPattern[2].replaceAll("\\s+","");
            searchPattern[3] = searchPattern[3].replaceAll("\\s+","");
            searchPattern[4] = searchPattern[4].replaceAll("\\s+","");
            System.out.println(searchPattern[1]);
            System.out.println(searchPattern[2]);
            System.out.println(searchPattern[3]);
            System.out.println(searchPattern[4]);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            //e.printStackTrace();
            logger.error("Configuration-Syntax is wrong. See config.xml.default.");
            logger.error("Cannot read configfile.");
            logger.debug("", e);
        }
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Configuration.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Configuration.password = password;
    }

    public static String getCmisurl() {
        return cmisurl;
    }

    public static void setCmisurl(String cmisurl) {
        Configuration.cmisurl = cmisurl;
    }

    public static Boolean getUseSMB() {
        return usesmb;
    }

    public static String getSMBUser() {
        return smbuser;
    }

    public static String getSMBPassword() {
        return smbpassword;
    }

    public static String getSMBHost() {
        return smbhost;
    }

    public static String getSMBSharename() {
        return smbSharename;
    }

    public static String getSMBDomain() {
        return smbDomain;
    }

    public static String[] getIgnoredResources() {
        return ignoredResources;
    }

    public static void setIgnoredResources(String[] ignoredResources) {
        Configuration.ignoredResources = ignoredResources;
    }

    public static void setConfigPath(String configPath) {
        CONFIG_PATH = configPath;
    }

    public static void setUsesmb(Boolean usesmb) {
        Configuration.usesmb = usesmb;
    }

    public static void setSmbuser(String smbuser) {
        Configuration.smbuser = smbuser;
    }

    public static void setSmbpassword(String smbpassword) {
        Configuration.smbpassword = smbpassword;
    }

    public static void setSmbhost(String smbhost) {
        Configuration.smbhost = smbhost;
    }

    public static void setSmbSharename(String smbSharename) {
        Configuration.smbSharename = smbSharename;
    }

    public static String getSMBSystem() {
        return smbSystem;
    }

    public static void setSmbDomain(String smbDomain) {
        Configuration.smbDomain = smbDomain;
    }

    public static SearchForMachineAndOrderStrategy getSearchStrategy() {
        return searchStrategy;
    }

    public static String[] getSearchPattern() {
        return searchPattern;
    }

    public enum SearchForMachineAndOrderStrategy {FOLDER, FOLDER_AND_FILENAME, FILENAME}
}
