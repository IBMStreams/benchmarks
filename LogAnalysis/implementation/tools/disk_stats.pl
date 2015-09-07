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
use warnings;

use POSIX qw(strftime);
use Time::HiRes qw(gettimeofday usleep);
use File::Basename;
use Getopt::Std;

my %opts;

my $outdir = "/tmp";
my $interval = 10;
my $disk_device = "";
my $newFileEveryHour = 0;
my $Debug = 0;
my $Verbose = 0;
my $stopfile = "";

my $sname = basename($0);
my $help="usage: $sname -d <disk device> -i <interval> -o <output directory> -s <stopfile> -H -V
   -d disk_device (required)
   -i interval in seconds (default 10) 
   -o output directory (default /tmp)
   -s stopfile (default 'running' concatenated with short hostname and timestamp)
   -D (include debug information)
   -H (start a new output file every hour)
   -V (verbose)";
getopts('Dd:Hi:o:s:V',\%opts) or die("\n$help\n");

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
if ($opts{'s'})
  {
   $stopfile=$opts{'s'};
  }
if ($opts{'V'})
  {
   $Verbose=1;
  }

if ("$disk_device" eq "") {
	die("-d is required\n$help\n");
}

my @devices = split /,/,$disk_device;
my $device_count = $#devices + 1;

my $uinterval = $interval * 1000000;

my $host = `hostname`;
chomp($host);
my $host_comma = "$host,";
my $short_host = `hostname -s`;
chomp($short_host);

my ($time, $us) = gettimeofday();
my ($sec, $min, $hour, $day, $month, $year) = localtime $time;
$month = $month + 1;
$year = $year + 1900;
my $tbase = sprintf "%s_%04d%02d%02d%02d%02d%02d", $short_host, $year, $month, $day, $hour,$min,$sec;

if (! -e $outdir) {
	`mkdir -p $outdir`;
}
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


while (-e $touchfile) {
	my $fbase = sprintf "%s_%04d%02d%02d%02d%02d", $short_host, $year, $month, $day, $hour, $min;
	my $filename = "$outdir/diskstats_$fbase.txt";
	open (OUTFILE, ">$filename");
	print "writing disk stats to $filename\n";
	my $need_header = 1;
	my $header_line = sprintf("%-40s %-11s %-9s %12s, %7s","host,", "date,", "time,", "epoch", "time_us");
	my $disk_header_line = "";
	my $device_index;
	my @last_disk_values = ();
	my $device;
	for ($device_index = 0; $device_index < $device_count; $device_index++) {
		$device = $devices[$device_index];
		$disk_header_line = sprintf(", %12s, %12s, %12s, %12s, %12s, %12s, %12s, %12s","${device}_rd_ops","${device}_rd_mrg","${device}_rd_sec","${device}_rd_msc","${device}_wr_ops","${device}_wr_mrg","${device}_wr_sec","${device}_wr_msc");
		$header_line .= "$disk_header_line";
		push @last_disk_values,(0,0,0,0,0,0,0,0,0,0,0);
	}
	if ($Debug) {
		$header_line .= ",  finish_us";
	}
	my $filehour = $hour;
	until (($newFileEveryHour && $filehour != $hour) || !(-e $touchfile)) {
		local $| = 1;
		my $line = sprintf '%-40s %04d/%02d/%02d, %02d:%02d:%02d, %12d, %7d', $host_comma, $year, $month, $day, $hour, $min, $sec, $time, $us;
		if ($need_header == 1) {
			print OUTFILE "$header_line\n";
			$need_header = 0;
		}
		for ($device_index = 0; $device_index < $device_count; $device_index++) {
			$device = $devices[$device_index];
			my $disk_info = `cat /sys/class/block/$device/stat`;
			chomp($disk_info);
			my @disk_values = split / +/,trim($disk_info);
			my $device_info_start = 8*$device_index;
			my $read_ops = $disk_values[0] - $last_disk_values[$device_info_start+0];
			my $merged_read_ops = $disk_values[1] - $last_disk_values[$device_info_start+1];
			my $read_sectors = $disk_values[2] - $last_disk_values[$device_info_start+2];
			my $read_milliseconds = $disk_values[3] - $last_disk_values[$device_info_start+3];
			my $write_ops = $disk_values[4] - $last_disk_values[$device_info_start+4];
			my $merged_write_ops = $disk_values[5] - $last_disk_values[$device_info_start+5];
			my $write_sectors = $disk_values[6] - $last_disk_values[$device_info_start+6];
			my $write_milliseconds = $disk_values[7] - $last_disk_values[$device_info_start+7];
			$line .= sprintf(", %12d, %12d, %12d, %12d, %12d, %12d, %12d, %12d",$read_ops,$merged_read_ops,$read_sectors,$read_milliseconds,$write_ops,$merged_write_ops,$write_sectors,$write_milliseconds);
			@last_disk_values[$device_info_start..$device_info_start+7] = @disk_values;
		}
		if ($Debug) {
			($time, $us) = gettimeofday();
			my $calc_val = $uinterval-(($interval - ($time % $interval)) * 1000000 - $us);
			$line .= sprintf(", %10d", $calc_val);
		}
		print OUTFILE "$line\n";
		($time, $us) = gettimeofday();
		my $sleep = ($interval - ($time % $interval)) * 1000000 - $us;
		usleep($sleep);
		($time, $us) = gettimeofday();
		($sec, $min, $hour, $day, $month, $year) = localtime $time;
		$month = $month + 1;
		$year = $year + 1900;
	}
	close(OUTFILE);
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
