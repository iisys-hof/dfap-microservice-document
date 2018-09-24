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
import de.iisys.duramentum.document.handler.fileHandler.FileHandler;
import de.iisys.duramentum.document.handler.HandlerFactory;
import de.iisys.duramentum.document.model.DocumentFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.nio.file.Paths;

import static de.iisys.duramentum.document.handler.fileHandler.FileHandlerType.CMIS;
import static de.iisys.duramentum.document.handler.fileHandler.FileHandlerType.SMB;

/**
 * @author Alexander Schmid <alexander.schmid@hof-university.de>
 *
 *     This class describes the rest-api for documents. The root-path for documents is '/file'..
 */
@Named
@Path("/file")
@RequestScoped
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);


    @Inject
    private HandlerFactory hf;

    private FileHandler fileHandler;

    @PostConstruct
    public void init() {
        if(Configuration.getUseSMB())
            fileHandler = hf.fileHandlerForType(SMB);
        else
            fileHandler = hf.fileHandlerForType(CMIS);
    }

    /**
     * Load the document with the given id without content itself.
     * @param id The id of the wanted document.
     * @return Return the document without the content as {@link DocumentFile};
     */
    @GET
    @Path("/{id:.+}")
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentFile getLiteDocument(@PathParam("id") String id) {
        logger.info("Get request on /file/" + id + ". Start loading document without content.");
        DocumentFile document = fileHandler.getDocument(id);

        logger.info("Loading done. Return document.");
        return document;
    }


    /**
     * Load the content of the document with the given id.
     * @param id The id of the wanted document.
     * @return Return the content of the document as string.
     */
    @GET
    @Path("/{id:.+}/content")
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentFile getFullDocument(@PathParam("id") String id) {
        logger.info("Get request on /file/" + id + "/content. Start loading content of the document.");
        try {
            DocumentFile document = fileHandler.getDocumentContent(id);

            System.out.println(document.getContent().getClass());
            logger.info("Loading done. Return documentcontent.");
            return document;
        } catch (Exception e) {
            logger.debug("NO FILE AVAILABLE");
            return new DocumentFile();
        }
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/feedback/{id}")
    public Response createFeedback(@PathParam("id") String id, String x){

        logger.info("Create feedback Feedback/" + id + ". Start writing feedback document.");

        String fileName = id + ".xlsx";


        fileHandler.writeFile(x, "Rückmeldung", fileName);
        logger.info("Writing file done");

        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/write/{id:.+}")
    public Response writeFile(@PathParam("id") String id, String x){

        logger.info("Create file " + id + ".");
        java.nio.file.Path p = Paths.get(id);

        String path = p.getParent().toString();
        String filename = p.getFileName().toString();

        fileHandler.writeFile(x, path, filename);
        logger.info("Writing file done");

        return Response.status(Response.Status.OK).build();
    }


}
