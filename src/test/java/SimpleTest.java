import com.xavidop.alexa.client.AlexaProactiveEventSenderClient;
import com.xavidop.alexa.model.event.ProactiveEvent;
import com.xavidop.alexa.model.urlregion.Environment;
import com.xavidop.alexa.model.urlregion.Region;
import com.xavidop.alexa.model.urlregion.URLRegion;
import com.xavidop.alexa.utilities.LogUtilities;
import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {

    @Test
    public void test() {
        LogUtilities logger = new LogUtilities();
        String clientId = "amzn1.application-oa2-client.539c5a60de5d4214a31d2b5b2ed6a20c";
        String secretId = "7e89ce0974413be4c14941f88dcb7681be2ae3b9b9118549295489372b5e38dd";

        AlexaProactiveEventSenderClient client = new AlexaProactiveEventSenderClient(clientId, secretId);
        logger.log("Token: " + client.getToken());
        Assert.assertNotNull(client.getToken());

        ProactiveEvent event = new ProactiveEvent();
        event.getEvent().getPayload().getMessageGroup().getCreator().setName("Test");

        URLRegion urlRegion = new URLRegion();
        urlRegion.setRegion(Region.NA);
        urlRegion.setEnvironment(Environment.DEV);

        boolean ok = client.sendProactiveEvent(event, urlRegion);
        logger.log("Response was: " + ok);
        Assert.assertTrue(ok);
    }

}
