package org.forest.backend.httpclient.executor;


import org.apache.http.client.methods.HttpDelete;
import org.forest.backend.httpclient.HttpclientRequestProvider;
import org.forest.backend.httpclient.request.HttpclientRequestSender;
import org.forest.backend.httpclient.response.HttpclientResponseHandler;
import org.forest.backend.url.URLBuilder;
import org.forest.http.ForestRequest;

/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2016-09-15
 */
public class HttpclientDeleteExecutor extends AbstractHttpclientExecutor<HttpDelete> {

    private final static HttpclientRequestProvider<HttpDelete> httpDeleteProvider =
            new HttpclientRequestProvider<HttpDelete>() {
                @Override
                public HttpDelete getRequest(String url) {
                    return new HttpDelete(url);
                }
            };

    @Override
    protected HttpclientRequestProvider<HttpDelete> getRequestProvider() {
        return httpDeleteProvider;
    }

    @Override
    protected URLBuilder getURLBuilder() {
        return URLBuilder.getQueryableURLBuilder();
    }

    public HttpclientDeleteExecutor(ForestRequest request, HttpclientResponseHandler httpclientResponseHandler, HttpclientRequestSender requestSender) {
        super(request, httpclientResponseHandler, requestSender);
    }

}


