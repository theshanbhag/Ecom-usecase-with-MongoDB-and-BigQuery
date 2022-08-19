package com.mongotopubsub.source;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;

import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MongoDbToPubSub {


    public static void main(String[] args) throws Exception {
        String PROJECT_ID = "ecomm-analysis";
        String topicId = "ecommerce_topic";


        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://mongotobq:M0ngoToBqPoc@mongo-bq.kn30v.mongodb.net"));
        MongoDatabase mongoDatabase = mongoClient.getDatabase("ecommerce");
        MongoCollection mongoCollection =  mongoDatabase.getCollection("orders");

        ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, topicId);
        List<ApiFuture<String>> futures = new ArrayList<>();
        final Publisher publisher = Publisher.newBuilder(topicName).build();

        Block<ChangeStreamDocument<Document>> publishToPubSub = new Block<ChangeStreamDocument<Document>>() {
            @Override
            public void apply(final ChangeStreamDocument<Document> changeStreamDocument) {
                //System.out.println(changeStreamDocument.getFullDocument().toJson());

                try {
                    System.out.println(changeStreamDocument.getFullDocument());
                    Document fullDocument = (Document)changeStreamDocument.getFullDocument();
                    String id = fullDocument.get("_id").toString();
                    fullDocument.put("_id",id);
                   /* String saleDate = fullDocument.get("saleDate").toString();
                    fullDocument.put("saleDate",saleDate);*/
                    //fullDocument.remove("items");
                   /* List<Document> itemsDoc = (ArrayList)fullDocument.get("items");
                    itemsDoc.stream().forEach(item -> {
                        item.put("price",Float.parseFloat(item.get("price").toString()));
                    });*/
                    //fullDocument.remove("customer");
                    System.out.println("Document is:"+fullDocument.toJson());

                    ByteString data = ByteString.copyFromUtf8(fullDocument.toJson());
                    PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                            .setData(data)
                            .build();
                    // Schedule a message to be published. Messages are automatically batched.
                    ApiFuture<String> future = publisher.publish(pubsubMessage);

                    futures.add(future);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        };


        mongoCollection.watch().fullDocument(FullDocument.UPDATE_LOOKUP).forEach(publishToPubSub);

    }
}
