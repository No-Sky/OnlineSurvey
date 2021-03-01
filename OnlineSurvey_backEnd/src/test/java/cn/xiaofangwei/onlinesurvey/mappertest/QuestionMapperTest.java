package cn.xiaofangwei.onlinesurvey.mappertest;

import cn.xiaofangwei.onlinesurvey.OnlinesurveyApplication;
import cn.xiaofangwei.onlinesurvey.entity.Question;
import cn.xiaofangwei.onlinesurvey.mapper.QuestionMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OnlinesurveyApplication.class)
public class QuestionMapperTest {

    @Resource
    QuestionMapper questionMapper;

    /*@Test
    public void selectQuestionsWithOptionsTest(){
        List<Question> questions = questionMapper.selectQuestionsWithOptions(1);
        System.out.println(questions);
    }

    @Test
    public void selectQuestionWithOptionsTest(){
        Question question = questionMapper.selectQuestionWithOptions(1);
        System.out.println(question);
    }
*/
}
