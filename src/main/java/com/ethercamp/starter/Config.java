package com.ethercamp.starter;

import com.ethercamp.starter.ethereum.EthereumBlockchain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class Config {

    @Bean
    EthereumBlockchain ethereumBean() throws Exception {
        EthereumBlockchain ethereumBlockchain = new EthereumBlockchain();
        Executors.newSingleThreadExecutor().
                submit(ethereumBlockchain::start);

        return ethereumBlockchain;
    }
}
