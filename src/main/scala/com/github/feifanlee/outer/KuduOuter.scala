package com.github.feifanlee.outer
import java.util
import java.util.Properties

import com.github.feifanlee.util.ConfigUtil
import org.apache.kudu.client.KuduClient.KuduClientBuilder
import org.apache.kudu.client._

/**
  * Created by lifeifan on 2019/3/13.
  */
class KuduOuter(table:KuduTable,session:KuduSession) extends Outer{

    override def init(): Unit = {
    }

    override def out(line: util.Map[String, Object]): Unit = {
        val insert = table.newInsert()
        val row = insert.getRow

        KuduOuter.insertRow(row,line)

        session.apply(insert)

        val scanner = KuduOuter.client.newScannerBuilder(table).build()
        while(scanner.hasMoreRows){
            val rstiter = scanner.nextRows()
            while(rstiter.hasNext){
                val rst  = rstiter.next()
                print("==="+rst.getString(0)+rst.getInt(1))
            }
        }

    }

    override def close(): Unit = {
        session.flush()
    }
}

object KuduOuter {
    var client : KuduClient = null

    //session is thread-safe
    var session : KuduSession = null
    //table is thread-safe
    var table : KuduTable = null

    //TODO: extract to meta util
    var insertRow: (PartialRow, java.util.Map[String, Object]) => Unit = null


    def apply(props: Properties): KuduOuter = {
        synchronized {

            val master = props.getProperty("out.kudu.master")
            val tableName = props.getProperty("out.kudu.table")


            if (client == null || client == null) {
                client = new KuduClientBuilder(master).build()

                session = client.newSession()
                session.setTimeoutMillis(10000)
                session.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_BACKGROUND)

                table = client.openTable(tableName)

                //col:typ,col:typ....
                val metacfg = props.getProperty("out.kudu.meta")
                insertRow = {
                    val meta = metacfg.split(",")
                        .map(kv => (kv.split(":")(0), kv.split(":")(1)))
                    (row: PartialRow, data: java.util.Map[String, Object]) => {
                        meta.foreach(col_typ => {
                            val (col, typ) = col_typ
                            typ match {
                                case "string" =>
                                    val value = data.get(col)
                                    if (value == null) {
                                        row.addStringUtf8(col, null)
                                    } else {
                                        row.addStringUtf8(col, value.toString.getBytes)
                                    }
                                case "int" =>
                                    val value = data.get(col)
                                    if (value == null) {
                                        row.addInt(col, -999)
                                    } else {
                                        row.addInt(col, value.asInstanceOf[java.lang.Integer])
                                    }
                                case "float" =>
                                    val value = data.get(col)
                                    if (value == null) {
                                        row.addFloat(col, -999)
                                    } else {
                                        row.addFloat(col, value.asInstanceOf[java.lang.Float])
                                    }
                                case "double" =>
                                    val value = data.get(col)
                                    if (value == null) {
                                        row.addDouble(col, -999)
                                    } else {
                                        row.addDouble(col, value.asInstanceOf[java.lang.Double])
                                    }
                                case "long" =>
                                    val value = data.get(col)
                                    if (value == null) {
                                        row.addLong(col, -999)
                                    } else {
                                        row.addLong(col, value.asInstanceOf[java.lang.Long])
                                    }
                            }
                        })

                    }

                }
            }


        }
        new KuduOuter(table, session)
    }

}
