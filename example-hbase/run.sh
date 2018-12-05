spark-submit \
 --master local[*]\
 --conf spark.streaming.concurrentJobs=3\
 --conf spark.streaming.receiver.maxRate=300\
 --jars lib/hbase-client-1.2.0.jar,lib/hbase-annotations-1.2.0.jar,lib/hbase-common-1.2.0.jar,lib/hbase-protocol-1.2.0.jar\
 --files my-io-config.properties\
 --class com.github.feifanlee.App \
 spark-streaming-my-io-1.0-SNAPSHOT.jar