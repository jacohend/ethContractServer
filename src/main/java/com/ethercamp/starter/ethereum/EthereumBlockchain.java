package com.ethercamp.starter.ethereum;

import org.adridadou.ethereum.EthereumBackend;
import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.EthereumProxy;
import org.adridadou.ethereum.converters.input.InputTypeHandler;
import org.adridadou.ethereum.converters.output.OutputTypeHandler;
import org.adridadou.ethereum.ethj.EthereumReal;
import org.adridadou.ethereum.ethj.EthereumTest;
import org.adridadou.ethereum.event.EthereumEventHandler;
import org.adridadou.ethereum.swarm.SwarmService;
import org.adridadou.ethereum.values.*;
import org.bouncycastle.util.encoders.Hex;
import org.ethereum.core.Account;
import org.ethereum.core.Transaction;
import org.ethereum.crypto.ECKey;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.solidity.compiler.SolidityCompiler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


public class EthereumBlockchain {

    Ethereum ethereum;
    EthereumReal eth;
    private InputTypeHandler inputTypeHandler;
    private OutputTypeHandler outputTypeHandler;
    private EthereumEventHandler handler;
    private EthereumProxy bcProxy;
    private EthereumFacade ethereumFacade;
    private EthereumListener listener;

    public void start(){
        this.ethereum = EthereumFactory.createEthereum();
        EthereumReal eth = new EthereumReal(ethereum);
        inputTypeHandler = new InputTypeHandler();
        outputTypeHandler = new OutputTypeHandler();
        handler = new EthereumEventHandler();
        bcProxy = new EthereumProxy(eth, handler, inputTypeHandler, outputTypeHandler);
        ethereumFacade = new EthereumFacade(bcProxy, inputTypeHandler, outputTypeHandler, SwarmService.from(SwarmService.PUBLIC_HOST), SolidityCompiler.getInstance());
        listener = new EthereumListener(ethereum);
        this.ethereum.addListener(listener);
    }

    public EthAccount getAccountFromPrivateKey(String privatehex){
        BigInteger privateKey = new BigInteger(Hex.decode(privatehex));
        ECKey key = ECKey.fromPrivate(privateKey);
        return new EthAccount(key);
    }

    public EthAddress getAddress(String address){
        try {
            return EthAddress.of(org.apache.commons.codec.binary.Hex.decodeHex(address.toCharArray()));
        }catch (Exception e){
            return null;
        }
    }

    public EthAccount newAccount(){
        ECKey key = new ECKey();
        EthAccount acct = new EthAccount(key);
        return acct;
    }

    public EthValue getBalance(String address){
        return ethereumFacade.getBalance(getAddress(address));
    }

    public Object getContract(EthAddress address, EthAccount acct,  Class<? extends Object> contractInterface){
        return ethereumFacade.createContractProxy(address, ethereumFacade.getAbi(address), contractInterface).forAccount(acct);
    }

    public EthAddress deploy(String contractName, String contract, String key){
        try {
            SoliditySource contractSource = new SoliditySource(contract);
            CompiledContract compiledContract = ethereumFacade.compile(contractSource).get().get(contractName);
            EthAddress address = ethereumFacade.publishContract(compiledContract, getAccountFromPrivateKey(key)).get();
            return address;
        }catch (Exception e){
        }
        return null;
    }

    public String sendTx(String fromKey, String to, EthValue value){
        try {
            return ethereumFacade.sendEther(getAccountFromPrivateKey(fromKey), getAddress(to), value).get().getResult().toString();
        }catch (Exception e){
            return "";
        }
    }

    public Boolean isPending(String txHash){
        for (Transaction tx : ethereum.getPendingStateTransactions()){
            if (Hex.toHexString(tx.getHash()).equals(txHash)){
                return true;
            }
        }
        for (Transaction tx : ethereum.getWireTransactions()){
            if (Hex.toHexString(tx.getHash()).equals(txHash)){
                return true;
            }
        }
        return false;
    }

    public Transaction findTx(String txHash, long blocksBackwards){
        long blockNumber = ethereum.getBlockchain().getBestBlock().getNumber();
        for (long i = blockNumber; i> i-blocksBackwards; i--){
            List<Transaction> txList = ethereum.getBlockchain().getBlockByNumber(i).getTransactionsList();
            if (txList.size() > 0){
                for (Transaction tx : txList){
                    if (Hex.toHexString(tx.getHash()).equals(txHash)){
                        return tx;
                    }
                }
            }
        }
        return null;
    }

    public String getBestBlock(){
        return "" + ethereum.getBlockchain().getBestBlock().getNumber();
    }

    public Boolean isSynced(){return listener.isSyncDone();}
}
