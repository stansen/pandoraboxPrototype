package com.rxjavahttprequest.utils;


import com.rxjavahttprequest.model.FileDownloadModel;

import java.util.List;
import java.util.Set;

/**
 *
 * The DB helper for handling all {@link FileDownloadModel} updating to DB.
 */
public interface IFileDownloadDBHelper {

    Set<FileDownloadModel> getAllUnComplete();

    Set<FileDownloadModel> getAllCompleted();

    void refreshDataFromDB();

    /**
     * @param id download id
     */
    FileDownloadModel find(final int id);


    void insert(final FileDownloadModel downloadModel);

    void update(final FileDownloadModel downloadModel);

    void update(final List<FileDownloadModel> downloadModelList);

    void remove(final int id);

    void update(final int id, final byte status, final long soFar, final long total);

    void updateRetry(final int id, final String errMsg, final int retryingTimes);

    void updateComplete(final int id, final long total);

    void updatePause(final int id, final long sofar);

    void updatePending(final int id);
}
