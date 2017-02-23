package com.ethercamp.starter.contracts;

import com.ethercamp.starter.contracts.EthNotary.EthNotary;
import com.ethercamp.starter.contracts.EthNotary.GetDocument;
import com.ethercamp.starter.ethereum.EthereumBlockchain;
import org.adridadou.ethereum.values.EthAccount;
import org.adridadou.ethereum.values.EthAddress;
import org.adridadou.ethereum.values.EthValue;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jacob on 2/23/17.
 */
public class ContractControl {

    @Autowired
    EthereumBlockchain ethereumBlockchain;
    EthAccount account;
    EthAddress contractAddress;

    public ContractControl(String accountKey, String contract){
        account = ethereumBlockchain.getAccountFromPrivateKey(accountKey);
        contractAddress = ethereumBlockchain.getAddress(contract);
    }

    public Boolean addDocument(String title, String cr, String doc, EthValue value){
        EthNotary notary = ((EthNotary) ethereumBlockchain.getContract(contractAddress, account, EthNotary.class));
        try {
            notary.add(title, cr, doc).with(value).get();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public GetDocument getDocument(String title){
        EthNotary notary = ((EthNotary) ethereumBlockchain.getContract(contractAddress, account, EthNotary.class));
        try {
            return notary.get(title);
        }catch (Exception e){
            return null;
        }
    }
}
