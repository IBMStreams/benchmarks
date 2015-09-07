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
my $ethernet_port = "";
my $newFileEveryHour = 0;
my $Debug = 0;
my $Verbose = 0;
my $stopfile = "";

my $sname = basename($0);
my $help="usage: $sname -p <ethernet port> -i <interval> -o <output directory> -s <stopfile> -H -V
   -p ethernet_port (required)
   -i interval in seconds (default 10) 
   -o output directory (default /tmp)
   -s stopfile (default 'running' concatenated with short hostname and timestamp)
   -D (include debug information)
   -H (start a new output file every hour)
   -V (verbose)";
getopts('DHi:o:p:s:V',\%opts) or die("\n$help\n");

if ($opts{'D'})
  {
   $Debug=1;
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
   $ethernet_port=$opts{'p'};
  }
if ($opts{'s'})
  {
   $stopfile=$opts{'s'};
  }
if ($opts{'V'})
  {
   $Verbose=1;
  }

if ("$ethernet_port" eq "") {
	die("-p is required\n$help\n");
}

my @ports = split /,/,$ethernet_port;
my $port_count = $#ports + 1;

my $uinterval = $interval * 1000000;


my $dev = $ports[0];
my $dir = "/sys/class/net/$dev/statistics";
my %stats = do {
    opendir +(my $dh), $dir;
    local @_ = readdir $dh;
    closedir $dh;
    map +($_, []), grep !/^\.\.?$/, @_;
};

my $host = `hostname`;
chomp($host);
my $short_host = `hostname -s`;
chomp($short_host);
`mkdir -p $outdir`;
if ("$stopfile" eq "") {
    $stopfile = "$outdir/running_$dev";
}
`touch $stopfile`;
print "starting collection on system $host, interface $dev - to end do:\n";
print "rm $stopfile\n";
my ($time, $us) = gettimeofday();
my ($sec, $min, $hour, $day, $month, $year) = localtime $time;
$month = $month + 1;
$year = $year + 1900;

while (-e $stopfile) {
	my $fbase = sprintf "%s_%s_%04d%02d%02d%02d", $short_host, $dev, $year, $month, $day, $hour;
	my $filename = "$outdir/$fbase.csv";
	open (OUTFILE, ">$filename");
	print "writing to $filename\n";
	my $need_header = 1;
	my $header_line = "host, interface, date, time, epoch, time us";
	my $filehour = $hour;
	until (($newFileEveryHour && $filehour != $hour) || !(-e $stopfile)) {
		local $| = 1;
		my $line = sprintf '%s, %s, %04d/%02d/%02d, %02d:%02d:%02d, %12d, %06d', $host, $dev, $year, $month, $day, $hour, $min, $sec, $time, $us;
		map {
        		chomp (my ($stat) = slurp("$dir/$_"));
        		if (@{$stats{$_}} > 0)  {
				$line .= sprintf ', %d', ($stat - $stats{$_}->[0])
			}
			if ($need_header == 1) {
				$header_line .= sprintf ', %s', $_
			}	
        		unshift @{$stats{$_}}, $stat;
        		pop @{$stats{$_}} if @{$stats{$_}} > 1;
		} sort keys %stats;
		if ($need_header == 1) {
			print OUTFILE "$header_line\n";
			$need_header = 0;
		}
		print OUTFILE "$line\n";
		usleep($us ? 1000000 - $us : 1000000);
		($time, $us) = gettimeofday();
		($sec, $min, $hour, $day, $month, $year) = localtime $time;
		$month = $month + 1;
		$year = $year + 1900;
	}
	close(OUTFILE);
}

sub slurp {
    local @ARGV = @_;
    local @_ = <>;
    @_;
}
