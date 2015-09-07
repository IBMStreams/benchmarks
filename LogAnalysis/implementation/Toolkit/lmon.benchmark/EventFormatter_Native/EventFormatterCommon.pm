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
package EventFormatterCommon;

use strict;
use warnings;

sub verify($)
{

    my ($model) = @_;
    my $inputPort = $model->getInputPortAt(0);

    SPL::CodeGen::checkMinimalSchema ($inputPort,
        { name => "timeMillis", type => "uint64" },
        { name => "eventKind", type => "enum{Msg_Sent,Msg_Rcvd,Filler}" });

    SPL::CodeGen::checkMaximalSchema ($inputPort,
        { name => "timeMillis", type => "uint64" },
        { name => "sourceKind", type => "enum{A,B}" },
        { name => "sourceIndex", type => "uint8" },
        { name => "eventKind", type => "enum{Msg_Sent,Msg_Rcvd,Filler}" },
        { name => "eventId", type => "uint64" },
        { name => "deviceId", type => "uint32" },
        { name => "messageId", type => "uint64" },
        { name => "randomId", type => "uint32" },
        { name => "customerName", type=> "rstring" },
        { name => "timeStamp", type=> "int64"});
}

1;
