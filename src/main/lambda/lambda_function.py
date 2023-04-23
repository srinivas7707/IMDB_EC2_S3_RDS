import json
import csv
import boto3
import mysql.connector
s3 = boto3.resource('s3')
def lambda_handler(event, context):
    bucket = event['Records'][0]['s3']['bucket']['name']
    csv_file = event['Records'][0]['s3']['object']['key']
    obj = s3.Object(bucket, csv_file)
    lines = obj.get()['Body'].read().decode('utf-8')
    results = []
    for row in csv.DictReader(lines):
        results.append(row.values())
    connection = mysql.connector.connect(host='database-1.cetvemwexoqz.ap-southeast-2.rds.amazonaws.com',database='imdb',user='root',password='Srinu777!')
    mysql_insert = "insert into highest_gross(title,genre,gross) values(%s,%s,%s)"
    cursor = connection.cursor()
    cursor.executemany(mysql_insert, results)
    connection.commit()
    print('Records loaded into database succesfully')
    return {
        
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }
