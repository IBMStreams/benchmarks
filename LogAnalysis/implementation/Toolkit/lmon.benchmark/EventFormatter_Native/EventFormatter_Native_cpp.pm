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

package EventFormatter_Native_cpp;
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
   print '#include <iostream>', "\n";
   print "\n";
     use EventFormatterCommon;
     EventFormatterCommon::verify($model);
     my $file = $model->getParameterByName("file")->getValueAt(0)->getCppExpression();
     my %tags;
     $tags{eventId} = "E_ID";
     $tags{deviceId} = "D_ID";
     $tags{messageId} = "M_ID";
     $tags{randomId} = "R";
     $tags{customerName} = "C_NAME";
   print "\n";
   print "\n";
   SPL::CodeGen::implementationPrologue($model);
   print "\n";
   print "\n";
   print 'using namespace std;', "\n";
   print "\n";
   print 'static string getError()', "\n";
   print '{', "\n";
   print '    char buf[1024];', "\n";
   print '    strerror_r(errno, buf, sizeof(buf));', "\n";
   print '    return buf;', "\n";
   print '}', "\n";
   print "\n";
   print '#define getLoc() static_cast<ostringstream&>(ostringstream() << __FILE__ << ":" << __LINE__).str()', "\n";
   print "\n";
   print 'MY_OPERATOR_SCOPE::MY_OPERATOR::MY_OPERATOR()', "\n";
   print '{', "\n";
   print "\n";
   print '    out_.open(';
   print $file;
   print '.c_str());', "\n";
   print '    if(!out_)', "\n";
   print '        throw SPLRuntimeException(getLoc(), getError());    ', "\n";
   print '}', "\n";
   print "\n";
   print '// Tuple procressing for mutating ports ', "\n";
   print 'void MY_OPERATOR_SCOPE::MY_OPERATOR::process(Tuple const & tuple, uint32_t port)', "\n";
   print '{', "\n";
   print '    AutoPortMutex apm(mutex_, *this);', "\n";
   print "\n";
   print '    IPort0Type const & ituple = static_cast<IPort0Type const &>(tuple);', "\n";
   print '    ', "\n";
   print '    uint64 timeMillis = ituple.get_timeMillis();', "\n";
   print '    time_t timeSecs = timeMillis / 1000;', "\n";
   print '    uint32 timeMillisRemains = timeMillis % 1000;', "\n";
   print "\n";
   print '    struct tm brokenDownTime;', "\n";
   print '    struct tm * tres = gmtime_r(&timeSecs, &brokenDownTime);', "\n";
   print '    assert(tres!=NULL);', "\n";
   print '    ', "\n";
   print '    size_t res = strftime(buffer_, sizeof(buffer_), "%m/%d %H:%M:%S.", &brokenDownTime);', "\n";
   print '    assert(res!=0);', "\n";
   print '    out_ << buffer_;', "\n";
   print '    out_ << timeMillisRemains;', "\n";
   print '    ';
   for my $attr (@{$model->getInputPortAt(0)->getAttributes()}){ 
           my $name = $attr->getName();
           if($name eq "sourceKind")
           {
       
   print "\n";
   print '          out_ << " " << ituple.get_sourceKind().getValue();', "\n";
   print '    ';
           }
           elsif($name eq "sourceIndex")
           { 
       
   print "\n";
   print '           out_ << ituple.get_sourceIndex();', "\n";
   print '    ';
     }
         }
       
   print ' ', "\n";
   print '    out_ << " : ";', "\n";
   print "\n";
   print '    out_ << "EVENT=" << ituple.get_eventKind().getValue();', "\n";
   print '    if (ituple.get_eventKind() != IPort0Type::eventKind_type::Filler) {', "\n";
   print '    ';
   for my $attr (@{$model->getInputPortAt(0)->getAttributes()}){ 
           my $name = $attr->getName();
           my $tag = $tags{$name};
           next unless($tag);
       
   print "\n";
   print '        out_ << ", ';
   print $tag;
   print '=" << ituple.get_';
   print $name;
   print '();', "\n";
   print '    ';
   }
   print "\n";
   print '    }    ', "\n";
   print '    out_ << "\\n";', "\n";
   print '}', "\n";
   print "\n";
   print 'void MY_OPERATOR_SCOPE::MY_OPERATOR::process(Punctuation const & punct, uint32_t port)', "\n";
   print '{', "\n";
   print '    if(punct==Punctuation::FinalMarker)', "\n";
   print '        out_.flush();', "\n";
   print '}', "\n";
   print '    ', "\n";
   print "\n";
   print "\n";
   SPL::CodeGen::implementationEpilogue($model);
   print "\n";
   print "\n";
   CORE::exit $SPL::CodeGen::USER_ERROR if ($SPL::CodeGen::sawError);
}
1;
