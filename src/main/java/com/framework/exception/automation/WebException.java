package com.framework.exception.automation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.BuildInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class WebException extends AutomationException {

    public static final String SESSION_ID = "Session ID";
    public static final String DRIVER_INFO = "Driver info";

    private static final long serialVersionUID = -8710980695994382082L;

    private WebDriver driver;
    
    public WebException(String message) {
        super(message);
    }

    public WebException(String message, WebDriver driver) {
        super(message);
        this.driver = driver;
    }

    public WebException(String message, AllegisDriver driver) {
        super(message);
        this.driver = driver.getWebDriver();
    }

    public WebException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return createExceptionMessage(super.getMessage());
    }

    private String createExceptionMessage(String originalMessageString) {
        String supportMessage = getSupportUrl() == null ? "" : "For documentation on this error, please visit: " + getSupportUrl() + "\n";

        return (originalMessageString == null ? "" : originalMessageString + "\n")
                + supportMessage
                + getBuildInformation() + "\n"
                + getDriverInfo(driver) + "\n"
                + getGridInfo(driver);
    }

    @Override
    public String getSupportUrl() {
        return null;
    }

    @Override
    public BuildInfo getBuildInformation() {
        return new BuildInfo();
    }

    @Override
    public String getDriverInfo(WebDriver driver) {
        String browserName = "N/A";
        String browserVersion = "N/A";
        String osInfo = "N/A";
        String sessionId = "N/A";

        if (driver instanceof AllegisDriver) {
            driver = ((AllegisDriver) driver).getWebDriver();
        }

        if (driver != null) {
            browserName = browserName(driver);
            browserVersion = browserVersion(driver);
            osInfo = platformOS(driver);
            sessionId = sessionId(driver);
        }
        return String.format("Browser info: name: '%s', version: '%s', os.info: '%s', driver session id: '%s'",
                browserName,
                browserVersion,
                osInfo,
                sessionId).trim();
    }

    @Override
    public String getGridInfo(WebDriver driver) {
        String info = "Selenium Node info: N/A";
        String[] gridNodeInfo = null;

        if (driver != null) {
            gridNodeInfo = hostNameAndPort(driver);

            info = String.format("Selenium Node info: hostname: '%s', port: '%s', session id: '%s'",
                    gridNodeInfo[0],
                    gridNodeInfo[1],
                    gridNodeInfo[2].equalsIgnoreCase("N/A") ? sessionId(driver) : gridNodeInfo[2]).trim();
        }
        return info;
    }

    private String[] hostNameAndPort(WebDriver driver) {
        String[] hostAndPort = new String[3];
        String sessionId = "N/A";

        if (driver == null) {
            return new String[] { "N/A", "N/A", "N/A" };
        }

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            String hostName = (((HttpCommandExecutor) ((RemoteWebDriver) driver).getCommandExecutor()).getAddressOfRemoteServer().getHost());
            int port = 4444;
            HttpHost host = new HttpHost(hostName, port);
            sessionId = sessionId(driver);
            URL sessionURL = new URL("http://" + hostName + ":" + port + "/grid/api/testsession?session=" + sessionId);
            BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("POST", sessionURL.toExternalForm());
            HttpResponse response = client.execute(host, r);
            JSONObject object = extractObject(response);
            URL myURL = new URL(object.getString("proxyId"));
            if ((myURL.getHost() != null) && (myURL.getPort() != -1)) {
                hostAndPort[0] = myURL.getHost();
                hostAndPort[1] = Integer.toString(myURL.getPort());
                hostAndPort[2] = sessionId;
            }
        } catch (Exception throw_away) {
            return new String[] { "N/A", "N/A", "N/A" };
        }

        return hostAndPort;
    }

    private static JSONObject extractObject(HttpResponse resp) throws IOException, JSONException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
        StringBuilder s = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            s.append(line);
        }
        rd.close();
        JSONObject objToReturn = new JSONObject(s.toString());
        return objToReturn;
    }
}