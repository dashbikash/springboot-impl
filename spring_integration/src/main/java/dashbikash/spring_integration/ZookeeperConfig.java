package dashbikash.spring_integration;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.zookeeper.config.LeaderInitiatorFactoryBean;
import org.springframework.integration.zookeeper.metadata.ZookeeperMetadataStore;

@Configuration
@EnableIntegration
public class ZookeeperConfig {

    @Bean
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
    }

    @Bean
    public ZookeeperMetadataStore zookeeperMetadataStore(CuratorFramework curatorFramework) {
        return new ZookeeperMetadataStore(curatorFramework);
    }

    @Bean
    public LeaderInitiatorFactoryBean leaderInitiatorFactoryBean(CuratorFramework client) {
        return new LeaderInitiatorFactoryBean().setClient(client).setPath("/leader");
    }

}