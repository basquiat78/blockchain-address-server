package com.basquiat.address.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.basquiat.address.node.BlockChainNodeInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * CommonUtil
 * 
 * created by basquiat
 * 
 */
@Component
public class CommonUtil {

	/**
	 * Object convert to json String
	 * 
	 * @param object
	 * @return String
	 * @throws JsonProcessingException
	 */
	public static String convertJsonStringFromObject(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
	
	/**
	 * json String convert Object
	 * 
	 * @param content
	 * @param clazz
	 * @return T
	 * @throws Exception
	 */
	public static <T> T convertObjectFromJsonString(String content, Class<T> clazz) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		T object = (T) mapper.readValue(content, clazz);
		return object;
	}
	
	/**
	 * Generic Collection Type covert method
	 * @param content
	 * @param clazz
	 * @return T
	 * @throws Exception
	 */
	public static <T> T convertObjectFromJsonStringByTypeRef(String content, TypeReference<T> clazz) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		T object = mapper.readValue(content, clazz);
		return object;
	}

	/**
	 * Gson을 사용한 타입 캐스트 변환
	 * @param jsonObject
	 * @param typeToken
	 * @return T
	 * @throws Exception
	 */
	public static <T> T convertTypeTokenFromGson(JsonElement jsonObject, TypeToken<T> typeToken) throws Exception {
		T object = new Gson().fromJson(jsonObject, typeToken.getType());
		return object;
	}
	
	/**
	 * Gson 타입의 json객체를 string으로 변환한다.
	 * @param <T>
	 * @param jsonObject
	 * @return String
	 * @throws JsonProcessingException
	 */
	public static <T> String convertJsonStringFromGson(JsonObject jsonObject) throws JsonProcessingException {
		return new Gson().toJson(jsonObject);
	}
	
	/**
	 * json형식의 스트링을 Gson json객체로 변환한다.
	 * @param jsonString
	 * @return JsonObject
	 * @throws JsonProcessingException
	 */
	public static JsonObject convertGsonFromString(String jsonString) throws JsonProcessingException {
		return new JsonParser().parse(jsonString).getAsJsonObject();
	}
	
	/**
	 * transaction정보로부터 vout list를 가져온다.
	 * @param jsonObject
	 * @return List<JsonObject>
	 * @throws Exception
	 */
	public static List<JsonObject> getVOutFromTransaction(JsonObject jsonObject) throws Exception {
		return CommonUtil.convertTypeTokenFromGson(jsonObject.get("vout"), new TypeToken<List<JsonObject>>(){});
	}
	
	/**
	 * vout으로부터 address를 추출한다.
	 * @param jsonObject
	 * @return String
	 * @throws Exception
	 */
	public static String getAddressFromVOut(JsonObject jsonObject) throws Exception {
		String address = null;
		JsonObject scriptPubKey = jsonObject.get("scriptPubKey").getAsJsonObject();
		if(scriptPubKey.has("addresses")) {
			List<String> addressList = CommonUtil.convertTypeTokenFromGson(scriptPubKey.get("addresses"), new TypeToken<List<String>>(){});
			address = addressList.get(0);
		}
		return address;
	}
	
	/**
	 * 개별적인 vout의 정보로부터 value를 가져온다.
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal getValueFromVOut(JsonObject jsonObject) throws Exception {
		return jsonObject.get("value").getAsBigDecimal();
	}
	
	/**
	 * block chain node connect url
	 * @param host
	 * @param port
	 * @return String
	 */
	public static String makeUrl(String host, String port) {
		return "http://"+host+ ":"+ port + "/";
	}
	
	/**
	 * block chain node connect url type eth
	 * @param host
	 * @param port
	 * @return String
	 */
	public static String makeUrlTypeOfEth(String host, String port) {
		return "http://"+host+ ":"+ port;
	}
	
	/**
	 * eth계열의 경우 Integer타입의 값들은 16진수 hex값으로 BigInteger로 변환해야한다.
	 * @param quantity
	 * @return
	 */
	public static BigInteger decodeQuantityTypeOfETH(String quantity) {
		return new BigInteger(quantity.substring(2), 16);
	}
	
	/**
	 * block내의 block time은 unix time으로 변환하는 과정이 필요하다.
	 * @param blockTime
	 * @return String
	 * @throws Exception
	 */
	public static String convertUnixTimeToString(Integer blockTime) throws Exception {
		long unixTime = Long.parseLong(blockTime.toString()) * 1000;
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(unixTime));
	}
	
	/**
	 * reflect create instance
	 * @param preFix
	 * @param coinCode
	 * @return BlockChainNodeInterface
	 * @throws Exception
	 */
	public static BlockChainNodeInterface createInstance(String preFix, String coinCode) throws Exception {
		Class<?> clazz =  Class.forName(preFix + coinCode);
		BlockChainNodeInterface object = (BlockChainNodeInterface) clazz.newInstance();
		return object;
	}

	/**
	 * Remote Call Bitcoin based BlockChain
	 * @param user
	 * @param password
	 * @param host
	 * @param port
	 * @param rpcCommand
	 * @param param
	 * @return Object
	 * @throws Exception
	 * @desc bitcoin 기반
	 */
	public static Object RemoteCallTypeOfBTC(String user, 
									String password, 
									String host, 
									String port, 
									String rpcCommand, 
									List<Object> param) throws Exception {
		
		CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create()
        									 .setDefaultCredentialsProvider(provider)
                							 .build();
		
		HttpPost request = new HttpPost(CommonUtil.makeUrl(host, port));

        JSONObject json = new JSONObject();
        json.put("method", rpcCommand);
        json.put("params", new JSONArray(param));
        request.setEntity( new StringEntity( json.toString() ) );
		
        HttpResponse response = client.execute( request );
        
        String contents = EntityUtils.toString(response.getEntity());
        JsonObject jsonObject = CommonUtil.convertGsonFromString(contents);
        
        if(jsonObject.get("error").isJsonNull()) {
            return jsonObject.get("result");
        } else {
            return null;
        }

	}
	
	/**
	 * Remote Call Block Chain for based on eth
	 * @param user
	 * @param password
	 * @param host
	 * @param port
	 * @param rpcCommand
	 * @param param
	 * @return Object
	 * @throws Exception
	 */
	public static Object RemoteCallTypeOfETH(String user, 
									   String password, 
									   String host, 
									   String port, 
									   String rpcCommand, 
									   List<Object> param) throws Exception {
		
		//auth
		CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create()
                							 .build();
        //connection
		HttpPost request = new HttpPost(CommonUtil.makeUrlTypeOfEth(host, port));
		
        JSONObject json = new JSONObject();
        json.put("method", rpcCommand);
        json.put("params", new JSONArray(param));
        json.put("id", "1");
        request.addHeader("content-type", "application/json");
        request.setEntity( new StringEntity( json.toString() ) );
		
        HttpResponse response = client.execute( request );
        
        String contents = EntityUtils.toString(response.getEntity());
        
        JsonObject jsonObject = CommonUtil.convertGsonFromString(contents);
        
        if(jsonObject.get("error") == null) {
            return jsonObject.get("result");
        } else {
            return null;
        }

	}
	
}
