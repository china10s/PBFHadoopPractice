package com.hadoop.zookeeper;


import org.apache.zookeeper.KeeperException;

import java.util.List;

public class ListGroupMember extends ConnectionWatcher{
    public void list(String groupName) throws KeeperException,InterruptedException{
        String path = "/"+groupName;
        try{
            List<String> children = zk.getChildren(path,false);
            if (children.isEmpty()){
                System.out.println("no memebers");
                System.exit(1);
            }
            for (String child:children){
                System.out.println(child);
            }
        }catch (KeeperException.NoNodeException e){
            System.out.println("Group not exist");
            System.exit(1);
        }
    }

    public static void main(String[] args) throws Exception{
        if(args.length < 1){
            System.out.println("less args");
            System.exit(-1);
        }
        ListGroupMember listGroup = new ListGroupMember();
        listGroup.connect(args[0]);
        listGroup.list("");
        listGroup.close();
    }
}
