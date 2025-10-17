package com.portal.mcp_client.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Adapted from https://github.com/kuldeepsingh99/mcp-server-with-spring-ai/tree/main
@RestController
public class AccountController {

    private final ChatClient chatClient;
    private  final String default_prompt = """ 
            For the following task, use ONLY the "Find all Seller Accounts by owner" tool:
            %s""";

    public AccountController(ChatClient.Builder chat, ToolCallbackProvider toolCallbackProvider) {
        FunctionCallback[] tools = toolCallbackProvider.getToolCallbacks();

// Now you can iterate and display tool names and metadata
        for (FunctionCallback tool :  tools) {
            System.out.println(tool.getDescription());
        }
        this.chatClient = chat.defaultTools(toolCallbackProvider)
                .build();
    }


    /*@GetMapping("/name")
    public String getAccountByName(@RequestParam("q") String name) {
        PromptTemplate pt = new PromptTemplate(
                default_prompt.formatted("Search account by given name" + name));
        return this.chatClient.prompt(pt.create())
                .call()
                .content();
    }*/

    @GetMapping("/owner")
    public String getAccountByOwner(@RequestParam("q") String name) {

        PromptTemplate pt = new PromptTemplate(default_prompt.formatted("Search account by given owner" + name));
        return this.chatClient.prompt(pt.create())
                .call()
                .content();
    }
}
