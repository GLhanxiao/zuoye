package com.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.TryCatchFinally;

import com.entity.PageBean;
import com.entity.Student;
import com.utils.MyJedis;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
/**
 * 通过分数示学生信息列表
 * @author Administrator
 *
 */
public class ShowStudentList extends HttpServlet {
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
		//获取当前页
		String str=request.getParameter("currentPage");
		if(str==null) str="1";
		int currentPage=Integer.parseInt(str);
		//声明jedis对象
		Jedis jedis = null;
		try {
			//获取jedis对象
			jedis = MyJedis.getJedis();
			//每页显示10条
			 int page=10;
			 //总记录数
			 Long sumRow=jedis.zcard("Score_info");
			 //起始索引
			 int index=(currentPage-1)*page;
			 //总页数
			 int sumPage=(int) Math.ceil(1.0*sumRow/page);
			 //获得学生id集合
			 Set<String> ByScore=jedis.zrevrangeByScore("Score_info", 100, 0,index,page);
			 if(ByScore!=null){
				 List<Student>list=new ArrayList<Student>();
				 //遍历学生id集合
				 for (String string : ByScore) {
					//通过学生id获取学生对象json串
					 String json=jedis.hget("Student_info", string);
					 //将串json转为学生对象
					 JSONObject fromObject=JSONObject.fromObject(json);
					 Student student=(Student) JSONObject.toBean(fromObject, Student.class);
					 //将学生对象添加到集合中
					 list.add(student);
				}
				 //分页
				 PageBean pageBean=new PageBean();
				 pageBean.setSumPage(sumPage);
				 pageBean.setList(list);
				 pageBean.setCurrentPage(currentPage);
				 pageBean.setIndex(index);
				 request.setAttribute("pageBean", pageBean);
				 //将学生对象集合转发到request作用域中
				 request.getRequestDispatcher("/student/list.jsp").forward(request, response);
			 }else{
				 response.sendRedirect(request.getContextPath()+"/error.jsp");
			 }
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally{
			jedis.close();
		}
	}
}
