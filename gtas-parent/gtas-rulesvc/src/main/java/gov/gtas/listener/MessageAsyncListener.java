package gov.gtas.listener;

import gov.gtas.event.MessageEvent;
import gov.gtas.multicaster.AsyncListener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@AsyncListener
@Component
public class MessageAsyncListener implements ApplicationListener<MessageEvent> {
    private static final Logger logger = LoggerFactory
            .getLogger(MessageAsyncListener.class);

    @Override
    public void onApplicationEvent(MessageEvent event) {

        List<Long> msgIds = event.getMessageIds();
        for (Long msgId : msgIds) {
            logger.info("MessageId:" + msgId + "\n");

        }

    }
}
