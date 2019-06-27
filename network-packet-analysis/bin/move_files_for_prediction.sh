echo "Copying files for machine learning predictions"
cat /home/team124/hackathon/SparkRepository/network-packet-analysis/data/inbound/tcp_dump* >> /home/team124/hackathon/SparkRepository/network-packet-analysis/data/processing/tcp_dump.txt
rm /home/team124/hackathon/SparkRepository/network-packet-analysis/data/inbound/tcp_dump*
echo "Files copied successfully for machine learning predictions"