import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/9/21 10:05
 */
public class Main {

    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;

        ClassLoader classLoader = Main.class.getClassLoader();

        File configFile = new File(classLoader.getResource("generatorConfig.xml").getFile());

        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }

    @Test
    public void Test() {
        SqlSession sqlSession = null;
        try {
            Reader reader = Resources.getResourceAsReader("mybatis.cfg.xml");

            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);

            sqlSession = sessionFactory.openSession();

            sqlSession.commit();
            // System.out.println(mapper.selectByPrimaryKey(1));
        } catch (IOException e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }
}
