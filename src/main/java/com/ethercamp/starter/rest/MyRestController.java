package com.ethercamp.starter.rest;


import com.ethercamp.starter.ethereum.EthereumBlockchain;
import org.adridadou.ethereum.values.EthAccount;
import org.adridadou.ethereum.values.EthValue;
import org.bouncycastle.util.encoders.Hex;
import org.ethereum.crypto.ECKey;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

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

    @RequestMapping(value = "/synced", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String isSynced() throws IOException {
        return ethereumBlockchain.isSynced().toString();
    }

    @RequestMapping(value = "/account/{key}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getAccount(@PathVariable("key") String key) throws IOException {
        return ethereumBlockchain.getAccountFromPrivateKey(key).getAddress().toString();
    }

    @RequestMapping(value = "/account/new", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createAccount() throws IOException {
        EthAccount acct = ethereumBlockchain.newAccount();
        ECKey key = acct.key;
        byte[] addr = key.getAddress();
        byte[] priv = key.getPrivKeyBytes();
        String addrBase16 = Hex.toHexString(addr);
        String privBase16 = Hex.toHexString(priv);
        JSONObject res = new JSONObject();
        res.put("address", addrBase16);
        res.put("privateKey", privBase16);
        return res.toJSONString();
    }

    @RequestMapping(value = "/deploy/{key}", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deployContract(@PathVariable("key") String key, @RequestParam("contract") String contract, @RequestParam("contractName") String contractName) throws IOException {
        return ethereumBlockchain.deploy(contractName, contract, key).toString();
    }

    @RequestMapping(value = "/send", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String send(@RequestParam("key") String key, @RequestParam("to") String to, @RequestParam("amount") BigDecimal amount){
        return ethereumBlockchain.sendTx(key, to, EthValue.ether(amount));
    }

    @RequestMapping(value = "/pending/{hash}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String pending(@PathVariable("hash") String hash){
        return ethereumBlockchain.isPending(hash).toString();
    }

}
