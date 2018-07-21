package com.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.Student;
import com.utils.MyJedis;
import com.utils.MyUUID;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
/***
 * 
 * 
 * 添加学生的信息
 * @author Administrator
 *
 */
public class AddStudent extends HttpServlet {
	private static final long serialVersionUID=1L;
	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
	
		
		//接收表单信息
		
		String name=request.getParameter("name");
		String birthday=request.getParameter("birthday");
		String description=request.getParameter("description");
		String str=request.getParameter("avgscore");
		int  avgscore=Integer.parseInt(str);
		
		//通过UUID自定义用户id
		String id=MyUUID.getUUID();
		
		//封装学生对象
		
		Student student=new Student();
		student.setId(id);
		student.setName(name);
		student.setBirthday(birthday);
		student.setDescription(description);
		student.setAvgscore(avgscore);
		
		//声明jedis对象
		Jedis jedis=null;
		
		//获取jedis资源
		try {
			jedis=MyJedis.getJedis();
			//将学生id和分数关联，存入hashset中排序
			jedis.zadd("Score_info", avgscore,id);
			//将学生对象转为json存入hashmap
			JSONObject json=JSONObject.fromObject(student);
			jedis.hset("Student_info", id, json.toString());
			response.sendRedirect(request.getContextPath()+"/showStudentList");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally{
			//关闭jedis
			jedis.close();
		}
	}
}
