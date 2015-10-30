package gov.gtas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.vo.MessageVo;

@Service
public abstract class MessageService {
    @Autowired
    protected LoaderRepository loaderRepo;

    @Autowired
    protected LoaderUtils utils;

    public abstract List<String> preprocess(String message);
    public abstract MessageVo parse(String message);
    public abstract void load(MessageVo parsedMessage);

    protected String filePath = null;
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
