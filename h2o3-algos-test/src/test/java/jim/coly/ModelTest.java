package jim.coly;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.GsonBuilder;

import hex.Model;
import hex.deeplearning.DeepLearningModel;
import water.AutoBuffer;
import water.H2O;
import water.Keyed;
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
		String modelPath = "src/test/resources/model/ecg_discord.bin";
		// 第1步：获取模型
		Model<?, ?, ?> model = Model.importBinaryModel(modelPath);
		System.out.println("#################");
		System.out.println(model.toJsonString());
		
		//String modelString = new String(model.writeJSON(new AutoBuffer()).buf());
		String modelString = new String(model.write(new AutoBuffer()).buf());
		System.out.println(modelString);
		
		//Model<?, ?, ?> model2 = model.readJSON(new AutoBuffer(modelString.getBytes()));
		//System.out.println("++++++++++++");
		//System.out.println(model2);
		
		AutoBuffer abw = new AutoBuffer(modelString.getBytes());
		DeepLearningModel model2 = (DeepLearningModel) Keyed.readAll(abw);
		System.out.println(model2);
	}
}
