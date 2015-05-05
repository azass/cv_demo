package demo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

	static final Logger log = LoggerFactory.getLogger(Receiver.class);
	
    @Autowired
    AppController controller;

	List<Task> list = new ArrayList<Task>();
    /**
     * When you receive a message, print it out, then shut down the application.
     * Finally, clean up any ActiveMQ server stuff.
     */
    @JmsListener(destination = "sim-destination", containerFactory = "myJmsContainerFactory", concurrency = "1")
    public void receiveMessage(Message<Task> message) {
    	log.info("Receiver Received <" + message + ">");
		try {
			Task task = message.getPayload();
			log.info(task.toString());
			list.add(task);
			controller.execute(task, this);

			log.info("COUNT=" + list.size());
			while (list.size() >= 2) {
				Thread.sleep(1000);
			}
			log.info("END receiveMessage");
		} catch (Exception e) {

			e.printStackTrace();
		}
    }
    
    public void init() {
    	log.info("init");
    }
    
    public void notifyComplete(Task task) {
    	log.info("notify SIZE=" + list.size());
    	list.remove(task);
    	log.info("notify=>" + task + " SIZE=" + list.size());
    }
}

