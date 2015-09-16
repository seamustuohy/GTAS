package gov.gtas.services;

import java.util.List;

import gov.gtas.delegates.vo.MessageVo;

public interface MessageService {
    public List<String> preprocess(String filePath);
    public MessageVo parse(String message);
    public void load(MessageVo messageVo);
}
