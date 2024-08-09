package org.servicenow.fileservice.leaderelection;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Leader {

    public Leader(CustomLeaderLatchListener customLeaderLatchListener) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
//        ## Another approach with more control on events
//        LeaderSelectorListener listener = new CustomLeaderListener();
//        LeaderSelector selector = new LeaderSelector(client, "/election", listener);
//        selector.autoRequeue();
//        selector.start();

        LeaderLatch leaderLatch = new LeaderLatch(client, "/election");
        leaderLatch.addListener(customLeaderLatchListener);
        leaderLatch.start();
    }
}
