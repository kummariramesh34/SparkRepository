echo "Started executing machine learning model for classification"
python /home/team124/hackathon/SparkRepository/network-packet-analysis/python/predict_sqli.py
mv /home/team124/hackathon/SparkRepository/network-packet-analysis/data/tcp_dump/* /home/team124/hackathon/SparkRepository/network-packet-analysis/data/archive
echo "Completed analyzing the data."