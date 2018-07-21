package com.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.Student;
import com.utils.MyJedis;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
/**
 * 用于修改单条学生信息
 * @author Administrator
 *
 */
public class UpdateStudent extends HttpServlet {
	private static final long serialVersionUID = 1L;
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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		        //获取学生id
				String id = request.getParameter("id");
				//获取学生name
				String name = request.getParameter("name");
				//获取学生生日
				String birthday = request.getParameter("birthday");
				//获取学生备注
				String description = request.getParameter("description");
				//获取学生平均分
				String str = request.getParameter("avgscore");
				int avgscore = Integer.parseInt(str);
				//定义学生对象
				Student student = new Student();
				student.setAvgscore(avgscore);
				student.setBirthday(birthday);
				student.setDescription(description);
				student.setId(id);
				student.setName(name);
				//声明jedis对象
				Jedis jedis = null;
				try {
					//取到redis对象
					jedis = MyJedis.getJedis();
					//将学生对象转为json对象
					JSONObject json = JSONObject.fromObject(student);
					//将json对象存入hashmap中
					jedis.hset("Student_info", id, json.toString());
					//将分数信息和id存入hashset中
					jedis.zadd("Score_info", avgscore, id);
					//重定向到列表页
					response.sendRedirect(request.getContextPath()+"/showStudentList");
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					jedis.close();
				}
			}
	}
