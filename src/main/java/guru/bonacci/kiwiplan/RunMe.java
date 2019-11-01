package guru.bonacci.kiwiplan;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.repeat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Integer.MAX_VALUE;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeTraverser;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class RunMe {

	public static void main(String[] args) {
		List<Employee> employees = newArrayList(
				new Employee(2, "Mickey", 10), 
				new Employee(7, "John", 2), 
				new Employee(3, "Jerry", 10),
				new Employee(5, "Sarah", 10), 
				new Employee(10, "Tom", 0));
		
		new RunMe().displayManagementTree(employees);
	}
	
	/*
	 * Run the solution to see the output of the test values in a console window. 
	 * 
	 * Start your method with this signature: 
	 * void displayManagementTree(List<Employee> employees) {
	 */
	@SuppressWarnings("deprecation")
	void displayManagementTree(List<Employee> employees) {
		
		// we cannot do without our highest boss
		Employee boss = new Employee(0, "The boss", MAX_VALUE); // this itself is not so beautiful
		boss.depth = 0; // the root of the tree
		employees.add(boss);

		// unfortunately my solution forces us to iterate the input list twice
		// we need the employees available by id to add the subordinates to each manager
		Map<Integer, Employee> employeesById = 
				employees.stream()
						 .collect(toMap(Employee::getId, identity()));	

		// this multimap represents our hierarchical tree structure
		Multimap<Integer, Employee> managedBy = ArrayListMultimap.create();
		employees.forEach(e -> {
			if (e.getId() != boss.id) {
				checkArgument(employeesById.keySet().contains(e.getManagerId()),
								String.format("managerId %s refers to non-existing employee", e.getManagerId()));

				checkArgument(!e.getId().equals(e.getManagerId()),
						String.format("employer with id %s attempts self-management", e.getId()));
				
				employeesById.get(e.getManagerId()).addToManages(e);
			}	
			managedBy.put(e.getManagerId(), e);	
		});

		// let's use the traversal-opportunity to set the tree depth
		TreeTraverser<Employee> traverser = TreeTraverser.using(node -> {
			Set<Employee> children = node.getManages();
			children.forEach(c -> c.depth = node.depth + 1);
			return children;
		});
		
		// and perform a depth first search
		traverser.preOrderTraversal(boss)
						.stream()
						.map(this::prettyPrintFormat) 
						.forEach(System.out::println); // and finally, print to the console
		
	}

	String prettyPrintFormat(Employee employee) {
		return repeat("->", employee.depth) + employee.getName();
	}


	@Data
	@RequiredArgsConstructor
	static class Employee {

		@NonNull private final Integer id;
		@NonNull private final String name;
		@NonNull private final Integer managerId;

		// some tree node related fields
		private Set<Employee> manages = newHashSet();
		int depth;
		
		void addToManages(Employee m) {
			manages.add(m);
		}
	}
}
