package guru.bonacci.atree;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import guru.bonacci.atree.RunMe;
import guru.bonacci.atree.RunMe.Employee;

public class RunMeTests {
	
	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	RunMe testMe;

	
	@Before
	public void init() {
		testMe = new RunMe();
	}

	@Test
	public void shouldPrintPretty() {
		Employee e = new Employee(1, "foo", 2);
		e.depth = 3;

		Assert.assertThat(testMe.prettyPrintFormat(e), is(equalTo("->->->foo")));
	}

	@Test
	public void shouldDisplayManagementTree() {
		List<Employee> es = newArrayList(
				new Employee(10, "Stacey", 1), 
				new Employee(4, "Alice", 3),
				new Employee(3, "David", 2), 
				new Employee(6, "Mike", 3), 
				new Employee(1, "Jerry", 0),
				new Employee(8, "Peter", 7), 
				new Employee(7, "Ken", 2), 
				new Employee(2, "Philip", 1),
				new Employee(9, "Tom", 2), 
				new Employee(5, "John", 4));

		testMe.displayManagementTree(es);
		Assert.assertThat(systemOutRule.getLog(), containsString("->Jerry"));
		Assert.assertThat(systemOutRule.getLog(), containsString("->->->David"));
		Assert.assertThat(systemOutRule.getLog(), containsString("->->->->->John"));

	}

	@Test(expected = IllegalArgumentException.class) 
	public void shouldRecogizeNonExistingReference() {
		List<Employee> es = newArrayList(
				new Employee(12, "I am invalid", 1), 
				new Employee(10, "I am valid", 0));

		testMe.displayManagementTree(es);
	}

	@Test(expected = IllegalArgumentException.class) 
	public void shouldRecogizeSelfManagament() {
		List<Employee> es = newArrayList(
				new Employee(12, "I am invalid", 12), 
				new Employee(10, "I am valid", 0));
		
		testMe.displayManagementTree(es);
	}
	
	@Test(expected = NullPointerException.class) 
	public void shouldBeCompleteEmployee() {
		new Employee(1, "foo and ...", null);
	}
}
