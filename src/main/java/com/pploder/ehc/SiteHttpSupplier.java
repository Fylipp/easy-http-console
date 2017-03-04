package com.pploder.ehc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A supplier of an HTML interface page.
 * Internally a template of the page is stored. Any occurance of {@code {{SERVED}}} is replaced with a formatted
 * version of the current date and time whenever a page is requested.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
public class SiteHttpSupplier implements Supplier<Page> {

    /**
     * The default date format.
     */
    public static final DateFormat DEFAULT_DATE_FORMAT =
            new SimpleDateFormat("EEEE, dd MMMM yyyy kk:mm ('UTC'Z)", Locale.US);

    /**
     * The default reponse code.
     * The response code {@code 200} signals that the request was completed successfully.
     */
    public static final int DEFAULT_RESPONSE_CODE = 200;

    /**
     * The default site template classpath.
     * It is relative to {@link Resource}.
     */
    public static final String DEFAULT_TEMPLATE = "interface.html";

    private final String template;

    private DateFormat dateFormat;
    private int responseCode;

    /**
     * Creates a new instance with the given template as well as the default date format and response code.
     * The default date format is {@link #DEFAULT_DATE_FORMAT} and the default response code {@link #DEFAULT_RESPONSE_CODE}.
     *
     * @param template The site HTML template.
     */
    public SiteHttpSupplier(String template) {
        this(template, DEFAULT_DATE_FORMAT);
    }

    /**
     * Create a new instance with the given template and date format as well as the default response code.
     * The default response code is {@link #DEFAULT_RESPONSE_CODE}.
     *
     * @param template   The site HTML template.
     * @param dateFormat The date format to use fot the timestamp.
     */
    public SiteHttpSupplier(String template, DateFormat dateFormat) {
        this(template, dateFormat, DEFAULT_RESPONSE_CODE);
    }

    /**
     * Creates a new instance with the given template, date format and response code.
     *
     * @param template     The site HTML template.
     * @param dateFormat   The date format to use for the timestamp.
     * @param responseCode The HTTP response code.
     */
    public SiteHttpSupplier(String template, DateFormat dateFormat, int responseCode) {
        this.template = Objects.requireNonNull(template);
        this.dateFormat = dateFormat;
        this.responseCode = responseCode;
    }

    @Override
    public Page get() {
        String page = getTemplate();

        if (dateFormat != null) {
            page = page.replace("{{SERVED}}", getDateFormat().format(Date.from(Instant.now())));
        }

        return new Page(page, getResponseCode(), "text/html", true);
    }

    /**
     * @return The HTML template of the site.
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @return The date format of the timestamp.
     */
    public DateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * @return The HTTP response code of the site.
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Reads the default site and creates an instance.
     * The default site's classpath relative to {@link Resource} is {@link #DEFAULT_TEMPLATE}
     * and the default date format is {@link #DEFAULT_DATE_FORMAT}.
     *
     * @return An instance with the default interface template.
     */
    public static SiteHttpSupplier defaultSite() {
        return fromResource(DEFAULT_TEMPLATE);
    }

    /**
     * Reads the classpath resource and creates an instance.
     * The classpath is relative to {@link Resource}.
     * The default date format at {@link #DEFAULT_DATE_FORMAT} will be used.
     *
     * @param classResource The HTML template classpath location.
     * @return An instance using the class resource as the template and the default date format.
     */
    public static SiteHttpSupplier fromResource(String classResource) {
        return fromResource(classResource, DEFAULT_DATE_FORMAT);
    }

    /**
     * Reads the classpath resource and creates an instance with it.
     * The classpath is relative to {@link Resource}.
     *
     * @param classResource The HTML template classpath location.
     * @param dateFormat    The date format of the site.
     * @return An instance using the class resource as the template and the given date format.
     */
    public static SiteHttpSupplier fromResource(String classResource, DateFormat dateFormat) {
        try {
            return new SiteHttpSupplier(new String(Files.readAllBytes(
                    Paths.get(SiteHttpSupplier.class.getResource(classResource).toURI())
            )), dateFormat);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
