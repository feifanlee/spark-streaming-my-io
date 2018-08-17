spark-submit \
 --master local[*]\
 --conf spark.streaming.concurrentJobs=3\
 --conf spark.streaming.receiver.maxRate=300\
 --jars mybatis-config.jar,lib/mybatis-3.4.4.jar,lib/mysql-connector-java-5.1.42.jar\
 --files my-io-config.properties\
 --class com.github.feifanlee.App \
 spark-streaming-my-io-1.0-SNAPSHOT.jar\
 192.168.1.56:2181,192.168.1.57:2181,192.168.1.60:2181 lfftopic lffgroup 3 3 
