package com.jijiancode.end.java;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class JJCode {
    private static final String JJCODE_URL = "https://api.jijiancode.com/api/s/third/verify_id";

    public static VerifyState verify(String appId, String userSecret,
                                     String mobile, String requestToken) throws JJCodeException {
        Map<String, String> params = new HashMap<>();
        params.put("app_id", appId);
        params.put("mobile", mobile);
        params.put("id", requestToken);
        params.put("country_code", "86");
        return request(JJCODE_URL, params, userSecret);
    }

    private static VerifyState request(String url, Map<String, String> params, String userSecret) throws JJCodeException {
        params.put("r", String.valueOf(System.currentTimeMillis()));
        params.put("key", createSign(params, userSecret));
        InputStream is = null;

        HttpURLConnection connection = null;
        try {
            URL uri = new URL(url);
            connection = (HttpURLConnection) uri.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);
            connection.setChunkedStreamingMode(0);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            String data = map2Json(params);
            if (!isEmpty(data)) {
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.print(data);
                out.close();
            }
            int code = connection.getResponseCode();
            if (code == 200) {
                is = connection.getInputStream();
                String ss = in2String(is);
                try {
                    JSONObject jsonObject = new JSONObject(ss);
                    int dCode = jsonObject.getInt("code");
                    if (dCode == 200) {
                        //请求正常
                        JSONObject dataJson = jsonObject.getJSONObject("data");
                        VerifyState verifyState = new VerifyState();
                        verifyState.setMsg(dataJson.getString("msg"));
                        verifyState.setStatus(dataJson.getInt("status"));
                        return verifyState;
                    } else {
                        String errMsg = jsonObject.getString("msg");
                        throw new JJCodeException(errMsg);
                    }
                } catch (JSONException jsonException) {
                    throw new JJCodeException("parse json data error.");
                }
            } else {
                throw new JJCodeException("network error: " + connection.getResponseMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new JJCodeException("unknown error.");
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    private static String map2Json(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return "{}";
        }
        try {
            JSONObject jo = new JSONObject();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!isEmpty(entry.getValue())) {
                    jo.put(entry.getKey(), entry.getValue());
                }
            }
            return jo.toString();
        } catch (Exception e) {
            System.out.println("format params error. err=" + e.getMessage());
        }
        return "{}";
    }

    private static String createSign(Map<String, String> map, String token) {
        if (map == null || map.size() == 0) {
            return "";
        }
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            String value = map.get(key);
            if (isEmpty(value)) {
                continue;
            }
            sb.append(key).append("=").append(value);
            sb.append("&");
        }
        sb.append("token=").append(token);
        return md5(sb.toString());
    }

    private static String in2String(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
        StringBuilder sb = new StringBuilder();
        String ss;
        while ((ss = br.readLine()) != null) {
            sb.append(ss);
        }
        br.close();
        return sb.toString();
    }

    private static String md5(String s) {
        if (s == null || s.equals("") || s.equals("null")) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(s.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static boolean isEmpty(String s) {
        return s == null || s.equals("") || s.equals("null");
    }

}
