package gov.gtas.services;

import gov.gtas.parsers.edifact.MessageVo;

public interface MessageService {
    public MessageVo parse(String filePath);
    public void load(MessageVo message);
}
