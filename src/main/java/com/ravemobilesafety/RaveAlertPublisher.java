package com.ravemobilesafety;


import com.google.publicalerts.cap.CapXmlBuilder;
import com.metissecure.api.PropertyManager;
import com.metissecure.api.alert.MetisCapUtils;
import com.metissecure.model.Group;
import com.metissecure.model.alert.Alert;
import com.metissecure.model.alert.Dispatch;
import com.metissecure.spi.alert.AlertPublisher;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author Matt McHenry
 */
public class RaveAlertPublisher implements AlertPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(RaveAlertPublisher.class);

    private static final String RAVE_WS_URL_PROP = "rave.url";
    private static final String RAVE_USER_PROP = "rave.username";
    private static final String RAVE_PASS_PROP = "rave.password";


    @Autowired MetisCapUtils capUtils;
    @Autowired PropertyManager propertyManager;

    private CapXmlBuilder builder = new CapXmlBuilder();


    @Override
    public boolean supportsFuture() {
        return true;
    }

    @Override
    public void dispatchAlert(Dispatch dispatch, Set<Group> groups, Alert alert)
            throws Exception
    {
        com.google.publicalerts.cap.Alert cap = capUtils.generate(dispatch, alert, groups);
        final byte[] xml = builder.toXml(cap).getBytes();

        final String urlStr = propertyManager.getProperty(RAVE_WS_URL_PROP);
        final String user = propertyManager.getProperty(RAVE_USER_PROP);
        final String pass = propertyManager.getProperty(RAVE_PASS_PROP);

        if (urlStr == null) {
            throw new Exception("Rave Web Service URL not set");
        }
        URL url = new URL(urlStr);

        // TODO: implement this in a non-blocking way? netty?
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", String.valueOf(xml.length));
            conn.setRequestProperty("Content-Type", "application/xml");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream out = conn.getOutputStream();
            out.write(xml);
            out.close();

            InputStream in = conn.getInputStream();
            while(in.read() != -1) {}
            in.close();

            final int code = conn.getResponseCode();
            final String msg = conn.getResponseMessage();
            LOG.warn("Rave returned: {} {}", code, msg);
        } catch (Exception ex) {
            throw new Exception("Error sending alert to Rave", ex);
        }
    }

    @Override
    public void cancelAlert(Dispatch dispatch, Set<Group> groups, Alert alert)
            throws Exception
    {}

}
