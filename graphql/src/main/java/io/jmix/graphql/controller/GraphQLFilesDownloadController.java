/*
 * Copyright 2021 Haulmont.
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

package io.jmix.graphql.controller;

import io.jmix.core.AccessManager;
import io.jmix.core.FileClientUtils;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.core.FileStorageLocator;
import io.jmix.core.Metadata;
import io.jmix.graphql.accesscontext.GraphQLAccessContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * GraphQL API controller that is used for downloading files
 */
@RestController("graphql_FileDownloadController")
public class GraphQLFilesDownloadController {

    private static final Logger log = LoggerFactory.getLogger(GraphQLFilesDownloadController.class);

    @Autowired
    protected FileStorageLocator fileStorageLocator;
    @Autowired
    protected Metadata metadata;
    @Autowired
    protected AccessManager accessManager;

    @GetMapping("/graphql/files")
    public void downloadFile(@RequestParam("fileRef") String fileRef,
                             @RequestParam(required = false) Boolean attachment,
                             HttpServletResponse response) {

        checkFileDownloadPermission();
        try {
            FileRef fileReference;
            fileReference = FileRef.fromString(fileRef);
            FileStorage fileStorage = FileClientUtils.getFileStorageByNameOrDefault(fileStorageLocator,
                    fileReference.getStorageName(), fileReference.getFileName());
            FileClientUtils.downloadAndWriteResponse(fileReference, fileStorage, attachment, response);
        } catch (IllegalArgumentException e) {
            throw new GraphQLControllerException("Invalid file reference",
                    String.format("Cannot convert '%s' into valid file reference", fileRef),
                    HttpStatus.BAD_REQUEST,
                    e);
        }

    }

    protected void checkFileDownloadPermission() {
        GraphQLAccessContext downloadContext =
                new GraphQLAccessContext(GraphQLAccessContext.GRAPHQL_FILE_DOWNLOAD_ENABLED);
        accessManager.applyRegisteredConstraints(downloadContext);

        if (!downloadContext.isPermitted()) {
            throw new GraphQLControllerException("File download failed", "File download is not permitted", HttpStatus.FORBIDDEN);
        }
    }
}