package com.github.feifanlee.outer
import java.util
import java.util.Properties

import com.github.feifanlee.util.HbaseUtil
import com.github.feifanlee.util.HbaseUtil._
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{Put, Table}
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.JavaConversions._

/**
  * Created by lifeifan on 2018/11/22.
  */
class HbaseOuter(table:String,cf:Array[Byte],rowkey:String) extends Outer{
    var htable:Table = null
    var puts:java.util.ArrayList[Put] = null
    override def init(): Unit = {
        htable = HbaseUtil.getConn.getTable(TableName.valueOf(table))
        puts = new util.ArrayList[Put]()
    }

    override def out(line: util.Map[String, Object]): Unit = {
        val id = getRowkey(line)
        val put = new Put(id.toBytes)
        line.entrySet().foreach(kv=>{
            put.addColumn(cf,kv.getKey.toBytes,kv.getValue.toBytes)
        })
        puts.add(put)
    }



    override def close(): Unit = {
        htable.put(puts)
        htable.close()
    }

    private def getRowkey(implicit line: java.util.Map[String,Object]): String ={
        rowkey.split(",")
            .map(key=>{
                line.getOrElse(key,"null").toString.trim
            })
            .mkString("-")
    }
}

object HbaseOuter{
    def apply(props:Properties): HbaseOuter = {
        val table = props.getProperty("outer.hbase.table")
        val cf = props.getProperty("outer.hbase.cf")
        val rowkey = props.getProperty("outer.hbase.rowkey")
        new HbaseOuter(table,cf.toBytes,rowkey)
    }
}
