spark-submit \
 --master yarn-client\
 --executor-memory 512M\
 --num-executors 4\
 --executor-cores 4\
 --conf spark.streaming.concurrentJobs=3\
 --conf spark.streaming.receiver.maxRate=300\
 --conf spark.streaming.receiver.writeAheadLog.enable=true\
 --conf spark.streaming.stopGracefullyOnShutdown=true\
 --jars lib/hbase-client-1.2.0.jar,lib/hbase-annotations-1.2.0.jar,lib/hbase-common-1.2.0.jar,lib/hbase-protocol-1.2.0.jar,addfitter.jar\
 --files my-io-config.properties\
 --class com.github.feifanlee.App \
 spark-streaming-my-io-1.0-SNAPSHOT.jar