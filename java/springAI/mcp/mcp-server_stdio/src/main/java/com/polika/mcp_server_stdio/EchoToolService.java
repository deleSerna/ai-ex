package com.polika.mcp_server_stdio;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EchoToolService {
    @Tool(name = "hello", description = "Respond with a hello message")
    public Map<String, Object> hello() {
        return Map.of("echo", "Hello!!");
    }

    @Tool(name = "echo", description = "Echoes back the given message")
    public String echo(String input) {
        return "hello " + input;
    }
}