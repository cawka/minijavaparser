#!/usr/bin/perl

%hash = ();

while( <STDIN> )
{
    my( $line )=$_;
    
    $line =~ s/\t//g;
    @vars=split( /,?\(?[a-z]+=/,$line );
    
#    print $vars[0], " ___ ", $vars[1], " ___ ", $vars[2];
    
#    if( $line =~ /^\t\([a-z]+=([a-zA-Z@\[\]0-9\. \t\(\)]*),?(.*)\)$/ )
#    {
#	print "$1, $2, $3\n";
#    }
#    else
#    {
#	print "Nahuy";
#    }
}