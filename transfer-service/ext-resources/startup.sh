#!/bin/sh
clear
echo "Launching transfer Service."
java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/log/transfer -Xloggc:/var/log/transfer/GC_`date '+%y%m%d_%H%M%S'`.log -Xms128m -Xmx256m -DbaseDir=. -Dlog4j.configuration=file:"./config/properties/log4j.properties" -jar transfer-service-1.0.RELEASE.jar --server.port=8100
