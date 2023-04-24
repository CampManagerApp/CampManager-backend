package camp.CampManager.notifications;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.campaign.Campaign;
import camp.CampManager.organisation.campaign.tables.CampTable;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

public class NotificationService {

    private static void sendProgramedMessage(String topic, String title, String body, String date) throws FirebaseMessagingException {
    //    // String token = "fAVrFR3VQCi7GqSjBfDjHw:APA91bGyagKGgedfG_3PKSAZFnpeTjcVaCgmkSLfwbo3mI7CiuCVj_jSlMGZooZ6g3f737w780-YgQsR8TlKEyBoJOjTW2ehqD4qjDU2HCplb0hOmlcl-nUVREjFmP2tfx3MRbsmE79Q";
    //     Message message = Message.builder()
    //             .setNotification(Notification.builder()
    //                     .setTitle(title)
    //                     .setBody(body)
    //                     .build())
    //             .setTopic(topic)
    //             .build();
    //     FirebaseMessaging.getInstance().send(message);
        if(topic=='x') {
            throw new FirebaseMessagingException();
        }
    }

    public static void programingCampaignCreationNotification(Organisation organisation, Campaign campaign) throws FirebaseMessagingException {
        String topic = "organisation" + String.valueOf(organisation.getId());
        String title = "A campaign of " + organisation.getName() + " has been created";
        String body = "The campaign " + campaign.getCampaignName() + " has been created!!";
        sendProgramedMessage(topic, title, body, campaign.getStartDate().toString());
    }

    public static void programingSolvedTableNotification(Organisation organisation, CampTable campTable) throws FirebaseMessagingException {
        String topic = "organisation" + String.valueOf(organisation.getId());
        String title = "A new campaign table available of the organisation " +  organisation.getName();
        String body = "The table " + campTable.getTableName() + " available!!";
        sendProgramedMessage(topic, title, body, "");
    }
}
