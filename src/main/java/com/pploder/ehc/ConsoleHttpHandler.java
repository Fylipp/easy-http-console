package com.pploder.ehc;

import lombok.extern.slf4j.XSlf4j;
import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Serves the HTTP content of the web interface.
 * This includes the HTML interface and some various resources (like icons).
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
@XSlf4j
class ConsoleHttpHandler implements HttpHandler {

    private final NetModule netModule;

    private final Map<String, Supplier<Page>> uriHandler = new HashMap<>();

    /**
     * Creates a new instance for the given host and with the given absolute HTML resource path.
     * The path is the page that gets served when the root page is requested.
     *
     * @param netModule         The module for which the site will be hosted.
     * @param interfaceSupplier The supplier for the interface.
     * @param resources         The resources to be served besides the interface
     * @throws IOException If such an exception occurs whilst reading from the resource at the given path.
     */
    public ConsoleHttpHandler(NetModule netModule, Supplier<Page> interfaceSupplier, Resource... resources)
            throws IOException {
        this.netModule = netModule;

        uriHandler.put("/", Objects.requireNonNull(interfaceSupplier));

        for (Resource resource : resources) {
            log.debug("Loading resource {}", resource.getPath());

            try {
                uriHandler.put(resource.getWebPath(), new RawHttpSupplier(
                        Files.readAllBytes(Paths.get(Resource.class.getResource(resource.getPath()).toURI())),
                        resource.getMime()
                ));
            } catch (IOException | URISyntaxException e) {
                log.catching(e);
            }
        }
    }

    @Override
    public void handleHttpRequest(HttpRequest httpRequest, HttpResponse httpResponse, HttpControl httpControl) throws Exception {
        log.debug("Incoming HTTP request of '{}' ({})", httpRequest.uri(), httpRequest.method());

        String uri = httpRequest.uri();

        Supplier<Page> pageSupplier = uriHandler.get(uri);

        if (pageSupplier == null) {
            httpResponse
                    .status(404)
                    .header("Content-Type", "text/html")
                    .content("URI handler not found for " + uri)
                    .end();
        } else {
            Page page = pageSupplier.get();

            Object data = page.getData();

            if (page.isSite()) {
                if (!(data instanceof String)) {
                    data = String.valueOf(data);
                }

                data = ((String) data)
                        .replace("{{ADDRESS}}", netModule.getHttpURL())
                        .replace("{{WEBSOCKET}}", netModule.getWebsocketURL());
            }

            httpResponse
                    .status(page.getResponseCode())
                    .header("Content-Type", page.getMime());

            if (data instanceof byte[]) {
                httpResponse.content((byte[]) data);
            } else if (data instanceof ByteBuffer) {
                httpResponse.content((ByteBuffer) data);
            } else {
                httpResponse.content(String.valueOf(data));
            }

            httpResponse.end();
        }

        /*
        if (uri.equals("/")) {
            log.debug("Requested URI is root; sending 200");
            httpResponse
                    .status(200)
                    .header("Content-Type", "text/html")
                    .content(site.replace("{{SERVED}}", dateFormat.format(Date.from(Instant.now()))))
                    .end();
        } else {
            Pair<String, byte[]> r = resources.get(uri);

            if (r == null) {
                log.debug("Requested URI not registered as resource; sending 404 ({})", uri);
                httpResponse
                        .status(404)
                        .header("Content-Type", "text/html")
                        .content("<?DOCTYPE html><html><head><title>404</title><meta charset=\"utf-8\"></head><body><h1>404: Resource not found</h1></body></html>")
                        .end();
            } else {
                log.debug("Requested URI registered as resource; sending 200 ({})", uri);
                httpResponse
                        .status(200)
                        .header("Content-Type", r.getKey())
                        .content(r.getValue())
                        .end();
            }
        }
        */
    }

}
