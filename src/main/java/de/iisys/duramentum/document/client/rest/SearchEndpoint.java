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
import de.iisys.duramentum.document.handler.HandlerFactory;
import de.iisys.duramentum.document.handler.folderHandler.FolderHandler;
import de.iisys.duramentum.document.model.DocumentFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static de.iisys.duramentum.document.handler.folderHandler.FolderHandlerType.CMIS;
import static de.iisys.duramentum.document.handler.folderHandler.FolderHandlerType.SMB;

@Named
@Path("/search")
@RequestScoped
public class SearchEndpoint {
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



    @GET
    @Path("{machineName}/{toolName}")
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentFolder getMAchine(@PathParam("machineName") String machine, @PathParam("toolName") String tool) {
        logger.info("Get request on /machine/tool. Start loading specific foldertree.");
        DocumentFolder rootFolder = folderHandler.getFolderByMachineAndTool(machine, tool);
        logger.info("Loading done. Return specific foldertree.");
        return rootFolder;
    }


    @GET
    @Path("{toolName}")
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentFolder getToolDocumentsWithPath(@PathParam("toolName") String toolName, @QueryParam("path") String path) {

        if(path == null) {
            logger.info("Get request on tool. Start loading specific foldertree.");
            DocumentFolder rootFolder = folderHandler.getFolderByTool(toolName);
            logger.info("Loading done. Return specific foldertree.");
            return rootFolder;
        } else {
            logger.info("Searching in path");
            logger.info(path);

            DocumentFolder rootFolder = folderHandler.getFolderByToolInPath(toolName, path);
            logger.info("Loading done.Return found documents.");
            return rootFolder;
        }

    }
}
