package csdn.yinwenjie;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

//https:// blog.csdn.net/yinwenjie/article/details/71524140?utm_source=copy
/**
 * ����һ���򵥵�Join/Fork������̣���1��1001�������
 */
public class TestForkJoinPool {

	private static final Integer MAX = 200;

	static class MyForkJoinTask extends RecursiveTask<Integer> {
		// ������ʼ�����ֵ
		private Integer startValue;

		// ��������������ֵ
		private Integer endValue;

		public MyForkJoinTask(Integer startValue, Integer endValue) {
			this.startValue = startValue;
			this.endValue = endValue;
		}

		@Override
		protected Integer compute() {
			// �������������˵�������������Ҫ�������ֵ��Ϊ�㹻С��
			// ������ʽ�����ۼӼ�����
			if (endValue - startValue < MAX) {
				System.out.println("��ʼ����Ĳ��֣�startValue = " + startValue + ";endValue = " + endValue);
				Integer totalValue = 0;
				for (int index = this.startValue; index <= this.endValue; index++) {
					totalValue += index;
				}
				return totalValue;
			}
			// �����ٽ��������֣���ֳ���������
			else {
				MyForkJoinTask subTask1 = new MyForkJoinTask(startValue, (startValue + endValue) / 2);
				subTask1.fork();
				MyForkJoinTask subTask2 = new MyForkJoinTask((startValue + endValue) / 2 + 1, endValue);
				subTask2.fork();
				return subTask1.join() + subTask2.join();
			}
		}
	}

	public static void main(String[] args) {
		// ����Fork/Join��ܵ��̳߳�
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinTask<Integer> taskFuture = pool.submit(new MyForkJoinTask(1, 1001));
		try {
			Integer result = taskFuture.get();
			System.out.println("result = " + result);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace(System.out);
		}
	}
}
