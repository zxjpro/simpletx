package com.xiaojiezhu.simpletx.server.transaction;

import com.xiaojiezhu.simpletx.protocol.context.ServerConnectionContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 20:27
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TransactionBlock {

    private TransactionBlockInfo transactionBlockInfo;

    private ServerConnectionContext connectionContext;
}
