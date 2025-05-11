package myPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
	
		String apikey="be76618aaf010e326987821d5b39e939";
		String city=request.getParameter("city");
		System.out.println(city);
		String apiurl="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apikey;
		URL ur=new URL(apiurl);
		HttpURLConnection connection=(HttpURLConnection)ur.openConnection();
		connection.setRequestMethod("GET");
		//reading the data from network
		InputStream inputstream=connection.getInputStream();
		InputStreamReader reader=new InputStreamReader(inputstream);
		//want to store data in string
		StringBuilder responsedata=new StringBuilder();
		Scanner scanner=new Scanner(reader);
		while(scanner.hasNext()) {
			responsedata.append(scanner.nextLine());
		}
		scanner.close();
		
		
		Gson gson=new Gson();
		JsonObject jsonobject=gson.fromJson(responsedata.toString(),JsonObject.class);
	
		
		long dateandtime=jsonobject.get("dt").getAsLong()*1000;
		String date=new Date(dateandtime).toString();
		double temperaturkelvin=jsonobject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperaturecelsuis=(int)(temperaturkelvin-273.15);
		int humidity=jsonobject.getAsJsonObject("main").get("humidity").getAsInt();
		double windspeed=jsonobject.getAsJsonObject("wind").get("speed").getAsDouble();
		String wheathercondition=jsonobject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature",temperaturecelsuis );
		request.setAttribute("wheathercondition",wheathercondition);
		request.setAttribute("humidity",humidity);
		request.setAttribute("windspeed", windspeed);
		request.setAttribute("wheatherdata", responsedata.toString());
		
		connection.disconnect()	;
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	
	}

}
