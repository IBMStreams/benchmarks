#!/bin/bash

date

# Stop the Redis servers on all the 5 nodes.
echo Stopping redis on vendor1 ...
ssh vendor@vendor1 'nohup killall redis-server < /dev/null > std.out 2> std.err &'
echo Stopping redis on vendor2 ...
ssh vendor@vendor2 'nohup killall redis-server < /dev/null > std.out 2> std.err &'
echo Stopping redis on vendor3 ...
ssh vendor@vendor3 'nohup killall redis-server < /dev/null > std.out 2> std.err &'
echo Stopping redis on vendor4 ...
ssh vendor@vendor4 'nohup killall redis-server < /dev/null > std.out 2> std.err &'
echo Stopping redis on vendor5 ...
ssh vendor@vendor5 'nohup killall redis-server < /dev/null > std.out 2> std.err &'

# Wait for 5 seconds
echo Waiting for 5 seconds
sleep 5

# Start the Redis servers on all the 5 nodes.
echo Starting two instances of redis on vendor1 ...
ssh vendor@vendor1 'nohup /datadrive/redis-2.8.24/src/redis-server /datadrive/redis-2.8.24/redis.conf.1 < /dev/null > std.out 2> std.err &'
ssh vendor@vendor1 'nohup /datadrive/redis-2.8.24/src/redis-server /datadrive/redis-2.8.24/redis.conf.2 < /dev/null > std.out 2> std.err &'

echo Starting two instances of redis on vendor2 ...
ssh vendor@vendor2 'nohup /datadrive/redis-2.8.24/src/redis-server /datadrive/redis-2.8.24/redis.conf.1 < /dev/null > std.out 2> std.err &'
ssh vendor@vendor2 'nohup /datadrive/redis-2.8.24/src/redis-server /datadrive/redis-2.8.24/redis.conf.2 < /dev/null > std.out 2> std.err &'

echo Starting two instances of redis on vendor3 ...
ssh vendor@vendor3 'nohup /datadrive/redis-2.8.24/src/redis-server /datadrive/redis-2.8.24/redis.conf.1 < /dev/null > std.out 2> std.err &'
ssh vendor@vendor3 'nohup /datadrive/redis-2.8.24/src/redis-server /datadrive/redis-2.8.24/redis.conf.2 < /dev/null > std.out 2> std.err &'

echo Starting two instances of redis on vendor4 ...
ssh vendor@vendor4 'nohup /datadrive/redis-2.8.24/src/redis-server /datadrive/redis-2.8.24/redis.conf.1 < /dev/null > std.out 2> std.err &'
ssh vendor@vendor4 'nohup /datadrive/redis-2.8.24/src/redis-server /datadrive/redis-2.8.24/redis.conf.2 < /dev/null > std.out 2> std.err &'

echo Starting two instances of redis on vendor5 ...
ssh vendor@vendor5 'nohup /datadrive/redis-2.8.24/src/redis-server /datadrive/redis-2.8.24/redis.conf.1 < /dev/null > std.out 2> std.err &'
ssh vendor@vendor5 'nohup /datadrive/redis-2.8.24/src/redis-server /datadrive/redis-2.8.24/redis.conf.2 < /dev/null > std.out 2> std.err &'

date
