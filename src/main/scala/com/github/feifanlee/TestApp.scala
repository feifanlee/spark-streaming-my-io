package com.github.feifanlee

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by lifeifan on 2018/8/13.
  */
object TestApp {
    def main(args:Array[String]): Unit ={
        val zk = args(0)
        val topic = args(1)
        val groupid = args(2)
        val partitions = args(3).toInt
        val window = args(4).toInt

        val conf = new SparkConf().setAppName("StreamingApp")
        val ssc = new StreamingContext(conf, Seconds(window))

        var topics: Map[String, Int] = Map()
        topics = topics.updated(topic, partitions)
        val stream = KafkaUtils.createStream(ssc, zk, groupid, topics)

        stream.print()

        ssc.start()
        ssc.awaitTermination()
    }
}
