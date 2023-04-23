package com.projects
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object imdb_data extends Serializable {

  def main(args:Array[String]):Unit={
  
    val spark=SparkSession.builder().master("local[2]").getOrCreate()
    spark.sparkContext.setLogLevel("Error")
    val imdb_data = spark.read.option("header","true").format("csv").load("s3a://srinu-imdb-bucket/contentDataPrime.csv")
    val imdb_genre = spark.read.option("header","true").format("csv").load("s3a://srinu-imdb-bucket/contentDataGenre.csv")
    val imdb_region = spark.read.option("header","true").format("csv").load("s3a://srinu-imdb-bucket/contentDataRegion.csv")
    
    import spark.implicits._
   
     //highest gross movies in specific genre :
    val df3 = imdb_data.join(imdb_genre, imdb_data("dataId") === imdb_genre("dataId"),"inner")
    val win=Window.partitionBy("genre").orderBy("gross")
    val filter_waste_data = df3.filter($"gross"!=="-1").filter($"gross"!=="0")
    val highest_gross=filter_waste_data.withColumn("hg",row_number().over(win)).where($"hg"===1).select("title","genre","gross")
   

    //highest avg user rating movie :
    val win2= Window.partitionBy("genre").orderBy(col("rating").cast("Double").desc)
    val highest_avg_rating = df3.withColumn("havg",row_number().over(win2)).withColumn("avg",avg("rating").over(win2))
      .where(col("havg")===1).select("title","genre","avg").orderBy("genre")
    
    //region wise webseries which started and ended :
    val df4 = imdb_data.join(imdb_region, imdb_data("dataId") === imdb_region("dataId"),"inner")
    val webseries=df4.select("title","region","releaseYear","endYear").filter($"contentType"==="tvSeries")
      .filter($"endYear"!=="-1")
	  
	  highest_gross.write.csv("s3a://srinu-imdb-output/highest_gross/")
	  highest_avg_rating.write.csv("s3a://srinu-imdb-output/highest_avg_rating/")
    webseries.write.csv("s3a://srinu-imdb-output/webseries/")

  }
}
