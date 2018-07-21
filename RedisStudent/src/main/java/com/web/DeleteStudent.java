package com.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.utils.MyJedis;

import redis.clients.jedis.Jedis;
/**
 * 用于通过学生id删除学生信息
 * @author Administrator
 *
 */
public class DeleteStudent extends HttpServlet {
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
		//获取要删除学生的id
		String id=request.getParameter("id");
			if(id==null) return;
				//声明jeids对象
				Jedis jedis	=null;
				try {//获取jedis对象
				jedis=MyJedis.getJedis();
				//通过id删除hashmap中学生的记录
				jedis.hdel("Student_info", id);
				//通过id删除hashset中的分数记录
				jedis.zrem("Score_info", id);
				response.sendRedirect(request.getContentLength()+"/showStudentList");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally{
			jedis.close();
		}
	}

}
