package org.servicenow.fileservice.leaderelection;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;

@Slf4j
public class CustomLeaderListener extends LeaderSelectorListenerAdapter {

    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        log.info("#######################");
        log.info("I'm the leader instance");
        log.info("#######################");
        log.info("takeLeadership thread : {}", Thread.currentThread().getName());
        while (true) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        if ((newState == ConnectionState.SUSPENDED) || (newState == ConnectionState.LOST)) {
            log.info("#######################################");
            log.info("Leadership revoked from zookeeper");
            log.info("#######################################");
        } else if ((newState == ConnectionState.CONNECTED) || (newState == ConnectionState.RECONNECTED)) {
            log.info("#######################################");
            log.info("Attempting for leadership {}", newState);
            log.info("#######################################");
        }
    }
}
