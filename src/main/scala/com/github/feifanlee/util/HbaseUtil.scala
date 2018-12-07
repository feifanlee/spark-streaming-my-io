package com.github.feifanlee.util

import java.util

import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Get, Table}
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

    val getTableName = new ThreadLocal[String]
    val getTableIns = new ThreadLocal[Table]
    def getRow(table:String,rowkey:String,cf:String)={
        val htable =
            if (table.equals(getTableName.get())) {
                getTableIns.get()
            }else{
                if(getTableIns.get() != null) {
                    getTableIns.get().close()
                }
                getTableName.set(table)
                getTableIns.set(conn.getTable(TableName.valueOf(table)))
                getTableIns.get()
            }
        val get = new Get(rowkey.toBytes)
        val rst = htable.get(get)
        val navmap = rst.getFamilyMap(cf.getBytes)

        navmap
    }

}
