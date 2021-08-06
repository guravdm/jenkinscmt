/*package lexprd006.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.common.data.JsonData;
import com.ss.common.share.Message;
import com.ss.common.share.Policy;
import com.ss.common.share.Recipient;
import com.ss.common.share.Share;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//Using the latest version of okHttp3 version: '4.3.1'

public class Utility {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        Utility util = new Utility();
        util.createShare();
//        util.createShare_Adhaar();
//        util.createMultipleShare();
//        util.createShare_Pan();
//        util.createShare_Passport();
//        util.createShare_Visa();
//        util.getShare();
//        util.getFilesWithFilter();
//        util.getShareNewFilters();
//        util.getMultipleFiles();
    }

    // please add the UserToken to perform operations
    private String userToken = "31dbf11e-fa9f-3f1c-8576-53687d5afecb";
    // change https://dev.securelyshare.com to your respective URl
    private static final String CreateShareUrl = "https://dev.securelyshare.com/v2/orgs/1/shares";
    private static final MediaType JSON = MediaType.parse("multipart/form-data");
    ObjectMapper mapper = new ObjectMapper();
    // please add your Identity Id you want to add to your headers
    private String identityId = "testuser@securelyshare.com";
    // please change the path to where the files exist
    private String filePath = "src/main/java/";

    private void createShare() throws IOException {
        Message messages = new Message();
        List<JsonData> fields = new ArrayList<>();
        JsonData desc = new JsonData();
        JsonData name = new JsonData();
        desc.setName("1");
        desc.setValue("Description for FILE");
        fields.add(desc);
        name.setName("2");
        name.setValue("{\"fileName\":\"sample.pdf\",\"mimeType\":\"application/pdf\"}");
        fields.add(name);
        messages.setMessageCode("0010").setFields(fields);
        Policy policy = new Policy();
        policy.setIrm(null);
        HashSet<Recipient> recipients = new HashSet<>();
        Recipient recipient = new Recipient();
        recipient.setIdentity("nag@ss.com");
        recipients.add(recipient);
        Share share = new Share();
        share.setMessage(messages).setPolicy(policy).setRecipients(recipients);
        String jsonString = mapper.writeValueAsString(share);
        System.err.println("the Share object after mapping is " + jsonString);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonString, JSON);
        RequestBody requestBody =
            new MultipartBody.Builder().addFormDataPart("share", null, body).setType(MultipartBody.FORM)
                .addFormDataPart("file", "sample.pdf",
                    RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath + "sample.pdf")))
                .build();
        Request request = new Request.Builder().url(CreateShareUrl).addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + userToken).addHeader("identity", identityId).post(requestBody)
            .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.err.println("Response" + responseBody);
            // ... do something with response
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void createShare_Pan() throws IOException {
        Message messages = new Message();
        List<JsonData> fields = new ArrayList<>();
        JsonData number = new JsonData();
        JsonData name = new JsonData();
        JsonData file = new JsonData();
        JsonData type = new JsonData();
        number.setName("3");
        number.setValue("1234");
        fields.add(number);
        file.setName("4");
        file.setValue("{\"fileName\":\"sample.pdf\",\"mimeType\":\"application/pdf\"}");
        fields.add(file);
        type.setName("2");
        type.setValue("PAN");
        fields.add(type);
        name.setName("5");
        name.setValue("ABCD1222");
        fields.add(name);
        messages.setMessageCode("0210").setFields(fields);
        Share share = new Share();
        share.setMessage(messages);
        String jsonString = mapper.writeValueAsString(share);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonString, JSON);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", "sample.pdf",
                RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath + "sample.pdf")))
            .addFormDataPart("share", null, body).build();
        Request request = new Request.Builder().url(CreateShareUrl).addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + userToken).addHeader("identity", identityId).post(requestBody)
            .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.err.println(responseBody);
            // ... do something with response
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void createShare_Adhaar() throws IOException {
        Message messages = new Message();
        List<JsonData> fields = new ArrayList<>();
        JsonData number = new JsonData();
        JsonData name = new JsonData();
        JsonData file = new JsonData();
        JsonData type = new JsonData();
        number.setName("1");
        number.setValue("345645671234");
        fields.add(number);
        file.setName("4");
        file.setValue("{\"fileName\":\"sample.pdf\",\"mimeType\":\"application/pdf\"}");
        fields.add(file);
        type.setName("2");
        type.setValue("AADHAAR");
        fields.add(type);
        name.setName("3");
        name.setValue("sumesh@ascent-online.com");
        fields.add(name);
        messages.setMessageCode("0200").setFields(fields);
        Share share = new Share();
        share.setMessage(messages);
        String jsonString = mapper.writeValueAsString(share);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonString, JSON);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", "sample.pdf",
                RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath + "sample.pdf")))
            .addFormDataPart("share", null, body).build();
        Request request = new Request.Builder().url(CreateShareUrl).addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + userToken).addHeader("identity", identityId).post(requestBody)
            .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.err.println(responseBody);
            // ... do something with response
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void createShare_Passport() throws IOException {
        Message messages = new Message();
        List<JsonData> fields = new ArrayList<>();
        JsonData number = new JsonData();
        JsonData name = new JsonData();
        JsonData file = new JsonData();
        JsonData type = new JsonData();
        number.setName("5");
        number.setValue("12333333");
        fields.add(number);
        file.setName("4");
        type.setName("2");
        type.setValue("PASSPORT");
        fields.add(type);
        name.setName("3");
        name.setValue("1002");
        fields.add(name);
        messages.setMessageCode("0220").setFields(fields);
        Share share = new Share();
        share.setMessage(messages);
        String jsonString = mapper.writeValueAsString(share);
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(jsonString, JSON);

        RequestBody requestBody =
            new MultipartBody.Builder().addFormDataPart("share", null, body).addFormDataPart("binary", "false").build();

        Request request = new Request.Builder().url(CreateShareUrl).addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + userToken).addHeader("identity", identityId).post(requestBody)
            .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.err.println(responseBody);
            // ... do something with response
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void createShare_Visa() throws IOException {
        Message messages = new Message();
        List<JsonData> fields = new ArrayList<>();
        JsonData number = new JsonData();
        JsonData name = new JsonData();
        JsonData file = new JsonData();
        JsonData type = new JsonData();
        number.setName(new String());
        number.setValue("tft65544");
        fields.add(number);
        file.setName("4");
        file.setValue("{\"fileName\":\"sample.pdf\",\"mimeType\":\"application/pdf\"}");
        fields.add(file);
        type.setName("2");
        type.setValue("VISA");
        fields.add(type);
        name.setName("3");
        name.setValue("1003");
        fields.add(name);
        messages.setMessageCode("0240").setFields(fields);
        Share share = new Share();
        share.setMessage(messages);
        String jsonString = mapper.writeValueAsString(share);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonString, JSON);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", "sample.pdf",
                RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath + "sample.pdf")))
            .addFormDataPart("share", null, body).build();
        Request request = new Request.Builder().url(CreateShareUrl).addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + userToken).addHeader("identity", identityId).post(requestBody)
            .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.err.print("the Response is" + responseBody);
            // ... do something with response
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void getShare() throws IOException {
        // add the shareId to get share
        Long shareId = 1545l;
        OkHttpClient client = new OkHttpClient();
        Request request =
            new Request.Builder().url(CreateShareUrl + "/" + shareId).addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + userToken).addHeader("identity", identityId).build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.err.println(responseBody);
            // ... do something with response
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void getShareNewFilters() throws IOException {
        OkHttpClient client = new OkHttpClient();
        // Add reference Id if available else add share Id
        String referenceId = "K6HKIV1LL00J79SZGVPW";
        Long shareId = 1566l;
        Request request;
        if (referenceId != "") {
            request = new Request.Builder().url(CreateShareUrl + "/" + referenceId + "?ref=true&data=true&plain=true")
                .addHeader("Content-Type", "application/json").addHeader("Authorization", "Bearer " + userToken)
                .addHeader("identity", identityId).build();
        } else {
            System.err.println("using shareId");
            request = new Request.Builder().url(CreateShareUrl + shareId + "?ref=false&data=true&plain=true")
                .addHeader("Content-Type", "application/json").addHeader("Authorization", "Bearer " + userToken)
                .addHeader("identity", identityId).build();
        }
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.err.println(responseBody);
            // ... do something with response
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getFilesWithFilter() throws IOException {
        // please enter the reference id of the share to get files
        String id = "K6HKIV1LL00J79SZGVPW";
        OkHttpClient client = new OkHttpClient();
        Request request;
        request = new Request.Builder().url(CreateShareUrl + "/files?id=" + id + "&native=false&ref=true")
            .addHeader("Content-Type", "application/json").addHeader("Authorization", "Bearer " + userToken)
            .addHeader("identity", identityId).build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.err.println(responseBody);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void createMultipleShare() throws IOException {
        Message messages = new Message();
        List<JsonData> fields = new ArrayList<>();
        JsonData desc = new JsonData();
        JsonData name = new JsonData();
        desc.setName("1");
        desc.setValue("Description for FILE");
        fields.add(desc);
        name.setName("2");
        name.setValue("{\"fileName\":\"A.pdf\",\"mimeType\":\"application/pdf\"}");
        fields.add(name);
        messages.setMessageCode("0010").setFields(fields);
        Policy policy = new Policy();
        policy.setIrm(null);
        HashSet<Recipient> recipients = new HashSet<>();
        Recipient recipient = new Recipient();
        recipient.setIdentity("nag@ss.com");
        recipients.add(recipient);
        Share share1 = new Share();
        share1.setMessage(messages).setPolicy(policy).setRecipients(recipients);
        String jsonString1 = mapper.writeValueAsString(share1);
        Share share2 = new Share();
        share2 = share1;
        share2.getMessage().getFields().get(1).setValue("{\"fileName\":\"B.pdf\",\"mimeType\":\"application/pdf\"}");
        String jsonString2 = mapper.writeValueAsString(share2);
        Share share3 = new Share();
        share3 = share1;
        share3.getMessage().getFields().get(1).setValue("{\"fileName\":\"C.pdf\",\"mimeType\":\"application/pdf\"}");
        String jsonString3 = mapper.writeValueAsString(share3);
        System.err.println("the Share object after mapping is " + jsonString1);

        OkHttpClient client = new OkHttpClient();
        RequestBody body1 = RequestBody.create(jsonString1, JSON);
        RequestBody body2 = RequestBody.create(jsonString2, JSON);
        RequestBody body3 = RequestBody.create(jsonString3, JSON);
        RequestBody requestBody = new MultipartBody.Builder().addFormDataPart("share", null, body1)
            .addFormDataPart("share", null, body2).addFormDataPart("share", null, body3).setType(MultipartBody.FORM)
            .addFormDataPart("file", "A.pdf",
                RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath + "A.pdf")))
            .addFormDataPart("file", "B.pdf",
                RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath + "B.pdf")))
            .addFormDataPart("file", "C.pdf",
                RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath + "C.pdf")))
            .build();
        Request request = new Request.Builder().url(CreateShareUrl).addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + userToken).post(requestBody).build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.err.println("Response" + responseBody);
            // ... do something with response
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void getMultipleFiles() throws IOException {
        // add the shareIds to id seperated by comma
        String id = "1573,1574,1575";
        // Please enter the Identity Id of the user for headers
        OkHttpClient client = new OkHttpClient();
        Request request;
        request = new Request.Builder().url(CreateShareUrl + "/files?id=" + id + "&native=true")
            .addHeader("Content-Type", "application/json").addHeader("Authorization", "Bearer " + userToken)
            .addHeader("identity", identityId).build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.err.print(responseBody);
            // ... do something with response
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
		}
	}

}
*/