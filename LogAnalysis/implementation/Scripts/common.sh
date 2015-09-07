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

# Common functionality that can be shared by different scripts.

function loadConfig {
  CONFIG=${DIR}/config.sh
  if [[ $# -ge 1 ]] ; then
    CONFIG=$1
  fi
  if [ ! -f $CONFIG ] ; then
    echo "Config file $CONFIG does not exist."
    usage
    exit 1
  else
    . $CONFIG
  fi

  # source streamsprofile.sh
  if [ ! -f ${STREAMS_INSTALL}/bin/streamsprofile.sh ] ; then
    echo "STREAMS_INSTALL is not set to an exisiting Streams installation: $STREAMS_INSTALL"
    usage
    exit 1
  else
    . ${STREAMS_INSTALL}/bin/streamsprofile.sh
  fi

}


