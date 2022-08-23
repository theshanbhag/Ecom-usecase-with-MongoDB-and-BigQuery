**Mongo change stream to Pubsub**

 **Purpose:**

There are 3 collections in mongodb i.e orders, products and users wherein if any change happens to any of the bson documents present, then this change needs to be propogated to big query. If watch is added upon a collection, then it publishes change stream and messages from change stream can be published to a google pubsub topic which is read by streaming dataflow job and it updates respective big query tables. 

**Details:** 

1.Mongo to pubsub application is developed as a maven based java application. mvn install needs to be run.

2.This is deployed and run as part of cloud run job. 

3.Below are set of gcloud commands to create docker image with code of mongopubsub application, upload it into container registry, then create a cloud run job then execute it 

gcloud builds submit --pack image=gcr.io/ecomm-analysis/mongotopubsubcr 

 //To create docker image out of code 

 
gcloud beta run jobs create job-mongotopubsubcr --image gcr.io/ecomm-analysis/mongotopubsubcr --tasks 1 --task-timeout 3600 --set-env-vars GOOGLE_APPLICATION_CREDENTIALS=ecomm-analysis-dataflow.json --set-env-vars ProjectId=ecomm-analysis --set-env-vars TopicId=ecommerce_topic --set-env-vars MongoURI=mongodb+srv://mongotobq:M0ngoToBqPoc@mongo-bq.kn30v.mongodb.net --set-env-vars Database=ecommerce --set-env-vars Collection=orders --max-retries 1 --region us-central1 

//To create job out of docker image by passing required environment variables. Note while preparing docker image it needs to contain google application credentials file as well.

 
gcloud beta run jobs --region=us-central1 execute job-mongotopubsubcr 

.//To execute job created above 
 

4.Above process will run mongotopubsub application in cloud run. 

5.Maximum idle time for cloud run job is 3600 seconds which is like one hour. 
