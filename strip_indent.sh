#!/bin/sh
#####################################################
# @author Latency McLaughlin
#
# @Description
# Removes the last 4 lines of the file(s) specfied.
#####################################################


Lines=4
String="//~ Formatted by Jindent --- http://www.jindent.com"

#####################################################
# DO NOT MODIFY ANYTHING BELOW THIS LINE
#####################################################

Arguments="$@"

if [ $# -lt 1 ]; then
  while :
  do
    clear
    read -p "Do you want to recursively remove Jindent tags (y/n)?  "
    case $REPLY in
      n)
        exit
        ;;
      y)
        # Recurisively lists all files only
        #`ls -l -R | grep ^- | awk '{print $9}'`
        Arguments=$(find . -iname '*.java' -type f -follow)
        break
        ;;
      *)
        echo -e "\r\nUsage:  `basename $0` <file>..."
        sleep 5
        ;;
    esac
  done
fi

for f in $Arguments
do
  # if file exists and is readable
  if [ -r $1 ]; then
    # if is a file and size greater than zero
    if [ -s $1 ]; then
      if [ "$(tail -1 "$f")" == "$String" ]; then
        # Remove the last 4 lines of the file && replace original
        lines=$(wc -l < "$f")
        target=$((lines-Lines+1))
        cat "$f" | head -${target} > "$f.bak" && mv "$f.bak" "$f"
        echo "Fixing Jindent format for '$f'"
      fi
    else
      echo "Error:  File '$f' is empty"
    fi
  else
    echo "Error:  Cannot read '$f' or does not exist."
  fi
done

echo "Jindent tag removal successful."