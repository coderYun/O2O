package com.hly.o2o.util.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.hly.o2o.dto.UserAccessToken;
import com.hly.o2o.dto.WeChatUser;
import com.hly.o2o.entity.PersonInfo;

import net.sf.json.JSONObject;

public class WeChatUtil {
	/**
	 * 获取UserAccessToken的实体类
	 * @param code
	 * @return
	 * @throws IOException
	 */
	private static Logger log=LoggerFactory.getLogger(WeChatUtil.class);
	public static UserAccessToken getUserAccessToken(String code) throws IOException {
		//微信测试号里面的appId
		String appId="wx9b66f571a856018b";
		//微信测试号里面的appSecret
		String appsecret="b5d8771610432f7c83318c393ed9a26c";
		log.debug("secret==="+appsecret);
		//根据传入的code,拼接出访问微信定义好的接口的URL
		String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ appId + "&secret=" + appsecret + "&code=" + code
				+ "&grant_type=authorization_code";
		//向相应的URL发送请求获取token json字符串
		String tokenStr=httpsRequest(url,"GET",null);
		
		log.debug("tookenStr=="+tokenStr);
		
		UserAccessToken accessToken=null;
		ObjectMapper mapper=new ObjectMapper();
		//解析json字符串，将json字符串转化为相对应的对象
		// 如果请求成功
	
		try{
			accessToken=mapper.readValue(tokenStr, UserAccessToken.class);
		}catch(JsonParseException e){
			log.error("获取用户asscessToken信息失败"+e.getMessage());
			e.printStackTrace();
		}catch(JsonMappingException e){
			log.error("获取用户asscessToken信息失败"+e.getMessage());
			e.printStackTrace();
		}catch(IOException e){
			log.error("获取用户asscessToken信息失败"+e.getMessage());
			e.printStackTrace();
		}
		if(accessToken==null){
			log.error("获取用户asscessToken信息失败");
			return null;
		}
		return accessToken;
	
	}
	
	/**
	 * 发送https请求并获取结果
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式  GET POST
	 * @param outputStr  提交的数据
	 * @return json字符串
	 */
	private static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		//JSONObject jsonObject=null;
		StringBuffer buffer=new StringBuffer();
		try{
			//创建SSLcontext对象，并使用我们指定信任管理器初始化
			TrustManager[] tm={new MyX509TrustManger()};
			SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			//从上述的sslContext对象中获得SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url=new URL(requestUrl);
			HttpsURLConnection httpUrlConn=(HttpsURLConnection)url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			//设置请求方式(GET POST)
			httpUrlConn.setRequestMethod(requestMethod);
			
			if("GET".equals(httpUrlConn.getRequestMethod())){
				httpUrlConn.connect();
			}
			
			//当有数据提交时
			if(null!=outputStr){
				OutputStream outputStream = httpUrlConn.getOutputStream();
				//防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
				
			}
			//将返回的输入流转化为字符串
			InputStream inputStream=httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
			BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
			String str=null;
			while((str=bufferedReader.readLine())!=null){
				buffer.append(str);
				
			}
			//关闭流，释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream=null;
			//断开连接
			httpUrlConn.disconnect();
			log.debug("http str=="+buffer.toString());
			//jsonObject = JSONObject.fromObject(buffer.toString());
			
		}catch(ConnectException e){
			log.error("wechat connection timed out");
			
		}catch(Exception e){
			log.error("https request error:{}",e);
		}
		return buffer.toString();
		
	}
	
	
	
	public static PersonInfo getPersonInfoFromRequest(WeChatUser user){
		PersonInfo personInfo=new PersonInfo();
		personInfo.setName(user.getNickName());
		personInfo.setGender(user.getSex()+"");
		personInfo.setProfileImg(user.getHeadimgurl());
		personInfo.setEnableStatus(1);
		return personInfo;
	}
	
	/**
	 * 获取微信用户的信息
	 * @param accessToken
	 * @param openId
	 * @return
	 */
	public static WeChatUser getUserInfo(String accessToken, String openId) {
		JSONObject jsonObject=null;
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ accessToken + "&openid=" + openId + "&lang=zh_CN";
		String jsonObjectStr = WeChatUtil.httpsRequest(url, "GET", null);
		jsonObject = JSONObject.fromObject(jsonObjectStr.toString());
		WeChatUser user = new WeChatUser();
		String openid = jsonObject.getString("openid");
		if (openid == null) {
			log.debug("获取用户信息失败。");
			return null;
		}
		user.setOpenId(openid);
		user.setNickName(jsonObject.getString("nickname"));
		user.setSex(jsonObject.getInt("sex"));
		user.setProvince(jsonObject.getString("province"));
		user.setCity(jsonObject.getString("city"));
		user.setCountry(jsonObject.getString("country"));
		user.setHeadimgurl(jsonObject.getString("headimgurl"));
		user.setPrivilege(null);
		// user.setUnionid(jsonObject.getString("unionid"));
		return user;
	}
	
	

}
