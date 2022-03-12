import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tzp.ss.ShardingSphereMain;
import com.tzp.ss.dao.CourseMapper;
import com.tzp.ss.dao.UdictMapper;
import com.tzp.ss.dao.UserMapper;
import com.tzp.ss.entity.Course;
import com.tzp.ss.entity.Udict;
import com.tzp.ss.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest(classes = ShardingSphereMain.class)
public class ShardingTest {

    @Resource
    private CourseMapper courseMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UdictMapper udictMapper;

    /**
     * 单库水平分表
     */
    @Test
    public void addCourse() {
        for (int i = 0; i < 100; i++) {
            Course course = new Course();
            course.setCname("java" + i);
            course.setUserId(100L);
            course.setCstatus("Normal" + i);
            courseMapper.insert(course);
        }
    }

    @Test
    public void findCourse() {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("cid",709411481342443520L);
        Course course = courseMapper.selectOne(wrapper);
        assertNotNull(course);
        assertEquals("java33", course.getCname());
        assertEquals(100, course.getUserId());
        assertEquals("Normal33", course.getCstatus());
    }

    /**
     * 水平分库分表测试
     */
    @Test
    public void addCourse2() {
        Course course = new Course();
        course.setCname("java");
        course.setUserId(1L);
        course.setCstatus("Normal");
        assertEquals(courseMapper.insert(course), 1);
        Course course2 = new Course();
        course2.setCname("mysql");
        course2.setUserId(2L);
        course2.setCstatus("Normal");
        assertEquals(courseMapper.insert(course2), 1);
//        long userId = 101L;
//        for (int i = 0; i < 100; i++) {
//            Course course = new Course();
//            course.setCname("java" + i);
//            course.setUserId(userId);
//            course.setCstatus("Normal" + i);
//            courseMapper.insert(course);
//        }
    }

    @Test
    public void findCourse2() {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("cid",709427882467786753L);
        wrapper.eq("user_id",1L);
        Course course = courseMapper.selectOne(wrapper);
        assertNotNull(course);
        assertEquals("java", course.getCname());
        assertEquals("Normal", course.getCstatus());
    }

    /**
     * 垂直分表
     */
    @Test
    public void addUser() {
        User user = new User();
        user.setUserName("tommy");
        user.setUserStatus("normal");
        assertEquals(1, userMapper.insert(user));
    }

    @Test
    public void findUser() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",709435602012995585L);
        User user = userMapper.selectOne(wrapper);
        assertNotNull(user);
        assertEquals("tommy", user.getUserName());
        assertEquals("normal", user.getUserStatus());
    }

    /**
     * 公共表测试
     */
    @Test
    public void addUdict() {
        Udict udict = new Udict();
        udict.setUstatus("a");
        udict.setUvalue("normal");
        assertEquals(1, udictMapper.insert(udict));
    }

    @Test
    public void getUdict() {
        QueryWrapper<Udict> wrapper = new QueryWrapper<>();
        wrapper.eq("dictid",709441445018206209L);
        Udict udict = udictMapper.selectOne(wrapper);
        assertNotNull(udict);
        assertEquals("a", udict.getUstatus());
        assertEquals("normal", udict.getUvalue());
    }

}
