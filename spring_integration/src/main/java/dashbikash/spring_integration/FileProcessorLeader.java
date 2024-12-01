package dashbikash.spring_integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.integration.zookeeper.leader.LeaderInitiator;
import org.springframework.stereotype.Component;

@Component
public class FileProcessorLeader {
	
    private final LeaderInitiator leaderInitiator;

    @Autowired
    public FileProcessorLeader(LeaderInitiator leaderInitiator) {
        this.leaderInitiator = leaderInitiator;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void startLeaderInitiator() {
        leaderInitiator.start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stopLeaderInitiator() {
        leaderInitiator.stop();
    }
}