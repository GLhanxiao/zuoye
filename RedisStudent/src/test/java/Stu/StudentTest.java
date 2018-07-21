package Stu;

import java.util.Set;

import org.junit.Test;

import com.entity.Student;
import com.utils.MyJedis;
import com.utils.MyUUID;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

public class StudentTest {
	@Test
	public void test()throws Exception{
		Jedis jedis = MyJedis.getJedis();
		Student student = new Student();
		student.setAvgscore(78);
		student.setBirthday("2018-7-19");
		student.setDescription("dd");
		student.setId(MyUUID.getUUID());
		student.setName("耿辽");
		JSONObject fromObject = JSONObject.fromObject(student);
		System.out.println(fromObject.toString());
		jedis.hset("STUDENT_INFO", student.getId(), fromObject.toString());
		jedis.zadd("SCORE_INFO",89, student.getId());
		jedis.close();
	}
	
	
}
