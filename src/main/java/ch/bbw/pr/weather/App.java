package ch.bbw.pr.weather;

import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

/**
 * @author Andrin Renggli
 * @version 07.1.2025
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello Weather");

        //Verbindung zu MongoDB mit Benutzername root und Passwort 1234
        String connectionString = "mongodb://root:1234@localhost:27017";
        //Erstellt Verbindung
        MongoClient mongoClient = MongoClients.create(connectionString);
        //Ünnötiger BS, Listet alle Datenbanken.
        System.out.println("List all databases:");
        mongoClient.listDatabases().forEach((Consumer<? super Document>) result -> System.out.println(result.toJson()));

        //holt Datebank namens "weathermeasuredb
        MongoDatabase statisticDB = mongoClient.getDatabase("weathermeasuredb");

        //holt collection ("Tabelle ähnlich") namens "measures"
        MongoCollection<Document> statisticCollection = statisticDB.getCollection("measures");

        //neues Dokument und fügt keys + values zum dokument
        Document doc = new Document();
        doc.append("type", "Wettermessung");
        doc.append("timestamp", new BsonDateTime(new Date().getTime()));
        Document embeddedDoc = new Document();
        embeddedDoc.append("city", "Winterthur");
        embeddedDoc.append("plz", "8400");

        //macht zwei Dokumente mit Werten
        Document m1 = new Document();
        m1.append("kind", "temperature");
        m1.append("value", 20.3);
        Document m2 = new Document();
        m2.append("kind", "windspeed");
        m2.append("value", 2.3);
        //Macht eine Liste aus den zwei Dokumenten und fügt sie dem Haupt Dokument hinzu
        List<Document> measures = List.of(m1, m2);
        doc.append("measures", measures);

        //Fügt das Dokument in die Collection
        statisticCollection.insertOne(doc);

        mongoClient.close();
    }
}
