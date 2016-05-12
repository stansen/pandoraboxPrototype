package com.rxjavahttprequest.exception;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.IOException;


public class FileDownloadOutOfSpaceException extends IOException {

    private long freeSpaceBytes, requiredSpaceBytes, breakpointBytes;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public FileDownloadOutOfSpaceException(long freeSpaceBytes, long requiredSpaceBytes,
                                           long breakpointBytes, Throwable cause) {
        super(String.format("The file is too large to store, breakpoint in bytes: " +
                " %d, required space in bytes: %d, but free space in bytes: " +
                "%d", breakpointBytes, requiredSpaceBytes, freeSpaceBytes), cause);

        init(freeSpaceBytes, requiredSpaceBytes, breakpointBytes);
    }

    public FileDownloadOutOfSpaceException(long freeSpaceBytes, long requiredSpaceBytes,
                                           long breakpointBytes) {
        super(String.format("The file is too large to store, breakpoint in bytes: " +
                " %d, required space in bytes: %d, but free space in bytes: " +
                "%d", breakpointBytes, requiredSpaceBytes, freeSpaceBytes));

        init(freeSpaceBytes, requiredSpaceBytes, breakpointBytes);

    }

    private void init(long freeSpaceBytes, long requiredSpaceBytes, long breakpointBytes) {
        this.freeSpaceBytes = freeSpaceBytes;
        this.requiredSpaceBytes = requiredSpaceBytes;
        this.breakpointBytes = breakpointBytes;
    }

    /**
     * @return The free space in bytes.
     */
    public long getFreeSpaceBytes() {
        return freeSpaceBytes;
    }

    /**
     * @return The required space in bytes use to store the datum will be fetched.
     */
    public long getRequiredSpaceBytes() {
        return requiredSpaceBytes;
    }

    /**
     * @return In normal Case: The value of breakpoint, which has already downloaded by past, if the
     * value is more than 0, it must be resuming from breakpoint. For Chunked Resource(Streaming media):
     * The value would be the filled size.
     */
    public long getBreakpointBytes() {
        return breakpointBytes;
    }
}
