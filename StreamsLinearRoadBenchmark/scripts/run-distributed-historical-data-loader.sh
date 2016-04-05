s=0	# Number of options to shift out
while getopts "d:i:h" options; do
    case $options in
    d) streams_domain_name=$OPTARG
       let s=s+2
       ;;
    i) streams_instance_name=$OPTARG
       let s=s+2
       ;;

    h | * ) echo "
Command line arguments
  -d STRING     streamsDomainName     (big_space)
  -i STRING     streamsInstanceName   (silver_stream)
 
  e-g:
  -d big_space
  -i silver_stream
"
        exit 1
        ;;
    esac
done
shift $s

# Validate the streams domain name  entered by the user.
if [ "$streams_domain_name" == "" ];
then
   echo "Missing or wrong streams domain name via the -d option."
   echo "Your streams domain name must be specified."
   echo ""
   echo "Get help using -h option."
   exit 1
fi

# Validate the streams instance name entered by the user.
if [ "$streams_instance_name" == "" ];
then
   echo "Missing or wrong streams instance name via the -i option."
   echo "Your streams instance name must be specified."
   echo ""
   echo "Get help using -h option."
   exit 1
fi


# Now, we can go ahead and start the instance (if not already running) and then
# start the dps test application.
echo "Ensuring that the Streams instance '$streams_instance_name' is running ..."
# If Streams instance is already started and running, it will display a warning on
# the stderr console. Let us suppress that warning by redirecting stderr and 
# stdout to a null device.
streamtool startinstance -d $streams_domain_name -i $streams_instance_name &> /dev/null

# Load the historical tolls data needed for a 10 minutes long 1xway linear road test run.
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive2/lr-input/Walmart-Test-Data/01xway/10m1x.tolls.dat

# Load the historical tolls data needed for a 30 minutes long 1xway linear road test run.
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive2/lr-input/Walmart-Test-Data/01xway/30m1x.tolls.dat

# Load the historical tolls data needed for a full 3 hours long 1xway linear road test run.
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive2/lr-input/Walmart-Test-Data/01xway/3h1x.tolls.dat

# Load the historical tolls data needed for a full 3 hours long 2xway linear road test run.
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive2/lr-input/Walmart-Test-Data/02xway/3h2x.tolls.dat

# Load the historical tolls data needed for a full 3 hours long 5xway linear road test run.
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive2/lr-input/Walmart-Test-Data/05xway/3h5x.tolls.dat

# Load the historical tolls data needed for a full 3 hours long 10xway linear road test run.
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive2/lr-input/Walmart-Test-Data/10xway/3h10x.tolls.dat

# Load the historical tolls data needed for a full 3 hours long 15xway linear road test run. [Load from the 50x data set by filtering beyond 15 expressways.]
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive2/lr-input/Walmart-Test-Data/50xway/3h50x.tolls.dat

# Load the historical tolls data needed for a full 3 hours long 20xway linear road test run.
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive2/lr-input/Walmart-Test-Data/20xway/3h20x.tolls.dat

# Load the historical tolls data needed for a full 3 hours long 25xway linear road test run. [Load from the 50x data set by filtering beyond 25 expressways.]
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive2/lr-input/Walmart-Test-Data/50xway/3h50x.tolls.dat

# Load the historical tolls data needed for a full 3 hours long 50xway linear road test run.
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive2/lr-input/Walmart-Test-Data/50xway/3h50x.tolls.dat

# Load the historical tolls data needed for a full 3 hours long 100xway linear road test run.
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive2/lr-input/Walmart-Test-Data/100xway/3h100x.tolls.dat

# Load the historical tolls data needed for a full 3 hours long 150xway linear road test run.
# streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive3/lr-input/Walmart-Test-Data/150xway/3h150x.tolls.dat

# Load the historical tolls data needed for a full 3 hours long 200xway linear road test run.
streamtool submitjob  -d $streams_domain_name -i $streams_instance_name  ../output/com.ibm.streamsx.lr.history.HistoricalDataLoader/Distributed/com.ibm.streamsx.lr.history.HistoricalDataLoader.sab -P TollHistoryFile=/datadrive3/lr-input/Walmart-Test-Data/200xway/3h200x.tolls.dat

echo "****** You can check the results from this run inside the PE stdouterr log files in the Streams application log directory.  ******"
date

