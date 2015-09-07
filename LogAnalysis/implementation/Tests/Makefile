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
#                                                                  
# Licensed Materials - Property of IBM                             
# Copyright IBM Corp. 2011                                         
# US Government Users Restricted Rights - Use, duplication or      
# disclosure restricted by GSA ADP Schedule Contract with          
# IBM Corp.                                                        
#                                                                  

.PHONY: all clean

all clean: 
	for dir in $(shell ls -d -1 */*/); do\
 		rm -f $$dir/Makefile ; \
		cp $$dir/../../Makefile.common $$dir/Makefile ; \
		make -C $$dir $@ || exit 1; \
	done
