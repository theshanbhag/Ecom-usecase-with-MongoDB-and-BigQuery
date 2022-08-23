**Reverse ETL from BigQuery to Mongo**

**Purpose:** 

To push back recommendations from big query back to mongo, so that these recommendations are made use of in mongo for various purposes. 

**Details:** 

1.Create a batch load job to load product affinity data as below. We need to pass mongodb url, collection name, big query table from where data needs to be loaded 
![image](https://user-images.githubusercontent.com/111537542/186254484-249ae442-624c-48b4-9eb3-6bbdb95848f4.png)


2.Create a batch load job to load user clustering  data(user_clusters_op) as below. We need to pass mongodb url, collection name, big query table from where data needs to be loaded 
![image](https://user-images.githubusercontent.com/111537542/186254520-4d5ecc6a-6849-43e1-862b-feed485c54e9.png)

 

 
