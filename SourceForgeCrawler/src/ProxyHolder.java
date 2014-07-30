import java.util.HashMap;

public class ProxyHolder {

	private static HashMap<Integer,String> proxies = new HashMap<Integer,String>();

	public static void setProxySettings(){

		proxies.put(1, "157.7.202.29");
		proxies.put(2, "178.124.141.150");
		proxies.put(3, "203.81.77.109");
		proxies.put(4, "41.220.28.51");
		proxies.put(5, "80.98.132.216");

		System.getProperties().put( "proxySet", "true" );
		System.getProperties().put( "proxyHost", "128.199.213.237");
		System.getProperties().put( "proxyPort", "3128" );
	}

	public static void switchProxySettings(int key) throws Exception {
		MainHandler.writer.writeOutputToFile("Switching Proxy. Proxy IP: "+proxies.get(key));
		System.getProperties().put( "proxyHost", proxies.get(key) );
		System.getProperties().put( "proxyPort", "3128" );
	}

}
