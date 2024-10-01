package org.gameserver.configurations;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfig {

    @Bean
    public CloseableHttpClient restTemplate() {
        return HttpClients.createDefault();
    }
}
