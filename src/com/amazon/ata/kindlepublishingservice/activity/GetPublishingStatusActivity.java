package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.PublishingRecordFoundException;
import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatus;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GetPublishingStatusActivity {

    private final PublishingStatusDao publishingStatusDao;
    private final CatalogDao catalogDao;

    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao, CatalogDao catalogDao) {
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
    }

    public GetPublishingStatusResponse execute(GetPublishingStatusRequest publishingStatusRequest) {
        String publishingRecordId = publishingStatusRequest.getPublishingRecordId();

        List<PublishingStatusItem> publishingStatusItemList =
                publishingStatusDao.getPublishingStatusItems(publishingRecordId);

        List<PublishingStatusRecord> publishingStatusRecordList = new ArrayList<>();
        for (PublishingStatusItem item : publishingStatusItemList) {
            PublishingStatusRecord statusRecord = PublishingStatusRecord.builder()
                    .withBookId(item.getBookId())
                    .withStatus(item.getStatus().name())
                    .withStatusMessage(item.getStatusMessage())
                    .build();
            publishingStatusRecordList.add(statusRecord);
        }

        return GetPublishingStatusResponse.builder()
                .withPublishingStatusHistory(publishingStatusRecordList)
                .build();
    }
}
