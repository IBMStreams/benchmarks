##README --  IBMStreams/benchmarks/StreamsLinearRoadBenchmark

Linear Road Benchmark is a publicly available specification developed by these academic institutions: MIT, Brown, Brandeis and Stanford.

http://www.cs.brandeis.edu/~linearroad/linear-road.pdf


For more general details about the Linear Road benchmark, you can visit the project page maintained by Brandeis University.

http://www.cs.brandeis.edu/~linearroad/index.html


In 2016, a team in IBM implemented the Linear Road benchmark using the IBM Streams product as part of a multi-vendor bakeoff done at a large U.S based enterprise customer. This particular IBM implementation performed and scaled very well on a given set of hardware infrstructure constraints imposed by that customer. Experiments were conducted for many different expressways (1, 2, 5, 10, 20, 25, 50, 100, 150 and 200) to ensure that the Linear Road application built using the IBM Streams product worked reliably for the steadily increasing real time and historical analysis data load. In this directory, the full implenentation for the 200 expressway version is included. In order to run this application, start reading the following file first.

StreamsLinearRoadBenchmark/doc/instructions-to-run-linear-road-test.txt

In order to run this application, one must generate the historical toll data and the linear road expressway traffic data beforehand. There is a set of custom built tools available that can be downloaded and used from the following web site (courtesy of Wal-Mart Stores, Inc.)

https://github.com/walmart/linearroad




