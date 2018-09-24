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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

this.documentWebsocket;
this.wsUri = "ws://" + document.location.host + document.location.pathname + "documentSocket";

/**
 * Creates a new websocket-connection to the given uri and sets all the callback-method.
 */
function connect() {
    console.log(wsUri);
    this.documentWebsocket = new WebSocket(wsUri);

    documentWebsocket.onerror = function (event) {
        onError(event);
    };

    documentWebsocket.onmessage = function (event) {
        onMessage(event);
    };

    documentWebsocket.onopen = function (event) {
        onOpen(event);
    };

    documentWebsocket.onclose = function (event) {
        onClose(event);
    };
}

/**
 * This method is called, if an error occur.
 * @param event
 */
function onError(event) {
    console.log(event.data);
    writeToScreen('<span style="color: red;">Error:</span>' + event.data);
}

/**
 * This method is called, if a message from server is received.
 * @param event The message from the server.
 */
function onMessage(event) {
    var json = JSON.parse(event.data);
    incomeMessageHandler(json);
}

/**
 * This method is called, if the connection is etablished.
 * @param event
 */
function onOpen(event) {
    console.log(event.data);
    writeToScreen('<span style="color: green;">Message:</span>' + event.data);
}

/**
 * This method is called, if the client or the server disconnect.
 *
 */
function onClose() {
    console.log('Connection closed');
    writeToScreen('<span style="color: red;">Message:</span>' + 'Connection closed');
}

/**
 * This method will be deletet soon. Only for development.
 * @param string
 */
function writeToScreen(string) {
    var p = document.createElement('p');
    p.innerHTML = string;

    document.getElementById('output').appendChild(p);
}

/**
 * This method writes the received data as an object element with the given mime-type to the screen.
 * @param data The received data with mime-type and content as base64-string.
 */
function writeContent(data) {
    var obj = document.createElement('object');
    var base64Data = 'data:' + data.mimeType + ';base64,' + data.content;

    obj.setAttribute('type', data.mimeType);
    obj.setAttribute('data', base64Data)

    //only for development
    obj.setAttribute('width', '100%');
    //only for development
    obj.id = data.mimeType;

    document.getElementById('output').appendChild(obj);
}

/**
 * This method writes the received data as an image element, if the data is an jpeg.
 * @param data The received data with the jped-data as base64-string.
 */
function writeImage(data) {
    var image = new Image();
    image.src = 'data:' + data.mimeType + ';base64,' + data.content;

    //only for development
    image.width = '250';

    document.getElementById('output').appendChild(image);
}

/**
 * Sends a message to the server.
 */
function sendMessage() {
    this.documentWebsocket.send(input.value);
}

/**
 * Save disconnect the websocket.
 */
function disconnect() {
    this.documentWebsocket.close();
}

/**
 * Helper method for outgoing messages
 * @param json The json-data that will be send.
 */
function outgoingMessageHandler(json) {

}

/**
 * Helper method for incoming messages.
 * @param json The json-data, that will be received.
 */
function incomeMessageHandler(json) {
    switch (json.action.toLowerCase()) {
        case 'getfolder':
            writeFolder(json.data);
            break;
        case 'getdocument':
            writeDocument(json.data);
            break;
        default:
    }
}

/**
 *
 * @param data
 */
function writeFolder(data) {
    writeToScreen(data.content);
}

/**
 * Helper method to write the correct content to the screen.
 * @param data
 */
function writeDocument(data) {
    switch (data.mimeType) {
        case 'image/jpeg':
            writeImage(data);
            break;
        default:
            writeContent(data);
    }
}