package com.example.agentic;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/route")
public class RouteApiController {
    final RoutingWorkflow routerWorkflow ;

   // @Autowired
    public RouteApiController(ChatClient.Builder chatClientBuilder) {
        routerWorkflow = new RoutingWorkflow(chatClientBuilder.build());
    }

    @GetMapping("/agent")
    public String ticketHandler(@RequestParam(value = "message", defaultValue = "Tell me a joke") String ticket) {
        System.out.println("###Input"+ticket);
        return  routerWorkflow.route(ticket, Application.supportRoutes);
    }
}
