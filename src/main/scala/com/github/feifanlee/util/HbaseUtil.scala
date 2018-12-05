package com.github.feifanlee.util

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.hadoop.hbase.util.Bytes

/**
  * Created by lifeifan on 2018/11/22.
  */
object HbaseUtil {
    lazy val conn = {
        val prop = ConfigUtil.getProps

        val conf = HBaseConfiguration.create();

        if(prop.getProperty("hbase.zookeeper.quorum")!=null)
            conf.set("hbase.zookeeper.quorum",prop.getProperty("hbase.zookeeper.quorum"))

        if(prop.getProperty("zookeeper.znode.parent")!=null)
            conf.set("zookeeper.znode.parent",prop.getProperty("zookeeper.znode.parent"))

        ConnectionFactory.createConnection(conf)
    }

    def getConn = conn

    implicit class ByteObj(obj:Object){
        def toBytes:Array[Byte]={
            obj match {
                case o:java.lang.String=>
                    o.getBytes()
                case o:java.lang.Integer=>
                    Bytes.toBytes(o)
                case o:java.lang.Double=>
                    Bytes.toBytes(o)
                case o:java.lang.Float=>
                    Bytes.toBytes(o)
                case _ =>
                    //alert
                    obj.toString.getBytes()
            }
        }
    }
}
