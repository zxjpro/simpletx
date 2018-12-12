package com.xiaojiezhu.simpletx.common.node;

import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/12 23:30
 */
public interface RelationNodeBlock extends NodeBlock {

    boolean isRoot();

    RelationNodeBlock getParentNodeBlock();


    /**
     * 获取子节点，这是一个有序节点
     * @return
     */
    List<RelationNodeBlock> getChildrenNodeBlocks();






}
