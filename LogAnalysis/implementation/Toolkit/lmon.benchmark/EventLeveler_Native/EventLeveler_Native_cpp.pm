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

package EventLeveler_Native_cpp;
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
   print "\n";
     use EventLevelerCommon;
     EventLevelerCommon::verify($model);
     my $maxTimeDiff = $model->getParameterByName("maxTimeDiff")
         ->getValueAt(0)->getCppExpression();
   print "\n";
   print "\n";
   SPL::CodeGen::implementationPrologue($model);
   print "\n";
   print "\n";
   print '#define SPECIAL_BACKOFF_SPINNER 0    // default is to use back off spinner supplied by Streams', "\n";
   print '#if SPECIAL_BACKOFF_SPINNER == 0', "\n";
   print "\n";
   print '#include <SPL/Toolkit/BackoffSpinner.h>', "\n";
   print "\n";
   print '#else', "\n";
   print "\n";
   print 'class BackoffSpinner {', "\n";
   print 'public:', "\n";
   print '    BackoffSpinner() : count_(1) {}', "\n";
   print '    void wait() {', "\n";
   print '      if(count_ <= (uint32_t) loops) {', "\n";
   print '            pause(count_);', "\n";
   print '            count_ *= 2; ', "\n";
   print '        } else {', "\n";
   print '            pthread_yield();', "\n";
   print '        }', "\n";
   print '    }', "\n";
   print '    static void pause(uint32_t count) {', "\n";
   print '        for (uint32_t i=0; i< count; i++) {', "\n";
   print '            __asm__ __volatile__("pause;");', "\n";
   print '        }', "\n";
   print '    }', "\n";
   print "\n";
   print 'private:', "\n";
   print '    uint32_t count_;', "\n";
   print '    static const uint32_t loops = 16;', "\n";
   print '};', "\n";
   print "\n";
   print '#endif', "\n";
   print "\n";
   print '// Constructor', "\n";
   print 'MY_OPERATOR_SCOPE::MY_OPERATOR::MY_OPERATOR()', "\n";
   print '    : port0LastTimeMillis_(0),', "\n";
   print '      port1LastTimeMillis_(0)', "\n";
   print '{', "\n";
   print '    maxTimeDiff_ = ';
   print $maxTimeDiff;
   print ';', "\n";
   print '}', "\n";
   print "\n";
   print '// Tuple processing for non-mutating ports', "\n";
   print 'void MY_OPERATOR_SCOPE::MY_OPERATOR::process(Tuple const & tuple, uint32_t port)', "\n";
   print '{ ', "\n";
   print '    IPort0Type const & ituple = static_cast<IPort0Type const &>(tuple);', "\n";
   print '    if(port==0) {', "\n";
   print '        port0LastTimeMillis_ = ituple.get_timeMillis();', "\n";
   print '        BackoffSpinner spinner;', "\n";
   print '        while(!getPE().getShutdownRequested() &&', "\n";
   print '              (port1LastTimeMillis_ == 0 ||', "\n";
   print '               (port0LastTimeMillis_>port1LastTimeMillis_ &&', "\n";
   print '                port0LastTimeMillis_-port1LastTimeMillis_ > maxTimeDiff_)))', "\n";
   print '            spinner.wait();', "\n";
   print '    } else {', "\n";
   print '        port1LastTimeMillis_ = ituple.get_timeMillis();', "\n";
   print '        BackoffSpinner spinner;', "\n";
   print '        while(!getPE().getShutdownRequested() &&', "\n";
   print '              (port0LastTimeMillis_ == 0 ||', "\n";
   print '               (port1LastTimeMillis_>port0LastTimeMillis_ &&', "\n";
   print '                port1LastTimeMillis_-port0LastTimeMillis_ > maxTimeDiff_)))', "\n";
   print '            spinner.wait();', "\n";
   print '    }', "\n";
   print '    submit(tuple, port);', "\n";
   print '}', "\n";
   print "\n";
   print '// Punctuation processing', "\n";
   print 'void MY_OPERATOR_SCOPE::MY_OPERATOR::process(Punctuation const & punct, uint32_t port)', "\n";
   print '{', "\n";
   print '    if(punct==Punctuation::WindowMarker) ', "\n";
   print '        submit(punct, port);', "\n";
   print '}', "\n";
   print "\n";
   SPL::CodeGen::implementationEpilogue($model);
   print "\n";
   print "\n";
   CORE::exit $SPL::CodeGen::USER_ERROR if ($SPL::CodeGen::sawError);
}
1;
