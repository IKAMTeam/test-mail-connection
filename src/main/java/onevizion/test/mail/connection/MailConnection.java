package onevizion.test.mail.connection;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MailConnection {
    private static final String MAIL_PROPERTIES_PATH = "/mail.properties";

    public static void main(String[] args) throws IOException, MessagingException {
        connect(args[0]);
    }

    private static void connect(String pathCredentials) throws MessagingException, IOException {
        Properties credentials = getCredentials(pathCredentials);
        Session session = Session.getInstance(getMailProperties());
        Store store = session.getStore("imap");
        store.connect(
                credentials.getProperty("host"),
                Integer.parseInt(credentials.getProperty("port")),
                credentials.getProperty("username"),
                getToken(credentials)
        );
    }

    private static String getToken(Properties props) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(props.getProperty("token_uri"));

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("client_id", props.getProperty("client_id")));
        params.add(new BasicNameValuePair("client_secret", props.getProperty("client_secret")));
        params.add(new BasicNameValuePair("scope", props.getProperty("scope")));
        params.add(new BasicNameValuePair("grant_type", props.getProperty("grant_type")));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);
        String responseJson = EntityUtils.toString(response.getEntity());

        JSONObject jsonObject = new JSONObject(responseJson);

        return jsonObject.getString("access_token");
    }

    private static Properties getMailProperties() {
        Properties prop = new Properties();
        try (InputStream inputStream = MailConnection.class.getResourceAsStream(MAIL_PROPERTIES_PATH)) {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop;
    }

    private static Properties getCredentials(String path) throws IOException {
        Properties prop = new Properties();
        try (InputStream inputStream = new FileInputStream(path)) {
            prop.load(inputStream);
        }
        return prop;
    }

}
