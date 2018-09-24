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

package de.iisys.duramentum.document.client.rest;

import de.iisys.duramentum.document.config.Configuration;
import de.iisys.duramentum.document.handler.folderHandler.FolderHandler;
import de.iisys.duramentum.document.handler.HandlerFactory;
import de.iisys.duramentum.document.model.DocumentFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static de.iisys.duramentum.document.handler.folderHandler.FolderHandlerType.CMIS;
import static de.iisys.duramentum.document.handler.folderHandler.FolderHandlerType.SMB;

/**
 * @author Alexander Schmid <alexander.schmid@hof-university.de>
 *
 *     This class describes the rest-api for folders. The root-path for folder is '/folder'.
 */
@Named
@Path("/folder")
@RequestScoped
public class FolderController {
    private static final Logger logger = LoggerFactory.getLogger(FolderController.class);
    @Inject
    private HandlerFactory hf;

    private FolderHandler folderHandler;

    @PostConstruct
    public void init() {
        if(Configuration.getUseSMB())
            folderHandler = hf.folderHandlerForType(SMB);
        else
            folderHandler = hf.folderHandlerForType(CMIS);
    }


    /**
     * Load the complete foldertree from the folder as root with the given id.
     * @param id The id for the folder as root of the foldertree.
     * @return Return the foldertree as {@link DocumentFolder}.
     */
    @GET
    @Path("/{id:.+}")
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentFolder getFolderTree(@PathParam("id") String id) {
        System.out.println("/folder");
        logger.info("Get request on /folder/" + id + ". Start loading foldertree.");
        DocumentFolder folder = folderHandler.getFolderTree(id);
        logger.info("Loading done. Returning foldertree.");
        return folder;
    }

    /**
     * Load the complete foldertree from the root folder of the dms.
     * @return Return the foldertree as {@link DocumentFolder}.
     */
    @GET
    @Path("/root")
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentFolder getRootTree() {
        logger.info("Get request on /folder/root. Start loading root foldertree.");
        DocumentFolder rootFolder = folderHandler.getRootTree();
        logger.info("Loading done. Return root foldertree.");

        return rootFolder;
    }
}
