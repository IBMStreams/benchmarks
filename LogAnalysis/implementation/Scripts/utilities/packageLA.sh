#!/bin/bash
# begin_generated_IBM_copyright_prolog                             
#                                                                  
# This is an automatically generated copyright prolog.             
# After initializing,  DO NOT MODIFY OR MOVE                       
# **************************************************************** 
# Licensed Materials - Property of IBM                             
# 5724-Y95                                                         
# (C) Copyright IBM Corp.  2015, 2015    All Rights Reserved.      
# US Government Users Restricted Rights - Use, duplication or      
# disclosure restricted by GSA ADP Schedule Contract with          
# IBM Corp.                                                        
#                                                                  
# end_generated_IBM_copyright_prolog                               
##############################################################################
# Copy files needed to run LA into a directory and zip them up into a tar file
# packageLA <source_releasse> <target_directory>
##############################################################################
source_release=$1
PERF_source="$source_release/repos/Distillery/Infrastructure/test/SystemTest/Performance"
LA_source="$PERF_source/Benchmarks/Applications/LogAnalysisBenchmark/implementation"
target_dir=$2

mkdir -p $target_dir/implementation
mkdir -p $target_dir/implementation/Data

cp -rf $LA_source/Scripts $target_dir/implementation
cp -rf $LA_source/Tests $target_dir/implementation
cp -rf $LA_source/Toolkit $target_dir/implementation
cp -rf $LA_source/Data/DataStore.txt $target_dir/implementation/Data
mkdir -p $target_dir/implementation/tools
cp -rf $PERF_source/ZKdata.pl $target_dir/implementation/tools
cp -rf $PERF_source/Tools/server_stats.pl $target_dir/implementation/tools
cp -rf $PERF_source/Tools/eth_stats.pl $target_dir/implementation/tools
cp -rf $PERF_source/Tools/process_stats.pl $target_dir/implementation/tools
cp -rf $PERF_source/Tools/disk_stats.pl $target_dir/implementation/tools
