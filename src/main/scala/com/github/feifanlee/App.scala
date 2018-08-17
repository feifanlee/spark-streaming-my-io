package com.github.feifanlee

import com.github.feifanlee.mapper.MapperBuilder
import com.github.feifanlee.outer.OuterBuilder
import com.github.feifanlee.util.MybatisUtil
import com.sun.jersey.api.json.JSONConfiguration.MappedBuilder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils

/**
  * Created by lifeifan on 2018/8/16.
  */
object App {
    def main(args: Array[String]): Unit = {
        val zk = args(0)
        val topic = args(1)
        val groupid = args(2)
        val partitions = args(3).toInt
        val window = args(4).toInt

        val conf = new SparkConf().setAppName("StreamingApp")
//                .set("spark.serializer","org.apache.spark.serializer.KryoSerialization")
//                .registerKryoClasses()
        val ssc = new StreamingContext(conf, Seconds(window))

        var topics: Map[String, Int] = Map()
        topics = topics.updated(topic, partitions)
        val stream = KafkaUtils.createStream(ssc, zk, groupid, topics)

        stream.foreachRDD(rdd=>{
            rdd.foreachPartition(part=>{
                val kvlist = part.toList
                if(!kvlist.isEmpty){
                    val mapper = MapperBuilder.build()
                    val outer = OuterBuilder.build()
                    outer.init()
                    kvlist.foreach(kv=>{
                        val value = kv._2
                        val map = mapper.toMap(value)
                        outer.out(map)
                    })
                    outer.close()
                }
            })
        })

        //        stream.print()

        ssc.start()
        ssc.awaitTermination()
    }
}
