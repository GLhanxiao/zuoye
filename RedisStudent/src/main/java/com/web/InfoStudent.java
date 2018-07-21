package com.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.utils.MyJedis;

import redis.clients.jedis.Jedis;
/**
 * 
 * 用于回显单条学生信息
 * @author Administrator
 *
 */
public class InfoStudent extends HttpServlet {
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
		//拿到显示学生的id
		String id=request.getParameter("currentId");
		
		//声明jedis对象
		Jedis jedis=null;
		try {
			//获取jedis对象
			
			jedis=MyJedis.getJedis();
			//通过学生id拿到学生的信息
			String suget=jedis.hget("Student_info", id);
			//把学生信息打到页面
			response.getWriter().write(suget);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		
	}
}
