package com.example.scheduler;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class SchedulerApplication {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerApplication.class);

    CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }

    @Bean
    MethodToolCallbackProvider methodToolCallbackProvider(DogAdoptionScheduler scheduler) {
        return MethodToolCallbackProvider.builder().toolObjects(scheduler).build();
    }

    @Bean
    public CommandLineRunner commandRunner(McpSyncServer mcpSyncServer) {
        return args -> {
            logger.info("Server: {}", mcpSyncServer.getServerInfo());
            latch.await();

            List<McpServerFeatures.SyncToolSpecification> newTools = McpToolUtils
                    .toSyncToolSpecifications(ToolCallbacks.from(new DogAdoptionScheduler()));

            for (McpServerFeatures.SyncToolSpecification newTool : newTools) {
                logger.info("Add new tool: {}", newTool);
                mcpSyncServer.addTool(newTool);
            }
            logger.info("Tools updated: ");
        };
    }
}

@Component
class DogAdoptionScheduler {

    @Tool(description = "schedule an appointment to pickup or adopt a " +
            "dog from a Pooch Palace location")
    String schedule(int dogId, String dogName) {
        System.out.println("Scheduling adoption for dog " + dogName);
        return Instant
                .now()
                .plus(3, ChronoUnit.DAYS)
                .toString();
    }
}