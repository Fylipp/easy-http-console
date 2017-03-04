package com.pploder.ehc;

import java.util.Objects;

/**
 * An enum representing the default resources.
 * The resources listed are exclusively for the favicon of the interface in various browsers.
 */
public enum DefaultResource implements Resource {
    ANDROID_CHROME_192x192("android-chrome-192x192.png", "image/png"),
    ANDROID_CHROME_256x256("android-chrome-256x256.png", "image/png"),
    APPLE_TOUCH_ICON("apple-touch-icon.png", "image/png"),
    BROWSERCONFIG("browserconfig.xml", "application/xml"),
    FAVICON("favicon.ico", "image/x-icon"),
    FAVICON_16x16("favicon-16x16.png", "image/png"),
    FAVICON_32x32("favicon-32x32.png", "image/png"),
    MANIFEST("manifest.json", "application/json"),
    MSTILE_150x150("mstile-150x150.png", "image/png"),
    SAFARI_PINNED_TAB("safari-pinned-tab.svg", "image/svg+xml");

    private final String path;
    private final String mime;

    DefaultResource(String path, String mime) {
        this.path = Objects.requireNonNull(path);
        this.mime = Objects.requireNonNull(mime);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getWebPath() {
        return "/" + path;
    }

    @Override
    public String getMime() {
        return mime;
    }

}
