package com.polika.mcp_client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpSchema;

import java.time.Duration;
import java.util.Map;

public class CustomMcpClient {
    public static void main(String[] args) {
        // URL of your MCP server with Streamable HTTP enabled
        String mcpUri = "http://localhost:8090/perplexity-mcp-server/";

        // Create Streamable HTTP transport
        HttpClientStreamableHttpTransport transport = HttpClientStreamableHttpTransport
                .builder(mcpUri)
                .build();

        // Create MCP client with transport
        try (McpSyncClient mcpClient = McpClient.sync(transport)
                .requestTimeout(Duration.ofSeconds(20)) // Set a reasonable request timeout
                .initializationTimeout(Duration.ofSeconds(21))
                // Set a reasonable initialization timeout
                .build()) {
            System.out.println("Connecting to MCP server via Streamable HTTP...");
            mcpClient.initialize();

            System.out.println("Discovering available tools...");
            var tools = mcpClient.listTools().tools();
            for (var tool : tools) {
                System.out.printf("- Tool: %s, Description: %s%n", tool.name(), tool.description());
            }

            // Example: Invoke 'get_info_by_title' tool with parameter
            Map<String, Object> params = Map.of("title", "Perplexity");


            McpSchema.CallToolRequest request =
                    McpSchema.CallToolRequest.builder()
                            .name("get_info_by_title")
                            .arguments(params)
                            .build();

            System.out.println("Invoking tool 'get_info_by_title' with parameters: " + params);
            var response = mcpClient.callTool(request).content();
            System.out.println("Response:");
            for (McpSchema.Content content : response) {
                System.out.println(content);
            }
            System.out.println("Disconnected.");
        }
    }
}
