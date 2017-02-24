package com.ethercamp.starter.rest;


import com.ethercamp.starter.contracts.EthNotary.EthNotaryControl;
import com.ethercamp.starter.ethereum.EthereumBlockchain;
import org.adridadou.ethereum.values.EthValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class MyRestController {

    @Autowired
    EthereumBlockchain ethereumBlockchain;

    @RequestMapping(value = "/bestBlock", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getBestBlock() throws IOException {
        return ethereumBlockchain.getBestBlock();
    }

    @RequestMapping(value = "/account/{key}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createAccount(@PathVariable("key") String key) throws IOException {
        return ethereumBlockchain.getAccountFromPrivateKey(key).getAddress().toString();
    }

    @RequestMapping(value = "/deploy/{key}", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deployContract(@PathVariable("key") String key, @RequestParam("contract") String contract, @RequestParam("contractName") String contractName) throws IOException {
        return ethereumBlockchain.deploy(contractName, contract, key).toString();
    }

    @RequestMapping(value = "/add/notary/{contact}/account/{key}", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String invokeNotaryAdd(@PathVariable("key") String key, @PathVariable("contract") String contract,
                               @RequestParam("title") String title, @RequestParam("cr") String cr, @RequestParam("doc") String doc){
        EthNotaryControl eNC = new EthNotaryControl(key, contract);
        return eNC.addDocument(title, cr, doc, EthValue.wei(1000000)).toString();
    }

    @RequestMapping(value = "/get/notary/{contact}/account/{key}", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String invokeNotaryGet(@PathVariable("key") String key, @PathVariable("contract") String contract, @RequestParam("title") String title){
        EthNotaryControl eNC = new EthNotaryControl(key, contract);
        return eNC.getDocument(title).toString();
    }

}
