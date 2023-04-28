package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;

public class BookPublishTask implements Runnable{

    private CatalogDao catalogDao;
    private PublishingStatusDao publishingStatusDao;
    private BookPublishRequestManager bookPublishRequestManager;



    @Inject
    public void BookPublishTask(CatalogDao catalogDao,PublishingStatusDao publishingStatusDao, BookPublishRequestManager
            bookPublishRequestManager) {

        this.catalogDao = catalogDao;
        this.publishingStatusDao = publishingStatusDao;
        bookPublishRequestManager = bookPublishRequestManager;

    }


    @Override
    public void run() {

        BookPublishRequest bookPublishRequest = bookPublishRequestManager.getBookPublishRequestToProcess();

        if(bookPublishRequest == null) {
            return;
        }

        if (bookPublishRequest != null) {

         publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                PublishingRecordStatus.IN_PROGRESS, bookPublishRequest.getBookId());

            KindleFormattedBook kindleFormattedBook = KindleFormatConverter.format(bookPublishRequest);


            try {
                catalogDao.createOrUpdateBook(kindleFormattedBook);

            } catch (BookNotFoundException e) {
                publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.FAILED, bookPublishRequest.getBookId());
            }
        }



    }
}
