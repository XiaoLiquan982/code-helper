package com.yupi.aicodehelper.ai.mcp;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {

    private static final Logger log = LoggerFactory.getLogger(McpConfig.class);

    @Value("${bigmodel.api-key}")
    private String apiKey;

    @Bean
    public McpToolProvider mcpToolProvider() {
        try {
            // Keep the application bootable even if the external MCP service is unavailable.
            McpTransport transport = new HttpMcpTransport.Builder()
                    .sseUrl("https://open.bigmodel.cn/api/mcp/web_search/sse?Authorization=" + apiKey)
                    .logRequests(true)
                    .logResponses(true)
                    .build();
            McpClient mcpClient = new DefaultMcpClient.Builder()
                    .key("yupiMcpClient")
                    .transport(transport)
                    .build();
            return McpToolProvider.builder()
                    .mcpClients(mcpClient)
                    .build();
        } catch (Exception e) {
            log.warn("Failed to initialize BigModel MCP tool provider, starting without MCP tools", e);
            return McpToolProvider.builder().build();
        }
    }
}
