
function sync_version() {
  source=$2
  for ver in $3; do
   	#skip base source
   	if [[ "$ver" == "$source" ]]; then
   		continue
   	fi
   	echo "copy $source/src to $ver/src"
   	#copy source code
   	rm -rf "$ver/src"
   	cp -rf "$source/src" "$ver/src"
   	#rename
   	for iv in `find "$ver/src" -type f`; do
		sed -i "" "s/Wrapper$source/Wrapper$ver/g" "$iv"
		if [[ $1 == 1 ]]; then
			sed -i "" "s/$source/$ver/g" "$iv"
		fi
   		mv "$iv" "${iv/$source/$ver}"
   	done
   done
}

#sync_version 1 "1_16_R1" "1_16_R2 1_16_R3"
#sync_version 0 "1_17_R1" "1_17_1_R1"
#sync_version 1 "1_18_R1" "1_18_R2" + changes
#sync_version 1 "1_18_R2" "1_19_R1" + changes
#sync_version 1 "1_19_R1" "1_19_R2 1_19_R3" + changes
#sync_version 0 "1_19_R3" "1_19_1_R1" + changes
#sync_version 1 "1_19_1_R1" "1_20_R1" + changes


#sync_version "1_17_R1" "1_17_1_R1 1_18_R1 1_18_R2 1_19_1_R1 1_19_R1 1_19_R2 1_19_R3 1_20_R"


#source="1_16_R1"
#versions="1_16_R1 1_16_R2 1_16_R3 1_17_1_R1 1_17_R1 1_18_R1 1_18_R2 1_19_1_R1 1_19_R1 1_19_R2 1_19_R3 1_20_R1"
