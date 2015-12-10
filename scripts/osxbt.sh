#!/bin/bash

### Usage ###
# ./osxbt.sh                    → Start screen session with `btserialbash` name (this is necessarily before work)
# ./osxbt.sh -s                 → Stop screen session
# ./osxbt.sh -r                 → Send random data
# ./osxbt.sh -d "HELLO WORLD"   → Send HELLO WORLD

args=("$@")

sensors=('t' 'i' 'v' 'r' 'h' 'p' 's')

function start {
  screen -d -m -S btserialbash /dev/cu.Bluetooth-Incoming-Port 9600
  screen -r btserialbash
}

function stop {
  screen -wipe
  pids=$(screen -ls | awk '/\.btserialbash\t/ {print $1}' | sed 's/[^0-9]*//g')
  while read -r pid; do
    if [ ${#pid} -ge 1 ]; then
      kill -9 "${pid}"
    fi
  done <<< "$pids"
  screen -wipe
}

function restart {
  stop
  start
}

function send {
  screen -S btserialbash -X stuff "$1"
}

function randomize {
  while true
  do
    data=''
    for sensor in "${sensors[@]}"
    do
      left=$(((RANDOM % 10) + 1))
      
      right=$[(100 + (RANDOM % 100))]$[1000 + (RANDOM % 1000)]
      right=${right:1:2}${right:4:3}

      num="${left}.${right}"

      data="$data${sensor}${num}"
    done
    send ${data}
    sleep 0.04
  done
}

if [ "${args[0]}" = '-d' ]; then
  send "${args[1]}"
elif [ "${args[0]}" = '-s' ]; then
  stop
elif [ "${args[0]}" = '-r' ]; then
  randomize
else
  restart
fi