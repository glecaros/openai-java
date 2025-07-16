package com.openai.example;

import com.azure.identity.AuthenticationUtil;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.openai.azure.AzureOpenAIServiceVersion;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.credential.BearerTokenCredential;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;

public final static class AzureUnifiedEndpointExample {

     public static void main(String[] args) throws Exception {
        OpenAIClient client = OpenAIOkHttpClient.builder()
            .baseUrl("https://<endpoint>/openai/v1")
            .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                        new DefaultAzureCredentialBuilder().build(), "https://cognitiveservices.azure.com/.default")))
            .putQueryParam("api-version", "preview")
            .build()
        ;

        ResponseCreateParams params = ResponseCreateParams.builder()
                .input("Write me a haiku about the number e")
                .model(ChatModel.GPT_4O_MINI)
                .build();

        client.responses().create(params).output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(outputText -> outputText.text())
                .forEach(System.out::println);
    }

}
