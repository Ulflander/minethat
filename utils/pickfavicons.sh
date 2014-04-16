#!/bin/sh
sites=( $(cat sites.csv) )
IFS=","
while read site
do
	set $site
	url=`echo $2`
	id=`echo $1`
	echo $url
	
	if [ $id -lt 1035 ]; then
		continue;
	fi
	
	status=$(curl -s --head --write-out %{http_code} --output /dev/null $url/favicon.ico)
	echo $status
	
	if [ $status != '404' ]; then
		curl -sL $url/favicon.ico > favs/$id.ico
		size=`wc -c < favs/$id.ico | sed 's/^ *\(.*\) *$/\1/'`
		echo $size
		if [ $size -gt 5 ] && [ $size -lt 5000 ]; then
			convert favs/$id.ico -thumbnail 16x16 -alpha on -background none -flatten favs/$id.png
		fi
		
	fi;
done < sites.csv
