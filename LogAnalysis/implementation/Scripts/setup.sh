#!/bin/bash
# begin_generated_IBM_copyright_prolog                             
#                                                                  
# This is an automatically generated copyright prolog.             
# After initializing,  DO NOT MODIFY OR MOVE                       
# **************************************************************** 
# Licensed Materials - Property of IBM                             
# 5724-Y95                                                         
# (C) Copyright IBM Corp.  2014, 2014    All Rights Reserved.      
# US Government Users Restricted Rights - Use, duplication or      
# disclosure restricted by GSA ADP Schedule Contract with          
# IBM Corp.                                                        
#                                                                  
# end_generated_IBM_copyright_prolog                               



# Setup logAnalysis benchmark
function usage
{
  name=$(basename $0)
  echo "Set up an environment used for the LogAnalysis Benchmark"
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

RC=0

##########################################
# Build source
# TODO update it so we don't have to build
# everything
# Don't need to do the builds if using
# autorunPerf
##########################################
#${DIR}/testBuild  ALL $NUMSOURCES

##########################################
# Create/Start the instance
# Don't need to do the builds if using
# autorunPerf
##########################################
#${DIR}/instanceStart
#RC=$?

exit $RC
