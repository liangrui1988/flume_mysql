
mvn clean 
mvn install
��������jar�����ϴ���flume��װĿ¼�µ�lib�ļ����У�ͬʱ��Ҫ�ϴ�MySQL������jar��

-------------���� Spooling Directory Source--------------------------
conf��mysql_sink.conf

# Name the components on this agent
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# Describe/configure the source
a1.sources.r1.type = spooldir
a1.sources.r1.spoolDir = /home/rui/log/flumespool
a1.sources.r1.fileHeader = true
a1.sources.r1.channels = c1

# Describe the sink
a1.sinks.k1.type = com.flume.dome.mysink.MysqlSink
a1.sinks.k1.hostname = 192.168.254.1
a1.sinks.k1.port = 3306
a1.sinks.k1.databaseName = flume_db
a1.sinks.k1.tableName = log_data
a1.sinks.k1.user = liang
a1.sinks.k1.password = 123456
a1.sinks.k1.channel = c1

# Use a channel which buffers events in memory
a1.channels.c1.type = memory
a1.channels.c1.capacity = 1000
a1.channels.c1.transactionCapacity = 100



����flume agent al
bin/flume-ng agent --conf conf --conf-file conf/mysql_sink.conf --name a1 -Dflume.root.logger=INFO,console


�����
CREATE TABLE `log_data` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`context`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`time`  bigint(20) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT
;


����:
cd /home/rui/log/flumespool
echo "hello world" > test.log

����д��ɹ�!

