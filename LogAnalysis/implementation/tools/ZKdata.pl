#!/usr/bin/perl
# begin_generated_IBM_copyright_prolog                             
#                                                                  
# This is an automatically generated copyright prolog.             
# After initializing,  DO NOT MODIFY OR MOVE                       
# **************************************************************** 
# Licensed Materials - Property of IBM                             
# 5724-Y95                                                         
# (C) Copyright IBM Corp.  2014, 2015    All Rights Reserved.      
# US Government Users Restricted Rights - Use, duplication or      
# disclosure restricted by GSA ADP Schedule Contract with          
# IBM Corp.                                                        
#                                                                  
# end_generated_IBM_copyright_prolog                               

use strict;
use warnings;

use POSIX qw(strftime);
use Time::HiRes qw(gettimeofday usleep);
use File::Basename;
use Getopt::Std;

my %opts;

my $outdir = "/tmp";
my $interval = 10;
my $disk_device = "";
my $ZKport ="2181";
my $newFileEveryHour = 0;
my $Debug = 0;
my $ResetStats = 0;
my $Verbose = 0;
my $stopfile = "";

my $sname = basename($0);
my $help="usage: $sname -d <disk device> -i <interval> -o <output directory> -s <stopfile> -H -V
   -d disk_device (default null - does not collect disk data)
   -i interval in seconds (default 10) 
   -o output directory (default /tmp)
   -p zookeeper port (default 2181)
   -s stopfile (default 'running' concatenated with short hostname and timestamp)
   -D (include debug information)
   -H (start a new output file every hour)
   -R reset ZK stats before starting
   -V (verbose)";
getopts('Dd:Hi:o:p:Rs:V',\%opts) or die("\n$help\n");

if ($opts{'D'})
  {
   $Debug=1;
  }
if ($opts{'d'})
  {
   $disk_device=$opts{'d'};
  }
if ($opts{'H'})
  {
   $newFileEveryHour = 1;
  }
if ($opts{'i'})
  {
   $interval=$opts{'i'};
  }
if ($opts{'o'})
  {
   $outdir=$opts{'o'};
  }
if ($opts{'p'})
  {
   $ZKport=$opts{'p'};
  }
if ($opts{'R'})
  {
   $ResetStats=1;
  }
if ($opts{'s'})
  {
   $stopfile=$opts{'s'};
  }
if ($opts{'V'})
  {
   $Verbose=1;
  }

my $uinterval = $interval * 1000000;

my $host = `hostname`;
chomp($host);
my $short_host = `hostname -s`;
chomp($short_host);

my ($time, $us) = gettimeofday();
my $last_interval_start_millis = $time * 1000 + $us/1000;
my @uptime_values = split / +/,trim(`cat /proc/uptime`);
my $interval_millis = $uptime_values[0]*1000;
my ($sec, $min, $hour, $day, $month, $year) = localtime $time;
$month = $month + 1;
$year = $year + 1900;
my $tbase = sprintf "%s_%04d%02d%02d%02d%02d%02d", $short_host, $year, $month, $day, $hour,$min,$sec;
my $tracetime = `cat /proc/uptime | awk '//{printf(\"%s\\n\",\$1)}'`;
chomp($tracetime);

my $ZKoutdir = "$outdir/ZKdata";
`mkdir $ZKoutdir`;
my $touchfile = "";
if ("$stopfile" ne "") {
	$touchfile = $stopfile;
}
else {
	$touchfile = "$outdir/running_$tbase";
}
`touch $touchfile`;
print "starting collection on system $host - to end do:\n";
print "rm $touchfile\n";

if ($ResetStats) {
	my @ZKreset = `echo 'srst' | nc localhost 2181`;
}

