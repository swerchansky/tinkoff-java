package edu.java.service.sender;

import edu.java.client.dto.LinkUpdateRequest;

public interface UpdateSender {
    void send(LinkUpdateRequest update);
}
