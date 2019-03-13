package com.github.feifanlee

import com.github.feifanlee.fitter.FitterBuilder
import com.github.feifanlee.mapper.MapperBuilder
import com.github.feifanlee.outer.OuterBuilder
import com.github.feifanlee.util.ConfigUtil
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import com.github.feifanlee.util.JavaUtil._

/**
  * Created by lifeifan on 2018/8/16.
  */
object App {
    def main(args: Array[String]): Unit = {
//        val zk = args(0)
//        val topic = args(1)
//        val groupid = args(2)
//        val partitions = args(3).toInt
//        val window = args(4).toInt

        val props = ConfigUtil.getProps
        val zk = props.getProperty("in.kafka.zookeeper")
        val topic = props.getProperty("in.kafka.topic")
        val groupid = props.getProperty("in.kafka.groupid")
        val partitions = props.getProperty("in.kafka.partitions").toInt
        val window = props.getProperty("in.kafka.window").toInt

        val conf = new SparkConf().setAppName("StreamingApp")
//                .set("spark.serializer","org.apache.spark.serializer.KryoSerialization")
//                .registerKryoClasses()
        val ssc = new StreamingContext(conf, Seconds(window))
        ssc.checkpoint(props.getProperty("in.kafka.checkpoint.path"))

        var topics: Map[String, Int] = Map()
        topics = topics.updated(topic, partitions)
        val stream = KafkaUtils.createStream(ssc, zk, groupid, topics,StorageLevel.MEMORY_AND_DISK_SER)
        stream.checkpoint(Seconds(props.getProperty("in.kafka.checkpoint.senconds").toInt))
        stream.foreachRDD(rdd=>{
            rdd.foreachPartition(part=>{
                val kvlist = part.toList
                if(!kvlist.isEmpty){
                    val mapper = MapperBuilder.build()
                    val outer = OuterBuilder.build()
                    val fitter = FitterBuilder.build()
                    outer.init()
                    kvlist.foreach(kv=>{
                        try {
                            val value = kv._2
                            val map = mapper.toJMap(value).lowwerKey
                            val line = fitter.fit(map)
                            outer.out(line)
                        }catch {
                            case e:Exception=>
                                e.printStackTrace()
                        }
                    })
                    outer.close()
                }
            })
        })
        //stream.checkpoint(Seconds(props.getProperty("in.kafka.checkpoint.senconds").toInt))

        //        stream.print()

        ssc.start()
        ssc.awaitTermination()
    }
}
