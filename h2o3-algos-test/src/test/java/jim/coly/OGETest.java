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
		double[] newData = new double[5];
		Settings sets = new Settings();
		calc(newData, sets);
	}

	public void calc(double[] newData, Settings sets) {

		// newData ������Y��ֵ

		// TODO Լ��ģ�ʹ��·��
		String modelPath = "";
		try {
			// ��1������ȡģ��
			Model<?, ?, ?> model = Model.importBinaryModel(modelPath);
			// ��2������Y0
			double y0 = model.score(newData);

			// ��3������Ӱ����
			double effect = sets.getEffect();

			int colNum = newData.length;
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
				System.out.println(effectY[i]);
			}

			// ��4����������ֵ
			// ��4.1����������������
			// TODO ������������
			Frame optFrame = null;
			// ��4.2�������������ݽ��д��Ԥ��
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
