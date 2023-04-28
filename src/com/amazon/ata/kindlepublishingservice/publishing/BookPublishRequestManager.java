package com.amazon.ata.kindlepublishingservice.publishing;

import java.util.LinkedList;
import java.util.Queue;
import javax.inject.Inject;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;

public class BookPublishRequestManager {

    private final Queue<BookPublishRequest> bookPublishRequestQueue = new LinkedList<>();

    private final CatalogDao catalogDao;

    @Inject
    public BookPublishRequestManager( CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }

    public BookPublishRequest addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        bookPublishRequestQueue.add(bookPublishRequest);
        return bookPublishRequest;
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
        return bookPublishRequestQueue.poll();
    }

}
