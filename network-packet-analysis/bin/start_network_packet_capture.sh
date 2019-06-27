echo "Starting network packet capture services"
ppid=`pidof tcpdump`
sudo kill -9 $ppid
ts=`date +%m_%d_%Y_%H_%M_%S`
file_name="tcp_dump_$ts.txt"
sudo tcpdump -i lo -vv -nn port 8080 >> /home/team124/hackathon/SparkRepository/network-packet-analysis/data/inbound/$file_name &
echo "Started capturing the network packets and writing into path /home/team124/hackathon/SparkRepository/network-packet-analysis/data/inbound/"