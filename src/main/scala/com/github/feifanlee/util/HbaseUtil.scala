package com.github.feifanlee.util


import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client._
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
                case o:java.lang.Integer=>
                    Bytes.toBytes(o)
                case o:java.lang.Double=>
                    Bytes.toBytes(o)
                case o:java.lang.Float=>
                    Bytes.toBytes(o)
                case o:java.lang.String=>
                    Bytes.toBytes(o)
                case o:String=>
                    Bytes.toBytes(o)
                case null=>
                    null
                case _ =>
                    //alert
                    Bytes.toBytes(obj.toString)
            }
        }
    }

    implicit class HbaseBytes(bts:Array[Byte]){
        def toJInt:java.lang.Integer = Bytes.toInt(bts)
        def toJDouble:java.lang.Double = Bytes.toDouble(bts)
        def toJFloat:java.lang.Float = Bytes.toFloat(bts)
        def toJString:java.lang.String = Bytes.toString(bts)
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
        val navmap = rst.getFamilyMap(cf.toBytes)

        navmap
    }

    val scanTableName = new ThreadLocal[String]
    val scanTableIns = new ThreadLocal[Table]
    def scanRows(table:String,prefix:String,cf:String,func:(Array[Byte],java.util.Map[Array[Byte],Array[Byte]])=>Unit)={
        val htable =
            if (table.equals(scanTableName.get())) {
                scanTableIns.get()
            }else{
                if(scanTableIns.get() != null) {
                    scanTableIns.get().close()
                }
                scanTableName.set(table)
                scanTableIns.set(conn.getTable(TableName.valueOf(table)))
                scanTableIns.get()
            }
        val scan = new Scan()
        scan.addFamily(cf.toBytes)
        scan.setStartRow((prefix+"!").toBytes)
        scan.setStopRow((prefix+"~").toBytes)
        val rstScner = htable.getScanner(scan)
//        val iter = rstScner.iterator()
//        while(iter.hasNext){
//            val rst = iter.next()
//            func(rst.getRow,rst.getFamilyMap(cf.toBytes))
//        }
        var rst = rstScner.next()
        while(rst != null){
            func(rst.getRow,rst.getFamilyMap(cf.toBytes))
            rst = rstScner.next()
        }

    }



    def main(args: Array[String]): Unit = {
        val tbl = args(0)
        val rowkey = args(1)
        val cf = args(2)
        val col = args(3)


        val map = getRow(tbl,rowkey,cf)
        val ch = map.get(col.toBytes)
        println(ch)
    }

}