while (-e $touchfile) {
	my $fbase = sprintf "%s_%04d%02d%02d%02d%02d", $short_host, $year, $month, $day, $hour, $min;
	my $ZKfilename = "$ZKoutdir/ZKdata$fbase.txt";
	open (ZKOUTFILE, ">$ZKfilename");
	print "writing ZK stat to $ZKfilename\n";
	my $need_header = 1;
	my $disk_header_line = "";
	if ("$disk_device" ne "") {
		$disk_header_line = sprintf(" %10s %10s %10s %10s %10s %10s %10s %10s","rd_ops","rd_mrg","rd_sec","rd_msc","wr_ops","wr_mrg","wr_sec","wr_msc");
	}
	my $zk_received_last = 0;
	my $zk_sent_last = 0;
	my @last_disk_values = (0,0,0,0,0,0,0,0,0,0,0);
	my $header_line = sprintf("%-40s %-10s %-8s %8s %-40s %12s %10s %10s %10s %10s %10s %10s %s","host", "date", "time", "timeus", "trace_time", "int_millis", "ZK role", "minlat", "avglat", "maxlat", "received", "sent",$disk_header_line);
	my $filehour = $hour;
	until (($newFileEveryHour && $filehour != $hour) || !(-e $touchfile)) {
		local $| = 1;
		my $line = sprintf '%-40s %04d/%02d/%02d %02d:%02d:%02d %8d %-40s %12d', $host, $year, $month, $day, $hour, $min, $sec, $us, $tracetime, $interval_millis;
		if ($need_header == 1) {
			print ZKOUTFILE "$header_line\n";
			$need_header = 0;
		}
		my @ZKinfo = `echo 'stat' | nc localhost $ZKport`;
		if ($ResetStats) {
			my @ZKreset = `echo 'srst' | nc localhost 2181`;
		}
		my $disk_line = "";
		if ("$disk_device" ne "") {
			$disk_line = `iostat -x | grep "^$disk_device"`;
			chomp($disk_line);
			my $disk_info = `cat /sys/class/block/$disk_device/stat`;
			chomp($disk_info);
			my @disk_values = split / +/,trim($disk_info);
			my $read_ops = $disk_values[0] - $last_disk_values[0];
			my $merged_read_ops = $disk_values[1] - $last_disk_values[1];
			my $read_sectors = $disk_values[2] - $last_disk_values[2];
			my $read_milliseconds = $disk_values[3] - $last_disk_values[3];
			my $write_ops = $disk_values[4] - $last_disk_values[4];
			my $merged_write_ops = $disk_values[5] - $last_disk_values[5];
			my $write_sectors = $disk_values[6] - $last_disk_values[6];
			my $write_milliseconds = $disk_values[7] - $last_disk_values[7];
			$disk_line = sprintf(" %10d %10d %10d %10d %10d %10d %10d %10d",$read_ops,$merged_read_ops,$read_sectors,$read_milliseconds,$write_ops,$merged_write_ops,$write_sectors,$write_milliseconds);
			@last_disk_values = @disk_values;
		}
		my $ZKrole = "unknown";
		my ($minLatency,$aveLatency,$maxLatency,$receivedCount,$sentCount) = (-1,-1,-1,-1,-1);
		foreach my $ZKinfo (@ZKinfo) {
			if ($ZKinfo =~ m/^Latency min\/avg\/max\:/) {
				$ZKinfo =~ /Latency min\/avg\/max\: ([0-9]+)\/([0-9]+)\/([0-9]+)/;
				($minLatency,$aveLatency,$maxLatency) = ($1,$2,$3);
			}
			elsif ($ZKinfo =~ m/^Received/) {
				$ZKinfo =~ /Received\: ([0-9]+)/;
				$receivedCount = $1 - $zk_received_last;
				if ($ResetStats) {
					$zk_received_last = 0;
				}
				else {
					$zk_received_last = $1;
				}
			} 
			elsif ($ZKinfo =~ m/^Sent/) {
				$ZKinfo =~ /Sent\: ([0-9]+)/;
				$sentCount = $1 - $zk_sent_last;
				if ($ResetStats) {
					$zk_sent_last = 0;
				}
				else {
					$zk_sent_last= $1;
				}
			} 
			elsif ($ZKinfo =~ m/^Mode/) {
				$ZKinfo =~ /Mode\: ([a-z]+)/;
				$ZKrole = $1;
			} 
		}
		$line .= sprintf(' %10s %10d %10d %10d %10d %10d %s',$ZKrole,$minLatency,$aveLatency,$maxLatency,$receivedCount,$sentCount,$disk_line);
		print ZKOUTFILE "$line\n";
		usleep($us ? $uinterval - $us : $uinterval);
		($time, $us) = gettimeofday();
		my $interval_start_millis = $time * 1000 + $us/1000;
		$interval_millis = $interval_start_millis - $last_interval_start_millis;
		$last_interval_start_millis = $interval_start_millis;
		($sec, $min, $hour, $day, $month, $year) = localtime $time;
		$month = $month + 1;
		$year = $year + 1900;
                $tracetime = `cat /proc/uptime | awk '//{printf(\"%s\\n\",\$1)}'`;
                chomp($tracetime);
	}
	close(ZKOUTFILE);
}
if ($Verbose) {
	print "Collection stopped\n";
}

sub slurp {
    local @ARGV = @_;
    local @_ = <>;
    @_;
}

sub  trim { my $s = shift; $s =~ s/^\s+|\s+$//g; return $s };
