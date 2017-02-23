package com.ethercamp.starter.contracts.EthNotary;

import org.adridadou.ethereum.values.Payable;

import java.util.concurrent.CompletableFuture;

/**
 * Created by jacob on 2/23/17.
 */
public interface EthNotary {
    Payable<Void> add (String title, String cr, String doc);
    GetDocument get (String title);
}
