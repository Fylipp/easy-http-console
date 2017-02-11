package com.pploder.ehc;

import javafx.util.Pair;
import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Serves the HTTP content of the web interface.
 * This includes the HTML interfaces and some icons for various browsers.
 *
 * @author Philipp Ploder
 * @version 1.0.0
 * @since 1.0.0
 */
class ConsoleHttpHandler implements HttpHandler {

    /**
     * The folder containing all of the required static resources.
     */
    public static final String RESOURCE_PATH = "/com/pploder/ehc/";

    private final HttpConsole httpConsole;
    private final String site;

    private final Map<String, Pair<String, byte[]>> resources = new HashMap<>();

    /**
     * Creates a new instance for the given host and with the given absolute HTML resource path.
     * The path is the page that gets served when the root page is requested. It does not depend on the {@link #RESOURCE_PATH} to allow a custom interface to be used.
     *
     * @param httpConsole The console for which the site will be hosted.
     * @param path        The absolute path of the HTML interface.
     * @throws IOException If such an exception occurs whilst reading from the resource at the given path.
     */
    public ConsoleHttpHandler(HttpConsole httpConsole, String path) throws IOException {
        this.httpConsole = httpConsole;

        try (InputStream stream = getClass().getResourceAsStream(path)) {
            Scanner sc = new Scanner(stream, "UTF-8").useDelimiter("\\A");

            if (sc.hasNext()) {
                site = sc.next()
                        .replace("{{ADDRESS}}", httpConsole.getHttpURL())
                        .replace("{{WEBSOCKET}}", httpConsole.getWebsocketURL());
            } else {
                site = "The resource at " + path + " did not containt any content.";
            }
        }

        loadResource("image/png", "android-chrome-192x192.png");
        loadResource("image/png", "android-chrome-256x256.png");
        loadResource("image/png", "apple-touch-icon.png");
        loadResource("application/xml", "browserconfig.xml");
        loadResource("image/x-icon", "favicon.ico");
        loadResource("image/png", "favicon-16x16.png");
        loadResource("image/png", "favicon-32x32.png");
        loadResource("application/json", "manifest.json");
        loadResource("image/png", "mstile-150x150.png");
        loadResource("image/svg+xml", "safari-pinned-tab.svg");
    }

    /**
     * Loads the given resource into memory and stores it together with its MIME type.
     * Any I/O error is suppressed for convenience as this method is only used in the constructor of this class.
     *
     * @param mime The MIME type of the resource.
     * @param name The resource name.
     */
    private void loadResource(String mime, String name) {
        try (InputStream stream = getClass().getResourceAsStream(RESOURCE_PATH + name)) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(16 * 1024);

            while (true) {
                int c = stream.read();

                if (c < 0) {
                    break;
                }

                bytes.write(c);
            }

            resources.put("/" + name, new Pair<>(mime, bytes.toByteArray()));
        } catch (IOException e) {
            // The exception is suppressed as further handling of an I/O error is not intended
        }
    }

    @Override
    public void handleHttpRequest(HttpRequest httpRequest, HttpResponse httpResponse, HttpControl httpControl) throws Exception {
        String uri = httpRequest.uri();
        if (uri.equals("/")) {
            httpResponse
                    .status(200)
                    .header("Content-Type", "text/html")
                    .content(site.replace("{{SERVED}}", "Saturday, February 2 2017 22:13 (UTC+1)"))
                    .end();
        } else {
            Pair<String, byte[]> r = resources.get(uri);

            if (r == null) {
                httpResponse
                        .status(404)
                        .header("Content-Type", "text/html")
                        .content("<?DOCTYPE html><html><head><title>404</title><meta charset=\"utf-8\"></head><body><h1>404: Resource not found</h1></body></html>")
                        .end();
            } else {
                httpResponse
                        .status(200)
                        .header("Content-Type", r.getKey())
                        .content(r.getValue())
                        .end();
            }
        }

    }
}