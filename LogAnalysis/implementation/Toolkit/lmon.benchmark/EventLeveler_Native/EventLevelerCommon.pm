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
package EventLevelerCommon;

use strict;
use warnings;

sub verify($) 
{

    my ($model) = @_;
    my $inputPort = $model->getInputPortAt(0); 

    SPL::CodeGen::checkMinimalSchema ($inputPort, 
        { name => "timeMillis", type => "uint64" });
}

1;
