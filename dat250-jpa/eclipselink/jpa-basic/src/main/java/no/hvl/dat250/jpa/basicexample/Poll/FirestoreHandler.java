package no.hvl.dat250.jpa.basicexample.Poll;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirestoreHandler{
    String projectId = "57796962883";
    Firestore db;

    public FirestoreHandler() throws IOException {
        // Use the application default credentials
        InputStream serviceAccount = new FileInputStream("C:/Users/krist/IdeaProjects/DAT250-Prosjekt/dat-250-2b8988763766.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();
    }


    public void addResult(Result pollResult) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("polls").document("pollId: " + pollResult.getPollId());
        Map<String, Object> data = new HashMap<>();
        data.put("pollId", pollResult.getPollId());
        data.put("userId", pollResult.getUserId());
        data.put("question", pollResult.getQuestion());
        data.put("Answer option 1", pollResult.getOpt1());
        data.put("Answer option 2", pollResult.getOpt2());
        data.put("Votes for option 1", pollResult.getVotesFor1());
        data.put("Votes for option 2", pollResult.getVotesFor2());
        data.put("Votes for option 1%", pollResult.getVotesFor1Percent());
        data.put("Votes for option 2%", pollResult.getVotesFor2Percent());
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        System.out.println("Update time : " + result.get().getUpdateTime());
    }

    public void update() throws ExecutionException, InterruptedException {
        PollDAO pollDAO = new PollDAO();
        for(Poll p : pollDAO.getPolls()){
            Result result = pollDAO.getResults(p);
            addResult(result);
        }
    }

    public void delete(Poll poll){
        DocumentReference docRef = db.collection("polls").document("pollId: " + poll.getId());
        docRef.delete();
    }

    public void delete(){
        PollDAO pollDAO = new PollDAO();
        for(Poll p : pollDAO.getPolls()){
            delete(p);
        }
    }


}
