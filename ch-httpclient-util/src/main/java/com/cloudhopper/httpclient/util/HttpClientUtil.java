package com.cloudhopper.httpclient.util;

/*
 * #%L
 * ch-httpclient-util
 * %%
 * Copyright (C) 2012 - 2013 Cloudhopper by Twitter
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.util.Arrays;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Utility class for processing an HttpResponse.
 * 
 * @author joelauer
 */
public class HttpClientUtil {

    /**
     * Quitely shuts down an HttpClient instance by shutting down its connection
     * manager and ignoring any errors that occur.
     * @param http The HttpClient to shutdown
     */
    static public void shutdownQuietly(HttpClient http) {
        if (http != null) {
            try {
                http.getConnectionManager().shutdown();
            } catch (Exception ignore) {
                // do nothing
            }
        }
    }

    /**
     * Useful method for verifying the status code of the HttpResponse.  If
     * the response status code does not match what we expect, this will throw
     * an exception.  This method checks for everything including a null
     * response, a null statusline within the response, and then the actual
     * status code itself.  Also, this will attempt to read the body of the
     * HttpResponse entity (if there was one) and include the first 100 characters
     * of it in the exception message.  The expected, actual, and entire response
     * body is available within the expection itself in case further handling
     * is required.
     * @param expectedStatusCode The HttpResponse code we expect to see in the
     *      HttpResponse.  Usually, you'll want to set this to 200 for OK or HttpStatus.SC_OK.
     * @param response The HttpResponse to check
     * @throws UnexpectedHttpStatusCodeException Thrown if the actual status
     *      code in the response does not match what we expect.
     */
    static public void expectStatusCode(int expectedStatusCode, HttpResponse response) throws UnexpectedHttpStatusCodeException {

        expectStatusCodes(new int[] {expectedStatusCode}, response);

        /**
        // verify the response wasn't null
        if (response == null) {
            throw new UnexpectedHttpStatusCodeException(expectedStatusCode, "HttpResponse was null [expected statusCode=" + expectedStatusCode + "]");
        }

        // get the status line
        StatusLine status = response.getStatusLine();

        // verify the status line object wasn't null
        if (status == null) {
            throw new UnexpectedHttpStatusCodeException(expectedStatusCode, "HttpResponse contained a null StatusLine [expected statusCode=" + expectedStatusCode + "]");
        }

        // verify the expected status code matches
        if (status.getStatusCode() != expectedStatusCode) {
            // prepare the error message we'll set in the exception
            StringBuilder message = new StringBuilder(200);
            String body = null;

            message.append("Unexpected HTTP status code: expected [");
            message.append(expectedStatusCode);
            message.append("] actual [");
            message.append(status.getStatusCode());
            message.append("] reason [");
            message.append(status.getReasonPhrase());
            message.append("]");

            // attempt to read the body of the response
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try {
                    // consume content and process it
                    body = EntityUtils.toString(entity);
                    // append the first 100 chars of response
                    if (body != null) {
                        message.append(" responseBody [");
                        if (body.length() > 100) {
                            message.append(body.substring(0, 100));
                        } else {
                            message.append(body);
                        }
                        message.append("]]");
                    }
                } catch (IOException e) {
                    //logger.warn("IOException while trying to read content for unexpected status code", e);
                } finally {
                    try { entity.consumeContent(); } catch (Exception ignore) { }
                }
            }

            throw new UnexpectedHttpStatusCodeException(expectedStatusCode, status.getStatusCode(), message.toString());
        }
         */
    }


    static public void expectStatusCodes(int [] expectedStatusCodes, HttpResponse response) throws UnexpectedHttpStatusCodeException {
        // verify the response wasn't null
        if (response == null) {
            throw new UnexpectedHttpStatusCodeException(expectedStatusCodes, "HttpResponse was null [expected statusCodes " + Arrays.toString(expectedStatusCodes) + "]");
        }

        // get the status line
        StatusLine status = response.getStatusLine();

        // verify the status line object wasn't null
        if (status == null) {
            throw new UnexpectedHttpStatusCodeException(expectedStatusCodes, "HttpResponse contained a null StatusLine [expected statusCode=" + Arrays.toString(expectedStatusCodes) + "]");
        }

        // try to match any of the expected status codes
        boolean wasExpected = false;
        for (int expectedStatusCode : expectedStatusCodes) {
            if (status.getStatusCode() == expectedStatusCode) {
                wasExpected = true;
                break;
            }
        }

        // verify the expected status code matches
        if (!wasExpected) {
            // prepare the error message we'll set in the exception
            StringBuilder message = new StringBuilder(200);
            String body = null;

            message.append("Unexpected HTTP status code: expected [");
            message.append(Arrays.toString(expectedStatusCodes));
            message.append("] actual [");
            message.append(status.getStatusCode());
            message.append("] reason [");
            message.append(status.getReasonPhrase());
            message.append("]");

            // attempt to read the body of the response
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try {
                    // consume content and process it
                    body = EntityUtils.toString(entity);
                    // append the first 100 chars of response
                    if (body != null) {
                        message.append(" responseBody [");
                        if (body.length() > 100) {
                            message.append(body.substring(0, 100));
                        } else {
                            message.append(body);
                        }
                        message.append("]]");
                    }
                } catch (IOException e) {
                    //logger.warn("IOException while trying to read content for unexpected status code", e);
                } finally {
                    try { entity.consumeContent(); } catch (Exception ignore) { }
                }
            }

            throw new UnexpectedHttpStatusCodeException(expectedStatusCodes, status.getStatusCode(), message.toString());
        }
    }

}
