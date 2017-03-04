package com.pploder.ehc;

/**
 * A resource that will be served to users.
 * Note that the path is relative to this interfaces location.
 * To use absolute paths add a leading {@code /} to the path. The webpath
 * should also start with a trailing forward slash.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
public interface Resource {

    /**
     * @return The path of the resource. Relative to this interfaces location.
     */
    String getPath();

    /**
     * @return The exposed URI of the resource (with a leading {@code /}.
     */
    String getWebPath();

    /**
     * @return The MIME-type of the resource.
     */
    String getMime();

}
