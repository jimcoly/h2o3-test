package jim.coly;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import hex.Model;
import water.H2O;
import water.TestUtil;

public class ModelTest extends TestUtil {

	@BeforeClass
	static public void setup() {
		if (H2O.SELF == null) {
			System.out.println("error");
		}
		// stall_till_cloudsize(1);
		H2O.main(new String[] {});
		// H2O.startServingRestApi();
		if (H2O.SELF == null) {
			System.out.println("error2");
		}
	}

	@Test
	public void test() throws IOException {

		// 约定模型存放路径
		String modelPath = "src/test/resources/model/oge.bin";
		// 第1步：获取模型
		Model<?, ?, ?> model = Model.importBinaryModel(modelPath);
		System.out.println(model.toJsonString());
	}
}
