package com.smart.framework;

import com.smart.framework.annotation.Order;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class OrderedRunner extends BlockJUnit4ClassRunner {
	private static List<FrameworkMethod> testMethodList;

	public OrderedRunner(Class<?> cls) throws InitializationError {
		super(cls);
	}

	protected List<FrameworkMethod> computeTestMethods() {
		if (testMethodList == null) {
			testMethodList = super.computeTestMethods();

			Collections.sort(testMethodList, new Comparator<FrameworkMethod>() {
				public int compare(FrameworkMethod m1, FrameworkMethod m2) {
					Order o1 = (Order) m1.getAnnotation(Order.class);
					Order o2 = (Order) m2.getAnnotation(Order.class);
					if ((o1 == null) || (o2 == null)) {
						return 0;
					}
					return o1.value() - o2.value();
				}
			});
		}
		return testMethodList;
	}
}