package csdn.yinwenjie;

import java.util.Arrays;
import java.util.Random;

/**
 * �鲢����
 * 
 * @author yinwenjie
 */
public class Merge1 {

	private static int MAX = 10000;

	private static int inits[] = new int[MAX];

	// ����Ϊ������һ������ΪMAX������������ϣ�׼����������
	// ���㷨����û��ʲô��ϵ
	static {
		Random r = new Random();
		for (int index = 1; index <= MAX; index++) {
			inits[index - 1] = r.nextInt(10000000);
		}
	}

	public static void main(String[] args) {
		long beginTime = System.currentTimeMillis();
		int results[] = forkits(inits);
		long endTime = System.currentTimeMillis();
		// ���������������ݷǳ��Ӵ󣬼ǵð����ִ�ӡ��ʽȥ��
		System.out.println("��ʱ=" + (endTime - beginTime) + " | " + Arrays.toString(results));
	}

	// ��ֳɽ�С��Ԫ�ػ��߽����㹻С��Ԫ�ؼ��ϵ�����
	private static int[] forkits(int source[]) {
		int sourceLen = source.length;
		if (sourceLen > 2) {
			int midIndex = sourceLen / 2;
			int result1[] = forkits(Arrays.copyOf(source, midIndex));
			int result2[] = forkits(Arrays.copyOfRange(source, midIndex, sourceLen));
			// ��������������飬�ϲ���һ�����������
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

	/**
	 * ����������ںϲ��������򼯺�
	 * 
	 * @param array1
	 * @param array2
	 */
	private static int[] joinInts(int array1[], int array2[]) {
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
