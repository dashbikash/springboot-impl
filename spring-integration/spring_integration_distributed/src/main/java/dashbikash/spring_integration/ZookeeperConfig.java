package dashbikash.spring_integration;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {

    @Bean
    public CuratorFramework curatorFramework() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                "localhost:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        return client;
    }

    @Bean
    public InterProcessMutex lock(CuratorFramework client) {
        return new InterProcessMutex(client, "/distributed-lock");
    }
}