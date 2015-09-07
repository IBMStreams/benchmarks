#!/usr/bin/perl
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

use strict;

if ($ARGV[0] eq 'includePath') {
  print "../../impl/include\n";
}
elsif ($ARGV[0] eq 'libPath') {
}
elsif ($ARGV[0] eq 'lib') {
  if (defined($ENV{PINNUMA})) {
    if (1 == $ENV{PINNUMA}) {
      print "numa\n";
    }
  }
}
exit 0;
