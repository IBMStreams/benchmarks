cd ../
rm -rf ./output

sc -a -M com.ibm.streamsx.lr::LinearRoad  --data-directory=./data    --output-directory=./output/com.ibm.streamsx.lr.LinearRoad/Distributed/     -t /datadrive/workspace/com.ibm.streamsx.dps:/home/vendor/InfoSphere_Streams/4.0.1.0/toolkits/com.ibm.streamsx.messaging

sc -a -M com.ibm.streamsx.lr.history::HistoricalDataLoader  --data-directory=./data    --output-directory=./output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed     -t /datadrive/workspace/com.ibm.streamsx.dps:/home/vendor/InfoSphere_Streams/4.0.1.0/toolkits/com.ibm.streamsx.messaging

date

