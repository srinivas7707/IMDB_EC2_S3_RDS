# IMDB_EC2_S3_RDS
A Workflow to analyze IMDB Data using spark in an EC2 Instance and load the data into s3 bucket and further send it to RDS MySql Database

<b> IMDB is a dataset of having more than 50000 movies with different details like Genre,Grossings,Regions,Ratings etc. </b>
Analysis done to fetch the following :
* Highest Grossing movies per Genre
* Highest Average user rating
* Region wise Tv-Series (Started and ended)

<h3> Steps to create an EC2 Instance </h3>

* Create an AWS Account. You can create a free tier account with pay as you go pricing.
* Navigate to the EC2 console.
* Click on Launch Instances
* Choose an instance Type(t2.micro 1 vCPU , 1 GiB memory)
* Create a key-pair(set of security credentials) to connect to the instance ,<b> Creates a .pem file </b>
* Give security group rules to control traffic for our instance.

<h3> EC2 instance configuration </h3> 

* Go to command prompt/powershell and cd to the location where .pem file is located. Make sure you dont have spaces in the .pem file name.
* SSH into EC2 using the command : ssh -i imdb-key-pair.pem ec2-user@public_dns_key 
  * public_dns_key can be found from the instance details
* Login to the ec2 instance either through AWS CLI / cmd.
* Create 
* Install Spark,scala,python,hadoop on the EC2 instance :
   * sudo yum update
   * sudo yum install java-openjdk8
   * sudo yum install python
   * sudo yum install scala
   * wget http://archive.apache.org/dist/spark/spark-3.3.0/spark-3.3.0-bin-hadoop2.tgz
   * tar -xvf spark-3.3.0-bin-hadoop2.tgz
   
* Create a bash profile and set the environment variables for the configurations set :
vi ~/.bashrc
<img width="588" alt="bash" src="https://user-images.githubusercontent.com/68941492/233819613-1a0bfa83-e917-4f63-9c3e-c05c7e162502.png">

* source ~/.bashrc

<h3> EC2 to access S3 bucket via Access-key and secret-access-key </h3>

* [ec2-user@ip-172-31-13-34 ~]$ aws configure
* AWS Access Key ID [****************5UX4]: AKIA47HKURGRQN255UX4
* AWS Secret Access Key [****************vBAu]: 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
* Default region name [ap-southeast-2]: ap-southeast
* Default output format [None]: 
 
<h3> IAM Roles for the entire flow : </h3>

* EC2 : Attach an IAM Role of (AmazonS3FullAccess) to EC2 which allows EC2 instances to call AWS services on your behalf.
* Lambda : Attach following 3 roles :
  * AmazonRDSFullAccess
  * AmazonS3FullAccess
  * CloudWatchFullAccess

<h3> Execute spark scala code to insert the data into s3 buckets </h3>

* Execute command : 	spark-shell -i ~/spark-3.3.0-bin-hadoop2.tgz/bin/imdb_data.scala

<h3> You can find spark scala code in the /src/main/spark/imdb_data.scala file </h3>



