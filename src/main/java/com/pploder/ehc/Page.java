package com.pploder.ehc;

import lombok.Data;

/**
 * Represents content that is either binary or plain text.
 * Will be delivered with a HTTP response code and the MIME content type.
 * The boolean {@link #site} determines whether the page is the primary interface.
 */
@Data
public class Page {

    private final Object data;
    private final int responseCode;
    private final String mime;
    private final boolean site;

}
