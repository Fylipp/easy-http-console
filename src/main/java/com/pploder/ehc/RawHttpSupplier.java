package com.pploder.ehc;

import java.util.function.Supplier;

/**
 * A supplier of raw binary data.
 * The data is kept as a byte-array.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
public class RawHttpSupplier implements Supplier<Page> {

    /**
     * The default reponse code.
     * The response code {@code 200} signals that the request was completed successfully.
     */
    public static final int DEFAULT_RESPONSE_CODE = 200;

    private final byte[] data;
    private int responseCode;
    private String mime;

    /**
     * Creates a new instance with the given data and MIME-type as well as the default response code.
     * The default response code is {@link #DEFAULT_RESPONSE_CODE}.
     *
     * @param data The raw binary data of the resource.
     * @param mime The MIME-type of the resource.
     */
    public RawHttpSupplier(byte[] data, String mime) {
        this(data, DEFAULT_RESPONSE_CODE, mime);
    }

    /**
     * Creates a new instance with the given data, response code and MIME-type.
     *
     * @param data         The raw binary data of the resource.
     * @param responseCode The HTTP response code of the resource.
     * @param mime         The MIME-type of the resource.
     */
    public RawHttpSupplier(byte[] data, int responseCode, String mime) {
        this.data = data;
        this.responseCode = responseCode;
        this.mime = mime;
    }

    @Override
    public Page get() {
        return new Page(getData(), getResponseCode(), getMime(), false);
    }

    /**
     * @return The binary data of the resource.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @return The HTTP response code of the resource.
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @return The MIME-type of the resource.
     */
    public String getMime() {
        return mime;
    }

}
