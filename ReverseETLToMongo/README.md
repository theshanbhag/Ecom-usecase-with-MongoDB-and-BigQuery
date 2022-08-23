Reverse ETL from BigQuery to Mongo 

Purpose: 

To push back recommendations from big query back to mongo, so that these recommendations are made use of in mongo for various purposes. 

Details: 

Create a batch load job to load product affinity data as below. We need to pass mongodb url, collection name, big query table from where data needs to be loaded 

 

Create a batch load job to load user clustering  data(user_clusters_op) as below. We need to pass mongodb url, collection name, big query table from where data needs to be loaded 

 

 
