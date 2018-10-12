package jim.coly;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import hex.Model;
import water.Key;
import water.TestUtil;
import water.fvec.Frame;
import water.fvec.Vec;

public class OGETest extends TestUtil {

	@BeforeClass
	static public void setup() {
		stall_till_cloudsize(1);
	}

	@Test
	public void test() {
		double[] newData = new double[] { 5.74, 5.86, 5.91, 5.95, 6, 6.04, 6.08, 6.11, 6.13, 6.17, 6.21, 6.23, 6.26,
				6.3, 6.34, 6.37, 6.39, 6.44, 6.49, 6.52, 6.55, 6.59, 6.65, 6.69, 6.73, 6.74, 6.81, 6.87, 6.88, 6.91,
				6.95, 6.98, 6.99, 6.99, 7.02, 7.03, 7.01, 6.99, 6.97, 6.96, 6.9, 6.84, 6.78, 6.73, 6.63, 6.54, 6.46,
				6.39, 6.3, 6.21, 6.18, 6.06, 5.99, 5.89, 5.82, 5.77, 5.72, 5.65, 5.58, 5.54, 5.5, 5.46, 5.4, 5.38, 5.37,
				5.33, 5.3, 5.29, 5.28, 5.26, 5.25, 5.23, 5.24, 5.24, 5.22, 5.21, 5.22, 5.23, 5.22, 5.21, 5.23, 5.22,
				5.21, 5.2, 5.22, 5.22, 5.22, 5.22, 5.24, 5.27, 5.26, 5.25, 5.25, 5.26, 5.26, 5.26, 5.25, 5.27, 5.25,
				5.22, 5.2, 5.2, 5.2, 5.17, 5.15, 5.14, 5.13, 5.11, 5.09, 5.1, 5.11, 5.12, 5.11, 5.1, 5.12, 5.09, 5.08,
				5.07, 5.08, 5.08, 5.06, 5.06, 5.06, 5.06, 5.06, 5.05, 5.05, 5.06, 5.03, 5.04, 5.04, 5.05, 5.04, 5.03,
				5.03, 5.04, 5.04, 5.02, 5.02, 5.04, 5.04, 5.02, 5.03, 5.03, 5.04, 5.02, 5.03, 5.04, 5.04, 5.04, 5.04,
				5.04, 5.05, 5.04, 5.03, 5.05, 5.06, 5.06, 5.07, 5.17, 5.52, 6.02, 5.95, 5.6, 5.46, 5.4, 5.34, 5.27,
				5.17, 4.99, 4.73, 4.43, 4.12, 3.79, 3.44, 3.33, 2.92, 2.76, 2.63, 2.54, 2.45, 2.34, 2.27, 2.23, 2.22,
				2.2, 2.18, 2.15, 2.17, 2.2, 2.2, 2.24, 2.34, 2.49, 2.65, 2.84, 3.05, 3.34, 3.63, 3.93, 4.02, 4.54, 4.8,
				5, 5.2, 5.36, 5.52, 5.64, 5.74, 5.83 };
		Settings sets = new Settings();
		calc(newData, sets);
	}

	public void calc(double[] newData, Settings sets) {

		int colNum = newData.length;

		// newData 不含有Y的值

		// TODO 约定模型存放路径
		String modelPath = "src/test/resources/model/ecg_discord.bin";
		try {
			// 第1步：获取模型
			Model<?, ?, ?> model = Model.importBinaryModel(modelPath);
			// 第2步：求Y0
			// double y0 = model.score(newData);
			// TODO 处理y0会等于零
			Vec[] y0Vecs = new Vec[colNum];
			Key<Vec>[] keys2 = Vec.VectorGroup.VG_LEN1.addVecs(colNum);
			for (int r = 0; r < colNum; r++) {
				y0Vecs[r] = Vec.makeVec(new double[] { newData[r] }, keys2[r]);// Key.make()
			}
			Frame y0Frame = new Frame(y0Vecs);
			Frame y0Score = model.score(y0Frame);
			Vec y0Vec = y0Score.vec(0);
			// for (int i = 0; i < y0Vec.length(); i++) {
			// System.out.println(y0Vec.at(i));
			// }
			double y0 = y0Vec.at(0);
			System.out.println(y0);

			// 第3步：求影响量
			double effect = sets.getEffect();

			Vec[] efdata = new Vec[colNum];
			Key<Vec>[] keys = Vec.VectorGroup.VG_LEN1.addVecs(colNum);
			for (int r = 0; r < colNum; r++) {
				double curval = newData[r];
				double[] vec = new double[colNum];
				for (int i = 0; i < vec.length; i++) {
					vec[i] = curval;
				}
				vec[r] = vec[r] * (1.0 + effect);
				efdata[r] = Vec.makeVec(vec, keys[r]);
			}
			Frame effectFrame = new Frame(efdata);
			Frame effectScore = model.score(effectFrame);
			Vec evec = effectScore.vec(0);
			double[] effectY = new double[colNum];
			for (int i = 0; i < evec.length(); i++) {
				double yn = evec.at(i);
				effectY[i] = (yn - y0) / y0;
				System.out.println("y" + i + ":" + yn + " - " + effectY[i]);
			}

			// 第4步：求最优值
			// 第4.1步：生成样本数据
			// TODO 生成样本数据
			Frame optFrame = null;
			// 第4.2步：对样本数据进行打分预测
			Frame optScore = model.score(optFrame);
			Vec ovec = optScore.vec(0);
			int dataSize = new Long(ovec.length()).intValue();
			double[] optY = new double[dataSize];
			for (int i = 0; i < dataSize; i++) {
				optY[i] = ovec.at(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
