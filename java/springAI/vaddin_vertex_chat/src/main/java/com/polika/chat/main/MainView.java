package com.polika.chat.main;

import com.google.cloud.vertexai.VertexAI;
import com.polika.chat.service.WatDoJe;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {
    @Value("${spring.ai.vertex.ai.gemini.project-id}")
    private String projectId;
    @Value("${spring.ai.vertex.ai.gemini.location}")
    private String locations;
    @Value("${spring.ai.vertex.ai.gemini.chat.options.model}")
    private String model;
    public MainView() {
        // Use TextField for standard text input
        TextField textField = new TextField("Ask me");

        // Button click listeners can be defined as lambda expressions
        VertexAiGeminiChatModel.Builder builder =  VertexAiGeminiChatModel.builder();
        builder.vertexAI(new VertexAI(projectId, locations));
        WatDoJe watDoJe = new WatDoJe(builder.build());
        Button button = new Button("Ask me", e ->  {
            add(new Paragraph(watDoJe.chat(textField.getValue())));
        });

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(textField, button);
    }
}
