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

/**
 * Project: duramentum-document
 * Package: de.iisys.duramentum.document.handler.strategy
 * Created: 21.09.17 - 12:17
 *
 * @author Thomas Winkler <thomas.winkler@iisys.de>
 */
package de.iisys.duramentum.document.handler.strategy;

import de.iisys.duramentum.document.config.Configuration;
import de.iisys.duramentum.document.handler.folderHandler.FolderHandlerForCMIS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class SearchWithMachineAndOrder implements SearchPattern {
    private static final Logger logger = LoggerFactory.getLogger(FolderHandlerForCMIS.class);

    private String regexFolder;

    public SearchWithMachineAndOrder(String machine, String tool) {

       // regexFolder = ".*\\/" + machine + "\\/(.*\\/){0,1}" + tool + ".*"; // Folders
        //regexFolder = ".*\\/" + machine + "\\s*" + tool + ".*"; //Combined
        //regexFolder = ".*" + machine + "\\s*" + tool + "\\..*"; //Files

        regexFolder = Configuration.getSearchPattern()[1]+machine+Configuration.getSearchPattern()[2]+Configuration.getSearchPattern()[3]+tool+Configuration.getSearchPattern()[4];
        logger.debug("REGEX: " + regexFolder);
    }
/*1 und 2*/
    @Override
    public Boolean isFileRequired(String name) {

        switch (Configuration.getSearchStrategy()) {
            case FOLDER: return false;
            case FOLDER_AND_FILENAME: return false;
            case FILENAME:
                if (name.matches(regexFolder)) {
                    logger.debug("FILENAME MATCH: " + name);
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public Boolean isFolderRequired(String name) {
        switch (Configuration.getSearchStrategy()) {
            case FOLDER:
                if (name.matches(regexFolder))
                    return true;
                break;

            case FOLDER_AND_FILENAME:
                if (name.matches(regexFolder))
                    return true;
                break;

            case FILENAME:
                return true;
        }
        return false;
    }
}
