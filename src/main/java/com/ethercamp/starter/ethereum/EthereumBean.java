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
import org.adridadou.ethereum.values.CompiledContract;
import org.adridadou.ethereum.values.EthAccount;
import org.adridadou.ethereum.values.EthAddress;
import org.adridadou.ethereum.values.SoliditySource;
import org.bouncycastle.util.encoders.Hex;
import org.ethereum.core.Account;
import org.ethereum.crypto.ECKey;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.solidity.compiler.SolidityCompiler;

import java.math.BigInteger;


public class EthereumBean {

    Ethereum ethereum;
    EthereumReal eth;
    private InputTypeHandler inputTypeHandler;
    private OutputTypeHandler outputTypeHandler;
    private EthereumEventHandler handler;
    private EthereumProxy bcProxy;
    private EthereumFacade ethereumFacade;

    public void start(){
        this.ethereum = EthereumFactory.createEthereum();
        EthereumReal eth = new EthereumReal(ethereum);
        inputTypeHandler = new InputTypeHandler();
        outputTypeHandler = new OutputTypeHandler();
        handler = new EthereumEventHandler();
        bcProxy = new EthereumProxy(eth, handler, inputTypeHandler, outputTypeHandler);
        ethereumFacade = new EthereumFacade(bcProxy, inputTypeHandler, outputTypeHandler, SwarmService.from(SwarmService.PUBLIC_HOST), SolidityCompiler.getInstance());
        this.ethereum.addListener(new EthereumListener(ethereum));
    }

    public EthAccount createAccount(String privatehex){
        BigInteger privateKey = new BigInteger(Hex.decode(privatehex));
        ECKey key = ECKey.fromPrivate(privateKey);
        return new EthAccount(key);
    }

    public EthAddress deploy(String contractName, String contract, String key){
        try {
            SoliditySource contractSource = new SoliditySource(contract);
            CompiledContract compiledContract = ethereumFacade.compile(contractSource).get().get(contractName);
            EthAddress address = ethereumFacade.publishContract(compiledContract, createAccount(key)).get();
            return address;
        }catch (Exception e){

        }
        return null;
    }

    public String getBestBlock(){
        return "" + ethereum.getBlockchain().getBestBlock().getNumber();
    }
}
