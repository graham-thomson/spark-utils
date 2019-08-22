# Spark Vector Math

SVM is a collection of user defined functions implemented 
in Scala to operate on [Apache Spark](https://spark.apache.org)'s 
main machine learning data object 
[Vectors](https://spark.apache.org/docs/latest/api/python/pyspark.ml.html#pyspark.ml.linalg.Vector).

Implemented in Scala to optimize UDF performance but can be used
directly in PySpark (see usage below). 

## Installation

* Clone repo.
* Build using [gradle](https://gradle.org/).

```bash
# clone repo
git clone https://github.com/graham-thomson/spark-utils.git

# build jar
cd spark-utils
gradle build

# copy jar to Spark home.
cp ./build/libs/spark-utils.jar ${SPARK_HOME}/jars/
```

## Usage

```python
from pyspark.sql import SparkSession

spark = SparkSession.builder.getOrCreate()

# register UDFs
spark._jvm.FeatureUDF.registerUdf()


df = spark.read.parquet("s3a://somebucket/some/path/data/")
df.show(3)

# +--------------------+-------------+-------------+
# |                uuid|   features_a|   features_b|
# +--------------------+-------------+-------------+
# |cc8137c4-ed75-4eb...|[1.0,0.0,1.0]|[1.0,0.0,0.0]|
# |2be573bc-4358-424...|[0.0,0.0,0.0]|[1.0,1.0,0.0]|
# |c0e960b7-f97e-47b...|[1.0,1.0,1.0]|[1.0,1.0,1.0]|
# +--------------------+-------------+-------------+

df = df.selectExpr("*", "jaccardDistance(features_a, features_b) AS jaccard_distance")
df.show(3)

# +--------------------+-------------+-------------+----------------+
# |                uuid|   features_a|   features_b|jaccard_distance|
# +--------------------+-------------+-------------+----------------+
# |cc8137c4-ed75-4eb...|[1.0,0.0,1.0]|[1.0,0.0,0.0]|             0.5|
# |2be573bc-4358-424...|[0.0,0.0,0.0]|[1.0,1.0,0.0]|             1.0|
# |c0e960b7-f97e-47b...|[1.0,1.0,1.0]|[1.0,1.0,1.0]|             0.0|
# +--------------------+-------------+-------------+----------------+
```

## License
[MIT](https://choosealicense.com/licenses/mit/)