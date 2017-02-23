package com.ethercamp.starter.rest;


import com.ethercamp.starter.ethereum.EthereumBlockchain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class MyRestController {

    @Autowired
    EthereumBlockchain ethereumBlockchain;

    @RequestMapping(value = "/bestBlock", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getBestBlock() throws IOException {
        return ethereumBlockchain.getBestBlock();
    }

    @RequestMapping(value = "/create/{account}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createAccount(@PathVariable("account") String account) throws IOException {
        return ethereumBlockchain.getAccountFromPrivateKey(account).getAddress().toString();
    }

}
