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



# Cleanup logAnalysis benchmark
function usage
{
  name=$(basename $0)
  echo "Cleanup up an environment used for the LogAnalysis Benchmark"
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
# Create/Start the instance
##########################################
${DIR}/instanceStop
RC=$?

##########################################
# Cleanup generated code
##########################################
${DIR}/testClean ALL
rm -rf $DIR/../Tests/*/*/output.*

exit $RC

