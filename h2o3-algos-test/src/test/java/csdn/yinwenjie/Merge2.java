package csdn.yinwenjie;
/**
 * ʹ��Fork/Join��ܵĹ鲢�����㷨
 * @author yinwenjie
 */

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class Merge2 {

	private static int MAX = 100000000;

	private static int inits[] = new int[MAX];

	// ͬ������������г�ʼ��������Ͳ���׸����
	static {
		Random r = new Random();
		for (int index = 1; index <= MAX; index++) {
			inits[index - 1] = r.nextInt(10000000);
		}
	}

	public static void main(String[] args) throws Exception {
		// ��ʽ��ʼ
		long beginTime = System.currentTimeMillis();
		ForkJoinPool pool = new ForkJoinPool();
		MyTask task = new MyTask(inits);
		ForkJoinTask<int[]> taskResult = pool.submit(task);
		try {
			taskResult.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace(System.out);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("��ʱ=" + (endTime - beginTime));
	}

	/**
	 * ���������������
	 * 
	 * @author yinwenjie
	 */
	static class MyTask extends RecursiveTask<int[]> {

		private int source[];

		public MyTask(int source[]) {
			this.source = source;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.concurrent.RecursiveTask#compute()
		 */
		@Override
		protected int[] compute() {
			int sourceLen = source.length;
			// �������������˵��������Ҫ��������ļ��ϻ�����С
			if (sourceLen > 2) {
				int midIndex = sourceLen / 2;
				// ��ֳ�����������
				MyTask task1 = new MyTask(Arrays.copyOf(source, midIndex));
				task1.fork();
				MyTask task2 = new MyTask(Arrays.copyOfRange(source, midIndex, sourceLen));
				task2.fork();
				// ��������������飬�ϲ���һ�����������
				int result1[] = task1.join();
				int result2[] = task2.join();
				int mer[] = joinInts(result1, result2);
				return mer;
			}
			// ����˵��������ֻ��һ����������Ԫ�أ����Խ���������Ԫ�صıȽ�������
			else {
				// �������������˵��������ֻ��һ��Ԫ�أ������������е�Ԫ�ض��Ѿ����к�λ����
				if (sourceLen == 1 || source[0] <= source[1]) {
					return source;
				} else {
					int targetp[] = new int[sourceLen];
					targetp[0] = source[1];
					targetp[1] = source[0];
					return targetp;
				}
			}
		}

		private int[] joinInts(int array1[], int array2[]) {
			int destInts[] = new int[array1.length + array2.length];
			int array1Len = array1.length;
			int array2Len = array2.length;
			int destLen = destInts.length;

			// ֻ��Ҫ���µļ���destInts�ĳ���Ϊ��׼������һ�μ���
			for (int index = 0, array1Index = 0, array2Index = 0; index < destLen; index++) {
				int value1 = array1Index >= array1Len ? Integer.MAX_VALUE : array1[array1Index];
				int value2 = array2Index >= array2Len ? Integer.MAX_VALUE : array2[array2Index];
				// �������������˵��Ӧ��ȡ����array1�е�ֵ
				if (value1 < value2) {
					array1Index++;
					destInts[index] = value1;
				}
				// ����ȡ����array2�е�ֵ
				else {
					array2Index++;
					destInts[index] = value2;
				}
			}

			return destInts;
		}
	}
}
