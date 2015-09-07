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
#                                                                  

use strict;
use warnings;

use POSIX qw(strftime);
use Time::HiRes qw(gettimeofday usleep);

use Getopt::Std;
use File::Basename;

###############################################
## Constance mapping stat and statm files
###############################################
our $CPU_USER=13;
our $CPU_KERNEL=14;
our $MEM_VIRTUAL=0;
our $MEM_REAL=1;

my $sname = basename($0);
my $help="usage: $sname -C -D -M -o <output directory> -s <stopfile>
	-C collect CPU stats
	-D embed debug information in output file
	-i interval (default 10)
	-M collect memory stats
	-n comma separated list of names -  use command name if not specifed
	-o output directory (default /tmp)
	-p comma separated list of process to monitor
	-s stopfile (default running concatenated with epoch";

###############################################
sub usage() {

  print("$help\n");
    return;

    }

sub main() {

	my ($time, $us) = gettimeofday();
	my ($sec, $min, $hour, $day, $month, $year) = localtime $time;
	my $timestring = sprintf "%04d%02d%02d%02d%02d%02d", $year, $month, $day, $hour, $min, $sec;
	my %opts;
	getopts('CDi:Mn:o:p:s:', \%opts) or die("$help\n");
	my $outdir = "/tmp";
	my $stopfile = "running_$timestring";
	my $interval = 10;
	my $CPU = 0;
	my $DEBUG = 0;
	my $MEMORY = 0;
	my $processes_p="";
	my $process_names_p="";

	my @cpu_in_lines;
	my @mem_in_lines;
	my $in_line;

	if ($opts{'C'}) {
		$CPU = 1;
	}
	if ($opts{'D'}) {
		$DEBUG = 1;
	}
	if ($opts{'i'}) {
		$interval = $opts{'i'};
	}	
	if ($opts{'M'}) {
		$MEMORY = 1;
	}
	if ($opts{'n'}) {
		$process_names_p = $opts{'n'};
	}
	if ($opts{'o'}) {
		$outdir = $opts{'o'};
		system("mkdir -p $outdir");
	}
	if ($opts{'p'}) {
		$processes_p = $opts{'p'};
	}
	else {
		die("$help\n");
	}
	if ($opts{'s'}) {
		$stopfile = $opts{'s'};
	}

	if (!($MEMORY || $CPU)) {
		die("must collect memory or cpu stats!!!\n$help");
	}

	my $pagesizeK = `getconf PAGE_SIZE`/1024;
	print("pagesize is $pagesizeK K\n");
	my $host=`hostname`;
	chomp $host;

	my @processes = split /,/,$processes_p;
	my @process_names = split /,/,$process_names_p;
	my $process;
	my $process_name;
	my $meminfo_cmd = "cat";
	my $cpuinfo_cmd = "cat";
	my $i;
	for ($i=0; $i<=$#processes; $i++) {
		$process = $processes[$i];
		my $cpufile = "/proc/$process/stat";
		my $memfile = "/proc/$process/statm";
		if (! -e $cpufile || ! -e $memfile) {
			die("file for $process does not exist!\n$help\n");
		}
		$meminfo_cmd = "$meminfo_cmd $memfile";
		$cpuinfo_cmd = "$cpuinfo_cmd $cpufile";
	}
	my @cpu_user;
	my @cpu_kernel;
	my $meminfo;
	my $cpuinfo;
	$meminfo = `$meminfo_cmd`;
	my $retcode = $_;
	if ($retcode) {
		die("$meminfo_cmd failed with $retcode\n");
	}
	$cpuinfo = `$cpuinfo_cmd`;
	$retcode = $?;
	if ($retcode) {
		die("$cpuinfo_cmd failed with $retcode\n");
	}
	my $process_names_given = $#process_names;
	@cpu_in_lines = split /\n/,$cpuinfo;
	for ($i = 0; $i<=$#processes; $i++) {
		$in_line = $cpu_in_lines[$i];
		my @parse_line = split / /,$in_line;
		$cpu_user[$i]=$parse_line[$CPU_USER];
		$cpu_kernel[$i]=$parse_line[$CPU_KERNEL];
		if ($in_line =~ m/.+\((.+)\)/) {
			$process_name = $1;
			if ($i > $process_names_given) {
				$process_names[$i] = $process_name;
			} 
		}
		else {
			die("process name not found in line: $in_line\n");
		}
	}

	($time, $us) = gettimeofday();
	my ($lasttime, $lastus) = ($time, $us);
	($sec, $min, $hour, $day, $month, $year) = localtime $time;
	$month = $month + 1;
	$year = $year + 1900;
	my $short_host = `hostname -s`;
	chomp($short_host);
	my $fbase = sprintf "process_%s_%s", $short_host, $timestring;
	if ($DEBUG) {
		$fbase = "DEBUG_$fbase";
	}
	my $filename = "$outdir/$fbase.csv";
	print("starting stats collection on system $host to file $filename for processes $processes_p on $interval second interval - to end do:\nrm $stopfile\n");
	system("touch $stopfile");
	open (OUTFILE, ">$filename");
	my $header_line0 = ",,,,,";
	my $header_line1 = ",,,,";
	my $header_line2 = "host, date, time, epoch, time us";
	for ($i = 0; $i<=$#process_names; $i++) {
		$header_line0 = "$header_line0 $processes[$i]-$process_names[$i]";
		if ($MEMORY) {
			$header_line0 = "$header_line0,,";
			$header_line1 = "$header_line1,Memory,";
			$header_line2 = "$header_line2,Real,Virtual";
		}
		if ($CPU) {
			$header_line0 = "$header_line0,,";
			$header_line1 = "$header_line1,CPU,";
			$header_line2 = "$header_line2,User,Kernel";
		}
	}
	print OUTFILE "$header_line0\n$header_line1\n$header_line2\n";

	($time, $us) = gettimeofday();
	my $sleep = ($interval - ($time % $interval)) * 1000000 - $us;
#	print "time $time.$us sleep $sleep\n";
	usleep($sleep);

	while (-e $stopfile) {
		($time, $us) = gettimeofday();
		if ($MEMORY) {
			$meminfo = `$meminfo_cmd`;
			my $retcode = $_;
			if ($retcode) {
				system("rm $stopfile");
				close(OUTFILE);
				die("$meminfo_cmd failed with $retcode\n");
			}
		}
		if ($CPU) {
			$cpuinfo = `$cpuinfo_cmd`;
			$retcode = $?;
			if ($retcode) {
				system("rm $stopfile");
				close(OUTFILE);
				die("$cpuinfo_cmd failed with $retcode\n");
			}
		}
		my $hundredths;
		if ( $us > $lastus) {
			use integer;
			$hundredths = ($time-$lasttime)*100 + ($us-$lastus)/10000;
		}
		else {
			use integer;
			$hundredths = ($time-$lasttime-1)*100 + (1000000+$us-$lastus)/10000;
		}
		($lasttime, $lastus) = ($time, $us);
		($sec, $min, $hour, $day, $month, $year) = localtime $time;
		$month = $month + 1;
		$year = $year + 1900;
		my $line = sprintf '%s,  %04d/%02d/%02d, %02d:%02d:%02d, %12d, %06d', $host, $year, $month, $day, $hour, $min, $sec, $time, $us;
		my $debug_line;
		if ($DEBUG) {
			$debug_line = "DEBUG, $time, $us, $hundredths";
		}
		my $calc_val=0;
		my $temp_val=0;
		if ($MEMORY) {
			@mem_in_lines = split /\n/, $meminfo;
		}
		if ($CPU) {
			@cpu_in_lines = split /\n/,$cpuinfo;
		}
		for ($i=0;$i<=$#processes;$i++) {
			if ($MEMORY) {
				$in_line=$mem_in_lines[$i];
				my @parse_line = split / /,$in_line;
				$calc_val = $parse_line[$MEM_REAL] * $pagesizeK;
				$line = "$line, $calc_val";
				$calc_val = $parse_line[$MEM_VIRTUAL] * $pagesizeK;
				$line = "$line, $calc_val";
			}
			if ($CPU) {
				$in_line=$cpu_in_lines[$i];
				my @parse_line = split / /,$in_line;
				$temp_val=$parse_line[$CPU_USER];
				$calc_val=$temp_val-$cpu_user[$i];
				$line="$line, $calc_val";
				$cpu_user[$i]=$temp_val;
				$temp_val=$parse_line[$CPU_KERNEL];
				$calc_val=$temp_val-$cpu_kernel[$i];
				$line="$line, $calc_val";
				$cpu_kernel[$i]=$temp_val;
			}
			if ($DEBUG) {
				$debug_line = "$debug_line, ";
			}
		}
		print OUTFILE "$line\n";
		if ($DEBUG) {
			print OUTFILE "$debug_line\n";
		}
		($time, $us) = gettimeofday();
		$sleep = ($interval - ($time % $interval)) * 1000000 - $us;
		usleep($sleep);
	}
	close(OUTFILE);
	return(0);
}

my $rc = main();
print "$sname complete\n";
exit($rc);

