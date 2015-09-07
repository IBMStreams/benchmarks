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

my $sname = basename($0);
my $help="usage: $sname -C -D -M -o <output directory> -P -s <stopfile>
	-C collect CPU stats
	-D embed debug information in output file
	-i interval (default 10)
	-M collect memory stats
	-o output directory (default /tmp)
	-P Collect processer information (model dependent)
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
	getopts('CDi:Mo:Ps:z:', \%opts) or die("$help\n");
	my $outdir = "/tmp";
	my $stopfile = "running_$timestring";
	my $interval = 10;
	my $CPU = 0;
	my $DEBUG = 0;
	my $MEMORY = 0;
	my $PROCESSOR = 0;
	my $ZOOKEEPER = 0;
	my $zk_config_file;
	my $zk_config;

	my @in_lines;
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
	if ($opts{'o'}) {
		$outdir = $opts{'o'};
	}
	if ($opts{'P'}) {
		$PROCESSOR = 1;
	}
	if ($opts{'s'}) {
		$stopfile = $opts{'s'};
	}
	if ($opts{'z'}) {
		$zk_config_file = $opts{'z'};
		$ZOOKEEPER = 1;

	}

	my $host=`hostname`;
	chomp $host;
	print("starting memory collection on system $host on $interval second interval - to end do:\n");
	print("rm $stopfile\n");
	system("touch $stopfile");

	($time, $us) = gettimeofday();
	my $meminfo = `numactl --hardware`;
	my $cpuinfo = `cat /proc/stat`;
	my ($lasttime, $lastus) = ($time, $us);
	($sec, $min, $hour, $day, $month, $year) = localtime $time;
	$month = $month + 1;
	$year = $year + 1900;
	my $short_host = `hostname -s`;
	chomp($short_host);
	my $fbase = sprintf "server_%s_%s", $short_host, $timestring;
	if ($DEBUG) {
		$fbase = "DEBUG_$fbase";
	}
	my $filename = "$outdir/$fbase.csv";
	open (OUTFILE, ">$filename");
	print "writing to $filename\n";
	my $header_line = "host, date, time, epoch, time us";
	if ($PROCESSOR) {
		$header_line = "$header_line,Avg_MHz";
	}
	if ($MEMORY) {
		@in_lines = split /\n/, $meminfo;
		foreach $in_line (@in_lines) {
			if ( $in_line =~ m/node (\d+) size:/ ) {
				$header_line = "$header_line,node $1 size";
			}
			elsif ( $in_line =~ m/node (\d+) free:/ ) {
				$header_line = "$header_line,node $1 free";
			}
		}
	}
	my @cpuN_idle;
	my @cpuN_total;
	if ($CPU) {
#		format of /proc/stat: user,nice,system,idle,iowait,irq,softirq
		@in_lines = split /\n/,$cpuinfo;
		foreach $in_line (@in_lines) {
			if ( $in_line =~ m/^cpu(\d+) (\d+) (\d+) (\d+) (\d+) (\d+) (\d+) (\d+)/ ) {
				$header_line = "$header_line, $1";
				$cpuN_idle[$1] = $5;
				$cpuN_total[$1] = $2+$3+$4+$5+$6+$7;
			}
		}
		$header_line = "$header_line, all";
	}
	print OUTFILE "$header_line\n";

	($time, $us) = gettimeofday();
	my $sleep = ($interval - ($time % $interval)) * 1000000 - $us;
#	print "time $time.$us sleep $sleep\n";
	usleep($sleep);

	while (-e $stopfile) {
		($time, $us) = gettimeofday();
		if ($MEMORY) {
			$meminfo = `numactl --hardware`;
		}
		if ($CPU) {
			$cpuinfo = `cat /proc/stat`;
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
			my $debug_line = "DEBUG, $time, $us, $hundredths";
		}
		my $all_total = 0;
		my $all_idle = 0;
		if ($PROCESSOR) {
			my $processor_info = `sudo turbostat -S echo ""`;
			print "processor info = $processor_info\n";
		}
		if ($MEMORY) {
			@in_lines = split /\n/, $meminfo;
			foreach my $in_line (@in_lines) {
				if ( $in_line =~ m/node (\d+) size: (\d+) MB/ ) {
					$line = "$line, $2";
				}
				if ( $in_line =~ m/node (\d+) free: (\d+) MB/ ) {
					$line = "$line, $2";
				}
			}
		}
		if ($CPU) {
			@in_lines = split /\n/,$cpuinfo;
			my $calc_val=0;
			foreach $in_line (@in_lines) {
				if ( $in_line =~ m/^cpu(\d+) (\d+) (\d+) (\d+) (\d+) (\d+) (\d+) (\d+)/ ) {
					use integer;
					my $idle_delta = ($5-$cpuN_idle[$1]);
					my $total_val = $2+$3+$4+$5+$6+$7;
					my $total_delta = ($total_val-$cpuN_total[$1]);
					$calc_val = 100-100*$idle_delta/$total_delta;
					$line = "$line, $calc_val";
					if ($DEBUG) {
						$debug_line = "$debug_line, $idle_delta, $total_delta";
					}
					$all_idle += $idle_delta;
					$all_total += $total_delta;
					$cpuN_idle[$1] = $5;
					$cpuN_total[$1] = $total_val;
				}
			}
			$calc_val = 100 - int(100*$all_idle/$all_total);
			$line = "$line, $calc_val";
			if ($DEBUG) {
				$debug_line = "$debug_line, $all_idle, $all_total";
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

