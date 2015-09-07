#!/bin/bash
# begin_generated_IBM_copyright_prolog                             
#                                                                  
# This is an automatically generated copyright prolog.             
# After initializing,  DO NOT MODIFY OR MOVE                       
# **************************************************************** 
# Licensed Materials - Property of IBM                             
# 5724-Y95                                                         
# (C) Copyright IBM Corp.  2014, 2015    All Rights Reserved.      
# US Government Users Restricted Rights - Use, duplication or      
# disclosure restricted by GSA ADP Schedule Contract with          
# IBM Corp.                                                        
#                                                                  
# end_generated_IBM_copyright_prolog                               



# Run logAnalysis benchmark
function usage
{
  name=$(basename $0)
  echo "Run LogAnalysis Benchmark"
  echo ""
  echo "usage:  $name [<config-file>]"
  echo ""
  echo "  - <config-file> : Location of configuration file to use for running the tests.  If specifying a relative path, it should be relative to the location of this script.  By default, it will use config.sh found in same directory as this script."
}


# main
DIR=$(dirname $0)
. ${DIR}/common.sh

##########################################
# Load the config file
##########################################
loadConfig $*

##########################################
# Log environment variables
##########################################
$DIR/internal/logMsg "LA_PRE_SUBMITJOB_COMMAND=$LA_PRE_SUBMITJOB_COMMAND"
$DIR/internal/logMsg "LA_ADDITIONAL_MONITORING_COMMAND=$LA_ADDITIONAL_MONITORING_COMMAND"
$DIR/internal/logMsg "OPERFPROFILE_COUNTERSPEC=$OPERFPROFILE_COUNTERSPEC"
STREAM_VER=`which streamtool`
$DIR/internal/logMsg "Streams build location = $STREAM_VER"

##########################################
# Run the test using the test driver
# script specified in the config file
##########################################
if [ ! -f $TESTDRIVER ] ; then
  echo "Test driver $TESTDRIVER does not exist."
  usage
  exit 1
fi
$TESTDRIVER 
RC=$?

exit $RC
