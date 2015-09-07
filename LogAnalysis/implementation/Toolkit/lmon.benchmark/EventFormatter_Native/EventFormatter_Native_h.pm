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

package EventFormatter_Native_h;
use strict; use Cwd 'realpath';  use File::Basename;  use lib dirname(__FILE__);  use SPL::Operator::Instance::OperatorInstance; use SPL::Operator::Instance::Context; use SPL::Operator::Instance::Expression; use SPL::Operator::Instance::ExpressionTree; use SPL::Operator::Instance::ExpressionTreeVisitor; use SPL::Operator::Instance::ExpressionTreeCppGenVisitor; use SPL::Operator::Instance::InputAttribute; use SPL::Operator::Instance::InputPort; use SPL::Operator::Instance::OutputAttribute; use SPL::Operator::Instance::OutputPort; use SPL::Operator::Instance::Parameter; use SPL::Operator::Instance::StateVariable; use SPL::Operator::Instance::Window; 
sub main::generate($$) {
   my ($xml, $signature) = @_;  
   print "// $$signature\n";
   my $model = SPL::Operator::Instance::OperatorInstance->new($$xml);
   unshift @INC, dirname ($model->getContext()->getOperatorDirectory()) . "/../impl/nl/include";
   $SPL::CodeGenHelper::verboseMode = $model->getContext()->isVerboseModeOn();
   print '/*                                                                  */', "\n";
   print '/* Licensed Materials - Property of IBM                             */', "\n";
   print '/* Copyright IBM Corp. 2011                                         */', "\n";
   print '/* US Government Users Restricted Rights - Use, duplication or      */', "\n";
   print '/* disclosure restricted by GSA ADP Schedule Contract with          */', "\n";
   print '/* IBM Corp.                                                        */', "\n";
   print '/*                                                                  */', "\n";
   print "\n";
   print '/* Additional includes go here */', "\n";
   print '#include <fstream>', "\n";
   print "\n";
     use EventFormatterCommon;
     EventFormatterCommon::verify($model);
   print "\n";
   print "\n";
   SPL::CodeGen::headerPrologue($model);
   print "\n";
   print "\n";
   print 'class MY_OPERATOR : public MY_BASE_OPERATOR ', "\n";
   print '{', "\n";
   print 'public:', "\n";
   print '  // Constructor', "\n";
   print '  MY_OPERATOR();', "\n";
   print "\n";
   print '  // Destructor', "\n";
   print '  virtual ~MY_OPERATOR() {}', "\n";
   print "\n";
   print '  // Tuple procressing for non-mutating ports ', "\n";
   print '  void process(Tuple const & tuple, uint32_t port);', "\n";
   print "\n";
   print '  // Punctuation procressing ', "\n";
   print '  void process(Punctuation const & tuple, uint32_t port);', "\n";
   print '    ', "\n";
   print 'private:', "\n";
   print '  // Members', "\n";
   print '  std::ofstream out_;', "\n";
   print '  char buffer_[2048];', "\n";
   print '  std::map<std::string, std::string> tags_; ', "\n";
   print "\n";
   print '  Mutex mutex_;', "\n";
   print '}; ', "\n";
   print "\n";
   SPL::CodeGen::headerEpilogue($model);
   print "\n";
   print "\n";
   CORE::exit $SPL::CodeGen::USER_ERROR if ($SPL::CodeGen::sawError);
}
1;
