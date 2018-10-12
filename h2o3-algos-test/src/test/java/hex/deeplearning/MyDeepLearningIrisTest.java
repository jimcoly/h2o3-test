package hex.deeplearning;

import org.junit.BeforeClass;
import org.junit.Test;

import hex.deeplearning.DeepLearningModel.DeepLearningParameters;
import hex.deeplearning.DeepLearningModel.DeepLearningParameters.Activation;
import hex.deeplearning.DeepLearningModel.DeepLearningParameters.InitialWeightDistribution;
import hex.deeplearning.DeepLearningModel.DeepLearningParameters.Loss;
import water.Key;
import water.TestUtil;
import water.fvec.Frame;

public class MyDeepLearningIrisTest extends TestUtil {
	static final String PATH = "smalldata/iris/iris.csv";
	Frame _train, _test;

	@BeforeClass()
	public static void setup() {
		stall_till_cloudsize(1);
	}

	// Default run is the short run
	@Test
	public void run() throws Exception {
		runFraction(0.05f);
	}

	void runFraction(float fraction) {
		long seed = 0xDECAF;
		Frame frame = null;
		try {
			frame = parse_test_file(Key.make("iris.hex"), PATH);

			Activation activation = Activation.Tanh;// Activation.Rectifier
			Loss loss = Loss.Quadratic; // Loss.CrossEntropy
			InitialWeightDistribution dist = InitialWeightDistribution.Normal;// Uniform,UniformAdaptive

			DeepLearningParameters p = new DeepLearningParameters();
			p._train = _train._key;
			p._response_column = _train.lastVecName();
			assert _train.lastVec().isCategorical();
			p._ignored_columns = null;

			p._seed = seed;
			p._hidden = new int[] { 1 };
			p._adaptive_rate = false;
			p._rho = 0;
			p._epsilon = 0;
			p._rate = 0.05;
			p._activation = activation;
			p._max_w2 = Float.POSITIVE_INFINITY;
			p._input_dropout_ratio = 0;
			p._rate_annealing = 0; // do not change - not implemented in reference
			p._l1 = 0;
			p._loss = loss;
			p._l2 = 0;
			// p._momentum_stable = momentum; // reference only supports constant momentum
			p._momentum_start = p._momentum_stable; // do not change - not implemented in reference
			p._momentum_ramp = 0; // do not change - not implemented in reference
			p._initial_weight_distribution = dist;
			// p._initial_weight_scale = scale;
			p._valid = null;
			p._quiet_mode = true;
			p._fast_mode = false; // to be the same as reference
			// p._fast_mode = true; //to be the same as old NeuralNet code
			p._nesterov_accelerated_gradient = false; // to be the same as reference
			// to be the same as old NeuralNet code
			// p._nesterov_accelerated_gradient = true;
			p._train_samples_per_iteration = 0; // sync once per period
			p._ignore_const_cols = false;
			p._shuffle_training_data = false;
			// don't stop early -> need to compare against reference, which doesn't stop
			// either
			p._classification_stop = -1;
			p._force_load_balance = false; // keep just 1 chunk for reproducibility
			p._overwrite_with_best_model = false;
			p._replicate_training_data = false;
			p._mini_batch_size = 1;
			p._single_node_mode = true;
			p._epochs = 50;
			p._elastic_averaging = false;
			DeepLearningModel mymodel = new DeepLearning(p).trainModel().get();
			System.out.println(mymodel);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(t);
		} finally {
			if (frame != null)
				frame.delete();
		}
	}

}
