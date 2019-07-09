package com.exam.project.scheduling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ProjectScheduling {

	public static void main(String[] args) {
		try {

			Scanner in = new Scanner(System.in);
			System.out.println("Enter number of task");
			int n = in.nextInt();
			int[] orderedTask = new int[n];

			String[] sArray = new String[n];

			System.out
					.println("Enter task and its dependent, format ( 1<4,3<2,4<5 ) whereas task 1 needs to be completed before to start task 4, so on.. so fort.. ");
			sArray = in.next().split(",");

			// reorder task based on dependencies
			orderedTask = reorderTask(sArray, n);

			// Enter start date of project
			System.out.println("Enter Start Date of project");
			String sDate = in.next();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			Date dSDate = formatter.parse(sDate);
			Date dEDate = formatter.parse(sDate);

			for (int x = 0; x < orderedTask.length; x++) {
				System.out.println("Enter duration for task " + orderedTask[x]);
				int endDays = in.nextInt();
				if (x == 0)
					dSDate.setDate(dSDate.getDate());
				else
					dSDate.setDate(dEDate.getDate() + 1);

				dEDate.setDate(dSDate.getDate() + endDays - 1);

				System.out.println("Task " + orderedTask[x]
						+ " Duration: From: " + dSDate + " to: " + dEDate);

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Re-order task by dependencies
	static int[] reorderTask(String[] lines, int n) {
		int numTasks = n;

		List<int[]> restrictions = new ArrayList<int[]>(lines.length - 1);
		for (int i = 1; i < lines.length; i++) {
			String[] strings = lines[i].split("<");
			restrictions.add(new int[] { Integer.parseInt(strings[0]),
					Integer.parseInt(strings[1]) });
		}

		int[] tasks = new int[numTasks];
		int current = 0;

		Set<Integer> left = new HashSet<Integer>(numTasks);
		for (int i = 1; i <= numTasks; i++) {
			left.add(i);
		}
		while (current < tasks.length) {

			Set<Integer> currentIteration = new HashSet<Integer>(left);
			for (int[] restriction : restrictions) {

				currentIteration.remove(restriction[1]);
			}
			if (currentIteration.isEmpty()) {

				throw new IllegalArgumentException(
						"There's circular dependencies");
			}
			for (Integer i : currentIteration) {
				tasks[current++] = i;
			}

			left.removeAll(currentIteration);

			Iterator<int[]> iterator = restrictions.iterator();
			while (iterator.hasNext()) {
				if (currentIteration.contains(iterator.next()[0])) {
					iterator.remove();
				}
			}
		}

		return tasks;
	}

}
