package camp.CampManager.notifications;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/notifications/")
public class NotificationsEndpoints {

    /*
    @PostMapping("/test")
    public String testNotification(@RequestBody Map<String, String> body) throws FirebaseMessagingException {
        String token = body.get("token");
        Message message = Message.builder()
                .setNotification(Notification.builder()
                    .setTitle("Test")
                    .setBody("Test body")
                    .build())
                .setToken(token)
                .build();
        return FirebaseMessaging.getInstance().send(message);
    }
     */
}
