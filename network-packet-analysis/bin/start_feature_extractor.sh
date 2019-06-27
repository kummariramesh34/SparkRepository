echo "Started extracting features from network packets"
java -jar /home/team124/hackathon/SparkRepository/featureextractor/target/feature-extractor-1.0-SNAPSHOT-shaded.jar /home/team124/hackathon/SparkRepository/network-packet-analysis/data/processing/ tcp_dump.txt POST [!http]
ts=`date +%m_%d_%Y_%H_%M_%S`
file_name="tcp_dump_$ts.txt"
filter_filename="tcp_dump_filtered_$ts.txt"
mv /home/team124/hackathon/SparkRepository/network-packet-analysis/data/processing/tcp_dump.txt /home/team124/hackathon/SparkRepository/network-packet-analysis/data/archive/$file_name
mv /home/team124/hackathon/SparkRepository/network-packet-analysis/data/processing/tcp_dump_filtered.txt /home/team124/hackathon/SparkRepository/network-packet-analysis/data/tcp_dump/$filter_filename
echo "Feature extraction completed successfully"