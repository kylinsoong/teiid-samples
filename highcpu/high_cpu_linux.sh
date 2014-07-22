#!/bin/sh
#
# Takes the JBoss PID as an argument. 
#
# Captures cpu by light weight thread and thread dumps a specified number of
# times and INTERVAL. Thread dumps will be in the file where stdout was
# redirected or in console output.
#
# Usage: sh ./high_cpu_linux.sh <JBOSS_PID>
# 
# Change Log:
# * 2011-05-02 19:00 <loleary>
# - Added date output to high-cpu.out for log correlation
# - Added -p argument to top to limit high-cpu.out to <JBOSS_PID>
#
#

# Number of times to collect data.
LOOP=6
# Interval in seconds between data points.
INTERVAL=20

for ((i=1; i <= $LOOP; i++))
do
   date >>high-cpu.out
   top -b -H -p $1 >>high-cpu.out
   kill -3  $1
   echo "thread dump #" $i
   if [ $i -lt $LOOP ]; then
      echo "Sleeping..."
      sleep $INTERVAL
   fi
done
